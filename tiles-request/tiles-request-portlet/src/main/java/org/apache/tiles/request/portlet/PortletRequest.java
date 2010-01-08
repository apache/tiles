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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.tiles.request.AbstractClientRequest;
import org.apache.tiles.request.ApplicationContext;

/**
 * Portlet-based TilesApplicationContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class PortletRequest extends AbstractClientRequest {

    private static final String[] SCOPES = {"request", "portletSession", "session", "application"};

    /**
     * <p>The lazily instantiated <code>Map</code> of header name-value
     * combinations (immutable).</p>
     */
    private Map<String, String> header = null;


    /**
     * <p>The lazily instantitated <code>Map</code> of header name-values
     * combinations (immutable).</p>
     */
    private Map<String, String[]> headerValues = null;

    /**
     * The <code>PortletContext</code> for this application.
     */
    private PortletContext context = null;

    /**
     * <p>The <code>PortletRequest</code> for this request.</p>
     */
    protected javax.portlet.PortletRequest request = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of request scope
     * attributes.</p>
     */
    private Map<String, Object> requestScope = null;


    /**
     * <p>The <code>PortletResponse</code> for this request.</p>
     */
    protected PortletResponse response = null;


    /**
     * The request objects, lazily initialized.
     */
    private Object[] requestObjects;

    /**
     * <p>The lazily instantiated <code>Map</code> of session scope
     * attributes.</p>
     */
    private Map<String, Object> sessionScope = null;

    /**
     * <p>The lazily instantiated <code>Map</code> of portlet session scope
     * attributes.</p>
     */
    private Map<String, Object> portletSessionScope = null;


    /**
     * <p>Indicates whether the request is an ActionRequest or RenderRequest.
     */
    private boolean isRenderRequest;
    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-value.</p>
     */
    protected Map<String, String> param = null;
    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-values.</p>
     */
    protected Map<String, String[]> paramValues = null;


    /**
     * Creates a new instance of PortletTilesRequestContext.
     *
     * @param applicationContext The Tiles application context.
     * @param context The portlet context to use.
     * @param request The request object to use.
     * @param response The response object to use.
     * @since 2.1.1
     */
    public PortletRequest(ApplicationContext applicationContext,
            PortletContext context, javax.portlet.PortletRequest request,
            PortletResponse response) {
        super(applicationContext);

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
    }

    /**
     * <p>Return the {@link PortletRequest} for this context.</p>
     *
     * @return The used portlet request.
     */
    public javax.portlet.PortletRequest getRequest() {
        return (this.request);
    }

    /**
     * <p>Return the {@link PortletResponse} for this context.</p>
     *
     * @return The used portlet response.
     */
    public PortletResponse getResponse() {
        return (this.response);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Map<String, String> getHeader() {
        if ((header == null) && (request != null)) {
            header = Collections.EMPTY_MAP;
        }
        return (header);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Map<String, String[]> getHeaderValues() {
        if ((headerValues == null) && (request != null)) {
            headerValues = Collections.EMPTY_MAP;
        }
        return (headerValues);
    }

    /** {@inheritDoc} */
    public Map<String, String> getParam() {
        if ((param == null) && (request != null)) {
            param = new PortletParamMap(request);
        }
        return (param);
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getParamValues() {
        if ((paramValues == null) && (request != null)) {
            paramValues = new PortletParamValuesMap(request);
        }
        return (paramValues);
    }

    /** {@inheritDoc} */
    public Map<String, Object> getRequestScope() {
        if ((requestScope == null) && (request != null)) {
            requestScope = new PortletRequestScopeMap(request);
        }
        return (requestScope);
    }

    /** {@inheritDoc} */
    public Map<String, Object> getSessionScope() {
        if ((sessionScope == null) && (request != null)) {
            sessionScope = new PortletSessionScopeMap(request, PortletSession.APPLICATION_SCOPE);
        }
        return (sessionScope);
    }

    /** {@inheritDoc} */
    public Map<String, Object> getPortletSessionScope() {
        if ((portletSessionScope == null) && (request != null)) {
            portletSessionScope = new PortletSessionScopeMap(request, PortletSession.PORTLET_SCOPE);
        }
        return (portletSessionScope);
    }

    @Override
    public String[] getNativeScopes() {
        return SCOPES;
    }

    /** {@inheritDoc} */
    public void doForward(String path) throws IOException {
        if (((RenderResponse)response).isCommitted()) {
            if (isRenderRequest) {
                include(path);
            } else {
                throw new IOException("Cannot forward or include the path '"
                        + path + "' because the response has been committed "
                        + "and the request is not a RenderRequest");
            }
        } else {
            forward(path);
        }
    }

    /** {@inheritDoc} */
    public void doInclude(String path) throws IOException {
        if (!isRenderRequest) {
            throw new IOException("Cannot include the path '" + path
                    + "' because the request is not a RenderRequest");
        }
        try {
            PortletRequestDispatcher rd = context.getRequestDispatcher(path);

            if (rd == null) {
                throw new IOException(
                        "No portlet request dispatcher returned for path '"
                                + path + "'");
            }

            rd.include((RenderRequest) request,
                (RenderResponse) response);
        } catch (PortletException e) {
            throw new IOException("PortletException while including path '"
                    + path + "'.", e);
        }
    }

    /** {@inheritDoc} */
    public OutputStream getOutputStream() throws IOException {
        return ((RenderResponse) response).getPortletOutputStream();
    }

    /** {@inheritDoc} */
    public PrintWriter getPrintWriter() throws IOException {
        return ((RenderResponse) response).getWriter();
    }

    /** {@inheritDoc} */
    public Writer getWriter() throws IOException {
        return ((RenderResponse) response).getWriter();
    }

    /** {@inheritDoc} */
    public boolean isResponseCommitted() {
        return ((RenderResponse) response).isCommitted();
    }

    /** {@inheritDoc} */
    public void setContentType(String contentType) {
        ((RenderResponse) response).setContentType(contentType);
    }

    /** {@inheritDoc} */
    public Object[] getRequestObjects() {
        if (requestObjects == null) {
            requestObjects = new Object[2];
            requestObjects[0] = request;
            requestObjects[1] = response;
        }
        return requestObjects;
    }

    /** {@inheritDoc} */
    public Locale getRequestLocale() {
        if (request != null) {
            return request.getLocale();
        }
        return null;
    }

    /** {@inheritDoc} */
    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    /** {@inheritDoc} */
    private void forward(String path) throws IOException {
        try {
            PortletRequestDispatcher rd = context.getRequestDispatcher(path);

            if (rd == null) {
                throw new IOException(
                        "No portlet request dispatcher returned for path '"
                                + path + "'");
            }

            rd.forward(request, response);
        } catch (PortletException e) {
            throw new IOException("PortletException while including path '"
                    + path + "'.", e);
        }
    }
}
