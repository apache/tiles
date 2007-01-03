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
 *
 */
package org.apache.tiles.factory;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.BasicTilesContextFactory;
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

    public static final String CONTAINER_FACTORY_INIT_PARAM =
        "org.apache.tiles.CONTAINER_FACTORY";

    public static final String CONTAINER_FACTORY_MUTABLE_INIT_PARAM =
        "org.apache.tiles.CONTAINER_FACTORY.mutable";

    public static final String CONTEXT_FACTORY_INIT_PARAM =
        "org.apache.tiles.CONTEXT_FACTORY";

    public static final String DEFINITIONS_FACTORY_INIT_PARAM =
        "org.apache.tiles.DEFINITIONS_FACTORY";

    public static final String PREPARER_FACTORY_INIT_PARAM =
        "org.apache.tiles.PREPARER_FACTORY";


    private static final Map<String, String> DEFAULTS =
        new HashMap<String, String>();

    static {
        DEFAULTS.put(CONTAINER_FACTORY_INIT_PARAM, TilesContainerFactory.class.getName());
        DEFAULTS.put(CONTEXT_FACTORY_INIT_PARAM, BasicTilesContextFactory.class.getName());
        DEFAULTS.put(DEFINITIONS_FACTORY_INIT_PARAM, UrlDefinitionsFactory.class.getName());
        DEFAULTS.put(PREPARER_FACTORY_INIT_PARAM, BasicPreparerFactory.class.getName());
    }

    protected Map<String, String> defaultConfiguration =
        new HashMap<String, String>(DEFAULTS);

    /**
     * Retrieve a factory instance as configured through the
     * specified context.
     * <p/>
     * The context will be queried and if a init parameter
     * named 'org.apache.tiles.CONTAINER_FACTORY' is discovered
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
     * Retrieve a factory instance as configured through the
     * specified context.
     * <p/>
     * The context will be queried and if a init parameter
     * named 'org.apache.tiles.CONTAINER_FACTORY' is discovered
     * this class will be instantiated and returned. Otherwise,
     * the factory will attempt to utilize one of it's internal
     * factories.
     *
     * @param context the executing applications context.
     *                Typically a ServletContext or PortletContext
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

    public TilesContainer createContainer(Object context) throws TilesException {
        String value = getInitParameter(context, CONTAINER_FACTORY_MUTABLE_INIT_PARAM);
        if (Boolean.parseBoolean(value)) {
            return createMutableTilesContainer(context);
        } else {
            return createTilesContainer(context);
        }
    }

    public void setDefaultConfiguration(Map<String, String> defaultConfiguration) {
        if (defaultConfiguration != null) {
            this.defaultConfiguration.putAll(defaultConfiguration);
        }
    }

    public void setDefaultValue(String key, String value) {
        this.defaultConfiguration.put(key, value);
    }

    public TilesContainer createTilesContainer(Object context)
        throws TilesException {
        BasicTilesContainer container = new BasicTilesContainer();
        initializeContainer(context, container);
        return container;
    }

    public MutableTilesContainer createMutableTilesContainer(Object context)
        throws TilesException {
        CachingTilesContainer container = new CachingTilesContainer();
        initializeContainer(context, container);
        return container;
    }

    protected void initializeContainer(Object context,
                                       BasicTilesContainer container)
        throws TilesException {
        storeContainerDependencies(context, container);
        container.init(getInitParameterMap(context));

    }

    protected void storeContainerDependencies(Object context,
                                              BasicTilesContainer container) throws TilesException {

        Map<String, String> configuration = new HashMap<String, String>(defaultConfiguration);
        configuration.putAll(getInitParameterMap(context));

        TilesContextFactory contextFactory =
            (TilesContextFactory) createFactory(configuration,
                CONTEXT_FACTORY_INIT_PARAM);

        DefinitionsFactory defsFactory =
            (DefinitionsFactory) createFactory(configuration,
                DEFINITIONS_FACTORY_INIT_PARAM);

        PreparerFactory prepFactory =
            (PreparerFactory) createFactory(configuration,
                PREPARER_FACTORY_INIT_PARAM);

        TilesApplicationContext tilesContext =
            contextFactory.createApplicationContext(context);

        container.setDefinitionsFactory(defsFactory);
        container.setContextFactory(contextFactory);
        container.setPreparerFactory(prepFactory);
        container.setApplicationContext(tilesContext);
    }


    protected static Object createFactory(Map<String, String> configuration, String initParameterName)
        throws TilesException {
        String factoryName = resolveFactoryName(configuration, initParameterName);
        return ClassUtil.instantiate(factoryName);
    }

    protected static String resolveFactoryName(Map<String, String> configuration, String parameterName)
        throws TilesException {
        Object factoryName = configuration.get(parameterName);
        return factoryName == null
            ? DEFAULTS.get(parameterName)
            : factoryName.toString();
    }

    protected static String getInitParameter(Object context,
                                             String parameterName) throws TilesException {
        Object value;
        try {
            Class contextClass = context.getClass();
            Method getInitParameterMethod =
                contextClass.getMethod("getInitParameter", String.class);
            value = getInitParameterMethod.invoke(context, parameterName);
        } catch (Exception e) {
            throw new TilesException("Unrecognized context.  Is this context" +
                " a ServletContext, PortletContext, or similar?", e);
        }
        return value == null ? null : value.toString();
    }

    protected static Map<String, String> getInitParameterMap(Object context)
        throws TilesException {
        Map<String, String> initParameters = new HashMap<String, String>();
        Class contextClass = context.getClass();
        try {
            Method method = contextClass.getMethod("getInitParameterNames");
            Enumeration e = (Enumeration) method.invoke(context);

            method = contextClass.getMethod("getInitParameter", String.class);
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                initParameters.put(key, (String) method.invoke(context, key));
            }
        } catch (Exception e) {
            throw new TilesException("Unable to retrieve init parameters." +
                " Is this context a ServletContext, PortletContext," +
                " or similar object?", e);
        }
        return initParameters;
    }


}
