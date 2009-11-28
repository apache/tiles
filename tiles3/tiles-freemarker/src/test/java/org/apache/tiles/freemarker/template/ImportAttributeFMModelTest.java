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

package org.apache.tiles.freemarker.template;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.Attribute;
import org.apache.tiles.freemarker.context.FreeMarkerTilesRequestContext;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.apache.tiles.template.ImportAttributeModel;
import org.junit.Before;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

/**
 * Tests {@link ImportAttributeFMModel}.
 *
 * @version $Rev$ $Date$
 */
public class ImportAttributeFMModelTest {

    /**
     * The FreeMarker environment.
     */
    private Environment env;

    /**
     * The locale object.
     */
    private Locale locale;

    /**
     * The template.
     */
    private Template template;

    /**
     * The template model.
     */
    private TemplateHashModel model;

    /**
     * The writer.
     */
    private StringWriter writer;

    /**
     * The object wrapper.
     */
    private ObjectWrapper objectWrapper;

    /**
     * Sets up the model.
     */
    @Before
    public void setUp() {
        template = createMock(Template.class);
        model = createMock(TemplateHashModel.class);
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        writer = new StringWriter();
        objectWrapper = DefaultObjectWrapper.getDefaultInstance();
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.ImportAttributeFMModel
     * #execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[],
     * freemarker.template.TemplateDirectiveBody)}.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecute() throws TemplateException {
        ImportAttributeModel tModel = createMock(ImportAttributeModel.class);
        ImportAttributeFMModel fmModel = new ImportAttributeFMModel(tModel);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel).anyTimes();
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("toName", objectWrapper.wrap("myToName"));
        params.put("ignore", objectWrapper.wrap(false));

		tModel.execute(eq("myName"), (String) isNull(), eq("myToName"), eq(false),
				isA(FreeMarkerTilesRequestContext.class));

        replay(tModel, body, attribute, applicationContext);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, tModel, body, servlet, servletContext,
                attribute, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.ImportAttributeFMModel
     * #execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[],
     * freemarker.template.TemplateDirectiveBody)}.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecuteRequest() throws TemplateException {
        ImportAttributeModel tModel = createMock(ImportAttributeModel.class);
        ImportAttributeFMModel fmModel = new ImportAttributeFMModel(tModel);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel).anyTimes();
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("toName", objectWrapper.wrap("myToName"));
        params.put("ignore", objectWrapper.wrap(false));
        params.put("scope", objectWrapper.wrap("request"));

        tModel.execute(eq("myName"), eq("request"), eq("myToName"), eq(false),
                isA(FreeMarkerTilesRequestContext.class));

        replay(tModel, body, attribute, applicationContext);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, tModel, body, servlet, servletContext,
                attribute, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.ImportAttributeFMModel
     * #execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[],
     * freemarker.template.TemplateDirectiveBody)}.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecuteSession() throws TemplateException {
        ImportAttributeModel tModel = createMock(ImportAttributeModel.class);
        ImportAttributeFMModel fmModel = new ImportAttributeFMModel(tModel);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel).anyTimes();
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("toName", objectWrapper.wrap("myToName"));
        params.put("ignore", objectWrapper.wrap(false));
        params.put("scope", objectWrapper.wrap("session"));

        tModel.execute(eq("myName"), eq("session"), eq("myToName"), eq(false),
                isA(FreeMarkerTilesRequestContext.class));

        replay(tModel, body, attribute, applicationContext);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, tModel, body, servlet,
                servletContext, attribute, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.ImportAttributeFMModel
     * #execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[],
     * freemarker.template.TemplateDirectiveBody)}.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecuteApplication() throws TemplateException {
        ImportAttributeModel tModel = createMock(ImportAttributeModel.class);
        ImportAttributeFMModel fmModel = new ImportAttributeFMModel(tModel);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servlet.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel).anyTimes();
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel).anyTimes();
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("toName", objectWrapper.wrap("myToName"));
        params.put("ignore", objectWrapper.wrap(false));
        params.put("scope", objectWrapper.wrap("application"));

        tModel.execute(eq("myName"), eq("application"), eq("myToName"), eq(false),
                isA(FreeMarkerTilesRequestContext.class));

        replay(tModel, body, attribute, applicationContext);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, tModel, body, servlet, servletContext,
                attribute, applicationContext);
    }

    /**
     * Initializes the FreeMarker environment.
     */
    private void initEnvironment() {
        replay(template, model);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
    }
}
