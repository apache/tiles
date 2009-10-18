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

package org.apache.tiles.definition.pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.Definition;
import org.apache.tiles.util.WildcardHelper;

/**
 * Uses wildcards syntax to match definition names and its parameters.
 *
 * @param <T> The type of the customization key.
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class WildcardPatternDefinitionResolver<T> implements
        PatternDefinitionResolver<T> {

    /**
     * Stores patterns depending on the locale they refer to.
     *
     * @since 2.2.0
     */
    private Map<T, List<WildcardMapping>> localePatternPaths =
        new HashMap<T, List<WildcardMapping>>();

    /**
     * An object that helps in resolving definitions with wildcards.
     *
     * @since 2.2.0
     */
    private WildcardHelper wildcardHelper = new WildcardHelper();

    /** {@inheritDoc} */
    public Definition resolveDefinition(String name, T customizationKey) {
        Definition retValue = null;
        if (localePatternPaths.containsKey(customizationKey)) {
            retValue = resolveWildcardDefinition(localePatternPaths
                    .get(customizationKey), name);
        }
        return retValue;
    }

    /** {@inheritDoc} */
    public Map<String, Definition> storeDefinitionPatterns(Map<String, Definition> localeDefsMap,
            T customizationKey) {
        List<WildcardMapping> lpaths = localePatternPaths
                .get(customizationKey);
        if (lpaths == null) {
            lpaths = new ArrayList<WildcardMapping>();
            localePatternPaths.put(customizationKey, lpaths);
        }

        return addWildcardPaths(lpaths, localeDefsMap);
    }

    /**
     * Adds wildcard paths that are stored inside a normal definition map.
     *
     * @param paths The list containing the currently stored paths.
     * @param defsMap The definition map to parse.
     * @return The map of the definitions not recognized as containing
     * definition patterns.
     */
    private Map<String, Definition> addWildcardPaths(List<WildcardMapping> paths,
            Map<String, Definition> defsMap) {
        Set<String> excludedKeys = new LinkedHashSet<String>();
        for (Map.Entry<String, Definition> de : defsMap.entrySet()) {
            String key = de.getKey();
            if (key.indexOf('*') != -1) {
                paths.add(new WildcardMapping(key,
                        new Definition(de.getValue())));
            } else {
                excludedKeys.add(key);
            }
        }
        return PatternUtil.createExtractedMap(defsMap, excludedKeys);
    }

    /**
     * Try to resolve a wildcard definition.
     *
     * @param paths The list containing the currently stored paths.
     * @param name The name of the definition to resolve.
     * @return A definition, if found, or <code>null</code> if not.
     */
    private Definition resolveWildcardDefinition(
            List<WildcardMapping> paths, String name) {
        Definition d = null;

        for (WildcardMapping wm : paths) {
            List<String> vars = wildcardHelper.match(name, wm.getPattern());
            if (vars != null) {
                d = PatternUtil.replacePlaceholders(wm.getDefinition(), name, vars.toArray());
                break;
            }
        }

        return d;
    }

    /**
     * Maps a pattern with a definition in cache.
     *
     * @since 2.2.0
     */
    private class WildcardMapping {

        /**
         * The compiled pattern.
         */
        private int[] pattern;

        /**
         * The definition that matches the pattern.
         */
        private Definition definition;

        /**
         * Constructor.
         *
         * @param pattern The compiled pattern.
         * @param definition A definition that matches the pattern.
         *
         * @since 2.2.0
         */
        public WildcardMapping(String pattern, Definition definition) {
            this.pattern = wildcardHelper.compilePattern(pattern);
            this.definition = definition;
        }

        /**
         * Returns the definition.
         *
         * @return The definition.
         * @since 2.2.0
         */
        public Definition getDefinition() {
            return definition;
        }

        /**
         * Returns the compiled pattern.
         *
         * @return The pattern.
         * @since 2.2.0
         */
        public int[] getPattern() {
            return pattern;
        }
    }
}
