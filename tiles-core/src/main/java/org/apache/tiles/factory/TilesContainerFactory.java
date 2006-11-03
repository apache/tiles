/*
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
import org.apache.tiles.preparer.BasicPreparerFactory;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.context.BasicTilesContextFactory;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory provided for convenience.
 * This factory creates a default implementation of
 * the container, initializes, and puts it into service.
 *
 * @version $Rev$
 * @since 2.0
 */
public class TilesContainerFactory {

    public static final String CONTAINER_FACTORY_INIT_PARAM =
        "org.apache.tiles.CONTAINER_FACTORY";

    public static final String CONTEXT_FACTORY_INIT_PARAM =
        "org.apache.tiles.CONTEXT_FACTORY";

    public static final String DEFINITIONS_FACTORY_INIT_PARAM =
        "org.apache.tiles.DEFINITIONS_FACTORY";

    public static final String PREPARER_FACTORY_INIT_PARAM =
        "org.apache.tiles.PREPARER_FACTORY";

    private static final Map DEFAULT_IMPLEMENTATIONS = new HashMap();

    static {
        DEFAULT_IMPLEMENTATIONS.put(CONTAINER_FACTORY_INIT_PARAM, TilesContainerFactory.class.getName());
        DEFAULT_IMPLEMENTATIONS.put(CONTEXT_FACTORY_INIT_PARAM, BasicTilesContextFactory.class.getName());
        DEFAULT_IMPLEMENTATIONS.put(DEFINITIONS_FACTORY_INIT_PARAM, UrlDefinitionsFactory.class.getName());
        DEFAULT_IMPLEMENTATIONS.put(PREPARER_FACTORY_INIT_PARAM, BasicPreparerFactory.class.getName());
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
     * @param context
     * @return
     * @throws TilesException
     */
    public static TilesContainerFactory getFactory(Object context)
        throws TilesException {
        return (TilesContainerFactory) TilesContainerFactory
            .createFactory(context, CONTAINER_FACTORY_INIT_PARAM);
    }


    public TilesContainer createContainer(Object context)
        throws TilesException {
        BasicTilesContainer container = new BasicTilesContainer();

        TilesContextFactory contextFactory =
            (TilesContextFactory) createFactory(context, CONTEXT_FACTORY_INIT_PARAM);

        DefinitionsFactory defsFactory =
            (DefinitionsFactory) createFactory(context, DEFINITIONS_FACTORY_INIT_PARAM);

        PreparerFactory prepFactory =
            (PreparerFactory) createFactory(context, PREPARER_FACTORY_INIT_PARAM);

        container.setDefinitionsFactory(defsFactory);
        container.setContextFactory(contextFactory);
        container.setPreparerFactory(prepFactory);

        TilesApplicationContext tilesContext =
            contextFactory.createApplicationContext(context);

        container.init(tilesContext);

        return container;
    }


    public Map getInitParameterMap(Object context)
        throws TilesException {
        Map initParameters = new HashMap();
        Class contextClass = context.getClass();
        try {
            Method method = contextClass.getMethod("getInitParameterNames");
            Enumeration e = (Enumeration) method.invoke(context);

            method = contextClass.getMethod("getInitParameter", String.class);
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                initParameters.put(key, method.invoke(context, key));
            }
        } catch (Exception e) {
            throw new TilesException("Unable to retrieve init parameters." +
                " Is this context a ServletContext, PortletContext," +
                " or similar object?");
        }
        return initParameters;
    }


    public static Object createFactory(Object context, String initParameterName)
        throws TilesException {
        String factoryName = resolveFactoryName(context, initParameterName);
        try {
            Class factoryClass = Class.forName(factoryName);
            return factoryClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new TilesException("Unable to resolve factory class: '" + factoryName + "'");
        } catch (IllegalAccessException e) {
            throw new TilesException("Unable to access factory class: '" + factoryName + "'");
        } catch (InstantiationException e) {
            throw new TilesException("Unable to instantiate factory class: '"
                + factoryName + "'. Make sure that this class has a default constructor");
        }
    }

    public static String resolveFactoryName(Object context, String parameterName)
        throws TilesException {

        Object factoryName = null;
        try {
            Class contextClass = context.getClass();
            Method getInitParameterMethod =
                contextClass.getMethod("getInitParameter", String.class);
            factoryName = getInitParameterMethod.invoke(context, parameterName);
        } catch (Exception e) {
            throw new TilesException("Unrecognized context.  Is this context" +
                "a ServletContext, PortletContext, or similar?", e);
        }
        return factoryName == null
            ? DEFAULT_IMPLEMENTATIONS.get(parameterName).toString()
            : factoryName.toString();
    }
}
