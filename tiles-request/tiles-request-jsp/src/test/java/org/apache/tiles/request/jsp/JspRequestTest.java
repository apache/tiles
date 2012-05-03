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

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.DispatchRequest;
import org.apache.tiles.request.collection.ScopeMap;
import org.apache.tiles.request.servlet.ServletRequest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link JspRequest}.
 *
 * @version $Rev$ $Date$
 */
public class JspRequestTest {

    /**
     * The enclosed request.
     */
    private DispatchRequest enclosedRequest;

    /**
     * The page context.
     */
    private PageContext context;

    /**
     * The request to test.
     */
    private JspRequest request;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        enclosedRequest = createMock(DispatchRequest.class);
        context = createMock(PageContext.class);
        request = new JspRequest(enclosedRequest, context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#getWriter()}.
     */
    @Test
    public void testGetWriter() {
        JspWriter writer = createMock(JspWriter.class);

        expect(context.getOut()).andReturn(writer);

        replay(context, enclosedRequest, writer);
        assertEquals(writer, request.getWriter());
        verify(context, enclosedRequest, writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#getPrintWriter()}.
     */
    @Test
    public void testGetPrintWriter() {
        JspWriter writer = createMock(JspWriter.class);

        expect(context.getOut()).andReturn(writer);

        replay(context, enclosedRequest, writer);
        assertEquals(writer, ((JspPrintWriterAdapter) request.getPrintWriter())
                .getJspWriter());
        verify(context, enclosedRequest, writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#doInclude(java.lang.String)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testDoInclude() throws ServletException, IOException {
        context.include("/my/path", false);

        replay(context, enclosedRequest);
        request.doInclude("/my/path");
        verify(context, enclosedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#doInclude(java.lang.String)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testDoIncludeException() throws ServletException, IOException {
        context.include("/my/path", false);
        expectLastCall().andThrow(new ServletException());

        replay(context, enclosedRequest);
        request.doInclude("/my/path");
        verify(context, enclosedRequest);
    }

    /**
     * Test method for {@link JspRequest#createServletJspRequest(ApplicationContext, PageContext)}.
     */
    @Test
    public void testCreateServletJspRequest() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse servletResponse = createMock(HttpServletResponse.class);

        expect(context.getRequest()).andReturn(servletRequest);
        expect(context.getResponse()).andReturn(servletResponse);

        replay(context, applicationContext, servletRequest, servletResponse);
        JspRequest request = JspRequest.createServletJspRequest(applicationContext, context);
        ServletRequest wrappedRequest = (ServletRequest) request.getWrappedRequest();
        assertEquals(servletRequest, wrappedRequest.getRequest());
        assertEquals(servletResponse, wrappedRequest.getResponse());
        verify(context, applicationContext, servletRequest, servletResponse);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#getPageScope()}.
     */
    @Test
    public void testGetPageScope() {
        replay(context, enclosedRequest);
        assertTrue(request.getPageScope() instanceof ScopeMap);
        verify(context, enclosedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#getRequestScope()}.
     */
    @Test
    public void testGetRequestScope() {
        replay(context, enclosedRequest);
        assertTrue(request.getRequestScope() instanceof ScopeMap);
        verify(context, enclosedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#getSessionScope()}.
     */
    @Test
    public void testGetSessionScope() {
        replay(context, enclosedRequest);
        assertTrue(request.getSessionScope() instanceof ScopeMap);
        verify(context, enclosedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#getApplicationScope()}.
     */
    @Test
    public void testGetApplicationScope() {
        replay(context, enclosedRequest);
        assertTrue(request.getApplicationScope() instanceof ScopeMap);
        verify(context, enclosedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspRequest#getPageContext()}.
     */
    @Test
    public void testGetPageContext() {
        replay(context, enclosedRequest);
        assertEquals(context, request.getPageContext());
        verify(context, enclosedRequest);
    }
}
