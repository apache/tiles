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

package org.apache.tiles.request.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;

/**
 * Utilities for Tiles request servlet support.
 *
 * @version $Rev$ $Date$
 * @since 3.0.0
 */
public final class ServletUtil {

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
            retValue = new IOException(message, rootCause);
        } else {
            retValue = new IOException(message, ex);
        }

        return retValue;
    }

    public static ApplicationContext getApplicationContext(ServletContext servletContext) {
        return (ApplicationContext) servletContext
                .getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE);
    }
}
