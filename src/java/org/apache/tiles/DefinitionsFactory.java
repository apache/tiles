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

package org.apache.tiles;

import java.util.Locale;
import java.util.Map;

/**
 * Interface for creating a {@link ComponentDefinitions} object and managing
 * its contents.
 *
 * <p>DefinitionsFactory implementations are responsible for maintaining the data
 * sources of Tiles configuration data and using the data to create 
 * ComponentDefinitions sets.  Implementations also know how to append
 * locale-specific configuration data to an existing ComponentDefinitions set.</p>
 *
 *
 * @version $Rev$ $Date$ 
 */
public interface DefinitionsFactory {
    
    /**
     * Property name that specifies the implementation of the DefinitionsReader.
     */
    public static final String READER_IMPL_PROPERTY =
            "org.apache.tiles.DefinitionsReader";
    /**
     * Property name that specifies the implementation of ComponentDefinitions.
     */
    public static final String DEFINITIONS_IMPL_PROPERTY =
            "org.apache.tiles.ComponentDefinitions";
    
    /**
     * Initializes the DefinitionsFactory and its subcomponents.
     *
     * Implementations may support configuration properties to be passed in via
     * the params Map.
     *
     * @param params The Map of configuration properties.
     * @throws DefinitionsFactoryException if an initialization error occurs.
     */
    public void init(Map params) throws DefinitionsFactoryException;
    
    /**
     * Adds a source where ComponentDefinition objects are stored.
     * 
     * Implementations should publish what type of source object they expect.
     * The source should contain enough information to resolve a configuration
     * source containing definitions.  The source should be a "base" source for
     * configurations.  Internationalization and Localization properties will be
     * applied by implementations to discriminate the correct data sources based
     * on locale.
     * 
     * @param source The configuration source for definitions.
     * @throws DefinitionsFactoryException if an invalid source is passed in or
     *      an error occurs resolving the source to an actual data store.
     */
    public void addSource(Object source) throws DefinitionsFactoryException;
    
    /**
     * Creates and returns a {@link ComponentDefinitions} set by reading 
     * configuration data from the applied sources.
     *
     * @throws DefinitionsFactoryException if an error occurs reading the 
     *      sources.
     */
    public ComponentDefinitions readDefinitions() 
            throws DefinitionsFactoryException;

    /**
     * Appends locale-specific {@link ComponentDefinition} objects to an existing
     * {@link ComponentDefinitions} set by reading locale-specific versions of
     * the applied sources.
     *
     * @param definitions The ComponentDefinitions object to append to.
     * @param locale The requested locale.
     * @throws DefinitionsFactoryException if an error occurs reading definitions.
     */
    public void addDefinitions(ComponentDefinitions definitions, Locale locale) 
            throws DefinitionsFactoryException;
    
    /**
     * Indicates whether a given locale has been processed or not.
     * 
     * This method can be used to avoid unnecessary synchronization of the
     * DefinitionsFactory in multi-threaded situations.  Check the return of
     * isLoacaleProcessed before synchronizing the object and reading 
     * locale-specific definitions.
     *
     * @param locale The locale to check.
     * @return true if the given lcoale has been processed and false otherwise.
     */
    public boolean isLocaleProcessed(Locale locale);
}
