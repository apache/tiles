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

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.collection.AddableParameterMap;
import org.apache.tiles.request.collection.HeaderValuesMap;
import org.apache.tiles.request.collection.ReadOnlyEnumerationMap;
import org.apache.tiles.request.collection.ScopeMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ServletRequest}.
 *
 * @version $Rev$ $Date$
 */
public class ServletRequestTest {

    /**
     * The application context.
     */
    private ApplicationContext applicationContext;

    /**
     * The request.
     */
    private HttpServletRequest request;

    /**
     * The response.
     */
    private HttpServletResponse response;

    /**
     * The request to test.
     */
    private ServletRequest req;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        applicationContext = createMock(ApplicationContext.class);
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        req = new ServletRequest(applicationContext, request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#doForward(java.lang.String)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testDoForward() throws ServletException, IOException {
        RequestDispatcher rd = createMock(RequestDispatcher.class);

        expect(response.isCommitted()).andReturn(false);
        expect(request.getRequestDispatcher("/my/path")).andReturn(rd);
        rd.forward(request, response);

        replay(applicationContext, request, response, rd);
        req.doForward("/my/path");
        verify(applicationContext, request, response, rd);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#doForward(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testDoForwardNoDispatcher() throws IOException {
        expect(response.isCommitted()).andReturn(false);
        expect(request.getRequestDispatcher("/my/path")).andReturn(null);

        replay(applicationContext, request, response);
        try {
            req.doForward("/my/path");
        } finally {
            verify(applicationContext, request, response);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#doForward(java.lang.String)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testDoForwardServletException() throws ServletException, IOException {
        RequestDispatcher rd = createMock(RequestDispatcher.class);

        expect(response.isCommitted()).andReturn(false);
        expect(request.getRequestDispatcher("/my/path")).andReturn(rd);
        rd.forward(request, response);
        expectLastCall().andThrow(new ServletException());

        replay(applicationContext, request, response, rd);
        try {
            req.doForward("/my/path");
        } finally {
            verify(applicationContext, request, response, rd);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#doForward(java.lang.String)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testDoForwardInclude() throws ServletException, IOException {
        RequestDispatcher rd = createMock(RequestDispatcher.class);

        expect(response.isCommitted()).andReturn(true);
        expect(request.getRequestDispatcher("/my/path")).andReturn(rd);
        rd.include(request, response);

        replay(applicationContext, request, response, rd);
        req.doForward("/my/path");
        verify(applicationContext, request, response, rd);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#doInclude(java.lang.String)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testDoInclude() throws IOException, ServletException {
        RequestDispatcher rd = createMock(RequestDispatcher.class);

        expect(request.getRequestDispatcher("/my/path")).andReturn(rd);
        rd.include(request, response);

        replay(applicationContext, request, response, rd);
        req.doInclude("/my/path");
        verify(applicationContext, request, response, rd);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#doInclude(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testDoIncludeNoDispatcher() throws IOException {
        expect(request.getRequestDispatcher("/my/path")).andReturn(null);

        replay(applicationContext, request, response);
        try {
            req.doInclude("/my/path");
        } finally {
            verify(applicationContext, request, response);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#doInclude(java.lang.String)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testDoIncludeServletException() throws IOException, ServletException {
        RequestDispatcher rd = createMock(RequestDispatcher.class);

        expect(request.getRequestDispatcher("/my/path")).andReturn(rd);
        rd.include(request, response);
        expectLastCall().andThrow(new ServletException());

        replay(applicationContext, request, response, rd);
        try {
            req.doInclude("/my/path");
        } finally {
            verify(applicationContext, request, response, rd);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getHeader()}.
     */
    @Test
    public void testGetHeader() {
        assertTrue(req.getHeader() instanceof AddableParameterMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getHeaderValues()}.
     */
    @Test
    public void testGetHeaderValues() {
        assertTrue(req.getHeaderValues() instanceof HeaderValuesMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getParam()}.
     */
    @Test
    public void testGetParam() {
        assertTrue(req.getParam() instanceof ReadOnlyEnumerationMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getParamValues()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetParamValues() {
        Map<String, String[]> paramMap = createMock(Map.class);

        expect(request.getParameterMap()).andReturn(paramMap);

        replay(applicationContext, request, response, paramMap);
        assertEquals(paramMap, req.getParamValues());
        verify(applicationContext, request, response, paramMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getRequestScope()}.
     */
    @Test
    public void testGetRequestScope() {
        assertTrue(req.getRequestScope() instanceof ScopeMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getSessionScope()}.
     */
    @Test
    public void testGetSessionScope() {
        assertTrue(req.getSessionScope() instanceof ScopeMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getNativeScopes()}.
     */
    @Test
    public void testGetNativeScopes() {
        assertArrayEquals(new String[] {"request", "session", "application"}, req.getNativeScopes());
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getOutputStream()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetOutputStream() throws IOException {
        ServletOutputStream os = createMock(ServletOutputStream.class);

        expect(response.getOutputStream()).andReturn(os);

        replay(applicationContext, request, response, os);
        assertEquals(req.getOutputStream(), os);
        verify(applicationContext, request, response, os);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetWriter() throws IOException {
        PrintWriter os = createMock(PrintWriter.class);

        expect(response.getWriter()).andReturn(os);

        replay(applicationContext, request, response, os);
        assertEquals(req.getWriter(), os);
        verify(applicationContext, request, response, os);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getPrintWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetPrintWriter() throws IOException {
        PrintWriter os = createMock(PrintWriter.class);

        expect(response.getWriter()).andReturn(os);

        replay(applicationContext, request, response, os);
        assertEquals(req.getPrintWriter(), os);
        verify(applicationContext, request, response, os);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#isResponseCommitted()}.
     */
    @Test
    public void testIsResponseCommitted() {
        expect(response.isCommitted()).andReturn(true);

        replay(applicationContext, request, response);
        assertTrue(req.isResponseCommitted());
        verify(applicationContext, request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#setContentType(java.lang.String)}.
     */
    @Test
    public void testSetContentType() {
        response.setContentType("text/html");

        replay(applicationContext, request, response);
        req.setContentType("text/html");
        verify(applicationContext, request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getRequestLocale()}.
     */
    @Test
    public void testGetRequestLocale() {
        Locale locale = Locale.ITALY;

        expect(request.getLocale()).andReturn(locale);

        replay(applicationContext, request, response);
        assertEquals(locale, req.getRequestLocale());
        verify(applicationContext, request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getRequest()}.
     */
    @Test
    public void testGetRequest() {
        replay(applicationContext, request, response);
        assertEquals(request, req.getRequest());
        verify(applicationContext, request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#getResponse()}.
     */
    @Test
    public void testGetResponse() {
        replay(applicationContext, request, response);
        assertEquals(response, req.getResponse());
        verify(applicationContext, request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletRequest#isUserInRole(java.lang.String)}.
     */
    @Test
    public void testIsUserInRole() {
        expect(request.isUserInRole("myrole")).andReturn(true);

        replay(applicationContext, request, response);
        assertTrue(req.isUserInRole("myrole"));
        verify(applicationContext, request, response);
    }

}
