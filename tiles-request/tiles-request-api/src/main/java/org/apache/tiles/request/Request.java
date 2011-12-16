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
package org.apache.tiles.request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;


/**
 * Encapsulation of request information.
 *
 * @since 2.0
 * @version $Rev$ $Date$
 */
public interface Request {

    /**
     * Return an immutable Map that maps header names to the first (or only)
     * header value (as a String).
     *
     * @return The header map.
     */
    Map<String, String> getHeader();

    /**
     * Return an immutable Map that maps header names to the set of all values
     * specified in the request (as a String array). Header names must be
     * matched in a case-insensitive manner.
     *
     * @return The header values map.
     */
    Map<String, String[]> getHeaderValues();

    /**
     * Return an unreadable Map that writes headers to the response.
     *
     * @return The header map.
     */
    Map<String, String> getResponseHeaders();

    /**
     * Returns a context map, given the scope name.
     *
     * @param scope The name of the scope.
     * @return The context.
     */
    Map<String, Object> getContext(String scope);

    /**
     * Returns the native scopes, i.e. scopes that are native to the
     * implementation of the request itself (request, session and application
     * for example).
     *
     * @return The native scopes.
     */
    String[] getNativeScopes();

    /**
     * Returns all available scopes, that are the ones returned by
     * {@link #getNativeScopes()} plus derivative scopes (e.g. flash scope).
     *
     * @return All the available scopes.
     */
    String[] getAvailableScopes();

    /**
     * Returns the associated application context.
     *
     * @return The application context associated to this request.
     * @since 2.1.1
     */
    ApplicationContext getApplicationContext();

    /**
     * Returns an output stream to be used to write directly in the response.
     *
     * @return The output stream that writes in the response.
     * @throws IOException If something goes wrong when getting the output stream.
     * @since 2.1.2
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * Returns a writer to be used to write directly in the response.
     *
     * @return The writer that writes in the response.
     * @throws IOException If something goes wrong when getting the writer.
     * @since 2.1.2
     */
    Writer getWriter() throws IOException;

    /**
     * Returns a print writer to be used to write directly in the response.
     *
     * @return The print writer that writes in the response.
     * @throws IOException If something goes wrong when getting the print
     * writer.
     * @since 2.1.2
     */
    PrintWriter getPrintWriter() throws IOException;

    /**
     * Checks if the response has been committed.
     *
     * @return <code>true</code> only if the response has been committed.
     * @since 2.2.0
     */
    boolean isResponseCommitted();

    /**
     * Return an immutable Map that maps request parameter names to the first
     * (or only) value (as a String).
     *
     * @return The parameter map.
     */
    Map<String, String> getParam();

    /**
     * Return an immutable Map that maps request parameter names to the set of
     * all values (as a String array).
     *
     * @return The parameter values map.
     */
    Map<String, String[]> getParamValues();

    /**
     * Return the preferred Locale in which the client will accept content.
     *
     * @return The current request locale. It is the locale of the request
     * object itself and it is NOT the locale that the user wants to use. See
     * {@link org.apache.tiles.locale.LocaleResolver} to implement strategies to
     * resolve locales.
     */
    Locale getRequestLocale();

    /**
     * Determine whether or not the specified user is in the given role.
     * @param role the role to check against.
     * @return <code>true</code> if the user is in the given role.
     */
    boolean isUserInRole(String role);

}
