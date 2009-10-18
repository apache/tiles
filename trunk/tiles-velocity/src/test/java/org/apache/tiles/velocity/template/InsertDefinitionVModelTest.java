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
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertDefinitionModel;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.Renderable;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link InsertDefinitionVModel}.
 */
public class InsertDefinitionVModelTest {

    /**
     * The attribute key that will be used to store the parameter map, to use across Velocity tool calls.
     */
    private static final String PARAMETER_MAP_STACK_KEY = "org.apache.tiles.velocity.PARAMETER_MAP_STACK";

    /**
     * The model to test.
     */
    private InsertDefinitionVModel model;

    /**
     * The template model.
     */
    private InsertDefinitionModel tModel;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(InsertDefinitionModel.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.InsertDefinitionVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        tModel.execute(container, "myDefinitionName", "myTemplate",
                "myTemplateType", "myTemplateExpression", "myRole",
                "myPreparer", velocityContext, request, response, writer);

        replay(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.InsertDefinitionVModel
     * #start(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> params = createParams();
        ArrayStack<Map<String, Object>> paramStack = new ArrayStack<Map<String, Object>>();

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(paramStack);
        tModel.start(container, velocityContext, request, response);

        replay(tModel, servletContext, container, request, response, velocityContext);
        initializeModel();
        model.start(request, response, velocityContext, params);
        assertEquals(1, paramStack.size());
        assertEquals(params, paramStack.peek());
        verify(tModel, servletContext, container, request, response, velocityContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.InsertDefinitionVModel
     * #end(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEnd() throws IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        ArrayStack<Map<String, Object>> paramStack = new ArrayStack<Map<String, Object>>();
        paramStack.push(params);

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(paramStack);
        tModel.end(container, "myDefinitionName", "myTemplate",
                "myTemplateType", "myTemplateExpression", "myRole",
                "myPreparer", velocityContext, request, response, writer);

        replay(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
        initializeModel();
        Renderable renderable = model.end(request, response, velocityContext);
        renderable.render(internalContextAdapter, writer);
        assertTrue(paramStack.isEmpty());
        verify(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new InsertDefinitionVModel(tModel, servletContext);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "myDefinitionName");
        params.put("template", "myTemplate");
        params.put("templateType", "myTemplateType");
        params.put("templateExpression", "myTemplateExpression");
        params.put("role", "myRole");
        params.put("preparer", "myPreparer");
        return params;
    }
}
