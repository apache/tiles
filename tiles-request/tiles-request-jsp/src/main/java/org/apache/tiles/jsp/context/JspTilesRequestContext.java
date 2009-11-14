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
package org.apache.tiles.jsp.context;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletTilesRequestContext;
import org.apache.tiles.request.servlet.ServletUtil;
import org.apache.tiles.request.util.TilesRequestContextWrapper;

/**
 * Context implementation used for executing tiles within a
 * jsp tag library.
 *
 * @version $Rev$ $Date$
 */
public class JspTilesRequestContext extends TilesRequestContextWrapper
    implements Request {

    /**
     * The current page context.
     */
    private PageContext pageContext;

    /**
     * The request objects, lazily initialized.
     */
    private Object[] requestObjects;

    public static JspTilesRequestContext createServletJspRequest(ApplicationContext applicationContext, PageContext pageContext) {
		return new JspTilesRequestContext(new ServletTilesRequestContext(
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
    public JspTilesRequestContext(Request enclosedRequest,
            PageContext pageContext) {
        super(enclosedRequest);
        this.pageContext = pageContext;
    }

    /**
     * Dispatches a path. In fact it "includes" it!
     *
     * @param path The path to dispatch to.
     * @throws IOException If something goes wrong during dispatching.
     * @see org.apache.tiles.request.servlet.ServletTilesRequestContext#dispatch(java.lang.String)
     */
    @Override
	public void dispatch(String path) throws IOException {
        include(path);
    }

    /** {@inheritDoc} */
    @Override
	public void include(String path) throws IOException {
        Boolean retValue = Boolean.valueOf(true);
		pageContext
				.setAttribute(
						org.apache.tiles.request.servlet.ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
						retValue, PageContext.REQUEST_SCOPE);
        try {
            pageContext.include(path, false);
        } catch (ServletException e) {
            throw ServletUtil.wrapServletException(e, "JSPException including path '"
                    + path + "'.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public PrintWriter getPrintWriter() throws IOException {
        return new JspPrintWriterAdapter(pageContext.getOut());
    }

    /** {@inheritDoc} */
    @Override
    public Writer getWriter() throws IOException {
        return pageContext.getOut();
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
