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
import org.apache.tiles.Initializable;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.dao.DefinitionDAO;
import org.apache.tiles.definition.dao.LocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.URLReader;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.util.LocaleUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * {@link DefinitionsFactory DefinitionsFactory} implementation that manages
 * Definitions configuration data from URLs, resolving inheritance when the URL
 * is loaded. <p/>
 * <p>
 * The Definition objects are read from the
 * {@link org.apache.tiles.definition.digester.DigesterDefinitionsReader DigesterDefinitionsReader}
 * class unless another implementation is specified.
 * </p>
 *
 * @version $Rev$ $Date$
 */
public class UrlDefinitionsFactory extends LocaleDefinitionsFactory implements DefinitionsFactory,
        Refreshable, TilesApplicationContextAware,
        Initializable {

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
     * Contains the URL objects identifying where configuration data is found.
     *
     * @deprecated Use {@link #sourceURLs}.
     */
    protected List<Object> sources;

    /**
     * Reader used to get definitions from the sources.
     *
     * @deprecated No more used.
     */
    protected DefinitionsReader reader;

    /**
     * Contains the dates that the URL sources were last modified.
     *
     * @deprecated No more used.
     */
    protected Map<String, Long> lastModifiedDates;

    /**
     * Creates a new instance of UrlDefinitionsFactory.
     */
    public UrlDefinitionsFactory() {
        definitionDao = new LocaleUrlDefinitionDAO();
        processedLocales = new ArrayList<Locale>();
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
    @SuppressWarnings("unchecked")
    public void init(Map<String, String> params) {
        super.init(params);
        loadDefinitions();
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
    @Override
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
        return (definitionDao instanceof RefreshMonitor)
                && ((RefreshMonitor) definitionDao).refreshRequired();
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

        // At the end of definitions loading, they can be assigned to
        // Definitions implementation, to allow inheritance resolution.
        localeSpecificDefinitions.put(locale, definitionDao
                .getDefinitions(locale));
        resolveInheritances(locale);
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
     * Creates a base set by reading configuration data from the applied
     * sources.
     *
     * @throws DefinitionsFactoryException if an error occurs reading the
     * sources.
     * @since 2.1.0
     */
    protected synchronized void loadDefinitions() {
        reset();

        baseDefinitions.putAll(definitionDao.getDefinitions(null));
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
     * @deprecated Use {@link URLReader#addSourceURL(URL)}.
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

        if (definitionDao instanceof URLReader) {
            ((URLReader) definitionDao).addSourceURL((URL) source);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected DefinitionDAO<Locale> createDefaultDefinitionDAO() {
        return new LocaleUrlDefinitionDAO();
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
     * Appends locale-specific {@link Definition} objects to an existing
     * {@link Definitions} set by reading locale-specific versions of
     * the applied sources.
     *
     * @param definitions  The Definitions object to append to.
     * @param tilesContext The requested locale.
     * @throws DefinitionsFactoryException if an error occurs reading definitions.
     * @deprecated Use {@link #addDefinitions(TilesRequestContext)}.
     */
    @Deprecated
    protected void addDefinitions(Definitions definitions,
            TilesRequestContext tilesContext) {
        addDefinitions(tilesContext);
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
     * @deprecated Use {@link LocaleUtil#concatPostfix(String,String)} instead
     */
    protected static String concatPostfix(String name, String postfix) {
        return LocaleUtil.concatPostfix(name, postfix);
    }

    /**
     * Calculate the postfixes along the search path from the base bundle to the
     * bundle specified by baseName and locale.
     * Method copied from java.util.ResourceBundle
     *
     * @param locale the locale
     * @return a list of
     * @deprecated Use {@link LocaleUtil#calculatePostfixes(Locale)} instead.
     */
    protected static List<String> calculatePostfixes(Locale locale) {
        return LocaleUtil.calculatePostfixes(locale);
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
     * @deprecated Deprecated without replacement.
     */
    @Deprecated
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
     * @deprecated Deprecated without replacement.
     */
    @Deprecated
    protected List<String> getResourceNames(String resourceString) {
        StringTokenizer tokenizer = new StringTokenizer(resourceString, ",");
        List<String> filenames = new ArrayList<String>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            filenames.add(tokenizer.nextToken().trim());
        }
        return filenames;
    }
}
