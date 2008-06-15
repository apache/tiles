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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.util.LocaleUtil;

/**
 * A definition DAO that uses {@link Locale} as a customization key and loads
 * definitions from URLs.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class LocaleUrlDefinitionDAO extends BaseLocaleUrlDefinitionDAO {

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
                    Map<String, Definition> defsMap = loadDefinitionsFromURL(newUrl);
                    if (defsMap != null) {
                        localeDefsMap.putAll(defsMap);
                    }
                } catch (MalformedURLException e) {
                    throw new DefinitionsFactoryException("Error parsing URL "
                        + newPath, e);
                }
            }
        }

        return localeDefsMap;
    }
}
