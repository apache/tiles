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

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.Definition;
import org.apache.tiles.TilesException;

import java.util.Map;

/**
 * Interface for creating a {@link org.apache.tiles.definition.Definitions} object and managing
 * its contents.
 * <p/>
 * <p>DefinitionsFactory implementations are responsible for maintaining the data
 * sources of Tiles configuration data and using the data to create
 * Definitions sets.  Implementations also know how to append
 * locale-specific configuration data to an existing Definitions set.</p>
 *
 * @version $Rev$ $Date$
 */
public interface DefinitionsFactory {

    /**
     * Property name that specifies the implementation of the DefinitionsReader.
     */
    String READER_IMPL_PROPERTY =
        "org.apache.tiles.definition.DefinitionsReader";

    /**
     * Property name that specifies the implementation of
     * {@link org.apache.tiles.locale.LocaleResolver}.
     */
    String LOCALE_RESOLVER_IMPL_PROPERTY =
        "org.apache.tiles.locale.LocaleResolver";

    /**
     * Initializes the DefinitionsFactory and its subcomponents. <p/>
     * Implementations may support configuration properties to be passed in via
     * the params Map.
     *
     * @param params The Map of configuration properties.
     * @throws TilesException If a Tiles exception, such as an initialization
     * error, occurs.
     */
    void init(Map<String, String> params) throws TilesException;

    /**
     * Returns a Definition object that matches the given name and
     * Tiles context.
     *
     * @param name         The name of the Definition to return.
     * @param tilesContext The Tiles context to use to resolve the definition.
     * @return the Definition matching the given name or null if none
     *         is found.
     * @throws DefinitionsFactoryException if an error occurs reading definitions.
     */
    Definition getDefinition(String name, TilesRequestContext tilesContext)
            throws DefinitionsFactoryException;

    /**
     * Adds a source where Definition objects are stored.
     * <p/>
     * Implementations should publish what type of source object they expect.
     * The source should contain enough information to resolve a configuration
     * source containing definitions.  The source should be a "base" source for
     * configurations.  Internationalization and Localization properties will be
     * applied by implementations to discriminate the correct data sources based
     * on locale.
     *
     * @param source The configuration source for definitions.
     * @throws DefinitionsFactoryException if an invalid source is passed in or
     *                                     an error occurs resolving the source to an actual data store.
     */
    void addSource(Object source) throws DefinitionsFactoryException;

    /**
     * Creates and returns a {@link Definitions} set by reading
     * configuration data from the applied sources.
     *
     * @return The read definitions.
     * @throws DefinitionsFactoryException if an error occurs reading the
     *                                     sources.
     */
    Definitions readDefinitions()
        throws DefinitionsFactoryException;
}
