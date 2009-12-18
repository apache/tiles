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

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.freemarker.FreemarkerRequest;
import org.junit.Before;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;

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
     * Tests {@link FreemarkerRequest#dispatch(String)}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDispatch() throws IOException {
        String path = "this way";
        Request enclosedRequest = createMock(Request.class);
        enclosedRequest.include(path);
        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        context.dispatch(path);
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getRequestLocale()}.
     */
    @Test
    public void testGetRequestLocale() {
        Request enclosedRequest = createMock(Request.class);
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
        Request enclosedRequest = createMock(Request.class);
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
        Request enclosedRequest = createMock(Request.class);
        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertEquals(env, context.getEnvironment());
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getPrintWriter()}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetPrintWriter() {
        Request enclosedRequest = createMock(Request.class);

        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertEquals(env, context.getEnvironment());
        assertNotNull(context.getPrintWriter());
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getWriter()}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetWriter() {
        Request enclosedRequest = createMock(Request.class);

        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        assertEquals(env, context.getEnvironment());
        assertNotNull(context.getWriter());
        verify(enclosedRequest);
    }

    /**
     * Tests {@link FreemarkerRequest#getRequestObjects()}.
     */
    @Test
    public void testGetRequestObjects() {
        Request enclosedRequest = createMock(Request.class);

        replay(enclosedRequest);
        context = new FreemarkerRequest(enclosedRequest, env);
        Object[] requestObjects = context.getRequestObjects();
        assertEquals(1, requestObjects.length);
        assertEquals(env, requestObjects[0]);
        verify(enclosedRequest);
    }
}
