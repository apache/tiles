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

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.apache.tiles.request.velocity.VelocityRequest;
import org.apache.tiles.template.GetAsStringModel;
import org.apache.velocity.context.InternalContextAdapter;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link GetAsStringDirective}.
 */
public class GetAsStringDirectiveTest {

    /**
     * The model to test.
     */
    private GetAsStringDirective model;

    /**
     * The template model.
     */
    private GetAsStringModel tModel;

    /**
     * The attribute value.
     */
    private Attribute attribute;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(GetAsStringModel.class);
        attribute = new Attribute("myAttributeValue");
    }

    /**
     * Test method for {@link GetAsStringDirective#start(InternalContextAdapter,
     * Writer, Map, HttpServletRequest, HttpServletResponse, ServletContext)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        Map<String, Object> params = createParams();
        Writer writer = createMock(Writer.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);
        tModel.start(eq(false), eq("myPreparer"), eq("myRole"), eq("myDefaultValue"),
                eq("myDefaultValueRole"), eq("myDefaultValueType"), eq("myName"), eq(attribute),
                isA(VelocityRequest.class));

        replay(tModel, request, response, velocityContext, writer, servletContext, applicationContext);
        initializeModel();
        model.start(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext, applicationContext);
    }

    /**
     * Test method for {@link GetAsStringDirective#end(InternalContextAdapter,
     * Writer, Map, HttpServletRequest, HttpServletResponse, ServletContext)}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEnd() throws IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        ServletContext servletContext = createMock(ServletContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> params = createParams();
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);
        tModel.end(eq(false), isA(VelocityRequest.class));

        replay(tModel, request, response, velocityContext, writer, servletContext, container);
        initializeModel();
        model.end(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext, container);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new GetAsStringDirective(tModel);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ignore", false);
        params.put("preparer", "myPreparer");
        params.put("role", "myRole");
        params.put("defaultValue", "myDefaultValue");
        params.put("defaultValueRole", "myDefaultValueRole");
        params.put("defaultValueType", "myDefaultValueType");
        params.put("name", "myName");
        params.put("value", attribute);
        params.put("role", "myRole");
        params.put("extends", "myExtends");
        return params;
    }
}
