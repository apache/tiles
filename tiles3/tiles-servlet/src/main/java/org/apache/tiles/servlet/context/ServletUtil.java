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

package org.apache.tiles.servlet.context;


import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.apache.tiles.NoSuchContainerException;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.NotAServletEnvironmentException;
import org.apache.tiles.request.servlet.ServletTilesApplicationContext;
import org.apache.tiles.request.servlet.ServletTilesRequestContext;
import org.apache.tiles.request.util.TilesRequestContextWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utilities for Tiles servlet support.
 *
 * @version $Rev$ $Date$
 * @since 2.0.6
 */
public final class ServletUtil {

    /**
     * Private constructor to avoid instantiation.
     */
    private ServletUtil() {
    }

    /**
     * Returns the default Tiles container.
     *
     * @param context The servlet context to use.
     * @return The default Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(ServletContext context) {
        return getContainer(context, TilesAccess.CONTAINER_ATTRIBUTE);
    }

    /**
     * Returns a specific Tiles container.
     *
     * @param context The servlet context to use.
     * @param key The key under which the container is stored. If null, the
     * default container will be returned.
     * @return The requested Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(ServletContext context, String key) {
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }
        return (TilesContainer) context.getAttribute(key);
    }

    /**
     * Configures the default container to be used in the application.
     *
     * @param context The servlet context object to use.
     * @param container The container object to set.
     * @since 2.1.2
     */
    public static void setContainer(ServletContext context,
            TilesContainer container) {
        setContainer(context, container, TilesAccess.CONTAINER_ATTRIBUTE);
    }

    /**
     * Configures the container to be used in the application.
     *
     * @param context The servlet context object to use.
     * @param container The container object to set.
     * @param key The key under which the container will be stored.
     * @since 2.1.2
     */
    public static void setContainer(ServletContext context,
            TilesContainer container, String key) {
        Logger log = LoggerFactory.getLogger(ServletUtil.class);
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }

        if (container == null) {
            if (log.isInfoEnabled()) {
                log.info("Removing TilesContext for context: " + context.getClass().getName());
            }
            context.removeAttribute(key);
        }
        if (container != null && log.isInfoEnabled()) {
            log.info("Publishing TilesContext for context: " + context.getClass().getName());
        }
        context.setAttribute(key, container);
    }

    /**
     * Sets the current container to use in web pages.
     *
     * @param request The request to use.
     * @param context The servlet context to use.
     * @param key The key under which the container is stored.
     * @since 2.1.0
     */
    public static void setCurrentContainer(ServletRequest request,
            ServletContext context, String key) {
        TilesContainer container = getContainer(context, key);
        if (container != null) {
            request.setAttribute(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        } else {
            throw new NoSuchContainerException("The container with the key '"
                    + key + "' cannot be found");
        }
    }

    /**
     * Sets the current container to use in web pages.
     *
     * @param request The request to use.
     * @param container The container to use as the current container.
     * @since 2.1.0
     */
    public static void setCurrentContainer(ServletRequest request,
            TilesContainer container) {
        if (container != null) {
            request.setAttribute(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        } else {
            throw new NoSuchContainerException("The container cannot be null");
        }
    }

    /**
     * Returns the current container that has been set, or the default one.
     *
     * @param request The request to use.
     * @param context The servlet context to use.
     * @return The current Tiles container to use in web pages.
     * @since 2.1.0
     */
    public static TilesContainer getCurrentContainer(ServletRequest request,
            ServletContext context) {
        TilesContainer container = (TilesContainer) request
                .getAttribute(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME);
        if (container == null) {
            container = getContainer(context);
            request.setAttribute(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        }

        return container;
    }

    /**
     * Opens a TilesRequestContext until it finds a ServletTilesRequestContext.
     *
     * @param request The request to open.
     * @return The servlet-based request context.
     * @throws NotAServletEnvironmentException If a servlet-based request
     * context could not be found.
     * @since 2.2.0
     */
    public static ServletTilesRequestContext getServletRequest(Request request) {
        Request currentRequest = request;
        while (true) {
            if (currentRequest == null) {
                throw new NotAServletEnvironmentException("Last Tiles request context is null");
            }

            if (currentRequest instanceof ServletTilesRequestContext) {
                return (ServletTilesRequestContext) currentRequest;
            }
            if (!(currentRequest instanceof TilesRequestContextWrapper)) {
                throw new NotAServletEnvironmentException("Not a Servlet environment, not supported");
            }
            currentRequest = ((TilesRequestContextWrapper) currentRequest).getWrappedRequest();
        }
    }

    /**
     * Gets a servlet context from a TilesApplicationContext.
     *
     * @param applicationContext The application context to analyze.
     * @return The servlet context.
     * @throws NotAServletEnvironmentException If the application context is not
     * servlet-based.
     * @since 2.2.0
     */
    public static ServletContext getServletContext(ApplicationContext applicationContext) {
        if (applicationContext instanceof ServletTilesApplicationContext) {
            return (ServletContext) ((ServletTilesApplicationContext) applicationContext).getContext();
        }

        throw new NotAServletEnvironmentException("Not a Servlet-based environment");
    }
}
