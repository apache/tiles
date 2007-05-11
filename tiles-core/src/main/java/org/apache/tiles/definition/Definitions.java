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

import java.util.Locale;
import java.util.Map;

import org.apache.tiles.Definition;

/**
 * Interface for managing collections of {@link Definition} objects.
 * <p/>
 * <p>The Definitions interface provides a pattern for managing
 * Definition objects.  Implementations will provide a means to append
 * new Definitions to the collection, add and retrieve lcale-specific
 * Definitions objects, and reset the collections.</p>
 *
 * @version $Rev$ $Date$
 */
public interface Definitions {

    /**
     * Returns a Definition object that matches the given name.
     *
     * @param name The name of the Definition to return.
     * @return the Definition matching the given name or null if none
     *         is found.
     */
    Definition getDefinition(String name);

    /**
     * Adds new Definition objects to the internal collection and
     * resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @throws NoSuchDefinitionException if a Definition extends from
     *                                   one that doesn't exist.
     */
    void addDefinitions(Map<String, Definition> defsMap) throws NoSuchDefinitionException;

    /**
     * Adds new locale-specific Definition objects to the internal
     * collection and resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @param locale  The locale to add the definitions to.
     * @throws NoSuchDefinitionException if a Definition extends from
     *                                   one that doesn't exist.
     */
    void addDefinitions(Map<String, Definition> defsMap, Locale locale)
            throws NoSuchDefinitionException;

    /**
     * Returns a Definition object that matches the given name and locale.
     *
     * @param name   The name of the Definition to return.
     * @param locale The locale to use to resolve the definition.
     * @return the Definition matching the given name or null if none
     *         is found.
     */
    Definition getDefinition(String name, Locale locale);

    /**
     * Resolves configuration inheritance properties.
     *
     * @throws NoSuchDefinitionException If parent definitions are not found.
     */
    void resolveInheritances() throws NoSuchDefinitionException;

    /**
     * Resolves locale-specific configuration inheritance properties.
     *
     * @param locale The locale object to use.
     * @throws NoSuchDefinitionException If parent definitions are not found.
     */
    void resolveInheritances(Locale locale) throws NoSuchDefinitionException;

    /**
     * Clears definitions.
     */
    void reset();

    /**
     * Returns base definitions collection.
     *
     * @return A map of the type "definition name -> definition".
     */
    Map<String, Definition> getBaseDefinitions();
}
