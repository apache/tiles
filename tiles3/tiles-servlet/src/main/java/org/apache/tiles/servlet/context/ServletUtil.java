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

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.NotAServletEnvironmentException;
import org.apache.tiles.request.servlet.ServletTilesApplicationContext;
import org.apache.tiles.request.servlet.ServletTilesRequestContext;
import org.apache.tiles.request.util.TilesRequestContextWrapper;


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
