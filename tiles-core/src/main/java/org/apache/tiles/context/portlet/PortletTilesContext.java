/*
 * $Id$
 *
 * Copyright 1999-2006 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.context.portlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.tiles.TilesContext;

/**
 * Portlet-based TilesContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class PortletTilesContext implements TilesContext {
    
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
     * <p>The lazily instantiated <code>Map</code> of header name-value
     * combinations (immutable).</p>
     */
    private Map header = null;


    /**
     * <p>The lazily instantitated <code>Map</code> of header name-values
     * combinations (immutable).</p>
     */
    private Map headerValues = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of context initialization
     * parameters.</p>
     */
    private Map initParam = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-value.</p>
     */
    private Map param = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-values.</p>
     */
    private Map paramValues = null;


    /**
     * <p>The <code>PortletRequest</code> for this request.</p>
     */
    protected PortletRequest request = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of request scope
     * attributes.</p>
     */
    private Map requestScope = null;


    /**
     * <p>The <code>PortletResponse</code> for this request.</p>
     */
    protected PortletResponse response = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of session scope
     * attributes.</p>
     */
    private Map sessionScope = null;

    
    /**
     * <p>Indicates whether the request is an ActionRequest or RenderRequest.
     */
    private boolean isRenderRequest;

       
    /** Creates a new instance of PortletTilesContext */
    public PortletTilesContext(PortletContext context) {
        initialize(context, null, null);
    }

    public PortletTilesContext(PortletContext context, PortletRequest request) {
        initialize(context, request, null);
    }

    public PortletTilesContext(PortletContext context, PortletRequest request, 
            PortletResponse response) {
        initialize(context, request, response);
    }

    /**
     * <p>Initialize (or reinitialize) this {@link PortletWebContext} instance
     * for the specified Portlet API objects.</p>
     *
     * @param context The <code>PortletContext</code> for this web application
     * @param request The <code>PortletRequest</code> for this request
     * @param response The <code>PortletResponse</code> for this request
     */
    public void initialize(PortletContext context,
                           PortletRequest request,
                           PortletResponse response) {

        // Save the specified Portlet API object references
        this.context = context;
        this.request = request;
        this.response = response;

        // Perform other setup as needed
        this.isRenderRequest = false;
        if (request != null) {
            if (request instanceof RenderRequest) {
                isRenderRequest = true;
            }
        }

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
        header = null;
        headerValues = null;
        initParam = null;
        param = null;
        paramValues = null;
        requestScope = null;
        sessionScope = null;

        // Release references to Portlet API objects
        context = null;
        request = null;
        response = null;

    }
    
    /**
     * <p>Return the {@link PortletContext} for this context.</p>
     */
    public PortletContext getContext() {
        return (this.context);
    }

    /**
     * <p>Return the {@link PortletRequest} for this context.</p>
     */
    public PortletRequest getRequest() {
        return (this.request);
    }

    /**
     * <p>Return the {@link PortletResponse} for this context.</p>
     */
    public PortletResponse getResponse() {
        return (this.response);
    }
    
    public Map getApplicationScope() {
        if ((applicationScope == null) && (context != null)) {
            applicationScope = new PortletApplicationScopeMap(context);
        }
        return (applicationScope);

    }

    public Map getHeader() {
        if ((header == null) && (request != null)) {
            header = Collections.EMPTY_MAP;
        }
        return (header);
    }

    public Map getHeaderValues() {
        if ((headerValues == null) && (request != null)) {
            headerValues = Collections.EMPTY_MAP;
        }
        return (headerValues);
    }

    public Map getInitParams() {
        if ((initParam == null) && (context != null)) {
            initParam = new PortletInitParamMap(context);
        }
        return (initParam);

    }

    public Map getParam() {
        if ((param == null) && (request != null)) {
            param = new PortletParamMap(request);
        }
        return (param);
    }

    public Map getParamValues() {
        if ((paramValues == null) && (request != null)) {
            paramValues = new PortletParamValuesMap(request);
        }
        return (paramValues);
    }

    public Map getRequestScope() {
        if ((requestScope == null) && (request != null)) {
            requestScope = new PortletRequestScopeMap(request);
        }
        return (requestScope);
    }

    public Map getSessionScope() {
        if ((sessionScope == null) && (request != null)) {
            sessionScope =
                    new PortletSessionScopeMap(request.getPortletSession());
        }
        return (sessionScope);
    }

    public void dispatch(String path) throws IOException, Exception {
        include(path);
    }

    public void include(String path) throws IOException, Exception {
        if (isRenderRequest) {
            context.getRequestDispatcher(path).include((RenderRequest) request, 
                    (RenderResponse) response);
        }
    }

    public URL getResource(String path) throws MalformedURLException {
        return context.getResource(path);
    }

    public Locale getRequestLocale() {
        if (request != null) {
            return request.getLocale();
        } else {
            return null;
        }
    }
    
}
