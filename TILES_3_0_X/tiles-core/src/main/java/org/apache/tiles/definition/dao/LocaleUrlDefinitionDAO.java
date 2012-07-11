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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.Definition;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.locale.LocaleUtil;

/**
 * A definition DAO that uses {@link Locale} as a customization key and loads
 * definitions from URLs. It does not cache definitions in any way.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class LocaleUrlDefinitionDAO extends BaseLocaleUrlDefinitionDAO {

    public LocaleUrlDefinitionDAO(ApplicationContext applicationContext) {
        super(applicationContext);
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
        return defsMap.get(name);
    }

    /** {@inheritDoc} */
    public Map<String, Definition> getDefinitions(Locale customizationKey) {
        ArrayList<Locale> postfixes = computeLocales(customizationKey);
        Map<String, Definition> localeDefsMap = new HashMap<String, Definition>();
        // process the postfixes from the root to the most specific
        for (Locale postfix : postfixes) {
            // For each postfix, all the sources must be loaded.
            for (ApplicationResource resource : sources) {
                ApplicationResource newResource = applicationContext.getResource(resource, postfix);
                if (newResource != null) {
                    Map<String, Definition> defsMap = loadDefinitionsFromResource(newResource);
                    if (defsMap != null) {
                        localeDefsMap.putAll(defsMap);
                    }
                }
            }
        }
        return localeDefsMap;
    }

    /**
     * Returns a list of locales from root to the customizationKey.
     * @param customizationKey the target Locale.
     * @return the list of its ancestors.
     */
    private ArrayList<Locale> computeLocales(Locale customizationKey) {
        Locale postfix;
        if(customizationKey == null) {
            postfix = Locale.ROOT;
        } else {
            postfix = customizationKey;
        }
        ArrayList<Locale> postfixes = new ArrayList<Locale>();
        while (postfix != null) {
            postfixes.add(postfix);
            postfix = LocaleUtil.getParentLocale(postfix);
        }
        Collections.reverse(postfixes);
        return postfixes;
    }
}
