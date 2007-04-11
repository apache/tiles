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
package org.apache.tiles.context;

import java.util.Map;
import java.util.Locale;
import java.io.IOException;

/**
 * Delegate for ease of customization.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public class TilesRequestContextWrapper implements TilesRequestContext {

    /**
     * The wrapper request context object.
     */
    private TilesRequestContext context;


    /**
     * Constructor.
     *
     * @param context The request context to wrap.
     */
    public TilesRequestContextWrapper(TilesRequestContext context) {
        this.context = context;
    }

    /** {@inheritDoc} */
    public Map<String, String> getHeader() {
        return context.getHeader();
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getHeaderValues() {
        return context.getHeaderValues();
    }

    /** {@inheritDoc} */
    public Map<String, Object> getRequestScope() {
        return context.getRequestScope();
    }

    /** {@inheritDoc} */
    public Map<String, Object> getSessionScope() {
        return context.getSessionScope();
    }

    /** {@inheritDoc} */
    public void dispatch(String path) throws IOException {
        context.dispatch(path);
    }

    /** {@inheritDoc} */
    public void include(String path) throws IOException {
        context.include(path);
    }

    /** {@inheritDoc} */
    public Map<String, String> getParam() {
        return context.getParam();
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getParamValues() {
        return context.getParamValues();
    }

    /** {@inheritDoc} */
    public Locale getRequestLocale() {
        return context.getRequestLocale();
    }

    /** {@inheritDoc} */
    public boolean isUserInRole(String role) {
        return context.isUserInRole(role);
    }


    /** {@inheritDoc} */
    public Object getResponse() {
        return context.getResponse();
    }

    /** {@inheritDoc} */
    public Object getRequest() {
        return context.getRequest();
    }
}
