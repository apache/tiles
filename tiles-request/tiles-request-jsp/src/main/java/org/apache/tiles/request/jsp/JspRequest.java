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
package org.apache.tiles.request.jsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.AbstractViewRequest;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.collection.ScopeMap;
import org.apache.tiles.request.jsp.extractor.ScopeExtractor;
import org.apache.tiles.request.jsp.extractor.SessionScopeExtractor;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;

/**
 * Context implementation used for executing tiles within a
 * jsp tag library.
 *
 * @version $Rev$ $Date$
 */
public class JspRequest extends AbstractViewRequest {

    private static final String[] SCOPES = {"page", "request", "session", "application"};

    /**
     * The current page context.
     */
    private PageContext pageContext;

    /**
     * The request objects, lazily initialized.
     */
    private Object[] requestObjects;

    /**
     * <p>The lazily instantiated <code>Map</code> of page scope
     * attributes.</p>
     */
    private Map<String, Object> pageScope = null;

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
     * <p>The lazily instantiated <code>Map</code> of application scope
     * attributes.</p>
     */
    private Map<String, Object> applicationScope = null;

    public static JspRequest createServletJspRequest(ApplicationContext applicationContext, PageContext pageContext) {
        return new JspRequest(new ServletRequest(
                applicationContext, (HttpServletRequest) pageContext
                        .getRequest(), (HttpServletResponse) pageContext
                        .getResponse()), pageContext);
    }

    /**
     * Constructor.
     *
     * @param enclosedRequest The request that is wrapped here.
     * @param pageContext The page context to use.
     */
    public JspRequest(Request enclosedRequest,
            PageContext pageContext) {
        super(enclosedRequest);
        this.pageContext = pageContext;
    }

    @Override
    public String[] getNativeScopes() {
        return SCOPES;
    }

    /** {@inheritDoc} */
    @Override
    protected void doInclude(String path) throws IOException {
        try {
            pageContext.include(path, false);
        } catch (ServletException e) {
            throw ServletUtil.wrapServletException(e, "JSPException including path '"
                    + path + "'.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public PrintWriter getPrintWriter() {
        return new JspPrintWriterAdapter(pageContext.getOut());
    }

    /** {@inheritDoc} */
    @Override
    public Writer getWriter() {
        return pageContext.getOut();
    }

    public Map<String, Object> getPageScope() {
        if ((pageScope == null) && (pageContext != null)) {
            pageScope = new ScopeMap(new ScopeExtractor(pageContext,
                    PageContext.PAGE_SCOPE));
        }
        return (pageScope);
    }

    public Map<String, Object> getRequestScope() {
        if ((requestScope == null) && (pageContext != null)) {
            requestScope = new ScopeMap(new ScopeExtractor(pageContext,
                    PageContext.REQUEST_SCOPE));
        }
        return (requestScope);
    }

    public Map<String, Object> getSessionScope() {
        if ((sessionScope == null) && (pageContext != null)) {
            sessionScope = new ScopeMap(new SessionScopeExtractor(pageContext));
        }
        return (sessionScope);
    }

    public Map<String, Object> getApplicationScope() {
        if ((applicationScope == null) && (pageContext != null)) {
            applicationScope = new ScopeMap(new ScopeExtractor(pageContext,
                    PageContext.APPLICATION_SCOPE));
        }
        return (applicationScope);
    }

    /** {@inheritDoc} */
    @Override
    public Object[] getRequestObjects() {
        if (requestObjects == null) {
            requestObjects = new Object[1];
            requestObjects[0] = pageContext;
        }
        return requestObjects;
    }

    /**
     * Returns the page context that originated the request.
     *
     * @return The page context.
     */
    public PageContext getPageContext() {
        return pageContext;
    }
}
