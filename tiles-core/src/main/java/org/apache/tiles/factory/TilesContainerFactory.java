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
package org.apache.tiles.factory;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.ChainedTilesContextFactory;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.preparer.BasicPreparerFactory;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.util.ClassUtil;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory provided for convenience.
 * This factory creates a default implementation of
 * the container, initializes, and puts it into service.
 *
 * @version $Rev$ $Date$
 * @since 2.0
 */
public class TilesContainerFactory {

    /**
     * Initialization parameter that represents the container factory class
     * name.
     */
    public static final String CONTAINER_FACTORY_INIT_PARAM =
        "org.apache.tiles.factory.TilesContainerFactory";

    /**
     * Initialization parameter that indicates if the container factory is
     * mutable.
     */
    public static final String CONTAINER_FACTORY_MUTABLE_INIT_PARAM =
        "org.apache.tiles.factory.TilesContainerFactory.MUTABLE";

    /**
     * Initialization parameter that represents the context factory class name.
     */
    public static final String CONTEXT_FACTORY_INIT_PARAM =
        "org.apache.tiles.context.TilesContextFactory";

    /**
     * Initialization parameter that represents the definitions factory class
     * name.
     */
    public static final String DEFINITIONS_FACTORY_INIT_PARAM =
        "org.apache.tiles.definition.DefinitionsFactory";

    /**
     * Initialization parameter that represents the preparer factory class name.
     */
    public static final String PREPARER_FACTORY_INIT_PARAM =
        "org.apache.tiles.preparer.PreparerFactory";


    /**
     * Default configuration parameters.
     */
    private static final Map<String, String> DEFAULTS =
        new HashMap<String, String>();

    static {
        DEFAULTS.put(CONTAINER_FACTORY_INIT_PARAM, TilesContainerFactory.class.getName());
        DEFAULTS.put(CONTEXT_FACTORY_INIT_PARAM, ChainedTilesContextFactory.class.getName());
        DEFAULTS.put(DEFINITIONS_FACTORY_INIT_PARAM, UrlDefinitionsFactory.class.getName());
        DEFAULTS.put(PREPARER_FACTORY_INIT_PARAM, BasicPreparerFactory.class.getName());
    }

    /**
     * The default configuration to be used by the factory.
     */
    protected Map<String, String> defaultConfiguration =
        new HashMap<String, String>(DEFAULTS);

    /**
     * Retrieve a factory instance as configured through the
     * specified context.
     * <p/>
     * The context will be queried and if a init parameter
     * named 'org.apache.tiles.factory.TilesContainerFactory' is discovered
     * this class will be instantiated and returned. Otherwise,
     * the factory will attempt to utilize one of it's internal
     * factories.
     *
     * @param context the executing applications context.
     *                Typically a ServletContext or PortletContext
     * @return a tiles container
     * @throws TilesException if an error occurs creating the factory.
     */
    public static TilesContainerFactory getFactory(Object context)
        throws TilesException {
        return getFactory(context, DEFAULTS);
    }

    /**
     * Retrieve a factory instance as configured through the specified context.
     * <p/> The context will be queried and if a init parameter named
     * 'org.apache.tiles.factory.TilesContainerFactory' is discovered this class
     * will be instantiated and returned. Otherwise, the factory will attempt to
     * utilize one of it's internal factories.
     *
     * @param context the executing applications context. Typically a
     * ServletContext or PortletContext
     * @param defaults Default configuration parameters values, used if the
     * context object has not the corresponding parameters.
     * @return a tiles container
     * @throws TilesException if an error occurs creating the factory.
     */
    public static TilesContainerFactory getFactory(Object context,
                                                   Map<String, String> defaults)
        throws TilesException {
        Map<String, String> configuration = new HashMap<String, String>(defaults);
        configuration.putAll(getInitParameterMap(context));
        TilesContainerFactory factory =
            (TilesContainerFactory) TilesContainerFactory.createFactory(configuration,
                CONTAINER_FACTORY_INIT_PARAM);
        factory.setDefaultConfiguration(defaults);
        return factory;
    }

    /**
     * Creates a Tiles container.
     *
     * @param context The (application) context object.
     * @return The created container.
     * @throws TilesException If something goes wrong during instantiation.
     */
    public TilesContainer createContainer(Object context) throws TilesException {
        String value = getInitParameter(context, CONTAINER_FACTORY_MUTABLE_INIT_PARAM);
        if (Boolean.parseBoolean(value)) {
            return createMutableTilesContainer(context);
        } else {
            return createTilesContainer(context);
        }
    }

    /**
     * Sets the default configuration parameters.
     *
     * @param defaultConfiguration The default configuration parameters.
     */
    public void setDefaultConfiguration(Map<String, String> defaultConfiguration) {
        if (defaultConfiguration != null) {
            this.defaultConfiguration.putAll(defaultConfiguration);
        }
    }

    /**
     * Sets one default configuration parameter value.
     *
     * @param key The key of the configuration parameter.
     * @param value The value of the configuration parameter.
     */
    public void setDefaultValue(String key, String value) {
        this.defaultConfiguration.put(key, value);
    }

    /**
     * Creates an immutable Tiles container.
     *
     * @param context The (application) context object.
     * @return The created Tiles container.
     * @throws TilesException If something goes wrong during initialization.
     */
    public TilesContainer createTilesContainer(Object context)
        throws TilesException {
        BasicTilesContainer container = new BasicTilesContainer();
        initializeContainer(context, container);
        return container;
    }

    /**
     * Creates a mutable Tiles container.
     *
     * @param context The (application) context object.
     * @return The created Tiles container.
     * @throws TilesException If something goes wrong during initialization.
     */
    public MutableTilesContainer createMutableTilesContainer(Object context)
        throws TilesException {
        CachingTilesContainer container = new CachingTilesContainer();
        initializeContainer(context, container);
        return container;
    }

    /**
     * Initializes a container.
     *
     * @param context The (application) context object to use.
     * @param container The container to be initialized.
     * @throws TilesException If something goes wrong during initialization.
     */
    protected void initializeContainer(Object context,
                                       BasicTilesContainer container)
        throws TilesException {
        Map <String, String> initParameterMap;

        initParameterMap = getInitParameterMap(context);
        Map<String, String> configuration = new HashMap<String, String>(defaultConfiguration);
        configuration.putAll(initParameterMap);
        storeContainerDependencies(context, initParameterMap, configuration, container);
        container.init(initParameterMap);
    }

    /**
     * Stores container dependencies, that is called before
     * {@link TilesContainer#init(Map)}.
     *
     * @param context The (application) context object to use.
     * @param initParameters The initialization parameters.
     * @param configuration The merged configuration parameters (both defaults
     * and context ones).
     * @param container The container to use.
     * @throws TilesException If something goes wrong during initialization.
     */
    protected void storeContainerDependencies(Object context,
                                              Map<String, String> initParameters,
                                              Map<String, String> configuration,
                                              BasicTilesContainer container) throws TilesException {
        TilesContextFactory contextFactory =
            (TilesContextFactory) createFactory(configuration,
                CONTEXT_FACTORY_INIT_PARAM);

        DefinitionsFactory defsFactory =
            (DefinitionsFactory) createFactory(configuration,
                DEFINITIONS_FACTORY_INIT_PARAM);

        PreparerFactory prepFactory =
            (PreparerFactory) createFactory(configuration,
                PREPARER_FACTORY_INIT_PARAM);

        contextFactory.init(configuration);
        TilesApplicationContext tilesContext =
            contextFactory.createApplicationContext(context);

        container.setDefinitionsFactory(defsFactory);
        container.setContextFactory(contextFactory);
        container.setPreparerFactory(prepFactory);
        container.setApplicationContext(tilesContext);
    }


    /**
     * Creates a factory instance.
     *
     * @param configuration The merged configuration parameters (both defaults
     * and context ones).
     * @param initParameterName The initialization parameter name from which the
     * class name is got.
     * @return The created factory.
     * @throws TilesException If something goes wrong during creation.
     */
    protected static Object createFactory(Map<String, String> configuration, String initParameterName)
        throws TilesException {
        String factoryName = resolveFactoryName(configuration, initParameterName);
        return ClassUtil.instantiate(factoryName);
    }

    /**
     * Resolves a factory class name.
     *
     * @param configuration The merged configuration parameters (both defaults
     * and context ones).
     * @param parameterName The name of the initialization parameter to use.
     * @return The factory class name.
     * @throws TilesException If something goes wrong during resolution.
     */
    protected static String resolveFactoryName(Map<String, String> configuration, String parameterName)
        throws TilesException {
        Object factoryName = configuration.get(parameterName);
        return factoryName == null
            ? DEFAULTS.get(parameterName)
            : factoryName.toString();
    }

    /**
     * Returns the value of an initialization parameter.
     *
     * @param context The (application) context object to use.
     * @param parameterName The parameter name to retrieve.
     * @return The parameter value.
     * @throws TilesException If the context has not been recognized.
     */
    protected static String getInitParameter(Object context,
                                             String parameterName) throws TilesException {
        Object value;
        try {
            Class<?> contextClass = context.getClass();
            Method getInitParameterMethod =
                contextClass.getMethod("getInitParameter", String.class);
            value = getInitParameterMethod.invoke(context, parameterName);
        } catch (Exception e) {
            throw new TilesException("Unrecognized context.  Is this context"
                    + " a ServletContext, PortletContext, or similar?", e);
        }
        return value == null ? null : value.toString();
    }

    /**
     * Returns a map containing parameters name-value entries.
     *
     * @param context The (application) context object to use.
     * @return The initialization parameters map.
     * @throws TilesException If the context object has not been recognized.
     */
    @SuppressWarnings("unchecked")
    protected static Map<String, String> getInitParameterMap(Object context)
        throws TilesException {
        Map<String, String> initParameters = new HashMap<String, String>();
        Class<?> contextClass = context.getClass();
        try {
            Method method = contextClass.getMethod("getInitParameterNames");
            Enumeration<String> e = (Enumeration<String>) method
                    .invoke(context);

            method = contextClass.getMethod("getInitParameter", String.class);
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                initParameters.put(key, (String) method.invoke(context, key));
            }
        } catch (Exception e) {
            throw new TilesException("Unable to retrieve init parameters."
                    + " Is this context a ServletContext, PortletContext,"
                    + " or similar object?", e);
        }
        return initParameters;
    }


}
