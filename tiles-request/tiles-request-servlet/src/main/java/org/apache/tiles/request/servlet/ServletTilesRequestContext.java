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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.AbstractRequest;
import org.apache.tiles.request.ApplicationContext;

/**
 * Servlet-based implementation of the TilesApplicationContext interface.
 *
 * @version $Rev$ $Date$
 */
public class ServletTilesRequestContext extends AbstractRequest {

    /**
     * The request object to use.
     */
    private HttpServletRequest request;

    /**
     * The response object to use.
     */
    private HttpServletResponse response;

    /**
     * The request objects, lazily initialized.
     */
    private Object[] requestObjects;

    /**
     * The response output stream, lazily initialized.
     */
    private OutputStream outputStream;

    /**
     * The response writer, lazily initialized.
     */
    private PrintWriter writer;

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
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-value.</p>
     */
    private Map<String, String> param = null;


    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-values.</p>
     */
    private Map<String, String[]> paramValues = null;

    /**
     * <p>The lazily instantiated <code>Map</code> of request scope
     * attributes.</p>
     */
    private Map<String, Object> requestScope = null;

    /**
     * <p>The lazily instantiated <code>Map</code> of session scope
     * attributes.</p>
     */
    private Map<String, Object> sessionScope = null;


    /**
     * Creates a new instance of ServletTilesRequestContext.
     *
     * @param applicationContext The application context.
     * @param request The request object.
     * @param response The response object.
     * @since 2.1.1
     */
    public ServletTilesRequestContext(
            ApplicationContext applicationContext,
            HttpServletRequest request, HttpServletResponse response) {
        super(applicationContext);
        initialize(request, response);
    }

    /** {@inheritDoc} */
    public Map<String, String> getHeader() {

        if ((header == null) && (request != null)) {
            header = new ServletHeaderMap(request, response);
        }
        return (header);

    }


    /** {@inheritDoc} */
    public Map<String, String[]> getHeaderValues() {

        if ((headerValues == null) && (request != null)) {
            headerValues = new ServletHeaderValuesMap(request);
        }
        return (headerValues);

    }


    /** {@inheritDoc} */
    public Map<String, String> getParam() {

        if ((param == null) && (request != null)) {
            param = new ServletParamMap(request);
        }
        return (param);

    }


    /** {@inheritDoc} */
    public Map<String, String[]> getParamValues() {

        if ((paramValues == null) && (request != null)) {
            paramValues = new ServletParamValuesMap(request);
        }
        return (paramValues);

    }


    /** {@inheritDoc} */
    public Map<String, Object> getRequestScope() {

        if ((requestScope == null) && (request != null)) {
            requestScope = new ServletRequestScopeMap(request);
        }
        return (requestScope);

    }


    /** {@inheritDoc} */
    public Map<String, Object> getSessionScope() {

        if ((sessionScope == null) && (request != null)) {
            sessionScope = new ServletSessionScopeMap(request);
        }
        return (sessionScope);

    }

    public Map<String, Object> getDefaultScope() {
        return getRequestScope();
    }

    /** {@inheritDoc} */
    public void dispatch(String path) throws IOException {
        if (response.isCommitted() || ServletUtil.isForceInclude(request)) {
            include(path);
        } else {
            forward(path);
        }
    }

    /**
     * Forwards to a path.
     *
     * @param path The path to forward to.
     * @throws IOException If something goes wrong during the operation.
     */
    protected void forward(String path) throws IOException {
        RequestDispatcher rd = request.getRequestDispatcher(path);

        if (rd == null) {
            throw new IOException("No request dispatcher returned for path '"
                    + path + "'");
        }

        try {
            rd.forward(request, response);
        } catch (ServletException ex) {
            throw ServletUtil.wrapServletException(ex, "ServletException including path '"
                    + path + "'.");
        }
    }


    /** {@inheritDoc} */
    public void include(String path) throws IOException {
        ServletUtil.setForceInclude(request, true);
        RequestDispatcher rd = request.getRequestDispatcher(path);

        if (rd == null) {
            throw new IOException("No request dispatcher returned for path '"
                    + path + "'");
        }

        try {
            rd.include(request, response);
        } catch (ServletException ex) {
            throw ServletUtil.wrapServletException(ex, "ServletException including path '"
                    + path + "'.");
        }
    }

    /** {@inheritDoc} */
    public OutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = response.getOutputStream();
        }
        return outputStream;
    }

    /** {@inheritDoc} */
    public Writer getWriter() throws IOException {
        return getPrintWriter();
    }

    /** {@inheritDoc} */
    public PrintWriter getPrintWriter() throws IOException {
        if (writer == null) {
            writer = response.getWriter();
        }
        return writer;
    }

    /** {@inheritDoc} */
    public boolean isResponseCommitted() {
        return response.isCommitted();
    }

    /** {@inheritDoc} */
    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    /** {@inheritDoc} */
    public Locale getRequestLocale() {
        return request.getLocale();
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
    public HttpServletRequest getRequest() {
        return request;
    }

    /** {@inheritDoc} */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * <p>Initialize (or reinitialize) this {@link ServletTilesRequestContext} instance
     * for the specified Servlet API objects.</p>
     *
     * @param request  The <code>HttpServletRequest</code> for this request
     * @param response The <code>HttpServletResponse</code> for this request
     */
    public void initialize(HttpServletRequest request,
                           HttpServletResponse response) {

        // Save the specified Servlet API object references
        this.request = request;
        this.response = response;
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
        header = null;
        headerValues = null;
        param = null;
        paramValues = null;
        requestScope = null;
        sessionScope = null;

        // Release references to Servlet API objects
        request = null;
        response = null;
    }


    /** {@inheritDoc} */
    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }
}
