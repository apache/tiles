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
 *
 * @version $Rev$ $Date$
 */
public class MockDefinitions implements Definitions {

    /**
     * Hokey way to verify that this was created.
     */
    private static int instanceCount = 0;

    /**
     * Hokey way to verify that this class was created.
     *
     * @return The number of created instances.
     */
    public static int getInstanceCount() {
        return instanceCount;
    }

    /** Creates a new instance of MockDefinitions. */
    public MockDefinitions() {
        instanceCount++;
    }

    /**
     * Returns a Definition object that matches the given name.
     *
     * @param name The name of the Definition to return.
     * @return the Definition matching the given name or null if none
     *  is found.
     */
    public Definition getDefinition(String name) {
        return null;
    }

    /**
     * Adds new locale-specific Definition objects to the internal
     * collection and resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @param locale The locale to add the definitions to.
     */
    public void addDefinitions(Map<String, Definition> defsMap,
            java.util.Locale locale) {
    }

    /**
     * Returns a Definition object that matches the given name and locale.
     *
     * @param name The name of the Definition to return.
     * @param locale The locale to use to resolve the definition.
     * @return the Definition matching the given name or null if none
     *  is found.
     */
    public Definition getDefinition(String name, java.util.Locale locale) {
        return null;
    }

    /**
     * Adds new Definition objects to the internal collection and
     * resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     */
    public void addDefinitions(Map<String, Definition> defsMap) {
    }

    /** {@inheritDoc} */
    public void resolveInheritances() throws NoSuchDefinitionException {
    }

    /** {@inheritDoc} */
    public void resolveInheritances(Locale locale) throws NoSuchDefinitionException {
    }

    /**
     * Clears definitions.
     */
    public void reset() {
    }

    /** {@inheritDoc} */
    public Map<String, Definition> getBaseDefinitions() {
        return null;
    }

}
