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
package org.apache.tiles.access;

import java.lang.reflect.Method;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.reflect.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provides static access to the tiles container.
 *
 * @version $Rev$ $Date$
 */
public final class TilesAccess {

    /**
     * Constructor, private to avoid instantiation.
     */
    private TilesAccess() {
    }

    /**
     * The name of the attribute to use when getting and setting the container
     * object in a context.
     */
    public static final String CONTAINER_ATTRIBUTE =
        "org.apache.tiles.CONTAINER";

    /**
     * The name of the attribute to get the {@link TilesApplicationContext} from
     * a context.
     */
    private static final String CONTEXT_ATTRIBUTE =
        "org.apache.tiles.APPLICATION_CONTEXT";

    /**
     * Finds and returns the default Tiles container object, if it was
     * previously initialized.
     *
     * @param context The (application) context object to use.
     * @return The container if it has been configured previously, otherwise
     * <code>null</code>.
     * @see #setContainer(Object, TilesContainer)
     * @deprecated Use one of the environment-specific Utilities (e.g.
     * ServletUtil).
     */
    @Deprecated
    public static TilesContainer getContainer(Object context) {
        return (TilesContainer) getAttribute(context, CONTAINER_ATTRIBUTE);
    }

    /**
     * Configures the default container to be used in the application.
     *
     * @param context The Tiles application context object to use.
     * @param container The container object to set.
     * @throws TilesAccessException If something goes wrong during manipulation of the
     * context.
     * @since 2.1.2
     */
    public static void setContainer(TilesApplicationContext context,
            TilesContainer container) {
        setContainer(context, container, CONTAINER_ATTRIBUTE);
    }

    /**
     * Configures the container to be used in the application.
     *
     * @param context The Tiles application context object to use.
     * @param container The container object to set.
     * @param key The key under which the container will be stored.
     * @throws TilesAccessException If something goes wrong during manipulation of the
     * context.
     * @since 2.1.2
     */
    public static void setContainer(TilesApplicationContext context,
            TilesContainer container, String key) {
        Logger log = LoggerFactory.getLogger(TilesAccess.class);
        if (key == null) {
            key = CONTAINER_ATTRIBUTE;
        }

        if (container == null) {
            if (log.isInfoEnabled()) {
                log.info("Removing TilesContext for context: " + context.getClass().getName());
            }
            context.getApplicationScope().remove(key);
        }
        if (container != null && log.isInfoEnabled()) {
            log.info("Publishing TilesContext for context: " + context.getClass().getName());
        }
        context.getApplicationScope().put(key, container);
    }

    /**
     * Configures the default container to be used in the application.
     *
     * @param context The (application) context object to use.
     * @param container The container object to set.
     * @throws TilesAccessException If something goes wrong during manipulation of the
     * context.
     * @deprecated Use {@link #setContainer(TilesApplicationContext, TilesContainer)}.
     */
    @Deprecated
    public static void setContainer(Object context, TilesContainer container) {
        Logger log = LoggerFactory.getLogger(TilesAccess.class);
        if (container == null) {
            if (log.isInfoEnabled()) {
                log.info("Removing TilesContext for context: " + context.getClass().getName());
            }
            removeAttribute(context, CONTAINER_ATTRIBUTE);
        }
        if (container != null && log.isInfoEnabled()) {
            log.info("Publishing TilesContext for context: " + context.getClass().getName());
        }
        setAttribute(context, CONTAINER_ATTRIBUTE, container);
    }

    /**
     * Returns the Tiles application context object.
     *
     * @param context The (application) context to use.
     * @return The required Tiles application context.
     * @deprecated Use one of the environment-specific Utilities (e.g.
     * ServletUtil).
     */
    @Deprecated
    public static TilesApplicationContext getApplicationContext(Object context) {
        TilesContainer container = getContainer(context);
        if (container != null) {
            return container.getApplicationContext();
        }
        return (TilesApplicationContext) getAttribute(context, CONTEXT_ATTRIBUTE);
    }

    /**
     * Returns an attribute from a context.
     *
     * @param context The context object to use.
     * @param attributeName The name of the attribute to search for.
     * @return The object, that is the value of the specified attribute.
     */
    private static Object getAttribute(Object context, String attributeName) {
        Class<?> contextClass = context.getClass();
        Method attrMethod = ClassUtil.getForcedAccessibleMethod(
                contextClass, "getAttribute", String.class);
        return ClassUtil.invokeMethod(context, attrMethod, attributeName);
    }

    /**
     * Sets an attribute in a context.
     *
     * @param context The context object to use.
     * @param name The name of the attribute to set.
     * @param value The value of the attribute to set.
     * @throws TilesAccessException If something goes wrong during setting the
     * attribute.
     */
    private static void setAttribute(Object context, String name, Object value) {
        Class<?> contextClass = context.getClass();
        Method attrMethod = ClassUtil.getForcedAccessibleMethod(
                contextClass, "setAttribute", String.class, Object.class);
        ClassUtil.invokeMethod(context, attrMethod, name, value);
    }

    /**
     * Removes an attribute from a context.
     *
     * @param context The context object to use.
     * @param name The name of the attribute to remove.
     * @throws TilesAccessException If something goes wrong during removal.
     */
    private static void removeAttribute(Object context, String name) {
        Class<?> contextClass = context.getClass();
        Method attrMethod = ClassUtil.getForcedAccessibleMethod(
                contextClass, "removeAttribute", String.class);
        ClassUtil.invokeMethod(context, attrMethod, name);
    }
}
