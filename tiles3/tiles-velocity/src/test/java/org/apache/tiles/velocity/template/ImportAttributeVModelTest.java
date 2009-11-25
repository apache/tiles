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
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.tiles.velocity.context.VelocityTilesRequestContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.Renderable;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ImportAttributeVModel}.
 */
public class ImportAttributeVModelTest {

    /**
     * The model to test.
     */
    private ImportAttributeVModel model;

    /**
     * The template model.
     */
    private ImportAttributeModel tModel;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    private ApplicationContext applicationContext;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(ImportAttributeModel.class);
        servletContext = createMock(ServletContext.class);
        applicationContext = createMock(ApplicationContext.class);
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecutePage() throws IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");

        expect(tModel.getImportedAttributes(eq("myName"), eq("myToName"), eq(false), isA(VelocityTilesRequestContext.class))).andReturn(attributes);
        expect(internalContextAdapter.put("one", "value1")).andReturn("value1");
        expect(internalContextAdapter.put("two", "value2")).andReturn("value2");

        replay(tModel, servletContext, request, response, velocityContext, internalContextAdapter, applicationContext);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, velocityContext, internalContextAdapter, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecuteRequest() throws IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "request");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");

        expect(tModel.getImportedAttributes(eq("myName"), eq("myToName"), eq(false), isA(VelocityTilesRequestContext.class))).andReturn(attributes);
        request.setAttribute("one", "value1");
        request.setAttribute("two", "value2");

        replay(tModel, servletContext, request, response, velocityContext, internalContextAdapter, applicationContext);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, velocityContext, internalContextAdapter, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecuteSession() throws IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        HttpSession session = createMock(HttpSession.class);
        Context velocityContext = createMock(Context.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "session");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");

        expect(tModel.getImportedAttributes(eq("myName"), eq("myToName"), eq(false), isA(VelocityTilesRequestContext.class))).andReturn(attributes);
        expect(request.getSession()).andReturn(session).times(2);
        session.setAttribute("one", "value1");
        session.setAttribute("two", "value2");

        replay(tModel, servletContext, request, response, session, velocityContext, internalContextAdapter, applicationContext);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, session, velocityContext, internalContextAdapter, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecuteApplication() throws IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "application");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");

        expect(tModel.getImportedAttributes(eq("myName"), eq("myToName"), eq(false), isA(VelocityTilesRequestContext.class))).andReturn(attributes);
        servletContext.setAttribute("one", "value1");
        servletContext.setAttribute("two", "value2");

        replay(tModel, servletContext, request, response, velocityContext, internalContextAdapter, applicationContext);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, velocityContext, internalContextAdapter, applicationContext);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new ImportAttributeVModel(tModel, servletContext);
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
