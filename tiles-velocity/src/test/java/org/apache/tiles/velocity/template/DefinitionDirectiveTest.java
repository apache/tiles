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
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.DefinitionModel;
import org.apache.velocity.context.InternalContextAdapter;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DefinitionDirective}.
 */
public class DefinitionDirectiveTest {

    /**
     * The model to test.
     */
    private DefinitionDirective model;

    /**
     * The template model.
     */
    private DefinitionModel tModel;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(DefinitionModel.class);
    }

    /**
     * Test method for {@link DefinitionDirective#start(InternalContextAdapter,
     * Writer, Map, HttpServletRequest, HttpServletResponse, ServletContext)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        Map<String, Object> params = createParams();
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Writer writer = createMock(Writer.class);
        ServletContext servletContext = createMock(ServletContext.class);

        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.start(composeStack, "myName", "myTemplate", "myRole", "myExtends", "myPreparer");

        replay(tModel, request, response, velocityContext, writer, servletContext);
        initializeModel();
        model.start(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext);
    }

    /**
     * Test method for {@link DefinitionDirective#end(InternalContextAdapter,
     * Writer, Map, HttpServletRequest, HttpServletResponse, ServletContext)}.
     */
    @Test
    public void testEnd() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        ServletContext servletContext = createMock(ServletContext.class);
        Map<String, Object> params = createParams();
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.end(container, composeStack, velocityContext, request, response);

        replay(tModel, request, response, velocityContext, writer, servletContext, container);
        initializeModel();
        model.end(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext, container);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new DefinitionDirective(tModel);
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
