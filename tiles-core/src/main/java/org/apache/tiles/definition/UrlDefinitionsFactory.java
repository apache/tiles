/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tiles.definition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Definition;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.locale.impl.DefaultLocaleResolver;
import org.apache.tiles.util.ClassUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * {@link DefinitionsFactory DefinitionsFactory} implementation
 * that manages Definitions configuration data from URLs.
 * <p/>
 * <p>The Definition objects are read from the
 * {@link org.apache.tiles.definition.digester.DigesterDefinitionsReader DigesterDefinitionsReader}
 * class unless another implementation is specified.</p>
 *
 * @version $Rev$ $Date$
 */
public class UrlDefinitionsFactory implements DefinitionsFactory,
        ReloadableDefinitionsFactory, TilesApplicationContextAware {

    /**
     * Compatibility constant.
     *
     * @deprecated use {@link DEFINITIONS_CONFIG} to avoid namespace collisions.
     */
    private static final String LEGACY_DEFINITIONS_CONFIG = "definitions-config";

    /**
     * LOG instance for all UrlDefinitionsFactory instances.
     */
    private static final Log LOG = LogFactory.getLog(UrlDefinitionsFactory.class);

    /**
     * Contains the URL objects identifying where configuration data is found.
     *
     * @since 2.1.0
     */
    protected List<URL> sourceURLs;

    /**
     * Contains the URL objects identifying where configuration data is found.
     *
     * @deprecated Use {@link #sourceURLs}.
     */
    protected List<Object> sources;

    /**
     * Reader used to get definitions from the sources.
     */
    protected DefinitionsReader reader;

    /**
     * Contains the dates that the URL sources were last modified.
     */
    protected Map<String, Long> lastModifiedDates;

    /**
     * The application context.
     *
     * @since 2.1.0
     */
    protected TilesApplicationContext applicationContext;

    /**
     * Contains a list of locales that have been processed.
     */
    private List<Locale> processedLocales;


    /**
     * The base set of Definition objects not discriminated by locale.
     */
    private Map<String, Definition> baseDefinitions;

    /**
     * The locale-specific set of definitions objects.
     */
    private Map<Locale, Map<String, Definition>> localeSpecificDefinitions;

    /**
     * The locale resolver object.
     */
    private LocaleResolver localeResolver;

    /**
     * Creates a new instance of UrlDefinitionsFactory.
     */
    public UrlDefinitionsFactory() {
        sourceURLs = new ArrayList<URL>();
        lastModifiedDates = new HashMap<String, Long>();
        processedLocales = new ArrayList<Locale>();
    }

    /** {@inheritDoc} */
    public void setApplicationContext(TilesApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Sets the source URLs to use.
     *
     * @param sourceURLs The source URLs.
     */
    public void setSourceURLs(List<URL> sourceURLs) {
        this.sourceURLs = sourceURLs;
    }

    /**
     * Sets the definitions reader that will read the URLs.
     *
     * @param reader The definitions reader.
     * @since 2.1.0
     */
    public void setReader(DefinitionsReader reader) {
        this.reader = reader;
    }

    /**
     * Sets the locale resolver to use.
     *
     * @param localeResolver The locale resolver.
     * @since 2.1.0
     */
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    /**
     * Initializes the DefinitionsFactory and its subcomponents.
     * <p/>
     * Implementations may support configuration properties to be passed in via
     * the params Map.
     *
     * @param params The Map of configuration properties.
     * @throws DefinitionsFactoryException if an initialization error occurs.
     */
    public void init(Map<String, String> params) {
        identifySources(params);
        String readerClassName =
            params.get(DefinitionsFactory.READER_IMPL_PROPERTY);

        if (readerClassName != null) {
            reader = (DefinitionsReader) ClassUtil.instantiate(readerClassName);
        } else {
            reader = new DigesterDefinitionsReader();
        }
        reader.init(params);

        String resolverClassName = params
                .get(DefinitionsFactory.LOCALE_RESOLVER_IMPL_PROPERTY);
        if (resolverClassName != null) {
            localeResolver = (LocaleResolver) ClassUtil.instantiate(resolverClassName);
        } else {
            localeResolver = new DefaultLocaleResolver();
        }
        localeResolver.init(params);
        loadDefinitions();
    }

    /**
     * Returns the definitions holder object.
     *
     * @return The definitions holder.
     * @deprecated Do not use! Deprecated with no replacement.
     */
    @Deprecated
    protected Definitions getDefinitions() {
        return new DefinitionsImpl(baseDefinitions, localeSpecificDefinitions);
    }


    /**
     * Returns a Definition object that matches the given name and
     * Tiles context.
     *
     * @param name         The name of the Definition to return.
     * @param tilesContext The Tiles context to use to resolve the definition.
     * @return the Definition matching the given name or null if none
     *         is found.
     * @throws DefinitionsFactoryException if an error occurs reading definitions.
     */
    public Definition getDefinition(String name,
            TilesRequestContext tilesContext) {

        Locale locale = null;

        if (tilesContext != null) {
            locale = localeResolver.resolveLocale(tilesContext);
            if (!isContextProcessed(tilesContext)) {
                addDefinitions(tilesContext);
            }
        }

        return getDefinition(name, locale);
    }

    /**
     * Adds a source where Definition objects are stored.
     * <p/>
     * Implementations should publish what type of source object they expect.
     * The source should contain enough information to resolve a configuration
     * source containing definitions.  The source should be a "base" source for
     * configurations.  Internationalization and Localization properties will be
     * applied by implementations to discriminate the correct data sources based
     * on locale.
     *
     * @param source The configuration source for definitions.
     * @throws DefinitionsFactoryException if an invalid source is passed in or
     *                                     an error occurs resolving the source to an actual data store.
     * @deprecated Do not call it, let the Definitions Factory load the sources
     * by itself.
     */
    public void addSource(Object source) {
        if (source == null) {
            throw new DefinitionsFactoryException(
                "Source object must not be null");
        }

        if (!(source instanceof URL)) {
            throw new DefinitionsFactoryException(
                "Source object must be an URL");
        }

        sourceURLs.add((URL) source);
    }

    /**
     * Appends locale-specific {@link Definition} objects to an existing
     * {@link Definitions} set by reading locale-specific versions of
     * the applied sources.
     *
     * @param definitions  The Definitions object to append to.
     * @param tilesContext The requested locale.
     * @throws DefinitionsFactoryException if an error occurs reading definitions.
     * @deprecated Use {@link #addDefinitions(TilesRequestContext)}.
     */
    protected void addDefinitions(Definitions definitions,
            TilesRequestContext tilesContext) {
        addDefinitions(tilesContext);
    }

    /**
     * Appends locale-specific {@link Definition} objects to existing
     * definitions set by reading locale-specific versions of the applied
     * sources.
     *
     * @param tilesContext The requested locale.
     * @throws DefinitionsFactoryException if an error occurs reading
     * definitions.
     * @since 2.1.0
     */
    protected synchronized void addDefinitions(TilesRequestContext tilesContext) {

        Locale locale = localeResolver.resolveLocale(tilesContext);

        if (isContextProcessed(tilesContext)) {
            return;
        }

        if (locale == null) {
            return;
        }

        processedLocales.add(locale);
        List<String> postfixes = calculatePostfixes(locale);
        Map<String, Definition> localeDefsMap = new HashMap<String, Definition>();
        for (Object postfix : postfixes) {
            // For each postfix, all the sources must be loaded.
            for (URL url : sourceURLs) {
                String path = url.toExternalForm();

                String newPath = concatPostfix(path, (String) postfix);
                try {
                    URL newUrl = new URL(newPath);
                    URLConnection connection = newUrl.openConnection();
                    connection.connect();
                    lastModifiedDates.put(newUrl.toExternalForm(),
                        connection.getLastModified());

                    // Definition must be collected, starting from the base
                    // source up to the last localized file.
                    Map<String, Definition> defsMap = reader
                            .read(connection.getInputStream());
                    if (defsMap != null) {
                        localeDefsMap.putAll(defsMap);
                    }
                } catch (FileNotFoundException e) {
                    // File not found. continue.
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("File " + newPath + " not found, continue");
                    }
                } catch (IOException e) {
                    throw new DefinitionsFactoryException(
                        "I/O error processing configuration.");
                }
            }
        }

        // At the end of definitions loading, they can be assigned to
        // Definitions implementation, to allow inheritance resolution.
        localeSpecificDefinitions.put(locale, localeDefsMap);
        resolveInheritances(locale);
    }

    /**
     * Creates and returns a {@link Definitions} set by reading
     * configuration data from the applied sources.
     *
     * @return The definitions holder object, filled with base definitions.
     * @throws DefinitionsFactoryException if an error occurs reading the
     * sources.
     * @deprecated Let the Definitions Factory use it.
     */
    @Deprecated
    public Definitions readDefinitions() {
        loadDefinitions();
        return new DefinitionsImpl(baseDefinitions, localeSpecificDefinitions);
    }

    /**
     * Indicates whether a given context has been processed or not.
     * <p/>
     * This method can be used to avoid unnecessary synchronization of the
     * DefinitionsFactory in multi-threaded situations.  Check the return of
     * isContextProcessed before synchronizing the object and reading
     * locale-specific definitions.
     *
     * @param tilesContext The Tiles context to check.
     * @return true if the given context has been processed and false otherwise.
     */
    protected boolean isContextProcessed(TilesRequestContext tilesContext) {
        return processedLocales.contains(localeResolver
                .resolveLocale(tilesContext));
    }

    /**
     * Creates a new instance of <code>Definitions</code>. Override this method
     * to provide your custom instance of Definitions.
     *
     * @return A new instance of <code>Definitions</code>.
     * @deprecated Do not use! Deprecated with no replacement.
     */
    @Deprecated
    protected Definitions createDefinitions() {
        return new DefinitionsImpl();
    }

    /**
     * Concat postfix to the name. Take care of existing filename extension.
     * Transform the given name "name.ext" to have "name" + "postfix" + "ext".
     * If there is no ext, return "name" + "postfix".
     *
     * @param name    Filename.
     * @param postfix Postfix to add.
     * @return Concatenated filename.
     */
    protected String concatPostfix(String name, String postfix) {
        if (postfix == null) {
            return name;
        }

        // Search file name extension.
        // take care of Unix files starting with .
        int dotIndex = name.lastIndexOf(".");
        int lastNameStart = name.lastIndexOf(java.io.File.pathSeparator);
        if (dotIndex < 1 || dotIndex < lastNameStart) {
            return name + postfix;
        }

        String ext = name.substring(dotIndex);
        name = name.substring(0, dotIndex);
        return name + postfix + ext;
    }

    /**
     * Creates a base set by reading configuration data from the applied
     * sources.
     *
     * @throws DefinitionsFactoryException if an error occurs reading the
     * sources.
     * @since 2.1.0
     */
    protected synchronized void loadDefinitions() {
        reset();

        try {
            for (URL source : sourceURLs) {
                URLConnection connection = source.openConnection();
                connection.connect();
                lastModifiedDates.put(source.toExternalForm(),
                    connection.getLastModified());
                Map<String, Definition> defsMap = reader
                        .read(connection.getInputStream());
                baseDefinitions.putAll(defsMap);
                resolveInheritances();
            }
        } catch (IOException e) {
            throw new DefinitionsFactoryException("I/O error accessing source.", e);
        }
    }

    /**
     * Calculate the postfixes along the search path from the base bundle to the
     * bundle specified by baseName and locale.
     * Method copied from java.util.ResourceBundle
     *
     * @param locale the locale
     * @return a list of
     */
    protected static List<String> calculatePostfixes(Locale locale) {
        final List<String> result = new ArrayList<String>();
        final String language = locale.getLanguage();
        final int languageLength = language.length();
        final String country = locale.getCountry();
        final int countryLength = country.length();
        final String variant = locale.getVariant();
        final int variantLength = variant.length();

        // The default configuration file must be loaded to allow correct
        // definition inheritance.
        result.add("");
        if (languageLength + countryLength + variantLength == 0) {
            //The locale is "", "", "".
            return result;
        }

        final StringBuffer temp = new StringBuffer();
        temp.append('_');
        temp.append(language);

        if (languageLength > 0) {
            result.add(temp.toString());
        }

        if (countryLength + variantLength == 0) {
            return result;
        }

        temp.append('_');
        temp.append(country);

        if (countryLength > 0) {
            result.add(temp.toString());
        }

        if (variantLength == 0) {
            return result;
        } else {
            temp.append('_');
            temp.append(variant);
            result.add(temp.toString());
            return result;
        }
    }


    /** {@inheritDoc} */
    public synchronized void refresh() {
        LOG.debug("Updating Tiles definitions. . .");
        processedLocales.clear();
        loadDefinitions();
    }


    /**
     * Indicates whether the DefinitionsFactory is out of date and needs to be
     * reloaded.
     *
     * @return If the factory needs refresh.
     */
    public boolean refreshRequired() {
        boolean status = false;

        Set<String> urls = lastModifiedDates.keySet();

        try {
            for (String urlPath : urls) {
                Long lastModifiedDate = lastModifiedDates.get(urlPath);
                URL url = new URL(urlPath);
                URLConnection connection = url.openConnection();
                connection.connect();
                long newModDate = connection.getLastModified();
                if (newModDate != lastModifiedDate) {
                    status = true;
                    break;
                }
            }
        } catch (Exception e) {
            LOG.warn("Exception while monitoring update times.", e);
            return true;
        }
        return status;
    }

    /**
     * Detects the sources to load.
     *
     * @param initParameters The initialization parameters.
     * @since 2.1.0
     */
    protected void identifySources(Map<String, String> initParameters) {
        if (applicationContext == null) {
            throw new IllegalStateException(
                    "The TilesApplicationContext cannot be null");
        }

        String resourceString = getResourceString(initParameters);
        List<String> resources = getResourceNames(resourceString);

        try {
            for (String resource : resources) {
                URL resourceUrl = applicationContext.getResource(resource);
                if (resourceUrl != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding resource '" + resourceUrl + "' to definitions factory.");
                    }
                    sourceURLs.add(resourceUrl);
                } else {
                    LOG.warn("Unable to find configured definition '" + resource + "'");
                }
            }
        } catch (IOException e) {
            throw new DefinitionsFactoryException("Unable to parse definitions from "
                + resourceString, e);
        }
    }

    /**
     * Derive the resource string from the initialization parameters. If no
     * parameter {@link DefinitionsFactory#DEFINITIONS_CONFIG} is available,
     * attempts to retrieve {@link BasicTilesContainer#DEFINITIONS_CONFIG} and
     * {@link UrlDefinitionsFactory#LEGACY_DEFINITIONS_CONFIG}. If neither are
     * available, returns "/WEB-INF/tiles.xml".
     *
     * @param parms The initialization parameters.
     * @return resource string to be parsed.
     */
    @SuppressWarnings("deprecation")
    protected String getResourceString(Map<String, String> parms) {
        String resourceStr = parms.get(DefinitionsFactory.DEFINITIONS_CONFIG);
        if (resourceStr == null) {
            resourceStr = parms.get(BasicTilesContainer.DEFINITIONS_CONFIG);
        }
        if (resourceStr == null) {
            resourceStr = parms.get(UrlDefinitionsFactory.LEGACY_DEFINITIONS_CONFIG);
        }
        if (resourceStr == null) {
            resourceStr = "/WEB-INF/tiles.xml";
        }
        return resourceStr;
    }

    /**
     * Parse the resourceString into a list of resource paths
     * which can be loaded by the application context.
     *
     * @param resourceString comma seperated resources
     * @return parsed resources
     */
    protected List<String> getResourceNames(String resourceString) {
        StringTokenizer tokenizer = new StringTokenizer(resourceString, ",");
        List<String> filenames = new ArrayList<String>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            filenames.add(tokenizer.nextToken().trim());
        }
        return filenames;
    }

    /**
     * Clears definitions.
     *
     * @since 2.1.0
     */
    protected void reset() {
        this.baseDefinitions = new HashMap<String, Definition>();
        this.localeSpecificDefinitions =
            new HashMap<Locale, Map<String, Definition>>();
    }

    /**
     * Resolve extended instances.
     *
     * @throws NoSuchDefinitionException If a parent definition is not found.
     * @since 2.1.0
     */
    protected void resolveInheritances() {
        Set<String> alreadyResolvedDefinitions = new HashSet<String>();

        for (Definition definition : baseDefinitions.values()) {
            resolveInheritance(definition, null, alreadyResolvedDefinitions);
        }  // end loop
    }

    /**
     * Resolve locale-specific extended instances.
     *
     * @param locale The locale to use.
     * @throws NoSuchDefinitionException If a parent definition is not found.
     * @since 2.1.0
     */
    protected void resolveInheritances(Locale locale) {
        resolveInheritances();

        Map<String, Definition> map = localeSpecificDefinitions.get(locale);
        if (map != null) {
            Set<String> alreadyResolvedDefinitions = new HashSet<String>();
            for (Definition definition : map.values()) {
                resolveInheritance(definition, locale,
                        alreadyResolvedDefinitions);
            }  // end loop
        }
    }

    /**
     * Resolve locale-specific inheritance.
     * First, resolve parent's inheritance, then set template to the parent's
     * template.
     * Also copy attributes setted in parent, and not set in child
     * If instance doesn't extend anything, do nothing.
     *
     * @param definition The definition to resolve
     * @param locale The locale to use.
     * @param alreadyResolvedDefinitions The set of the definitions that have
     * been already resolved.
     * @throws NoSuchDefinitionException If an inheritance can not be solved.
     * @since 2.1.0
     */
    protected void resolveInheritance(Definition definition, Locale locale,
            Set<String> alreadyResolvedDefinitions) {
        // Already done, or not needed ?
        if (!definition.isExtending()
                || alreadyResolvedDefinitions.contains(definition.getName())) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolve definition for child name='"
                + definition.getName()
                + "' extends='" + definition.getExtends() + "'.");
        }

        // Set as visited to avoid endless recurisvity.
        alreadyResolvedDefinitions.add(definition.getName());

        // Resolve parent before itself.
        Definition parent = getDefinition(definition.getExtends(),
            locale);
        if (parent == null) { // error
            String msg = "Error while resolving definition inheritance: child '"
                + definition.getName()
                + "' can't find its ancestor '"
                + definition.getExtends()
                + "'. Please check your description file.";
            // to do : find better exception
            throw new NoSuchDefinitionException(msg);
        }

        resolveInheritance(parent, locale, alreadyResolvedDefinitions);

        definition.inherit(parent);
    }

    /**
     * Returns a Definition object that matches the given name and locale.
     *
     * @param name The name of the Definition to return.
     * @param locale The locale to use to resolve the definition.
     * @return the Definition matching the given name or null if none is found.
     * @since 2.1.0
     */
    protected Definition getDefinition(String name, Locale locale) {
        Definition definition = null;

        if (locale != null) {
            Map<String, Definition> localeSpecificMap =
                localeSpecificDefinitions.get(locale);
            if (localeSpecificMap != null) {
                definition = localeSpecificMap.get(name);
            }
        }

        if (definition == null) {
            definition = baseDefinitions.get(name);
        }

        return definition;
    }
}
