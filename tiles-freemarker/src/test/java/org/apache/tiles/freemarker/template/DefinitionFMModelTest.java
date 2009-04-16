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

import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.freemarker.io.NullWriter;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.DefinitionModel;
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
 * Tests {@link DefinitionFMModel}.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionFMModelTest {

    /**
     * The number of times the method is called.
     */
    private static final int CALL_COUNT = 3;

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
     * @throws java.lang.Exception If something goes wrong.
     */
    @Before
    public void setUp() throws Exception {
        template = createMock(Template.class);
        model = createMock(TemplateHashModel.class);
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        writer = new StringWriter();
        objectWrapper = DefaultObjectWrapper.getDefaultInstance();
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.DefinitionFMModel
     * #execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[],
     * freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecute() throws TemplateException, IOException {
        DefinitionModel tModel = createMock(DefinitionModel.class);
        DefinitionFMModel fmModel = new DefinitionFMModel(tModel);
        MutableTilesContainer container = createMock(MutableTilesContainer.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        Stack<Object> composeStack = new Stack<Object>();
        expect(request.getAttribute(FreeMarkerUtil.COMPOSE_STACK_ATTRIBUTE_NAME)).andReturn(composeStack).times(2);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(null);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        expect(servletContext.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel).times(CALL_COUNT);
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", objectWrapper.wrap("myName"));
        params.put("template", objectWrapper.wrap("myTemplate"));
        params.put("role", objectWrapper.wrap("myRole"));
        params.put("extends", objectWrapper.wrap("myExtends"));
        params.put("preparer", objectWrapper.wrap("myPreparer"));

        tModel.start(composeStack, "myName", "myTemplate", "myRole", "myExtends", "myPreparer");
        tModel.end(container, composeStack, env);
        body.render(isA(NullWriter.class));

        replay(tModel, body, container);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, tModel, body, container, servlet, servletContext);
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
