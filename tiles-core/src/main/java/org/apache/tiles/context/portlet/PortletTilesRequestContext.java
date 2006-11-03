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

import org.apache.tiles.TilesRequestContext;

import javax.portlet.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Portlet-based TilesApplicationContext implementation.
 *
 * @version $Rev: 405486 $ $Date$
 */
public class PortletTilesRequestContext extends PortletTilesApplicationContext implements TilesRequestContext {

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
    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-value.</p>
     */
    protected Map param = null;
    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-values.</p>
     */
    protected Map paramValues = null;


    /**
     * Creates a new instance of PortletTilesRequestContext
     */
    public PortletTilesRequestContext(PortletContext context, PortletRequest request,
                                      PortletResponse response) {
        super(context);
        initialize(request, response);
    }

    /**
     * <p>Initialize (or reinitialize) this {@link PortletTilesRequestContext} instance
     * for the specified Portlet API objects.</p>
     *
     * @param request  The <code>PortletRequest</code> for this request
     * @param response The <code>PortletResponse</code> for this request
     */
    public void initialize(PortletRequest request,
                           PortletResponse response) {
        // Save the specified Portlet API object references
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
        header = null;
        headerValues = null;
        param = null;
        paramValues = null;
        requestScope = null;
        sessionScope = null;

        // Release references to Portlet API objects
        context = null;
        request = null;
        response = null;
        super.release();

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

    public Locale getRequestLocale() {
        if (request != null) {
            return request.getLocale();
        } else {
            return null;
        }
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }
}
