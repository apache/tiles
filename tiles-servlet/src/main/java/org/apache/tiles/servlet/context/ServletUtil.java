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

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextWrapper;
import org.apache.tiles.impl.NoSuchContainerException;
import org.apache.tiles.util.TilesIOException;
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
     * The name of the attribute that will contain the compose stack.
     */
    public static final String COMPOSE_STACK_ATTRIBUTE_NAME = "org.apache.tiles.template.COMPOSE_STACK";

    /**
     * Name of the attribute used to store the force-include option.
     * @since 2.0.6
     */
    public static final String FORCE_INCLUDE_ATTRIBUTE_NAME =
        "org.apache.tiles.servlet.context.ServletTilesRequestContext.FORCE_INCLUDE";

    /**
     * Name of the attribute used to store the current used container.
     */
    public static final String CURRENT_CONTAINER_ATTRIBUTE_NAME =
        "org.apache.tiles.servlet.context.ServletTilesRequestContext.CURRENT_CONTAINER_KEY";

    /**
     * Private constructor to avoid instantiation.
     */
    private ServletUtil() {
    }

    /**
     * Returns true if forced include of the result is needed.
     *
     * @param request The HTTP request.
     * @return If <code>true</code> the include operation must be forced.
     * @since 2.0.6
     */
    public static boolean isForceInclude(HttpServletRequest request) {
        Boolean retValue = (Boolean) request
                .getAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME);
        return retValue != null && retValue.booleanValue();
    }

    /**
     * Sets the option that enables the forced include of the response.
     *
     * @param request The HTTP request.
     * @param forceInclude If <code>true</code> the include operation must be
     * forced.
     * @since 2.0.6
     */
    public static void setForceInclude(HttpServletRequest request,
            boolean forceInclude) {
        Boolean retValue = Boolean.valueOf(forceInclude);
        request.setAttribute(
                ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
                retValue);
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
            request.setAttribute(CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        } else {
            throw new NoSuchContainerException("The container with the key '"
                    + key + "' cannot be found");
        }
    }

    /**
     * Sets the current container to use in web pages.
     *
     * @param request The request to use.
     * @param context The servlet context to use.
     * @param container The container to use as the current container.
     * @since 2.1.0
     */
    public static void setCurrentContainer(ServletRequest request,
            ServletContext context, TilesContainer container) {
        if (container != null) {
            request.setAttribute(CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
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
                .getAttribute(CURRENT_CONTAINER_ATTRIBUTE_NAME);
        if (container == null) {
            container = getContainer(context);
            request.setAttribute(CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        }

        return container;
    }

    /**
     * Wraps a ServletException to create an IOException with the root cause if present.
     *
     * @param ex The exception to wrap.
     * @param message The message of the exception.
     * @return The wrapped exception.
     * @since 2.1.1
     */
    public static IOException wrapServletException(ServletException ex,
            String message) {
        IOException retValue;
        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            // Replace the ServletException with an IOException, with the root
            // cause of the first as the cause of the latter.
            retValue = new TilesIOException(message, rootCause);
        } else {
            retValue = new TilesIOException(message, ex);
        }

        return retValue;
    }

    /**
     * Returns the compose stack, that is used by the tags to compose
     * definitions, attributes, etc.
     *
     * @param request The HTTP request.
     * @return The compose stack.
     * @since 2.2.0
     */
    @SuppressWarnings("unchecked")
    public static ArrayStack<Object> getComposeStack(HttpServletRequest request) {
        ArrayStack<Object> composeStack = (ArrayStack<Object>) request.getAttribute(
                COMPOSE_STACK_ATTRIBUTE_NAME);
        if (composeStack == null) {
            composeStack = new ArrayStack<Object>();
            request.setAttribute(COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        }
        return composeStack;
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
    public static ServletTilesRequestContext getServletRequest(TilesRequestContext request) {
        TilesRequestContext currentRequest = request;
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
    public static ServletContext getServletContext(TilesApplicationContext applicationContext) {
        if (applicationContext instanceof ServletTilesApplicationContext) {
            return (ServletContext) ((ServletTilesApplicationContext) applicationContext).getContext();
        }

        throw new NotAServletEnvironmentException("Not a Servlet-based environment");
    }
}
