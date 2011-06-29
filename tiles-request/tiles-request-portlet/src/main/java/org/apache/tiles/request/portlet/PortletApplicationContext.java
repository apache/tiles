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
package org.apache.tiles.request.portlet;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.collection.ReadOnlyEnumerationMap;
import org.apache.tiles.request.collection.ScopeMap;
import org.apache.tiles.request.portlet.extractor.ApplicationScopeExtractor;
import org.apache.tiles.request.portlet.extractor.InitParameterExtractor;

/**
 * Portlet-based TilesApplicationContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class PortletApplicationContext implements ApplicationContext {

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
    public PortletApplicationContext(PortletContext context) {
        initialize(context);
    }

    /** {@inheritDoc} */
    public Object getContext() {
        return context;
    }

    /**
     * <p>Initialize (or reinitialize) this {@link PortletApplicationContext} instance
     * for the specified Portlet API objects.</p>
     *
     * @param context The <code>PortletContext</code> for this web application
     */
    public void initialize(PortletContext context) {

        // Save the specified Portlet API object references
        this.context = context;

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
            applicationScope = new ScopeMap(new ApplicationScopeExtractor(
                    context));
        }
        return (applicationScope);

    }

    /** {@inheritDoc} */
    public Map<String, String> getInitParams() {
        if ((initParam == null) && (context != null)) {
            initParam = new ReadOnlyEnumerationMap<String>(
                    new InitParameterExtractor(context));
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
