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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationContextUtil;
import org.apache.tiles.template.AddAttributeModel;
import org.apache.tiles.velocity.context.VelocityTilesRequestContext;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AddAttributeVModel}.
 */
public class AddAttributeVModelTest {

    /**
     * The attribute key that will be used to store the parameter map, to use across Velocity tool calls.
     */
    private static final String PARAMETER_MAP_STACK_KEY = "org.apache.tiles.velocity.PARAMETER_MAP_STACK";

    /**
     * The model to test.
     */
    private AddAttributeVModel model;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * The template model.
     */
    private AddAttributeModel tModel;

    private ApplicationContext applicationContext;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(AddAttributeModel.class);
        servletContext = createMock(ServletContext.class);
        model = new AddAttributeVModel(tModel, servletContext);
        applicationContext = createMock(ApplicationContext.class);
        expect(servletContext.getAttribute(ApplicationContextUtil
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.AddAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testExecute() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        Map<String, Object> params = createParams();

        tModel.execute(eq("myValue"), eq("myExpression"), (String) isNull(),
                eq("myRole"), eq("myType"), isA(VelocityTilesRequestContext.class));

        replay(tModel, request, response, velocityContext, servletContext, applicationContext);
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.execute(request, response, velocityContext, params));
        verify(tModel, request, response, velocityContext, servletContext, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.AddAttributeVModel
     * #start(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        Map<String, Object> params = createParams();
        ArrayStack<Map<String, Object>> parameterMapStack = new ArrayStack<Map<String, Object>>();

        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(parameterMapStack);
        tModel.start(isA(VelocityTilesRequestContext.class));

        replay(tModel, request, response, velocityContext, servletContext, applicationContext);
        model.start(request, response, velocityContext, params);
        assertEquals(1, parameterMapStack.size());
        assertEquals(params, parameterMapStack.peek());
        verify(tModel, request, response, velocityContext, servletContext, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.AddAttributeVModel
     * #end(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context)}.
     */
    @Test
    public void testEnd() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        Map<String, Object> params = createParams();
        ArrayStack<Map<String, Object>> parameterMapStack = new ArrayStack<Map<String, Object>>();
        parameterMapStack.push(params);

        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(parameterMapStack);
        tModel.end(eq("myValue"), eq("myExpression"), (String) isNull(),
                eq("myRole"), eq("myType"), isA(VelocityTilesRequestContext.class));

        replay(tModel, request, response, velocityContext, applicationContext);
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.end(request, response, velocityContext));
        assertTrue(parameterMapStack.isEmpty());
        verify(tModel, request, response, velocityContext, applicationContext);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("value", "myValue");
        params.put("expression", "myExpression");
        params.put("role", "myRole");
        params.put("type", "myType");
        return params;
    }
}
