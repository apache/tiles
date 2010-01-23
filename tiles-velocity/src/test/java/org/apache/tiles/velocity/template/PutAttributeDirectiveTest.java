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

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.PutAttributeModel;
import org.apache.velocity.context.InternalContextAdapter;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link PutAttributeDirective}.
 */
public class PutAttributeDirectiveTest {

    /**
     * The model to test.
     */
    private PutAttributeDirective model;

    /**
     * The template model.
     */
    private PutAttributeModel tModel;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(PutAttributeModel.class);
    }

    /**
     * Test method for {@link PutAttributeDirective#start(InternalContextAdapter,
     * Writer, Map, HttpServletRequest, HttpServletResponse, ServletContext)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        ServletContext servletContext = createMock(ServletContext.class);
        Map<String, Object> params = createParams();
        ArrayStack<Object> composeStack = new ArrayStack<Object>();

        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.start(composeStack);

        replay(tModel, request, response, velocityContext, writer, servletContext);
        initializeModel();
        model.start(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext);
    }

    /**
     * Test method for {@link PutAttributeDirective#end(InternalContextAdapter,
     * Writer, Map, String, HttpServletRequest, HttpServletResponse, ServletContext)}.
     */
    @Test
    public void testEnd() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        ServletContext servletContext = createMock(ServletContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> params = createParams();
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        ArrayStack<Map<String, Object>> parameterMapStack = new ArrayStack<Map<String, Object>>();
        parameterMapStack.push(params);

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.end(container, composeStack, "myName", "myValue",
                "myExpression", "myBody", "myRole", "myType",
                false, velocityContext, request, response, writer);

        replay(tModel, request, response, velocityContext, writer, servletContext, container);
        initializeModel();
        model.end(velocityContext, writer, params, "myBody", request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext, container);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "myName");
        params.put("value", "myValue");
        params.put("expression", "myExpression");
        params.put("role", "myRole");
        params.put("type", "myType");
        params.put("cascade", false);
        return params;
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new PutAttributeDirective(tModel);
    }
}
