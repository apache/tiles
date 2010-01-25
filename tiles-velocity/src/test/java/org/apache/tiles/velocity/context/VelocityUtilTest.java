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
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.Node;
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

    /**
     * Test method for
     * {@link VelocityUtil#getBodyAsString(InternalContextAdapter, Node)}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetBodyAsString() throws IOException {
        InternalContextAdapter context = createMock(InternalContextAdapter.class);
        Node node = createMock(Node.class);
        ASTBlock block = new CustomBlock();

        expect(node.jjtGetChild(1)).andReturn(block);

        replay(context, node);
        assertEquals("myBody", VelocityUtil.getBodyAsString(context, node));
        verify(context, node);
    }

    /**
     * Test method for
     * {@link VelocityUtil#evaluateBody(InternalContextAdapter, Writer, Node)}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEvaluateBody() throws IOException {
        InternalContextAdapter context = createMock(InternalContextAdapter.class);
        Node node = createMock(Node.class);
        Writer writer = createMock(Writer.class);
        ASTBlock block = createMock(ASTBlock.class);

        expect(node.jjtGetChild(1)).andReturn(block);

        replay(context, node, writer);
        VelocityUtil.evaluateBody(context, writer, node);
        verify(context, node, writer);
    }

    /**
     * Test method for
     * {@link VelocityUtil#getBodyAsString(InternalContextAdapter, Node)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetParameters() {
        InternalContextAdapter context = createMock(InternalContextAdapter.class);
        Node node = createMock(Node.class);
        ASTMap block = createMock(ASTMap.class);
        Map<String, Object> params = createMock(Map.class);

        expect(node.jjtGetChild(0)).andReturn(block);
        expect(block.value(context)).andReturn(params);

        replay(context, node, block, params);
        assertEquals(params, VelocityUtil.getParameters(context, node));
        verify(context, node, block, params);
    }

    /**
     * Custom block to render a specific string.
     *
     * @version $Rev$ $Date$
     */
    private static class CustomBlock extends ASTBlock {

        /**
         * Constructor.
         */
        public CustomBlock() {
            super(1);
        }

        /** {@inheritDoc} */
        @Override
        public boolean render(InternalContextAdapter context, Writer writer)
                throws IOException {
            writer.write("myBody");
            return true;
        }
    }
}
