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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.tiles.Definition;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.dao.DefinitionDAO;
import org.apache.tiles.definition.dao.ResolvingLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.URLReader;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.locale.impl.DefaultLocaleResolver;
import org.apache.tiles.util.LocaleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DefinitionsFactory DefinitionsFactory} implementation that manages
 * Definitions configuration data from URLs, resolving inheritance when the URL
 * is loaded.
 * <p/>
 * <p>
 * The Definition objects are read from the
 * {@link org.apache.tiles.definition.digester.DigesterDefinitionsReader
 * DigesterDefinitionsReader} class unless another implementation is specified.
 * </p>
 *
 * @version $Rev$ $Date$
 * @deprecated Use {@link UnresolvingLocaleDefinitionsFactory} and using
 * {@link ResolvingLocaleUrlDefinitionDAO} as Tiles DAO.
 */
public class UrlDefinitionsFactory extends UnresolvingLocaleDefinitionsFactory
        implements Refreshable {

    /**
     * Compatibility constant.
     *
     * @deprecated use {@link DefinitionsFactory#DEFINITIONS_CONFIG} to avoid
     * namespace collisions.
     */
    private static final String LEGACY_DEFINITIONS_CONFIG = "definitions-config";

    /**
     * LOG instance for all UrlDefinitionsFactory instances.
     */
    private final Logger log = LoggerFactory
            .getLogger(UrlDefinitionsFactory.class);

    /**
     * Contains the URL objects identifying where configuration data is found.
     *
     * @deprecated Use {@link URLReader#addSourceURL(URL)}.
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

    /** {@inheritDoc} */
    public synchronized void refresh() {
        log.debug("Updating Tiles definitions. . .");
        if (definitionDao instanceof Refreshable) {
            ((Refreshable) definitionDao).refresh();
        }
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
     * Indicates whether a given context has been processed or not.
     * <p/>
     * This method can be used to avoid unnecessary synchronization of the
     * DefinitionsFactory in multi-threaded situations.  Check the return of
     * isContextProcessed before synchronizing the object and reading
     * locale-specific definitions.
     *
     * @param tilesContext The Tiles context to check.
     * @return true if the given context has been processed and false otherwise.
     * @deprecated It always return <code>true</code>.
     */
    @Deprecated
    protected boolean isContextProcessed(TilesRequestContext tilesContext) {
        return true;
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

    /**
     * Creates the default definition DAO, if it has not been specified outside.
     *
     * @return The default definition DAO.
     * @since 2.1.0
     */
    protected ResolvingLocaleUrlDefinitionDAO createDefaultDefinitionDAO() {
        return new ResolvingLocaleUrlDefinitionDAO();
    }

    @Override
    public void init(Map<String, String> params) {
        super.init(params);
        setLocaleResolver(new DefaultLocaleResolver());
        ResolvingLocaleUrlDefinitionDAO dao = createDefaultDefinitionDAO();
        dao.setApplicationContext(applicationContext);
        dao.init(params);
        setDefinitionDAO(dao);

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
        return new CompatibilityDefinitionsImpl(definitionDao);
    }

    /**
     * Returns the definitions holder object.
     *
     * @return The definitions holder.
     * @deprecated Do not use! Deprecated with no replacement.
     */
    @Deprecated
    protected Definitions getDefinitions() {
        return new CompatibilityDefinitionsImpl(definitionDao);
    }

    /**
     * Appends locale-specific {@link Definition} objects to an existing
     * {@link Definitions} set by reading locale-specific versions of
     * the applied sources.
     *
     * @param definitions  The Definitions object to append to.
     * @param tilesContext The requested locale.
     * @throws DefinitionsFactoryException if an error occurs reading definitions.
     * @deprecated Let the definitions be loaded by a {@link DefinitionDAO}.
     */
    @Deprecated
    protected void addDefinitions(Definitions definitions,
            TilesRequestContext tilesContext) {
        Locale locale = localeResolver.resolveLocale(tilesContext);
        Map<String, Definition> defsMap = definitionDao.getDefinitions(locale);
        if (defsMap == null) {
            throw new NullPointerException(
                    "There are no definitions mapped to locale '"
                            + locale.toString() + "'");
        }
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
        return new CompatibilityDefinitionsImpl(definitionDao);
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

    /**
     * {@link Definitions} implementation that uses a {@link DefinitionDAO}.
     *
     * @since 2.1.0
     * @deprecated Here only for compatibility reasons.
     */
    @Deprecated
    private static final class CompatibilityDefinitionsImpl implements Definitions {

        /**
         * The definition DAO to use.
         *
         * @since 2.1.0
         */
        private DefinitionDAO<Locale> definitionDao;

        /**
         * Constructor.
         *
         * @param definitionDao The definition DAO to use.
         * @since 2.1.0
         */
        public CompatibilityDefinitionsImpl(DefinitionDAO<Locale> definitionDao) {
            this.definitionDao = definitionDao;
        }

        /** {@inheritDoc} */
        public void addDefinitions(Map<String, Definition> defsMap) {
            Map<String, Definition> definitions = definitionDao
                    .getDefinitions(null);
            if (definitions == null) {
                throw new NullPointerException(
                        "No definitions loaded for default locale");
            }
            definitions.putAll(defsMap);
        }

        /** {@inheritDoc} */
        public void addDefinitions(Map<String, Definition> defsMap,
                Locale locale) {
            Map<String, Definition> definitions = definitionDao
                    .getDefinitions(locale);
            if (definitions == null) {
                throw new NullPointerException(
                        "No definitions loaded for locale '"
                                + locale.toString() + "'");
            }
            definitions.putAll(defsMap);
        }

        /** {@inheritDoc} */
        public Map<String, Definition> getBaseDefinitions() {
            return definitionDao.getDefinitions(null);
        }

        /** {@inheritDoc} */
        public Definition getDefinition(String name) {
            return definitionDao.getDefinition(name, null);
        }

        /** {@inheritDoc} */
        public Definition getDefinition(String name, Locale locale) {
            return definitionDao.getDefinition(name, locale);
        }

        /** {@inheritDoc} */
        public void reset() {
            if (definitionDao instanceof Refreshable) {
                ((Refreshable) definitionDao).refresh();
            }
        }

        /** {@inheritDoc} */
        public void resolveInheritances() {
            // Does nothing.
        }

        /** {@inheritDoc} */
        public void resolveInheritances(Locale locale) {
            // Does nothing.
        }
    }
}
