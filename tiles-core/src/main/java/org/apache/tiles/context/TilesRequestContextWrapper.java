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
 *
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

    private TilesRequestContext context;


    public TilesRequestContextWrapper(TilesRequestContext context) {
        this.context = context;
    }

    public Map getHeader() {
        return context.getHeader();
    }

    public Map getHeaderValues() {
        return context.getHeaderValues();
    }

    public Map getRequestScope() {
        return context.getRequestScope();
    }

    public Map getSessionScope() {
        return context.getSessionScope();
    }

    public void dispatch(String path) throws IOException {
        context.dispatch(path);
    }

    public void include(String path) throws IOException {
        context.include(path);
    }

    public Map getParam() {
        return context.getParam();
    }

    public Map getParamValues() {
        return context.getParamValues();
    }

    public Locale getRequestLocale() {
        return context.getRequestLocale();
    }

    public boolean isUserInRole(String role) {
        return context.isUserInRole(role);
    }


    public Object getResponse() {
        return context.getResponse();
    }

    public Object getRequest() {
        return context.getRequest();
    }
}
