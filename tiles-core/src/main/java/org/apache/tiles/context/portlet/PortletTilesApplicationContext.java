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
package org.apache.tiles.context.portlet;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Portlet-based TilesApplicationContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class PortletTilesApplicationContext implements TilesApplicationContext {

    /**
     * <p>The lazily instantiated <code>Map</code> of application scope
     * attributes.</p>
     */
    private Map applicationScope = null;


    /**
     * <p>The <code>PortletContext</code> for this web application.</p>
     */
    protected PortletContext context = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of context initialization
     * parameters.</p>
     */
    private Map initParam = null;


    /**
     * Creates a new instance of PortletTilesApplicationContext
     */
    public PortletTilesApplicationContext(PortletContext context) {
        initialize(context);
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
     */
    public PortletContext getPortletContext() {
        return (this.context);
    }


    public Map getApplicationScope() {
        if ((applicationScope == null) && (context != null)) {
            applicationScope = new PortletApplicationScopeMap(context);
        }
        return (applicationScope);

    }

    public Map getInitParams() {
        if ((initParam == null) && (context != null)) {
            initParam = new PortletInitParamMap(context);
        }
        return (initParam);

    }


    public URL getResource(String path) throws MalformedURLException {
        return context.getResource(path);
    }

    public Set<URL> getResources(String path) throws MalformedURLException {
        HashSet<URL> set = new HashSet<URL>();
        set.add(getResource(path));
        return set;
    }

    public TilesRequestContext createRequestContext(Object request, Object response) {
        if (request instanceof PortletRequest && response instanceof PortletResponse) {
            return new PortletTilesRequestContext(
                context,
                (PortletRequest) request,
                (PortletResponse) response);
        } else {
            throw new IllegalArgumentException("Invalid context specified. "
                + context.getClass().getName());
        }
    }


}
