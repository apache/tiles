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
 * @since 2.0
 * @version $Rev$ $Date$
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
    
    private Map<String, String> defaults =
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
    		Map<String, String> defaults) throws TilesException {
        TilesContainerFactory factory =
            (TilesContainerFactory) TilesContainerFactory.createFactory(context,
                    CONTAINER_FACTORY_INIT_PARAM, defaults);
        factory.setDefaults(defaults);
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
    
    public void setDefaults(Map<String, String> defaults) {
        if (defaults != null) {
            this.defaults.putAll(defaults);
        }
    }
    
    public void setDefaultValue(String key, String value) {
        this.defaults.put(key, value);
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

        TilesContextFactory contextFactory =
            (TilesContextFactory) createFactory(context,
            		CONTEXT_FACTORY_INIT_PARAM, defaults);

        DefinitionsFactory defsFactory =
            (DefinitionsFactory) createFactory(context,
            		DEFINITIONS_FACTORY_INIT_PARAM, defaults);

        PreparerFactory prepFactory =
            (PreparerFactory) createFactory(context,
            		PREPARER_FACTORY_INIT_PARAM, defaults);

        TilesApplicationContext tilesContext =
            contextFactory.createApplicationContext(context);

        container.setDefinitionsFactory(defsFactory);
        container.setContextFactory(contextFactory);
        container.setPreparerFactory(prepFactory);
        container.setApplicationContext(tilesContext);

        container.init(getInitParameterMap(context));

    }


    protected Map<String, String> getInitParameterMap(Object context)
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


    protected static Object createFactory(Object context,
    		String initParameterName, Map<String, String> defaults)
        throws TilesException {
        String factoryName = resolveFactoryName(context, initParameterName,
        		defaults);
        return ClassUtil.instantiate(factoryName);
    }

    protected static String resolveFactoryName(Object context,
    		String parameterName, Map<String, String> defaults)
        throws TilesException {
        Object factoryName = getInitParameter(context, parameterName);
        if (factoryName == null && defaults != null) {
        	factoryName = defaults.get(parameterName);
        }
        return factoryName == null
            ? DEFAULTS.get(parameterName)
            : factoryName.toString();
    }

    private static String getInitParameter(Object context, String parameterName)
        throws TilesException {
        Object value;
        try {
            Class contextClass = context.getClass();
            Method getInitParameterMethod =
                contextClass.getMethod("getInitParameter", String.class);
            value = getInitParameterMethod.invoke(context, parameterName);
        } catch (Exception e) {
            throw new TilesException("Unrecognized context.  Is this context" +
                "a ServletContext, PortletContext, or similar?", e);
        }
        return value == null ? null : value.toString();
    }
}
