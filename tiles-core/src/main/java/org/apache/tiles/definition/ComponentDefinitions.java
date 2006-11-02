/*
 * $Id$
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.definition;

import java.util.Locale;
import java.util.Map;

/**
 * Interface for managing collections of {@link ComponentDefinition} objects.
 * <p/>
 * <p>The ComponentDefinitions interface provides a pattern for managing
 * ComponentDefinition objects.  Implementations will provide a means to append
 * new ComponentDefinitions to the collection, add and retrieve lcale-specific
 * ComponentDefinitions objects, and reset the collections.</p>
 *
 * @version $Rev$ $Date$
 */
public interface ComponentDefinitions {

    /**
     * Returns a ComponentDefinition object that matches the given name.
     *
     * @param name The name of the ComponentDefinition to return.
     * @return the ComponentDefinition matching the given name or null if none
     *         is found.
     */
    public ComponentDefinition getDefinition(String name);

    /**
     * Adds new ComponentDefinition objects to the internal collection and
     * resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @throws NoSuchDefinitionException if a ComponentDefinition extends from
     *                                   one that doesn't exist.
     */
    public void addDefinitions(Map defsMap) throws NoSuchDefinitionException;

    /**
     * Adds new locale-specific ComponentDefinition objects to the internal
     * collection and resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @param locale  The locale to add the definitions to.
     * @throws NoSuchDefinitionException if a ComponentDefinition extends from
     *                                   one that doesn't exist.
     */
    public void addDefinitions(Map defsMap, Locale locale) throws NoSuchDefinitionException;

    /**
     * Returns a ComponentDefinition object that matches the given name and locale.
     *
     * @param name   The name of the ComponentDefinition to return.
     * @param locale The locale to use to resolve the definition.
     * @return the ComponentDefinition matching the given name or null if none
     *         is found.
     */
    public ComponentDefinition getDefinition(String name, Locale locale);

    /**
     * Resolves configuration inheritance properties.
     */
    public void resolveInheritances() throws NoSuchDefinitionException;

    /**
     * Resolves locale-specific configuration inheritance properties.
     */
    public void resolveInheritances(Locale locale) throws NoSuchDefinitionException;

    /**
     * Clears definitions.
     */
    public void reset();

    /**
     * Returns base definitions collection;
     */
    public Map getBaseDefinitions();
}
