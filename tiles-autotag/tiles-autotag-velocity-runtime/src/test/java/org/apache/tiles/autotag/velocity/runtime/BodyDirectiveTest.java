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

package org.apache.tiles.autotag.velocity.runtime;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.apache.tiles.request.velocity.VelocityRequest;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewToolContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link BodyDirective}.
 *
 * @version $Rev$ $Date$
 */
public class BodyDirectiveTest {

    /**
     * The directive to test.
     */
    private BodyDirective directive;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() {
        directive = createMockBuilder(BodyDirective.class).createMock();
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.BlockDirective#getType()}.
     */
    @Test
    public void testGetType() {
        replay(directive);
        assertEquals(DirectiveConstants.BLOCK, directive.getType());
        verify(directive);
    }

    /**
     * Test method for {@link BlockDirective#render(InternalContextAdapter, Writer, Node)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRenderInternalContextAdapterWriterNode() throws IOException {
        InternalContextAdapter context = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        Node node = createMock(Node.class);
        ViewToolContext viewContext = createMock(ViewToolContext.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ASTMap astMap = createMock(ASTMap.class);
        ASTBlock block = createMock(ASTBlock.class);
        Map<String, Object> params = createMock(Map.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(context.getInternalUserContext()).andReturn(viewContext);
        expect(viewContext.getRequest()).andReturn(request);
        expect(viewContext.getResponse()).andReturn(response);
        expect(viewContext.getServletContext()).andReturn(servletContext);
        expect(node.jjtGetChild(0)).andReturn(astMap);
        expect(astMap.value(context)).andReturn(params);
        expect(node.jjtGetChild(1)).andReturn(block);
        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);

        directive.execute(eq(params), isA(VelocityRequest.class), isA(VelocityModelBody.class));

        replay(directive, context, writer, node, viewContext,
                applicationContext, servletContext, request, response, astMap,
                params, block);
        directive.render(context, writer, node);
        verify(directive, context, writer, node, viewContext,
                applicationContext, servletContext, request, response, astMap,
                params, block);
    }
}
