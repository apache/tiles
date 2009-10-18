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

package org.apache.tiles.freemarker.context;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;
import static org.apache.tiles.freemarker.context.FreeMarkerUtil.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.freemarker.io.NullWriter;
import org.apache.tiles.impl.NoSuchContainerException;
import org.apache.tiles.servlet.context.ServletUtil;
import org.junit.Before;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Tests {@link FreeMarkerUtil}.
 *
 * @version $Rev$ $Date$
 */
public class FreeMarkerUtilTest {

    /**
     * A dummy value.
     */
    private static final int DUMMY_VALUE = 10;

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
     * @throws java.lang.Exception If something goes wrong.
     */
    @Before
    public void setUp() throws Exception {
        template = createMock(Template.class);
        model = createMock(TemplateHashModel.class);
        writer = new StringWriter();
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #isForceInclude(freemarker.core.Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testIsForceInclude() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);
        expect(request.getAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME)).andReturn(true);

        replay(template, model, request, objectWrapper);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        assertTrue(isForceInclude(env));
        verify(template, model, request, objectWrapper);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #setForceInclude(freemarker.core.Environment, boolean)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testSetForceInclude() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);
        request.setAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME, true);

        replay(template, model, request, objectWrapper);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        setForceInclude(env, true);
        verify(template, model, request, objectWrapper);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #getContainer(freemarker.core.Environment, java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetContainer() throws TemplateModelException {
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        TilesContainer container = createMock(TilesContainer.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        replay(servlet, objectWrapper);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(model.get("Application")).andReturn(servletContextModel);
        expect(servletContext.getAttribute("myKey")).andReturn(container);

        replay(template, model, servletContext, container);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        assertEquals(container, getContainer(env, "myKey"));
        verify(template, model, servlet, servletContext, objectWrapper, container);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #setCurrentContainer(freemarker.core.Environment, java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testSetCurrentContainerEnvironmentString() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        replay(servlet, objectWrapper);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        expect(model.get("Application")).andReturn(servletContextModel);
        expect(servletContext.getAttribute("myKey")).andReturn(container);

        replay(template, model, servletContext, container, request);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        setCurrentContainer(env, "myKey");
        verify(template, model, servlet, servletContext, objectWrapper, container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #setCurrentContainer(freemarker.core.Environment, java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test(expected = NoSuchContainerException.class)
    public void testSetCurrentContainerEnvironmentStringException() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        replay(servlet, objectWrapper);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);
        expect(model.get("Application")).andReturn(servletContextModel);
        expect(servletContext.getAttribute("myKey")).andReturn(null);

        replay(template, model, servletContext, request);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        setCurrentContainer(env, "myKey");
        verify(template, model, servlet, servletContext, objectWrapper, request);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #setCurrentContainer(freemarker.core.Environment, org.apache.tiles.TilesContainer)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testSetCurrentContainerEnvironmentTilesContainer() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        replay(servlet, objectWrapper);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        expect(model.get("Application")).andReturn(servletContextModel);

        replay(template, model, servletContext, container, request);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        setCurrentContainer(env, container);
        verify(template, model, servlet, servletContext, objectWrapper, container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #getCurrentContainer(freemarker.core.Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetCurrentContainer() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        replay(servlet, objectWrapper);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(model.get("Application")).andReturn(servletContextModel);

        replay(template, model, servletContext, container, request);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        assertEquals(container, getCurrentContainer(env));
        verify(template, model, servlet, servletContext, objectWrapper, container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #getRequestHashModel(freemarker.core.Environment)}.
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
        assertEquals(requestModel, getRequestHashModel(env));
        verify(template, model, request, objectWrapper);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
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
        assertEquals(servletContextModel, getServletContextHashModel(env));
        verify(template, model, servlet, servletContext, objectWrapper);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #setAttribute(freemarker.core.Environment, java.lang.String, java.lang.Object, java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testSetAttributePage() throws TemplateModelException {
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        GenericServlet servlet = createMock(GenericServlet.class);
        TemplateModel wrappedObj = createMock(TemplateModel.class);
        Integer myObj = new Integer(DUMMY_VALUE);
        expect(objectWrapper.wrap(myObj)).andReturn(wrappedObj);
        replay(servlet, objectWrapper);

        expect(template.getObjectWrapper()).andReturn(objectWrapper);

        replay(template, model, wrappedObj);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        setAttribute(env, "myObj", myObj, null);
        verify(template, model, servlet, objectWrapper, wrappedObj);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #setAttribute(freemarker.core.Environment, java.lang.String, java.lang.Object, java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testSetAttributeRequest() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        GenericServlet servlet = createMock(GenericServlet.class);
        TemplateModel wrappedObj = createMock(TemplateModel.class);
        Integer myObj = new Integer(DUMMY_VALUE);
        replay(servlet, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);
        request.setAttribute("myObj", myObj);

        replay(template, model, wrappedObj, request);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        setAttribute(env, "myObj", myObj, "request");
        verify(template, model, servlet, objectWrapper, wrappedObj, request);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #setAttribute(freemarker.core.Environment, java.lang.String, java.lang.Object, java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testSetAttributeSession() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpSession session = createMock(HttpSession.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        GenericServlet servlet = createMock(GenericServlet.class);
        TemplateModel wrappedObj = createMock(TemplateModel.class);
        Integer myObj = new Integer(DUMMY_VALUE);
        replay(servlet, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);
        expect(request.getSession()).andReturn(session);
        session.setAttribute("myObj", myObj);

        replay(template, model, wrappedObj, request, session);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        setAttribute(env, "myObj", myObj, "session");
        verify(template, model, servlet, objectWrapper, wrappedObj, request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #setAttribute(freemarker.core.Environment, java.lang.String, java.lang.Object, java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testSetAttributeApplication() throws TemplateModelException {
        ServletContext servletContext = createMock(ServletContext.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        GenericServlet servlet = createMock(GenericServlet.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        TemplateModel wrappedObj = createMock(TemplateModel.class);
        Integer myObj = new Integer(DUMMY_VALUE);
        servletContext.setAttribute("myObj", myObj);
        replay(servlet, objectWrapper, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(model.get("Application")).andReturn(servletContextModel);

        replay(template, model, wrappedObj);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        setAttribute(env, "myObj", myObj, "application");
        verify(template, model, servlet, servletContext, objectWrapper, wrappedObj);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #getComposeStack(freemarker.core.Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetComposeStack() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        GenericServlet servlet = createMock(GenericServlet.class);
        TemplateModel wrappedObj = createMock(TemplateModel.class);
        replay(servlet, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel).times(2);
        expect(request.getAttribute(COMPOSE_STACK_ATTRIBUTE_NAME)).andReturn(null);
        request.setAttribute(eq(COMPOSE_STACK_ATTRIBUTE_NAME),
                isA(ArrayStack.class));
        ArrayStack<Object> myStack = new ArrayStack<Object>();
        expect(request.getAttribute(COMPOSE_STACK_ATTRIBUTE_NAME)).andReturn(myStack);

        replay(template, model, wrappedObj, request);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        assertNotNull(getComposeStack(env));
        assertEquals(myStack, getComposeStack(env));
        verify(template, model, servlet, objectWrapper, wrappedObj, request);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #evaluateBody(freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testEvaluateBody() throws TemplateException, IOException {
        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        body.render(isA(NullWriter.class));

        replay(body);
        evaluateBody(body);
        verify(body);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerUtil
     * #renderAsString(freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testRenderAsString() throws TemplateException, IOException {
        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        body.render(isA(StringWriter.class));

        replay(body);
        assertEquals("", renderAsString(body));
        verify(body);
    }

}
