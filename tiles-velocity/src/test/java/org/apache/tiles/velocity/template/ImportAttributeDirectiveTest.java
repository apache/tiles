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

package org.apache.tiles.velocity.template;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewToolContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ImportAttributeDirective}.
 */
public class ImportAttributeDirectiveTest {

    /**
     * The model to test.
     */
    private ImportAttributeDirective model;

    /**
     * The template model.
     */
    private ImportAttributeModel tModel;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(ImportAttributeModel.class);
    }

    /**
     * Test method for {@link ImportAttributeDirective#render(InternalContextAdapter, Writer, Node)}.
     */
    @Test
    public void testExecutePage() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ServletContext servletContext = createMock(ServletContext.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        ViewToolContext viewContext = createMock(ViewToolContext.class);
        ASTMap astMap = createMock(ASTMap.class);
        Node node = createMock(Node.class);
        TilesContainer container = createMock(TilesContainer.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");

        expect(velocityContext.getInternalUserContext()).andReturn(viewContext);
        expect(viewContext.getRequest()).andReturn(request);
        expect(viewContext.getResponse()).andReturn(response);
        expect(viewContext.getServletContext()).andReturn(servletContext);
        expect(node.jjtGetChild(0)).andReturn(astMap);
        expect(astMap.value(velocityContext)).andReturn(params);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(tModel.getImportedAttributes(container, "myName", "myToName", false,
                velocityContext, request, response)).andReturn(attributes);
        expect(velocityContext.put("one", "value1")).andReturn("value1");
        expect(velocityContext.put("two", "value2")).andReturn("value2");

        replay(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap);
        initializeModel();
        model.render(velocityContext, writer, node);
        verify(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testExecuteRequest() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ServletContext servletContext = createMock(ServletContext.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        ViewToolContext viewContext = createMock(ViewToolContext.class);
        ASTMap astMap = createMock(ASTMap.class);
        Node node = createMock(Node.class);
        TilesContainer container = createMock(TilesContainer.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "request");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");

        expect(velocityContext.getInternalUserContext()).andReturn(viewContext);
        expect(viewContext.getRequest()).andReturn(request);
        expect(viewContext.getResponse()).andReturn(response);
        expect(viewContext.getServletContext()).andReturn(servletContext);
        expect(node.jjtGetChild(0)).andReturn(astMap);
        expect(astMap.value(velocityContext)).andReturn(params);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(tModel.getImportedAttributes(container, "myName", "myToName", false,
                velocityContext, request, response)).andReturn(attributes);
        request.setAttribute("one", "value1");
        request.setAttribute("two", "value2");

        replay(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap);
        initializeModel();
        model.render(velocityContext, writer, node);
        verify(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testExecuteSession() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        HttpSession session = createMock(HttpSession.class);
        ServletContext servletContext = createMock(ServletContext.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        ViewToolContext viewContext = createMock(ViewToolContext.class);
        ASTMap astMap = createMock(ASTMap.class);
        Node node = createMock(Node.class);
        TilesContainer container = createMock(TilesContainer.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "session");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");

        expect(velocityContext.getInternalUserContext()).andReturn(viewContext);
        expect(viewContext.getRequest()).andReturn(request);
        expect(viewContext.getResponse()).andReturn(response);
        expect(viewContext.getServletContext()).andReturn(servletContext);
        expect(node.jjtGetChild(0)).andReturn(astMap);
        expect(astMap.value(velocityContext)).andReturn(params);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(tModel.getImportedAttributes(container, "myName", "myToName", false,
                velocityContext, request, response)).andReturn(attributes);
        expect(request.getSession()).andReturn(session).times(2);
        session.setAttribute("one", "value1");
        session.setAttribute("two", "value2");

        replay(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap);
        initializeModel();
        model.render(velocityContext, writer, node);
        verify(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testExecuteApplication() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ServletContext servletContext = createMock(ServletContext.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        ViewToolContext viewContext = createMock(ViewToolContext.class);
        ASTMap astMap = createMock(ASTMap.class);
        Node node = createMock(Node.class);
        TilesContainer container = createMock(TilesContainer.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "application");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");

        expect(velocityContext.getInternalUserContext()).andReturn(viewContext);
        expect(viewContext.getRequest()).andReturn(request);
        expect(viewContext.getResponse()).andReturn(response);
        expect(viewContext.getServletContext()).andReturn(servletContext);
        expect(node.jjtGetChild(0)).andReturn(astMap);
        expect(astMap.value(velocityContext)).andReturn(params);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(tModel.getImportedAttributes(container, "myName", "myToName", false,
                velocityContext, request, response)).andReturn(attributes);
        servletContext.setAttribute("one", "value1");
        servletContext.setAttribute("two", "value2");

        replay(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap);
        initializeModel();
        model.render(velocityContext, writer, node);
        verify(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new ImportAttributeDirective(tModel);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ignore", false);
        params.put("name", "myName");
        params.put("toName", "myToName");
        return params;
    }
}
