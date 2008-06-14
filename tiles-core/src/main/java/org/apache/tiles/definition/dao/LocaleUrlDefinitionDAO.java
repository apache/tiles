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
package org.apache.tiles.definition.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Definition;
import org.apache.tiles.Initializable;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.RefreshMonitor;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.util.ClassUtil;
import org.apache.tiles.util.LocaleUtil;

/**
 * A definition DAO that uses {@link Locale} as a customization key and loads
 * definitions from URLs.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class LocaleUrlDefinitionDAO implements DefinitionDAO<Locale>,
        Initializable, TilesApplicationContextAware, RefreshMonitor, URLReader {

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory
            .getLog(LocaleUrlDefinitionDAO.class);

    /**
     * Compatibility constant.
     *
     * @deprecated use {@link DEFINITIONS_CONFIG} to avoid namespace collisions.
     */
    private static final String LEGACY_DEFINITIONS_CONFIG = "definitions-config";

    /**
     * Contains the URL objects identifying where configuration data is found.
     *
     * @since 2.1.0
     */
    protected List<URL> sourceURLs;

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
     * Reader used to get definitions from the sources.
     */
    protected DefinitionsReader reader;

    /**
     * Constructor.
     */
    public LocaleUrlDefinitionDAO() {
        sourceURLs = new ArrayList<URL>();
        lastModifiedDates = new HashMap<String, Long>();
    }

    /**
     * <p>
     * Returns a definition, given its name and the customization key.
     * </p>
     * <strong>WARNING!</strong> This method is slow! It loads all the
     * definitions and then selects the needed one.
     *
     * @param name The name of the definition.
     * @param customizationKey The customization key.
     * @return The requested definition, if found, otherwise <code>null</code>.
     * The inheritance of the definition must not be resolved.
     * @since 2.1.0
     */
    public Definition getDefinition(String name, Locale customizationKey) {
        Map<String, Definition> defsMap = getDefinitions(customizationKey);
        return defsMap != null ? defsMap.get(name) : null;
    }

    /** {@inheritDoc} */
    public Map<String, Definition> getDefinitions(Locale customizationKey) {
        List<String> postfixes = LocaleUtil.calculatePostfixes(customizationKey);
        Map<String, Definition> localeDefsMap = new HashMap<String, Definition>();
        for (Object postfix : postfixes) {
            // For each postfix, all the sources must be loaded.
            for (URL url : sourceURLs) {
                String path = url.toExternalForm();

                String newPath = LocaleUtil.concatPostfix(path,
                        (String) postfix);
                try {
                    URL newUrl = new URL(newPath);
                    URLConnection connection = newUrl.openConnection();
                    connection.connect();
                    lastModifiedDates.put(newUrl.toExternalForm(), connection
                            .getLastModified());

                    // Definition must be collected, starting from the base
                    // source up to the last localized file.
                    Map<String, Definition> defsMap = reader.read(connection
                            .getInputStream());
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
                            "I/O error processing configuration.", e);
                }
            }
        }

        return localeDefsMap;
    }

    /**  {@inheritDoc}*/
    public void setSourceURLs(List<URL> sourceURLs) {
        this.sourceURLs = sourceURLs;
    }

    /**  {@inheritDoc}*/
    public void setReader(DefinitionsReader reader) {
        this.reader = reader;
    }

    /**  {@inheritDoc}*/
    public void addSourceURL(URL sourceURL) {
        if (sourceURLs == null) {
            sourceURLs = new ArrayList<URL>();
        }
        sourceURLs.add(sourceURL);
    }

    /** {@inheritDoc} */
    public void setApplicationContext(TilesApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /** {@inheritDoc} */
    public void init(Map<String, String> params) {
        identifySources(params);
        String readerClassName = params
                .get(DefinitionsFactory.READER_IMPL_PROPERTY);

        if (readerClassName != null) {
            reader = (DefinitionsReader) ClassUtil.instantiate(readerClassName);
        } else {
            reader = new DigesterDefinitionsReader();
        }
        reader.init(params);
    }

    /** {@inheritDoc} */
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
        String[] resources = getResourceNames(resourceString);

        try {
            for (int i = 0; i < resources.length; i++) {
                URL resourceUrl = applicationContext.getResource(resources[i]);
                if (resourceUrl != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding resource '" + resourceUrl
                                + "' to definitions factory.");
                    }
                    sourceURLs.add(resourceUrl);
                } else {
                    LOG.warn("Unable to find configured definition '"
                            + resources[i] + "'");
                }
            }
        } catch (IOException e) {
            throw new DefinitionsFactoryException(
                    "Unable to parse definitions from " + resourceString, e);
        }
    }

    /**
     * Derive the resource string from the initialization parameters. If no
     * parameter {@link DefinitionsFactory#DEFINITIONS_CONFIG} is available,
     * attempts to retrieve {@link BasicTilesContainer#DEFINITIONS_CONFIG} and
     * {@link #LEGACY_DEFINITIONS_CONFIG}. If neither are available, returns
     * "/WEB-INF/tiles.xml".
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
            resourceStr = parms.get(LEGACY_DEFINITIONS_CONFIG);
        }
        if (resourceStr == null) {
            resourceStr = "/WEB-INF/tiles.xml";
        }
        return resourceStr;
    }

    /**
     * Parse the resourceString into a list of resource paths which can be
     * loaded by the application context.
     *
     * @param resourceString comma separated resources
     * @return parsed resources
     */
    protected String[] getResourceNames(String resourceString) {
        return resourceString.split(",");
    }
}
