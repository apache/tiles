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

package org.apache.tiles.request.velocity;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.DispatchRequest;
import org.apache.tiles.request.servlet.ExternalWriterHttpServletResponse;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VelocityRequest}.
 */
public class VelocityRequestTest {

    /**
     * The request context to test.
     */
    private VelocityRequest context;

    /**
     * The Velocity context.
     */
    private Context velocityContext;

    /**
     * A string writer.
     */
    private StringWriter writer;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        velocityContext = createMock(Context.class);
        writer = new StringWriter();
    }

    /**
     * Tests {@link VelocityRequest
     * #createVelocityRequest(ApplicationContext, HttpServletRequest, HttpServletResponse, Context, Writer)}.
     */
    @Test
    public void testCreateVelocityRequest() {
        HttpServletRequest httpRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        replay(velocityContext, httpRequest, response, applicationContext);
        context = VelocityRequest.createVelocityRequest(applicationContext,
                httpRequest, response, velocityContext, writer);
        ServletRequest servletRequest = (ServletRequest) context.getWrappedRequest();
        assertEquals(httpRequest, servletRequest.getRequest());
        assertEquals(response, servletRequest.getResponse());
        verify(velocityContext, httpRequest, response, applicationContext);
    }

    /**
     * Tests {@link VelocityRequest#doInclude(String)}.
     *
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testDoInclude() throws IOException, ServletException {
        String path = "this way";
        ServletRequest enclosedRequest = createMock(ServletRequest.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        RequestDispatcher dispatcher = createMock(RequestDispatcher.class);

        expect(servletRequest.getRequestDispatcher("this way")).andReturn(dispatcher);
        dispatcher.include(eq(servletRequest), isA(ExternalWriterHttpServletResponse.class));
        replay(servletRequest, response, dispatcher);

        expect(enclosedRequest.getRequest()).andReturn(servletRequest);
        expect(enclosedRequest.getResponse()).andReturn(response);

        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, writer);
        context.doInclude(path);
        verify(velocityContext, enclosedRequest, servletRequest, response, dispatcher);
    }

    /**
     * Tests {@link VelocityRequest#doInclude(String)}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testDoIncludeNoRequestDispatcher() throws IOException {
        String path = "this way";
        ServletRequest enclosedRequest = createMock(ServletRequest.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);

        expect(servletRequest.getRequestDispatcher("this way")).andReturn(null);
        replay(servletRequest, response);

        expect(enclosedRequest.getRequest()).andReturn(servletRequest);
        expect(enclosedRequest.getResponse()).andReturn(response);

        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, writer);
        context.doInclude(path);
        verify(velocityContext, enclosedRequest, servletRequest, response);
    }

    /**
     * Tests {@link VelocityRequest#doInclude(String)}.
     *
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testDoIncludeServletException() throws IOException, ServletException {
        String path = "this way";
        ServletRequest enclosedRequest = createMock(ServletRequest.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        RequestDispatcher dispatcher = createMock(RequestDispatcher.class);

        expect(servletRequest.getRequestDispatcher("this way")).andReturn(dispatcher);
        dispatcher.include(eq(servletRequest), isA(ExternalWriterHttpServletResponse.class));
        expectLastCall().andThrow(new ServletException());
        replay(servletRequest, response, dispatcher);

        expect(enclosedRequest.getRequest()).andReturn(servletRequest);
        expect(enclosedRequest.getResponse()).andReturn(response);

        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, writer);
        context.doInclude(path);
        verify(velocityContext, enclosedRequest, servletRequest, response, dispatcher);
    }

    /**
     * Tests {@link VelocityRequest#getPrintWriter()}.
     */
    @Test
    public void testGetPrintWriter() {
        DispatchRequest enclosedRequest = createMock(DispatchRequest.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, writer);
        assertNotNull(context.getPrintWriter());
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityRequest#getPrintWriter()}.
     */
    @Test
    public void testGetPrintWriterPrintWriter() {
        DispatchRequest enclosedRequest = createMock(DispatchRequest.class);

        PrintWriter printWriter = new PrintWriter(writer);
        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, printWriter);
        assertEquals(printWriter, context.getPrintWriter());
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityRequest#getPrintWriter()}.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetPrintWriterNoWriter() {
        DispatchRequest enclosedRequest = createMock(DispatchRequest.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, null);
        context.getPrintWriter();
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityRequest#getWriter()}.
     */
    @Test
    public void testGetWriter() {
        DispatchRequest enclosedRequest = createMock(DispatchRequest.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, writer);
        assertEquals(writer, context.getWriter());
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityRequest#getWriter()}.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetWriterNoWriter() {
        DispatchRequest enclosedRequest = createMock(DispatchRequest.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, null);
        context.getWriter();
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityRequest#getPageScope()}.
     */
    @Test
    public void testGetPageScope() {
        DispatchRequest enclosedRequest = createMock(DispatchRequest.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityRequest(enclosedRequest, velocityContext, writer);
        assertTrue(context.getPageScope() instanceof VelocityScopeMap);
        verify(velocityContext, enclosedRequest);
    }
}
