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

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.apache.tiles.request.velocity.VelocityRequest;
import org.apache.tiles.template.AddListAttributeModel;
import org.apache.velocity.context.InternalContextAdapter;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AddListAttributeDirective}.
 */
public class AddListAttributeDirectiveTest {

    /**
     * The model to test.
     */
    private AddListAttributeDirective model;

    /**
     * The template model.
     */
    private AddListAttributeModel tModel;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(AddListAttributeModel.class);
        model = new AddListAttributeDirective(tModel);
    }

    /**
     * Test method for {@link AddListAttributeDirective#start(InternalContextAdapter,
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
        tModel.start(eq("myRole"), isA(VelocityRequest.class));

        replay(tModel, request, response, velocityContext, writer, servletContext, applicationContext);
        model.start(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext, applicationContext);
    }

    /**
     * Test method for {@link AddListAttributeDirective#end(InternalContextAdapter,
     * Writer, Map, HttpServletRequest, HttpServletResponse, ServletContext)}.
     */
    @Test
    public void testEnd() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        Map<String, Object> params = createParams();
        Writer writer = createMock(Writer.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);
        tModel.end(isA(VelocityRequest.class));

        replay(tModel, request, response, velocityContext, writer, servletContext, applicationContext);
        model.end(velocityContext, writer, params, request, response, servletContext);
        verify(tModel, request, response, velocityContext, writer, servletContext, applicationContext);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("role", "myRole");
        return params;
    }
}
