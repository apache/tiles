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

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Servlet-based implementation of the TilesApplicationContext interface.
 *
 * @version $Rev$ $Date$
 */
public class ServletTilesApplicationContext implements TilesApplicationContext {

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
    public ServletTilesApplicationContext(ServletContext servletContext) {
        initialize(servletContext);
    }


    /** {@inheritDoc} */
    public Map<String, Object> getApplicationScope() {

        if ((applicationScope == null) && (servletContext != null)) {
            applicationScope = new ServletApplicationScopeMap(servletContext);
        }
        return (applicationScope);

    }


    /** {@inheritDoc} */
    public Map<String, String> getInitParams() {

        if ((initParam == null) && (servletContext != null)) {
            initParam = new ServletInitParamMap(servletContext);
        }
        return (initParam);

    }

    /** {@inheritDoc} */
    public URL getResource(String path) throws MalformedURLException {
        return servletContext.getResource(path);
    }

    /** {@inheritDoc} */
    public Set<URL> getResources(String path) throws MalformedURLException {
        HashSet<URL> urls = new HashSet<URL>();
        urls.add(getResource(path));
        return urls;
    }

    /**
     * Returns the servlet context.
     *
     * @return The servlet context.
     */
    public ServletContext getServletContext() {
        return servletContext;
    }


    /**
     * <p>Initialize (or reinitialize) this {@link TilesApplicationContext} instance
     * for the specified Servlet API objects.</p>
     *
     * @param context The <code>ServletContext</code> for this web application
     */
    public void initialize(ServletContext context) {
        // Save the specified Servlet API object references
        this.servletContext = context;

        // Perform other setup as needed
    }


    /**
     * <p>Release references to allocated resources acquired in
     * <code>initialize()</code> of via subsequent processing.  After this
     * method is called, subsequent calls to any other method than
     * <code>initialize()</code> will return undefined results.</p>
     */
    public void release() {

        // Release references to allocated collections
        applicationScope = null;
        initParam = null;

        // Release references to Servlet API objects
        servletContext = null;

    }

    /**
     * Creates a servlet context for a given request/response pair.
     *
     * @param request The request object.
     * @param response The response object.
     * @return The corresponding Tiles request context.
     */
    public TilesRequestContext createRequestContext(Object request, Object response) {
        if (request instanceof HttpServletRequest) {
            return new ServletTilesRequestContext(
                servletContext,
                (HttpServletRequest) request,
                (HttpServletResponse) response
            );
        } else {
            throw new IllegalArgumentException("Invalid context specified. "
                + servletContext.getClass().getName());
        }
    }
}
