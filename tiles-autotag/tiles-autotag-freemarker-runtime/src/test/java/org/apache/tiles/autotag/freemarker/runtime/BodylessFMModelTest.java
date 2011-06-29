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
package org.apache.tiles.autotag.freemarker.runtime;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.freemarker.FreemarkerRequest;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.core.Macro;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Tests {@link BodyFMModel}.
 *
 * @version $Rev$ $Date$
 */
public class BodylessFMModelTest {

    /**
     * Test method for
     * {@link BodylessFMModel#execute(Environment, Map, TemplateModel[], TemplateDirectiveBody)}
     * .
     *
     * @throws IOException If something goes wrong.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException, TemplateModelException {
        BodylessFMModel model = createMockBuilder(BodylessFMModel.class).createMock();
        @SuppressWarnings("unchecked")
        Map<String, TemplateModel> params = createMock(Map.class);
        Template template = createMock(Template.class);
        TemplateHashModel rootDataModel = createMock(TemplateHashModel.class);
        Writer out = createMock(Writer.class);
        GenericServlet servlet = createMock(GenericServlet.class);
        ObjectWrapper wrapper = createMock(ObjectWrapper.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        HttpServletRequest httpServletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);

        expect(template.getMacros()).andReturn(new HashMap<String, Macro>());
        expect(servlet.getServletContext()).andReturn(servletContext)
                .anyTimes();
        expect(
                servletContext
                        .getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);

        replay(servlet, wrapper, servletContext, applicationContext,
                httpServletRequest, httpServletResponse);
        ServletContextHashModel servletContextHashModel = new ServletContextHashModel(
                servlet, wrapper);
        HttpRequestHashModel httpRequestHashModel = new HttpRequestHashModel(
                httpServletRequest, httpServletResponse, wrapper);

        expect(rootDataModel.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(
                servletContextHashModel);
        expect(rootDataModel.get(FreemarkerServlet.KEY_REQUEST)).andReturn(
                httpRequestHashModel);

        replay(template, rootDataModel, out);
        Environment env = new Environment(template, rootDataModel, out);

        model.execute(eq(params), isA(FreemarkerRequest.class));

        replay(model, params);
        model.execute(env, params, new TemplateModel[0], null);
        verify(model, params, template, rootDataModel, out, servlet, wrapper,
                servletContext, httpServletRequest, httpServletResponse,
                applicationContext);
    }

}
