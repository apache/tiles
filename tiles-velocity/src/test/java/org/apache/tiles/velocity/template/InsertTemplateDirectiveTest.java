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

import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertTemplateModel;
import org.apache.velocity.context.InternalContextAdapter;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link InsertTemplateDirective}.
 */
public class InsertTemplateDirectiveTest {

    /**
     * The model to test.
     */
    private InsertTemplateDirective model;

    /**
     * The template model.
     */
    private InsertTemplateModel tModel;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(InsertTemplateModel.class);
    }

    /**
     * Test method for {@link InsertTemplateDirective#start(InternalContextAdapter,
     * Writer, Map, HttpServletRequest, HttpServletResponse, ServletContext)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        ServletContext servletContext = createMock(ServletContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> params = createParams();

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        tModel.start(container, velocityContext, request, response);

        replay(tModel, request, response, velocityContext, writer, servletContext, container);
        initializeModel();
        model.start(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext, container);
    }

    /**
     * Test method for {@link InsertTemplateDirective#end(InternalContextAdapter,
     * Writer, Map, HttpServletRequest, HttpServletResponse, ServletContext)}.
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

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        tModel.end(container, "myTemplate", "myTemplateType",
                "myTemplateExpression", "myRole",
                "myPreparer", velocityContext, request, response, writer);

        replay(tModel, request, response, velocityContext, writer, servletContext, container);
        initializeModel();
        model.end(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext, container);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new InsertTemplateDirective(tModel);
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
