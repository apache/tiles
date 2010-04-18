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
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.collection.ParameterMap;
import org.apache.tiles.request.collection.ScopeMap;
import org.apache.tiles.request.servlet.extractor.ApplicationScopeExtractor;
import org.apache.tiles.request.servlet.extractor.InitParameterExtractor;

/**
 * Servlet-based implementation of the TilesApplicationContext interface.
 *
 * @version $Rev$ $Date$
 */
public class ServletApplicationContext implements ApplicationContext {

    /**
     * The servlet context to use.
     */
    private ServletContext servletContext;


    /**
     * <p>The lazily instantiated <code>Map</code> of application scope
     * attributes.</p>
     */
    private Map<String, Object> applicationScope = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of context initialization
     * parameters.</p>
     */
    private Map<String, String> initParam = null;


    /**
     * Creates a new instance of ServletTilesApplicationContext.
     *
     * @param servletContext The servlet context to use.
     */
    public ServletApplicationContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /** {@inheritDoc} */
    public Object getContext() {
        return servletContext;
    }

    /** {@inheritDoc} */
    public Map<String, Object> getApplicationScope() {

        if ((applicationScope == null) && (servletContext != null)) {
            applicationScope = new ScopeMap(new ApplicationScopeExtractor(servletContext));
        }
        return (applicationScope);

    }


    /** {@inheritDoc} */
    public Map<String, String> getInitParams() {

        if ((initParam == null) && (servletContext != null)) {
            initParam = new ParameterMap(new InitParameterExtractor(servletContext));
        }
        return (initParam);

    }

    /** {@inheritDoc} */
    public URL getResource(String path) throws IOException {
        return servletContext.getResource(path);
    }

    /** {@inheritDoc} */
    public Set<URL> getResources(String path) throws IOException {
        HashSet<URL> urls = new HashSet<URL>();
        urls.add(getResource(path));
        return urls;
    }
}
