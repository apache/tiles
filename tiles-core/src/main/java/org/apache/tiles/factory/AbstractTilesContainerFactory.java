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

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.util.ClassUtil;

/**
 * Abstract Factory that creates instances of {@link TilesContainerFactory}.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public abstract class AbstractTilesContainerFactory {

    /**
     * Initialization parameter that represents the container factory class
     * name.
     *
     * @since 2.1.0
     */
    public static final String CONTAINER_FACTORY_INIT_PARAM =
        "org.apache.tiles.factory.TilesContainerFactory";

    /**
     * Default configuration parameters.
     */
    private static final Map<String, String> DEFAULTS =
        new HashMap<String, String>();

    static {
        DEFAULTS.put(CONTAINER_FACTORY_INIT_PARAM, TilesContainerFactory.class.getName());
    }

    /**
     * Creates a factory instance.
     *
     * @param context The application context object.
     * @return The created factory.
     * @throws TilesContainerFactoryException If something goes wrong during
     * creation.
     * @since 2.1.0
     */
    @SuppressWarnings("deprecation")
    public static AbstractTilesContainerFactory getTilesContainerFactory(Object context) {
        AbstractTilesContainerFactory retValue;
        String factoryName = getInitParameter(context, CONTAINER_FACTORY_INIT_PARAM);
        if (factoryName == null) {
            factoryName = getInitParameter(context,
                    TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM);
        }
        if (factoryName != null) {
            retValue = (AbstractTilesContainerFactory) ClassUtil.instantiate(factoryName);
        } else {
            retValue = new TilesContainerFactory();
        }
        return retValue;
    }

    /**
     * Creates a Tiles container.
     *
     * @param context The (application) context object.
     * @return The created container.
     * @throws TilesContainerFactoryException If something goes wrong during
     * instantiation.
     * @since 2.1.0
     */
    public abstract TilesContainer createContainer(Object context);

    /**
     * Returns a map containing parameters name-value entries.
     *
     * @param context The (application) context object to use.
     * @return The initialization parameters map.
     * @throws TilesContainerFactoryException If the context object has not been
     * recognized.
     * @since 2.1.0
     */
    @SuppressWarnings("unchecked")
    protected static Map<String, String> getInitParameterMap(Object context) {
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
            throw new TilesContainerFactoryException(
                    "Unable to retrieve init parameters."
                    + " Is this context a ServletContext, PortletContext,"
                    + " or similar object?", e);
        }
        return initParameters;
    }

    /**
     * Returns the value of an initialization parameter.
     *
     * @param context The (application) context object to use.
     * @param parameterName The parameter name to retrieve.
     * @return The parameter value.
     * @throws TilesContainerFactoryException If the context has not been
     * recognized.
     * @since 2.1.0
     */
    protected static String getInitParameter(Object context,
            String parameterName) {
        Object value;
        try {
            Class<?> contextClass = context.getClass();
            Method getInitParameterMethod =
                contextClass.getMethod("getInitParameter", String.class);
            value = getInitParameterMethod.invoke(context, parameterName);
        } catch (Exception e) {
            throw new TilesContainerFactoryException(
                    "Unrecognized context.  Is this context"
                    + " a ServletContext, PortletContext, or similar?", e);
        }
        return value == null ? null : value.toString();
    }
}
