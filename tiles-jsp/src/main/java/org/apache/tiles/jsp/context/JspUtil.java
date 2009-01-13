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

package org.apache.tiles.jsp.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.NoSuchContainerException;
import org.apache.tiles.servlet.context.ServletUtil;

import javax.servlet.jsp.PageContext;

/**
 * Utility class for working within a Jsp environment.
 *
 * @version $Rev$ $Date$
 */
public final class JspUtil {

    /**
     * The logging object.
     */
    private static final Log LOG =
        LogFactory.getLog(ServletUtil.class);

    /**
     * Constructor, private to avoid instantiation.
     */
    private JspUtil() {
    }

    /**
     * Returns true if forced include of the result is needed.
     *
     * @param context The page context.
     * @return If <code>true</code> the include operation must be forced.
     * @since 2.0.6
     */
    public static boolean isForceInclude(PageContext context) {
        Boolean retValue = (Boolean) context.getAttribute(
                ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
                PageContext.REQUEST_SCOPE);
        return retValue != null && retValue.booleanValue();
    }

    /**
     * Sets the option that enables the forced include of the response.
     *
     * @param context The page context.
     * @param forceInclude If <code>true</code> the include operation must be
     * forced.
     * @since 2.0.6
     */
    public static void setForceInclude(PageContext context, boolean forceInclude) {
        Boolean retValue = Boolean.valueOf(forceInclude);
        context.setAttribute(
                ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
                retValue, PageContext.REQUEST_SCOPE);
    }

    /**
     * Returns the default Tiles container.
     *
     * @param context The page context to use.
     * @return The default Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(PageContext context) {
        return getContainer(context, TilesAccess.CONTAINER_ATTRIBUTE);
    }

    /**
     * Returns a specific Tiles container.
     *
     * @param context The page context to use.
     * @param key The key under which the container is stored. If null, the
     * default container will be returned.
     * @return The requested Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(PageContext context, String key) {
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }
        return (TilesContainer) context.getAttribute(key,
                PageContext.APPLICATION_SCOPE);
    }

    /**
     * Configures the default container to be used in the application.
     *
     * @param context The page context object to use.
     * @param container The container object to set.
     * @since 2.1.2
     */
    public static void setContainer(PageContext context,
            TilesContainer container) {
        setContainer(context, container, TilesAccess.CONTAINER_ATTRIBUTE);
    }

    /**
     * Configures the container to be used in the application.
     *
     * @param context The page context object to use.
     * @param container The container object to set.
     * @param key The key under which the container will be stored.
     * @since 2.1.2
     */
    public static void setContainer(PageContext context,
            TilesContainer container, String key) {
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }

        if (container == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Removing TilesContext for context: " + context.getClass().getName());
            }
            context.removeAttribute(key, PageContext.APPLICATION_SCOPE);
        }
        if (container != null && LOG.isInfoEnabled()) {
            LOG.info("Publishing TilesContext for context: " + context.getClass().getName());
        }
        context.setAttribute(key, container, PageContext.APPLICATION_SCOPE);
    }

    /**
     * Sets the current container to use in web pages.
     *
     * @param context The page context to use.
     * @param key The key under which the container is stored.
     * @since 2.1.0
     */
    public static void setCurrentContainer(PageContext context, String key) {
        TilesContainer container = getContainer(context, key);
        if (container != null) {
            context.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                    container, PageContext.REQUEST_SCOPE);
        } else {
            throw new NoSuchContainerException("The container with the key '"
                    + key + "' cannot be found");
        }
    }

    /**
     * Sets the current container to use in web pages.
     *
     * @param context The page context to use.
     * @param container The container to use as the current container.
     * @since 2.1.0
     */
    public static void setCurrentContainer(PageContext context,
            TilesContainer container) {
        if (container != null) {
            context.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                    container, PageContext.REQUEST_SCOPE);
        } else {
            throw new NoSuchContainerException("The container cannot be null");
        }
    }

    /**
     * Returns the current container that has been set, or the default one.
     *
     * @param context The page context to use.
     * @return The current Tiles container to use in web pages.
     * @since 2.1.0
     */
    public static TilesContainer getCurrentContainer(PageContext context) {
        TilesContainer container = (TilesContainer) context.getAttribute(
                ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                PageContext.REQUEST_SCOPE);
        if (container == null) {
            container = getContainer(context);
            context.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                    container, PageContext.REQUEST_SCOPE);
        }

        return container;
    }
}
