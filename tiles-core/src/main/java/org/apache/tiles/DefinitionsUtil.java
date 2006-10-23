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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.taglib.ComponentConstants;

/**
 * Utilities class for definitions factory.
 * Also define userDebugLevel property (TODO to be moved from this class ?).
 * @deprecated Use {@link TilesUtil#createDefinitionsFactory(TilesApplicationContext, DefinitionsFactoryConfig)}
 */
public class DefinitionsUtil extends TilesUtil implements ComponentConstants {

    /** 
     * Commons Logging instance. 
     */
    protected static Log log = LogFactory.getLog(DefinitionsUtil.class);

    /** 
     * Global user defined debug level.
     * @deprecated This will be removed in a release after Struts 1.2. 
     */
    public static int userDebugLevel = 0;

    /** 
     * User Debug level. 
     * @deprecated This will be removed in a release after Struts 1.2. 
     */
    public static final int NO_DEBUG = 0;

    /** 
     * Name of init property carrying debug level. 
     */
    public static final String DEFINITIONS_CONFIG_USER_DEBUG_LEVEL =
        "definitions-debug";

    /** 
     * Name of init property carrying factory class name. 
     */
    public static final String DEFINITIONS_FACTORY_CLASSNAME =
        "definitions-factory-class";

    /** 
     * Constant name used to store factory in context. 
     */
    public static final String DEFINITIONS_FACTORY =
        "org.apache.tiles.DEFINITIONS_FACTORY";

    /** 
     * Constant name used to store definition in jsp context.
     * Used to pass definition from a Struts action to servlet forward. 
     */
    public static final String ACTION_DEFINITION =
        "org.apache.tiles.ACTION_DEFINITION";

    /**
     * Create Definition factory.
     * If a factory class name is provided, a factory of this class is created. Otherwise,
     * default factory is created.
     * @param classname Class name of the factory to create.
     * @param tilesContext The current Tiles application context.
     * @param properties Map of name/property used to initialize factory configuration object.
     * @return newly created factory.
     * @throws DefinitionsFactoryException If an error occur while initializing factory
     * @deprecated Use createDefinitionsFactory(ServletContext servletContext, ServletConfig servletConfig)
     */
    public static DefinitionsFactory createDefinitionsFactory(
        TilesApplicationContext tilesContext,
        Map properties,
        String classname)
        throws DefinitionsFactoryException {

        // Create config object
        DefinitionsFactoryConfig factoryConfig = new DefinitionsFactoryConfig();
        // populate it from map.
        try {
            factoryConfig.populate(properties);

        } catch (Exception ex) {
            throw new DefinitionsFactoryException(
                "Error - createDefinitionsFactory : Can't populate config object from properties map",
                ex);
        }

        // Add classname
        if (classname != null)
            factoryConfig.setFactoryClassname(classname);

        // Create factory using config object
        return createDefinitionsFactory(tilesContext, factoryConfig);
    }

    /**
     * Create default Definition factory.
     * @param tilesContext The current Tiles application context.
     * @param properties Map of name/property used to initialize factory configuration object.
     * @return newly created factory of type ConfigurableDefinitionsFactory.
     * @throws DefinitionsFactoryException If an error occur while initializing factory
     */
    public static DefinitionsFactory createDefinitionsFactory(
        TilesApplicationContext tilesContext,
        Map properties)
        throws DefinitionsFactoryException {

        return createDefinitionsFactory(tilesContext, properties, null);
    }

    /**
     * Create Definition factory.
     * Create configuration object from servlet web.xml file, then create
     * ConfigurableDefinitionsFactory and initialized it with object.
     * <p>
     * Convenience method. Calls createDefinitionsFactory(ServletContext servletContext, DefinitionsFactoryConfig factoryConfig)
     *
     * @param tilesContext The current Tiles application context.
     * @return newly created factory of type ConfigurableDefinitionsFactory.
     * @throws DefinitionsFactoryException If an error occur while initializing factory
     */
    public static DefinitionsFactory createDefinitionsFactory(
        TilesApplicationContext tilesContext)
        throws DefinitionsFactoryException {

        DefinitionsFactoryConfig factoryConfig = readFactoryConfig(tilesContext);

        return createDefinitionsFactory(tilesContext, factoryConfig);
    }

    /**
     * Create Definition factory.
     * Create configuration object from servlet web.xml file, then create
     * ConfigurableDefinitionsFactory and initialized it with object.
     * <p>
     * If checkIfExist is true, start by checking if factory already exist. If yes,
     * return it. If no, create a new one.
     * <p>
     * If checkIfExist is false, factory is always created.
     * <p>
     * Convenience method. Calls createDefinitionsFactory(ServletContext servletContext, DefinitionsFactoryConfig factoryConfig)
     *
     * @param tilesContext The current Tiles application context.
     * @param checkIfExist Check if factory already exist. If true and factory exist, return it.
     * If true and factory doesn't exist, create it. If false, create it in all cases.
     * @return newly created factory of type ConfigurableDefinitionsFactory.
     * @throws DefinitionsFactoryException If an error occur while initializing factory
     */
    public static DefinitionsFactory createDefinitionsFactory(
        TilesApplicationContext tilesContext,
        boolean checkIfExist)
        throws DefinitionsFactoryException {

        if (checkIfExist) {
            // Check if already exist in context
            DefinitionsFactory factory = getDefinitionsFactory(tilesContext);
            if (factory != null)
                return factory;
        }

        return createDefinitionsFactory(tilesContext);
    }

    /**
     * Get definition factory from appropriate servlet context.
     *
     * @param tilesContext the current Tiles application context.
     * @return Definitions factory or null if not found.
     * @deprecated Use {@link TilesUtil#getDefinitionsFactory(TilesApplicationContext)
     * @since 20020708
     */
    public static DefinitionsFactory getDefinitionsFactory(TilesApplicationContext tilesContext) {
        return (DefinitionsFactory) tilesContext.getApplicationScope().get(DEFINITIONS_FACTORY);
    }

    /**
     * Get Definition stored in jsp context by an action.
     * @return ComponentDefinition or null if not found.
     */
    public static ComponentDefinition getActionDefinition(TilesRequestContext tilesContext) {
        return (ComponentDefinition) tilesContext.getRequestScope().get(ACTION_DEFINITION);
    }

    /**
     * Store definition in jsp context.
     * Mainly used by Struts to pass a definition defined in an Action to the forward.
     */
    public static void setActionDefinition(
        TilesRequestContext tilesContext,
        ComponentDefinition definition) {

        tilesContext.getRequestScope().put(ACTION_DEFINITION, definition);
    }

    /**
     * Remove Definition stored in jsp context.
     * Mainly used by Struts to pass a definition defined in an Action to the forward.
     */
    public static void removeActionDefinition(
        TilesRequestContext tilesContext,
        ComponentDefinition definition) {

        tilesContext.getRequestScope().remove(ACTION_DEFINITION);
    }

    /**
     * Populate Definition Factory Config from web.xml properties.
     * @param factoryConfig Definition Factory Config to populate.
     * @param tilesContext The current Tiles application context.
     * @exception IllegalAccessException if the caller does not have
     *  access to the property accessor method
     * @exception java.lang.reflect.InvocationTargetException if the property accessor method
     *  throws an exception
     * @see org.apache.commons.beanutils.BeanUtils
     * @since tiles 20020708
     */
    public static void populateDefinitionsFactoryConfig(
        DefinitionsFactoryConfig factoryConfig,
        TilesApplicationContext tilesContext)
        throws IllegalAccessException, InvocationTargetException {

        Map properties = new DefinitionsUtil.ServletPropertiesMap(tilesContext);
        factoryConfig.populate(properties);
    }

    /**
     * Create FactoryConfig and initialize it from web.xml.
     *
     * @param tilesContext the current Tiles application context.
     * @exception DefinitionsFactoryException if this <code>PlugIn</code> cannot
     *  be successfully initialized
     */
    protected static DefinitionsFactoryConfig readFactoryConfig(TilesApplicationContext tilesContext)
        throws DefinitionsFactoryException {

        // Create tiles definitions config object
        DefinitionsFactoryConfig factoryConfig = new DefinitionsFactoryConfig();

        // Get init parameters from web.xml files
        try {
            DefinitionsUtil.populateDefinitionsFactoryConfig(
                factoryConfig,
                tilesContext);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DefinitionsFactoryException(
                "Can't populate DefinitionsFactoryConfig class from 'web.xml'.",
                ex);
        }

        return factoryConfig;
    }

    /**
     * Inner class.
     * Wrapper for application init parameters.
     * Object of this class is an hashmap containing parameters and values
     * defined in the servlet config file (web.xml).
     */
    static class ServletPropertiesMap extends HashMap {
        /**
         * Constructor.
         */
        ServletPropertiesMap(TilesApplicationContext tilesContext) {
            // This implementation is very simple.
            // It is possible to avoid creation of a new structure, but this need
            // imply writing all Map interface.
            Set initParams = tilesContext.getInitParams().keySet();
            Iterator i = initParams.iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                put(key, tilesContext.getInitParams().get(key));
            }
        }
    } // end inner class

}
