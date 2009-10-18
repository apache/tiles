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

import static org.apache.tiles.velocity.context.VelocityUtil.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tiles.ArrayStack;
import org.apache.velocity.context.Context;
import org.junit.Test;

/**
 * Tests {@link VelocityUtil}.
 */
public class VelocityUtilTest {

    /**
     * A dummy value.
     */
    private static final Integer DUMMY_VALUE = new Integer(10);

    /**
     * The parameter stack key.
     */
    private static final String PARAMETER_MAP_STACK_KEY = "org.apache.tiles.velocity.PARAMETER_MAP_STACK";

    /**
     * Test method for {@link org.apache.tiles.velocity.context.VelocityUtil
     * #toSimpleBoolean(java.lang.Boolean, boolean)}.
     */
    @Test
    public void testToSimpleBoolean() {
        assertEquals(true, toSimpleBoolean(Boolean.TRUE, true));
        assertEquals(false, toSimpleBoolean(Boolean.FALSE, true));
        assertEquals(true, toSimpleBoolean(Boolean.TRUE, false));
        assertEquals(false, toSimpleBoolean(Boolean.FALSE, false));
        assertEquals(true, toSimpleBoolean(null, true));
        assertEquals(false, toSimpleBoolean(null, false));
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.context.VelocityUtil
     * #getParameterStack(org.apache.velocity.context.Context)}.
     */
    @Test
    public void testGetParameterStack() {
        Context velocityContext = createMock(Context.class);

        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(null);
        expect(velocityContext.put(eq(PARAMETER_MAP_STACK_KEY),
                isA(ArrayStack.class))).andReturn(null);
        replay(velocityContext);
        ArrayStack<Map<String, Object>> paramStack = getParameterStack(velocityContext);
        assertNotNull(paramStack);
        assertEquals(0, paramStack.size());
        verify(velocityContext);

        reset(velocityContext);

        paramStack = new ArrayStack<Map<String, Object>>();
        paramStack.push(new HashMap<String, Object>());
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(paramStack);

        replay(velocityContext);
        assertEquals(paramStack, getParameterStack(velocityContext));
        verify(velocityContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.context.VelocityUtil
     * #setAttribute(org.apache.velocity.context.Context, javax.servlet.http.HttpServletRequest,
     * javax.servlet.ServletContext, java.lang.String, java.lang.Object, java.lang.String)}.
     */
    @Test
    public void testSetAttributePage() {
        Context velocityContext = createMock(Context.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ServletContext servletContext = createMock(ServletContext.class);
        Object value = DUMMY_VALUE;
        expect(velocityContext.put("myName", value)).andReturn(value);

        replay(velocityContext, request, servletContext);
        setAttribute(velocityContext, request, servletContext, "myName", value, null);
        verify(velocityContext, request, servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.context.VelocityUtil
     * #setAttribute(org.apache.velocity.context.Context, javax.servlet.http.HttpServletRequest,
     * javax.servlet.ServletContext, java.lang.String, java.lang.Object, java.lang.String)}.
     */
    @Test
    public void testSetAttributeRequest() {
        Context velocityContext = createMock(Context.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ServletContext servletContext = createMock(ServletContext.class);
        Object value = DUMMY_VALUE;
        request.setAttribute("myName", value);

        replay(velocityContext, request, servletContext);
        setAttribute(velocityContext, request, servletContext, "myName", value, "request");
        verify(velocityContext, request, servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.context.VelocityUtil
     * #setAttribute(org.apache.velocity.context.Context, javax.servlet.http.HttpServletRequest,
     * javax.servlet.ServletContext, java.lang.String, java.lang.Object, java.lang.String)}.
     */
    @Test
    public void testSetAttributeSession() {
        Context velocityContext = createMock(Context.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpSession session = createMock(HttpSession.class);
        ServletContext servletContext = createMock(ServletContext.class);
        Object value = DUMMY_VALUE;
        expect(request.getSession()).andReturn(session);
        session.setAttribute("myName", value);

        replay(velocityContext, request, servletContext, session);
        setAttribute(velocityContext, request, servletContext, "myName", value, "session");
        verify(velocityContext, request, servletContext, session);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.context.VelocityUtil
     * #setAttribute(org.apache.velocity.context.Context, javax.servlet.http.HttpServletRequest,
     * javax.servlet.ServletContext, java.lang.String, java.lang.Object, java.lang.String)}.
     */
    @Test
    public void testSetAttributeApplication() {
        Context velocityContext = createMock(Context.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ServletContext servletContext = createMock(ServletContext.class);
        Object value = DUMMY_VALUE;
        servletContext.setAttribute("myName", value);

        replay(velocityContext, request, servletContext);
        setAttribute(velocityContext, request, servletContext, "myName", value, "application");
        verify(velocityContext, request, servletContext);
    }
}
