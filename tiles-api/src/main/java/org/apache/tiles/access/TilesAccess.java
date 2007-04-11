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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;

import java.lang.reflect.Method;


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
     * The logging object.
     */
    private static final Log LOG =
        LogFactory.getLog(TilesAccess.class);

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
     * Finds and returns a Tiles container object, if it was previously initialized.
     *
     * @param context The (application) context object to use.
     * @return The container if it has been configured previously, otherwise
     * <code>null</code>.
     * @see #setContainer(Object, TilesContainer)
     */
    public static TilesContainer getContainer(Object context) {
        return (TilesContainer) getAttribute(context, CONTAINER_ATTRIBUTE);
    }

    /**
     * Configures the container to be used in the application.
     *
     * @param context The (application) context object to use.
     * @param container The container object to set.
     * @throws TilesException If something goes wrong during manipulation of the
     * context.
     */
    public static void setContainer(Object context, TilesContainer container)
        throws TilesException {

        if (container == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Removing TilesContext for context: " + context.getClass().getName());
            }
            removeAttribute(context, CONTAINER_ATTRIBUTE);
        }
        if (container != null && LOG.isInfoEnabled()) {
            LOG.info("Publishing TilesContext for context: " + context.getClass().getName());
        }
        setAttribute(context, CONTAINER_ATTRIBUTE, container);
    }

    /**
     * Returns the Tiles application context object.
     *
     * @param context The (application) context to use.
     * @return The required Tiles application context.
     */
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
        try {
            Class<?> contextClass = context.getClass();
            Method attrMethod = contextClass.getMethod("getAttribute", String.class);
            return attrMethod.invoke(context, attributeName);
        } catch (Exception e) {
            LOG.warn("Unable to retrieve container from specified context: '" + context + "'", e);
            return null;
        }
    }

    /**
     * Sets an attribute in a context.
     *
     * @param context The context object to use.
     * @param name The name of the attribute to set.
     * @param value The value of the attribute to set.
     * @throws TilesException If something goes wrong during setting the
     * attribute.
     */
    private static void setAttribute(Object context, String name, Object value)
        throws TilesException {
        try {
            Class<?> contextClass = context.getClass();
            Method attrMethod = contextClass.getMethod("setAttribute", String.class, Object.class);
            attrMethod.invoke(context, name, value);
        } catch (Exception e) {
            throw new TilesException("Unable to set attribute for specified context: '" + context + "'");
        }
    }

    /**
     * Removes an attribute from a context.
     *
     * @param context The context object to use.
     * @param name The name of the attribute to remove.
     * @throws TilesException If something goes wrong during removal.
     */
    private static void removeAttribute(Object context, String name)
        throws TilesException {
        try {
            Class<?> contextClass = context.getClass();
            Method attrMethod = contextClass.getMethod("removeAttribute", String.class);
            attrMethod.invoke(context, name);
        } catch (Exception e) {
            throw new TilesException("Unable to remove attribute for specified context: '" + context + "'");
        }
    }
}
