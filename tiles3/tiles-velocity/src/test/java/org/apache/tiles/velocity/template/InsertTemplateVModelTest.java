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
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertTemplateModel;
import org.apache.tiles.velocity.context.VelocityTilesRequestContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.Renderable;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link InsertTemplateVModel}.
 */
public class InsertTemplateVModelTest {

    /**
     * The attribute key that will be used to store the parameter map, to use across Velocity tool calls.
     */
    private static final String PARAMETER_MAP_STACK_KEY = "org.apache.tiles.velocity.PARAMETER_MAP_STACK";

    /**
     * The model to test.
     */
    private InsertTemplateVModel model;

    /**
     * The template model.
     */
    private InsertTemplateModel tModel;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(InsertTemplateModel.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.InsertTemplateVModel
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
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(container.getApplicationContext()).andReturn(applicationContext);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        tModel.execute(eq(container), eq("myTemplate"), eq("myTemplateType"),
        		eq("myTemplateExpression"), eq("myRole"), eq("myPreparer"),
                isA(VelocityTilesRequestContext.class));

        replay(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter, applicationContext);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.InsertTemplateVModel
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
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(container.getApplicationContext()).andReturn(applicationContext);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(paramStack);
        tModel.start(eq(container), isA(VelocityTilesRequestContext.class));

        replay(tModel, servletContext, container, request, response, velocityContext, applicationContext);
        initializeModel();
        model.start(request, response, velocityContext, params);
        assertEquals(1, paramStack.size());
        assertEquals(params, paramStack.peek());
        verify(tModel, servletContext, container, request, response, velocityContext, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.InsertTemplateVModel
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
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(container.getApplicationContext()).andReturn(applicationContext);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(paramStack);
        tModel.end(eq(container), eq("myTemplate"), eq("myTemplateType"),
        		eq("myTemplateExpression"), eq("myRole"),
        		eq("myPreparer"), isA(VelocityTilesRequestContext.class));

        replay(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter, applicationContext);
        initializeModel();
        Renderable renderable = model.end(request, response, velocityContext);
        renderable.render(internalContextAdapter, writer);
        assertTrue(paramStack.isEmpty());
        verify(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter, applicationContext);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new InsertTemplateVModel(tModel, servletContext);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("template", "myTemplate");
        params.put("templateType", "myTemplateType");
        params.put("templateExpression", "myTemplateExpression");
        params.put("role", "myRole");
        params.put("preparer", "myPreparer");
        return params;
    }
}
