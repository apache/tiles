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
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.NoSuchContainerException;


/**
 * Utilities for Tiles servlet support.
 *
 * @version $Rev$ $Date$
 * @since 2.0.6
 */
public final class ServletUtil {

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
     * Sets the current container to use in web pages.
     *
     * @param request The request to use.
     * @param context The servlet context to use.
     * @param key The key under which the container is stored.
     * @since 2.1.0
     */
    public static void setCurrentContainer(ServletRequest request,
            ServletContext context, String key) {
        TilesContainer container = TilesAccess.getContainer(context, key);
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
            container = TilesAccess.getContainer(context);
            request.setAttribute(CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        }

        return container;
    }
}
