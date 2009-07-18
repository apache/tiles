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
import java.util.List;
import java.util.Map;

import org.apache.tiles.Definition;

/**
 * Uses wildcards syntax to match definition names and its parameters.
 *
 * @param <T> The type of the customization key.
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class BasicPatternDefinitionResolver<T> implements
        PatternDefinitionResolver<T> {

    /**
     * Stores patterns depending on the locale they refer to.
     */
    private Map<T, List<DefinitionPatternMatcher>> localePatternPaths =
        new HashMap<T, List<DefinitionPatternMatcher>>();

    /**
     * The factory of pattern matchers.
     */
    private DefinitionPatternMatcherFactory definitionPatternMatcherFactory;

    /**
     * The pattern recognizer.
     */
    private PatternRecognizer patternRecognizer;

    /**
     * Constructor.
     *
     * @param definitionPatternMatcherFactory The definition pattern matcher factory.
     * @param patternRecognizer The pattern recognizer.
     */
    public BasicPatternDefinitionResolver(DefinitionPatternMatcherFactory definitionPatternMatcherFactory,
            PatternRecognizer patternRecognizer) {
        this.definitionPatternMatcherFactory = definitionPatternMatcherFactory;
        this.patternRecognizer = patternRecognizer;
    }

    /** {@inheritDoc} */
    public Definition resolveDefinition(String name, T customizationKey) {
        Definition retValue = null;
        if (localePatternPaths.containsKey(customizationKey)) {
            retValue = searchAndResolveDefinition(localePatternPaths
                    .get(customizationKey), name);
        }
        return retValue;
    }

    /** {@inheritDoc} */
    public void storeDefinitionPatterns(Map<String, Definition> localeDefsMap,
            T customizationKey) {
        List<DefinitionPatternMatcher> lpaths = localePatternPaths
                .get(customizationKey);
        if (lpaths == null) {
            lpaths = new ArrayList<DefinitionPatternMatcher>();
            localePatternPaths.put(customizationKey, lpaths);
        }

        addWildcardPaths(lpaths, localeDefsMap);
    }

    /**
     * Adds wildcard paths that are stored inside a normal definition map.
     *
     * @param paths The list containing the currently stored paths.
     * @param defsMap The definition map to parse.
     */
    private void addWildcardPaths(List<DefinitionPatternMatcher> paths,
            Map<String, Definition> defsMap) {
        for (Map.Entry<String, Definition> de : defsMap.entrySet()) {
            if (patternRecognizer.isPatternRecognized(de.getKey())) {
                paths.add(definitionPatternMatcherFactory
                        .createDefinitionPatternMatcher(de.getKey(), de
                                .getValue()));
            }
        }
    }

    /**
     * Try to resolve a definition by iterating all pattern matchers.
     *
     * @param paths The list containing the currently stored paths.
     * @param name The name of the definition to resolve.
     * @return A definition, if found, or <code>null</code> if not.
     */
    private Definition searchAndResolveDefinition(
            List<DefinitionPatternMatcher> paths, String name) {
        Definition d = null;

        for (DefinitionPatternMatcher wm : paths) {
            d = wm.createDefinition(name);
            if (d != null) {
                break;
            }
        }

        return d;
    }
}
