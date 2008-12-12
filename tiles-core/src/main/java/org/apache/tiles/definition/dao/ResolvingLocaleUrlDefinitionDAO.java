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

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.NoSuchDefinitionException;

/**
 * <p>
 * A definitions DAO (loading URLs and using Locale as a customization key) that
 * caches definitions that have been loaded and resolves inheritances.
 * </p>
 * <p>
 * It can check if the URLs change, but by default this feature is turned off.
 * </p>
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class ResolvingLocaleUrlDefinitionDAO extends
        CachingLocaleUrlDefinitionDAO {

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory.getLog(ResolvingLocaleUrlDefinitionDAO.class);

    /** {@inheritDoc} */
    @Override
    protected Map<String, Definition> loadParentDefinitions(Locale parentLocale) {
        return loadDefinitionsFromURLs(parentLocale);
    }

    /** {@inheritDoc} */
    @Override
    protected void postDefinitionLoadOperations(
            Map<String, Definition> localeDefsMap, Locale customizationKey) {
        resolveInheritances(localeDefsMap, customizationKey);
        super.postDefinitionLoadOperations(localeDefsMap, customizationKey);
    }

    /**
     * Resolve locale-specific extended instances.
     *
     * @param map The definition map containing the definitions to resolve.
     * @param locale The locale to use.
     * @throws NoSuchDefinitionException If a parent definition is not found.
     * @since 2.1.0
     */
    protected void resolveInheritances(Map<String, Definition> map, Locale locale) {
        if (map != null) {
            Set<String> alreadyResolvedDefinitions = new HashSet<String>();
            for (Definition definition : map.values()) {
                resolveInheritance(definition, map, locale,
                        alreadyResolvedDefinitions);
            }  // end loop
        }
    }

    /**
     * Resolve locale-specific inheritance. First, resolve parent's inheritance,
     * then set template to the parent's template. Also copy attributes setted
     * in parent, and not set in child If instance doesn't extend anything, do
     * nothing.
     *
     * @param definition The definition to resolve
     * @param definitions The definitions to take when obtaining a parent
     * definition.
     * @param locale The locale to use.
     * @param alreadyResolvedDefinitions The set of the definitions that have
     * been already resolved.
     * @throws NoSuchDefinitionException If an inheritance can not be solved.
     * @since 2.1.0
     */
    protected void resolveInheritance(Definition definition,
            Map<String, Definition> definitions, Locale locale,
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
        Definition parent = definitions.get(definition.getExtends());
        if (parent == null) { // error
            String msg = "Error while resolving definition inheritance: child '"
                + definition.getName()
                + "' can't find its ancestor '"
                + definition.getExtends()
                + "'. Please check your description file.";
            // to do : find better exception
            throw new NoSuchDefinitionException(msg);
        }

        resolveInheritance(parent, definitions, locale,
                alreadyResolvedDefinitions);

        definition.inherit(parent);
    }
}
