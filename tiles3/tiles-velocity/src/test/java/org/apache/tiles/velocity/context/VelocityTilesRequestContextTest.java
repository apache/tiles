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

package org.apache.tiles.velocity.context;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletUtil;
import org.apache.tiles.request.servlet.ExternalWriterHttpServletResponse;
import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VelocityTilesRequestContext}.
 */
public class VelocityTilesRequestContextTest {

    /**
     * The request context to test.
     */
    private VelocityTilesRequestContext context;

    /**
     * The Velocity context.
     */
    private Context velocityContext;

    /**
     * A string writer.
     */
    private StringWriter writer;

    /**
     * @throws java.lang.Exception If something goes wrong.
     */
    @Before
    public void setUp() throws Exception {
        velocityContext = createMock(Context.class);
        writer = new StringWriter();
    }

    /**
     * Tests {@link VelocityTilesRequestContext#dispatch(String)}.
     *
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testDispatch() throws IOException, ServletException {
        String path = "this way";
        Request enclosedRequest = createMock(Request.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        RequestDispatcher dispatcher = createMock(RequestDispatcher.class);

        servletRequest.setAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME, true);
        expect(servletRequest.getRequestDispatcher("this way")).andReturn(dispatcher);
        dispatcher.include(eq(servletRequest), isA(ExternalWriterHttpServletResponse.class));
        replay(servletRequest, response, dispatcher);
        Object[] requestItems = new Object[] {servletRequest, response};

        expect(enclosedRequest.getRequestObjects()).andReturn(requestItems);

        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        context.dispatch(path);
        verify(velocityContext, enclosedRequest, servletRequest, response, dispatcher);
    }

    /**
     * Tests {@link VelocityTilesRequestContext#include(String)}.
     *
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testInclude() throws IOException, ServletException {
        String path = "this way";
        Request enclosedRequest = createMock(Request.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        RequestDispatcher dispatcher = createMock(RequestDispatcher.class);

        servletRequest.setAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME, true);
        expect(servletRequest.getRequestDispatcher("this way")).andReturn(dispatcher);
        dispatcher.include(eq(servletRequest), isA(ExternalWriterHttpServletResponse.class));
        replay(servletRequest, response, dispatcher);
        Object[] requestItems = new Object[] {servletRequest, response};

        expect(enclosedRequest.getRequestObjects()).andReturn(requestItems);

        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        context.include(path);
        verify(velocityContext, enclosedRequest, servletRequest, response, dispatcher);
    }

    /**
     * Tests {@link VelocityTilesRequestContext#getPrintWriter()}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetPrintWriter() throws IOException {
        Request enclosedRequest = createMock(Request.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        assertNotNull(context.getPrintWriter());
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityTilesRequestContext#getWriter()}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetWriter() throws IOException {
        Request enclosedRequest = createMock(Request.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        assertEquals(writer, context.getWriter());
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityTilesRequestContext#getRequestObjects()}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testGetRequestObjects() throws ServletException, IOException {
        Request enclosedRequest = createMock(Request.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);

        replay(servletRequest, response);
        Object[] requestItems = new Object[] {servletRequest, response};

        expect(enclosedRequest.getRequestObjects()).andReturn(requestItems);

        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        assertArrayEquals(new Object[] { velocityContext, servletRequest,
                response, writer }, context.getRequestObjects());
        verify(velocityContext, enclosedRequest, servletRequest, response);
    }
}
