/*
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
 *
 */
package org.apache.tiles.context;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * Encapsulation of request information.
 *
 * @version $Rev$
 * @since 2.0
 */
public interface TilesRequestContext {

    /**
     * Return an immutable Map that maps header names to the first (or only)
     * header value (as a String).
     */
    Map getHeader();

    /**
     * Return an immutable Map that maps header names to the set of all values
     * specified in the request (as a String array). Header names must be
     * matched in a case-insensitive manner.
     */
    Map getHeaderValues();

    /**
     * Return a mutable Map that maps request scope attribute names to their
     * values.
     */
    Map getRequestScope();

    /**
     * Return a mutable Map that maps session scope attribute names to their
     * values.
     */
    Map getSessionScope();

    /**
     * Dispatches the request to a specified path.
     */
    void dispatch(String path) throws IOException, Exception;

    /**
     * Includes the response from the specified URL in the current response output.
     */
    void include(String path) throws IOException, Exception;

    /**
     * Return an immutable Map that maps request parameter names to the first
     * (or only) value (as a String).
     */
    Map getParam();

    /**
     * Return an immutable Map that maps request parameter names to the set of
     * all values (as a String array).
     */
    Map getParamValues();

    /**
     * Return the preferred Locale in which the client will accept content.
     */
    Locale getRequestLocale();

    /**
     * Determine whether or not the specified user is in the given role
     * @param role
     * @return
     */
    boolean isUserInRole(String role);
}
