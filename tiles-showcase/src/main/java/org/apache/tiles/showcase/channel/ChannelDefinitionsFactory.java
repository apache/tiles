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
 *
 */

package org.apache.tiles.showcase.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.ComponentDefinitions;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.UrlDefinitionsFactory;

/**
 * Channel Definitions factory.
 * A definition is retrieved by its name, and using locale setted in appropriate 
 * session context.  Definitions are defined in different files, one for each locale. 
 * A definition file is loaded using common name extended with locale code 
 * (ex : templateDefinitions_fr.xml). If no file is found under this name, use default 
 * file.
 *
 * @version $Rev$ $Date$
 */
public class ChannelDefinitionsFactory extends UrlDefinitionsFactory {

    // FIXME This class contains too much code from UrlDefinitionsFactory.
    // Probably that class must be refactored to allow an easier extension, but
    // maybe we should wait until Dimensions is incubated.
    
    private static final Log log = LogFactory.getLog(ChannelDefinitionsFactory.class);

    /** 
     * Config file parameter name. 
     */
    public static final String FACTORY_SELECTOR_KEY =
        "ChannelDefinitionsFactory.factorySelectorKey";

    /**
     * Contains a list of locales that have been processed.
     */
    private List<String> processedLocales;

    private ComponentDefinitions definitions;

    /**
     * Returns a ComponentDefinition object that matches the given name and
     * Tiles context
     *
     * @param name         The name of the ComponentDefinition to return.
     * @param tilesContext The Tiles context to use to resolve the definition.
     * @return the ComponentDefinition matching the given name or null if none
     *         is found.
     * @throws DefinitionsFactoryException if an error occurs reading definitions.
     */
    @Override
    public ComponentDefinition getDefinition(String name,
                                             TilesRequestContext tilesContext)
        throws DefinitionsFactoryException {

        ComponentDefinitions definitions = getComponentDefinitions();
        String key = getLocaleKey(tilesContext);
        Locale locale = getLocale(key);

        if (tilesContext != null) {
            locale = tilesContext.getRequestLocale();
            if (!isContextProcessed(tilesContext)) {
                synchronized (definitions) {
                    addDefinitions(definitions, tilesContext);
                }
            }
        }

        return definitions.getDefinition(name, locale);
    }

    /**
     * Appends locale-specific {@link ComponentDefinition} objects to an existing
     * {@link ComponentDefinitions} set by reading locale-specific versions of
     * the applied sources.
     *
     * @param definitions  The ComponentDefinitions object to append to.
     * @param tilesContext The requested locale.
     * @throws DefinitionsFactoryException if an error occurs reading definitions.
     */
    @Override
    protected void addDefinitions(ComponentDefinitions definitions,
                                  TilesRequestContext tilesContext)
        throws DefinitionsFactoryException {
        String key = getLocaleKey(tilesContext);

        if (isContextProcessed(tilesContext)) {
            return;
        }
        
        if (key == null) {
            return;
        }

        processedLocales.add(key);
        Locale locale = getLocale(key);
        List<String> postfixes = calculatePostfixes(locale);
        Map localeDefsMap = new HashMap();
        for (Object postfix : postfixes) {
            // For each postfix, all the sources must be loaded.
            for (Object source : sources) {
                URL url = (URL) source;
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
                    Map defsMap = reader.read(connection.getInputStream());
                    if (defsMap != null) {
                        localeDefsMap.putAll(defsMap);
                    }
                } catch (FileNotFoundException e) {
                    // File not found. continue.
                } catch (IOException e) {
                    throw new DefinitionsFactoryException(
                        "I/O error processing configuration.");
                }
            }
        }
        
        // At the end of definitions loading, they can be assigned to
        // ComponentDefinitions implementation, to allow inheritance resolution.
        definitions.addDefinitions(localeDefsMap, locale);
    }

    /**
     * Extract key that will be used to get the sub factory.
     * @param name Name of requested definition
     * @param request Current servlet request.
     * @param servletContext Current servlet context
     * @return the key or null if not found.
     * @roseuid 3AF6F887018A
     */
    @Override
    protected boolean isContextProcessed(TilesRequestContext tilesContext) {
        String key = null;

        Map session = tilesContext.getSessionScope();
        if (session != null) {
            key = (String) session.get(FACTORY_SELECTOR_KEY);
        }
        
        return processedLocales.contains(key);
    }

    private ComponentDefinitions getComponentDefinitions()
        throws DefinitionsFactoryException {
        if (definitions == null) {
            definitions = readDefinitions();
        }
        return definitions;
    }
    
    private String getLocaleKey(TilesRequestContext tilesContext) {
        String key = null;

        Map session = tilesContext.getSessionScope();
        if (session != null) {
            key = (String) session.get(FACTORY_SELECTOR_KEY);
        }
        
        return key;
    }
    
    private Locale getLocale(String localeKey) {
        Locale retValue = null;
        String[] localeArray;
        
        if (localeKey != null) {
            localeArray = localeKey.split("_");
            
            retValue = new Locale(localeArray.length > 0 ? localeArray[0] : null,
                    localeArray.length > 1 ? localeArray[1] : null,
                    localeArray.length > 2 ? localeArray[2] : null);
        }
        
        return retValue;
    }
}
