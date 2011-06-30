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

package org.apache.tiles.definition;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;

/**
 * Creates a TilesApplicationContext that contains only a Locale.
 *
 * @version $Rev$ $Date$
 */
public class MockOnlyLocaleTilesContext implements Request {

    /**
     * The locale object.
     */
    private Locale locale;

    /** Creates a new instance of MockOnlyLocaleTilesContext.
     *
     * @param locale The locale object to use.
     */
    public MockOnlyLocaleTilesContext(Locale locale) {
        this.locale = locale;
    }

    /**
     * Returns the locale specified in the constructor.
     *
     * @return The locale of the request.
     * @see org.apache.tiles.request.Request#getRequestLocale()
     */
    public Locale getRequestLocale() {
        return locale;
    }

    // The rest of the implemented methods has a "dummy" behaviour, doing
    // nothing or returning null, because they are not needed at all in tests
    // that use this class.

    /** {@inheritDoc} */
    public void dispatch(String path) {
    }

    /** {@inheritDoc} */
    public Map<String, String> getHeader() {
        return null;
    }

    /** {@inheritDoc} */
    public OutputStream getOutputStream() {
        return null;
    }

    /** {@inheritDoc} */
    public Writer getWriter() {
        return null;
    }

    /** {@inheritDoc} */
    public PrintWriter getPrintWriter() {
        return null;
    }

    /** {@inheritDoc} */
    public boolean isResponseCommitted() {
        return false;
    }

    /** {@inheritDoc} */
    public void setContentType(String contentType) {
        // Does nothing
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getHeaderValues() {
        return null;
    }

    /** {@inheritDoc} */
    public Map<String, String> getParam() {
        return null;
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getParamValues() {
        return null;
    }

    /** {@inheritDoc} */
    public Map<String, Object> getRequestScope() {
        return null;
    }

    /** {@inheritDoc} */
    public Map<String, Object> getSessionScope() {
        return null;
    }

    @Override
    public Map<String, Object> getContext(String scope) {
        return null;
    }

    /** {@inheritDoc} */
    public ApplicationContext getApplicationContext() {
        return null;
    }

    /** {@inheritDoc} */
    public void include(String path) {
    }

    /** {@inheritDoc} */
    public boolean isUserInRole(String role) {
        return false;
    }

    /** {@inheritDoc} */
    public Object[] getRequestObjects() {
        return null;
    }

    @Override
    public String[] getAvailableScopes() {
        return null;
    }

    @Override
    public String[] getNativeScopes() {
        return null;
    }
}
