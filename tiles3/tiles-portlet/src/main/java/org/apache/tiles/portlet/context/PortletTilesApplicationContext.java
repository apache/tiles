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
package org.apache.tiles.portlet.context;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletContext;

import org.apache.tiles.request.ApplicationContext;

/**
 * Portlet-based TilesApplicationContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class PortletTilesApplicationContext implements ApplicationContext {

    /**
     * <p>The lazily instantiated <code>Map</code> of application scope
     * attributes.</p>
     */
    private Map<String, Object> applicationScope = null;


    /**
     * <p>The <code>PortletContext</code> for this web application.</p>
     */
    protected PortletContext context = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of context initialization
     * parameters.</p>
     */
    private Map<String, String> initParam = null;


    /**
     * Creates a new instance of PortletTilesApplicationContext.
     *
     * @param context The portlet context to use.
     */
    public PortletTilesApplicationContext(PortletContext context) {
        initialize(context);
    }

    /** {@inheritDoc} */
    public Object getContext() {
        return context;
    }

    /**
     * <p>Initialize (or reinitialize) this {@link PortletTilesApplicationContext} instance
     * for the specified Portlet API objects.</p>
     *
     * @param context The <code>PortletContext</code> for this web application
     */
    public void initialize(PortletContext context) {

        // Save the specified Portlet API object references
        this.context = context;

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

        // Release references to Portlet API objects
        context = null;

    }

    /**
     * <p>Return the {@link PortletContext} for this context.</p>
     *
     * @return The original portlet context.
     */
    public PortletContext getPortletContext() {
        return (this.context);
    }


    /** {@inheritDoc} */
    public Map<String, Object> getApplicationScope() {
        if ((applicationScope == null) && (context != null)) {
            applicationScope = new PortletApplicationScopeMap(context);
        }
        return (applicationScope);

    }

    /** {@inheritDoc} */
    public Map<String, String> getInitParams() {
        if ((initParam == null) && (context != null)) {
            initParam = new PortletInitParamMap(context);
        }
        return (initParam);

    }


    /** {@inheritDoc} */
    public URL getResource(String path) throws IOException {
        return context.getResource(path);
    }

    /** {@inheritDoc} */
    public Set<URL> getResources(String path) throws IOException {
        HashSet<URL> set = new HashSet<URL>();
        set.add(getResource(path));
        return set;
    }
}
