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

package org.apache.tiles.portlet.context;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.NoSuchContainerException;
import org.apache.tiles.servlet.context.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utilities for Tiles portlet support.
 *
 * @version $Rev$ $Date$
 * @since 2.0.6
 */
public final class PortletUtil {

    /**
     * Private constructor to avoid instantiation.
     */
    private PortletUtil() {
    }

    /**
     * Returns true if forced include of the result is needed.
     *
     * @param request The portlet request.
     * @return If <code>true</code> the include operation must be forced.
     * @since 2.0.6
     */
    public static boolean isForceInclude(PortletRequest request) {
        Boolean retValue = (Boolean) request
                .getAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME);
        return retValue != null && retValue.booleanValue();
    }

    /**
     * Sets the option that enables the forced include of the response.
     *
     * @param request The portlet request.
     * @param forceInclude If <code>true</code> the include operation must be
     * forced.
     * @since 2.0.6
     */
    public static void setForceInclude(PortletRequest request,
            boolean forceInclude) {
        Boolean retValue = Boolean.valueOf(forceInclude);
        request.setAttribute(
                ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
                retValue);
    }

    /**
     * Returns the default Tiles container.
     *
     * @param context The portlet context to use.
     * @return The default Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(PortletContext context) {
        return getContainer(context, TilesAccess.CONTAINER_ATTRIBUTE);
    }

    /**
     * Returns a specific Tiles container.
     *
     * @param context The portlet context to use.
     * @param key The key under which the container is stored. If null, the
     * default container will be returned.
     * @return The requested Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(PortletContext context, String key) {
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }
        return (TilesContainer) context.getAttribute(key);
    }

    /**
     * Configures the default container to be used in the application.
     *
     * @param context The portlet context object to use.
     * @param container The container object to set.
     * @since 2.1.2
     */
    public static void setContainer(PortletContext context,
            TilesContainer container) {
        setContainer(context, container, TilesAccess.CONTAINER_ATTRIBUTE);
    }

    /**
     * Configures the container to be used in the application.
     *
     * @param context The portlet context object to use.
     * @param container The container object to set.
     * @param key The key under which the container will be stored.
     * @since 2.1.2
     */
    public static void setContainer(PortletContext context,
            TilesContainer container, String key) {
        Logger log = LoggerFactory.getLogger(PortletUtil.class);
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
     * @param context The portlet context to use.
     * @param key The key under which the container is stored.
     * @since 2.1.0
     */
    public static void setCurrentContainer(PortletRequest request,
            PortletContext context, String key) {
        TilesContainer container = getContainer(context, key);
        if (container != null) {
            request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        } else {
            throw new NoSuchContainerException("The container with the key '"
                    + key + "' cannot be found");
        }
    }

    /**
     * Sets the current container to use in web pages.
     *
     * @param request The request to use.
     * @param context The portlet context to use.
     * @param container The container to use as the current container.
     * @since 2.1.0
     */
    public static void setCurrentContainer(PortletRequest request,
            PortletContext context, TilesContainer container) {
        if (container != null) {
            request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        } else {
            throw new NoSuchContainerException("The container cannot be null");
        }
    }

    /**
     * Returns the current container that has been set, or the default one.
     *
     * @param request The request to use.
     * @param context The portlet context to use.
     * @return The current Tiles container to use in web pages.
     * @since 2.1.0
     */
    public static TilesContainer getCurrentContainer(PortletRequest request,
            PortletContext context) {
        TilesContainer container = (TilesContainer) request
                .getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME);
        if (container == null) {
            container = getContainer(context);
            request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                    container);
        }

        return container;
    }

    /**
     * Returns the compose stack, that is used by the tags to compose
     * definitions, attributes, etc.
     *
     * @param request The portlet request.
     * @return The compose stack.
     * @since 2.2.0
     */
    @SuppressWarnings("unchecked")
    public static ArrayStack<Object> getComposeStack(PortletRequest request) {
        ArrayStack<Object> composeStack = (ArrayStack<Object>) request.getAttribute(
                ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME);
        if (composeStack == null) {
            composeStack = new ArrayStack<Object>();
            request.setAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        }
        return composeStack;
    }
}
