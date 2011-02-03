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
package org.apache.tiles.request.freemarker;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Before;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * Tests {@link FreemarkerRequestUtil}.
 *
 */
public class FreemarkerRequestUtilTest {

    /**
     * A string writer.
     */
    private StringWriter writer;

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
     * Sets up the model.
     */
    @Before
    public void setUp() {
        template = createMock(Template.class);
        model = createMock(TemplateHashModel.class);
        writer = new StringWriter();
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
    }

    /**
     * Test method for {@link FreemarkerRequestUtil#getRequestHashModel(freemarker.core.Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetRequestHashModel() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);

        replay(template, model, request, objectWrapper);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        assertEquals(requestModel, FreemarkerRequestUtil.getRequestHashModel(env));
        verify(template, model, request, objectWrapper);
    }

    /**
     * Test method for {@link FreemarkerRequestUtil#getRequestHashModel(freemarker.core.Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test(expected = NotAvailableFreemarkerServletException.class)
    public void testGetRequestHashModelException() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);

        expect(model.get("Request")).andThrow(new TemplateModelException());

        replay(template, model, request, objectWrapper);
        try {
            env = new Environment(template, model, writer);
            locale = Locale.ITALY;
            env.setLocale(locale);
            FreemarkerRequestUtil.getRequestHashModel(env);
        } finally {
            verify(template, model, request, objectWrapper);
        }
    }


    /**
     * Test method for {@link FreemarkerRequestUtil
     * #getServletContextHashModel(freemarker.core.Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetServletContextHashModel() throws TemplateModelException {
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        expect(servlet.getServletContext()).andReturn(servletContext);
        replay(servlet, objectWrapper);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(model.get("Application")).andReturn(servletContextModel);

        replay(template, model, servletContext);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        assertEquals(servletContextModel, FreemarkerRequestUtil.getServletContextHashModel(env));
        verify(template, model, servlet, servletContext, objectWrapper);
    }

    /**
     * Test method for {@link FreemarkerRequestUtil
     * #getServletContextHashModel(freemarker.core.Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test(expected = NotAvailableFreemarkerServletException.class)
    public void testGetServletContextHashModelException() throws TemplateModelException {
        GenericServlet servlet = createMock(GenericServlet.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        replay(servlet, objectWrapper);

        expect(model.get("Application")).andThrow(new TemplateModelException());

        replay(template, model);
        try {
            env = new Environment(template, model, writer);
            locale = Locale.ITALY;
            env.setLocale(locale);
            FreemarkerRequestUtil.getServletContextHashModel(env);
        } finally {
            verify(template, model, servlet, objectWrapper);
        }
    }

    /**
     * Test method for {@link FreemarkerRequestUtil
     * #getApplicationContext(Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetApplicationContext() throws TemplateModelException {
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext);

        replay(servlet, objectWrapper);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(model.get("Application")).andReturn(servletContextModel);

        replay(template, model, servletContext);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        assertEquals(applicationContext, FreemarkerRequestUtil.getApplicationContext(env));
        verify(template, model, servlet, servletContext, objectWrapper);
    }
}
