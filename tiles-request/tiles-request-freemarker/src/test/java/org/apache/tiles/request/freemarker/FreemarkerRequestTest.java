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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.WebRequest;
import org.apache.tiles.request.scope.ContextResolver;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Before;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * Tests {@link FreemarkerRequest}.
 *
 * @version $Rev$ $Date$
 */
public class FreemarkerRequestTest {

    /**
     * The reuqest context to test.
     */
    private FreemarkerRequest context;

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
     * Sets up the test.
     */
    @Before
    public void setUp() {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        writer = new StringWriter();
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        replay(template, model);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
    }

    /**
     * Tests {@link FreemarkerRequest#createServletFreemarkerRequest(ApplicationContext, Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testCreateServletFreemarkerRequest() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        PrintWriter writer = new PrintWriter(new StringWriter());
        HttpServletRequest httpRequest = createMock(HttpServletRequest.class);
        HttpServletResponse httpResponse = createMock(HttpServletResponse.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());

        replay(httpRequest, httpResponse, objectWrapper);
        HttpRequestHashModel requestHashModel = new HttpRequestHashModel(httpRequest, httpResponse, objectWrapper);
        expect(model.get("Request")).andReturn(requestHashModel);

        replay(template, model, applicationContext);
        Environment env = new Environment(template, model, writer);
        Locale locale = Locale.ITALY;
        env.setLocale(locale);

        FreemarkerRequest request = FreemarkerRequest.createServletFreemarkerRequest(applicationContext, env);
        ServletRequest servletRequest = (ServletRequest) request.getWrappedRequest();
        assertEquals(httpRequest, servletRequest.getRequest());
        assertEquals(httpResponse, servletRequest.getResponse());
        verify(template, model, httpRequest, httpResponse, objectWrapper, applicationContext);
    }

    /**
     * Tests {@link FreemarkerRequest#dispatch(String)}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDispatch() throws IOException {
        String path = "this way";
        WebRequest enclosedRequest = createMock(WebRequest.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        Map<String, Object> requestScope = new HashMap<String, Object>();
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        enclosedRequest.include(path);
        expect(enclosedRequest.getApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        replay(enclosedRequest, applicationContext);
        context = new FreemarkerRequest(enclosedRequest, env);
        expect(resolver.getContext(isA(Request.class), eq("request"))).andReturn(requestScope);
        replay(resolver);
        context.dispatch(path);
        verify(enclosedRequest, applicationContext, resolver);
    }

    /**
     * Tests {@link FreemarkerRequest#getPageScope()}.
     */
    @Test
    public void testGetPageScope() {
        WebRequest enclosedRequest = createMock(WebRequest.class);
        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertTrue(context.getPageScope() instanceof EnvironmentScopeMap);
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getNativeScopes()}.
     */
    @Test
    public void testGetNativeScopes() {
        WebRequest enclosedRequest = createMock(WebRequest.class);
        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertArrayEquals(new String[] {"page"}, context.getNativeScopes());
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getRequestLocale()}.
     */
    @Test
    public void testGetRequestLocale() {
        WebRequest enclosedRequest = createMock(WebRequest.class);
        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertEquals(locale, context.getRequestLocale());
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getRequest()}.
     */
    @Test
    public void testGetRequest() {
        WebRequest enclosedRequest = createMock(WebRequest.class);
        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertEquals(env, context.getEnvironment());
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getResponse()}.
     */
    @Test
    public void testGetResponse() {
        WebRequest enclosedRequest = createMock(WebRequest.class);
        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertEquals(env, context.getEnvironment());
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getPrintWriter()}.
     */
    @Test
    public void testGetPrintWriter() {
        WebRequest enclosedRequest = createMock(WebRequest.class);

        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertEquals(env, context.getEnvironment());
        assertNotNull(context.getPrintWriter());
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getPrintWriter()}.
     */
    @Test
    public void testGetPrintWriterPrintWriter() {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        PrintWriter writer = new PrintWriter(new StringWriter());
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        replay(template, model);
        Environment env = new Environment(template, model, writer);
        Locale locale = Locale.ITALY;
        env.setLocale(locale);
        WebRequest enclosedRequest = createMock(WebRequest.class);

        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertSame(writer, context.getPrintWriter());
        verify(enclosedRequest, template, model);
    }


    /**
     * Tests {@link FreemarkerRequest#getWriter()}.
     */
    @Test
    public void testGetWriter() {
        WebRequest enclosedRequest = createMock(WebRequest.class);

        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertEquals(env, context.getEnvironment());
        assertNotNull(context.getWriter());
        verify(enclosedRequest);
    }
}
