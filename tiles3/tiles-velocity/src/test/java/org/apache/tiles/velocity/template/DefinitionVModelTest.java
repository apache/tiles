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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.DefinitionModel;
import org.apache.tiles.velocity.context.VelocityTilesRequestContext;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DefinitionVModel}.
 */
public class DefinitionVModelTest {

    /**
     * The model to test.
     */
    private DefinitionVModel model;

    /**
     * The template model.
     */
    private DefinitionModel tModel;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(DefinitionModel.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.DefinitionVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testExecute() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Map<String, Object> params = createParams();
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(container.getApplicationContext()).andReturn(applicationContext);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        tModel.execute(eq(container), eq("myName"), eq("myTemplate"),
                eq("myRole"), eq("myExtends"), eq("myPreparer"),
                isA(VelocityTilesRequestContext.class));

        replay(tModel, servletContext, request, response, velocityContext, container, applicationContext);
        initializeModel();
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.execute(request, response, velocityContext, params));
        verify(tModel, servletContext, request, response, velocityContext, container, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.DefinitionVModel
     * #start(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        Map<String, Object> params = createParams();
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(container.getApplicationContext()).andReturn(applicationContext);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        tModel.start(eq("myName"), eq("myTemplate"), eq("myRole"),
                eq("myExtends"), eq("myPreparer"),
                isA(VelocityTilesRequestContext.class));

        replay(tModel, servletContext, request, response, velocityContext);
        initializeModel();
        model.start(request, response, velocityContext, params);
        verify(tModel, servletContext, request, response, velocityContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.DefinitionVModel
     * #end(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEnd() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(container.getApplicationContext()).andReturn(applicationContext);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        tModel.end(eq(container), isA(VelocityTilesRequestContext.class));

        replay(tModel, servletContext, request, response, velocityContext, container, applicationContext);
        initializeModel();
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.end(request, response, velocityContext));
        verify(tModel, servletContext, request, response, velocityContext, container, applicationContext);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new DefinitionVModel(tModel, servletContext);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "myName");
        params.put("template", "myTemplate");
        params.put("role", "myRole");
        params.put("extends", "myExtends");
        params.put("preparer", "myPreparer");
        return params;
    }
}
