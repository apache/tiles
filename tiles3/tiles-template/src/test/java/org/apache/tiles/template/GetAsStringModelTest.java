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

package org.apache.tiles.template;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link GetAsStringModel}.
 *
 * @version $Rev$ $Date$
 */
public class GetAsStringModelTest {

    /**
     * The mock resolver.
     */
    private AttributeResolver resolver;

    /**
     * The model to test.
     */
    private GetAsStringModel model;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        resolver = createMock(AttributeResolver.class);
        model = new GetAsStringModel(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.template.GetAsStringModel
     * #start(boolean, java.lang.String, java.lang.String, java.lang.Object,
     * java.lang.String, java.lang.String, java.lang.String, org.apache.tiles.Attribute, Request)}.
     */
    @Test
    public void testStart() {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        Attribute attribute = new Attribute();
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(request.getContext("request")).andReturn(requestScope).anyTimes();
        container.prepare("myPreparer", request);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", request)).andReturn(attribute);
        expect(container.startContext(request)).andReturn(attributeContext);

        replay(resolver, container, attributeContext, request, applicationContext);
        model.start(false, "myPreparer", "myRole", "myDefaultValue", "myDefaultValueRole", "myDefaultValueType",
                "myName", attribute, request);
        assertEquals(1, composeStack.size());
        assertEquals(attribute, composeStack.peek());
        verify(resolver, container, attributeContext, request, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.GetAsStringModel
     * #end(boolean, Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEnd() throws IOException {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Attribute attribute = new Attribute("myValue");
        composeStack.push(attribute);
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        Writer writer = createMock(Writer.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(request.getWriter()).andReturn(writer);
        expect(request.getContext("request")).andReturn(requestScope).anyTimes();
        writer.write("myValue");
        container.endContext(request);

        replay(resolver, container, writer, request, applicationContext);
        model.end(false, request);
        verify(resolver, container, writer, request, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.GetAsStringModel
     * #execute(boolean, java.lang.String, java.lang.String,
     * java.lang.Object, java.lang.String, java.lang.String, java.lang.String,
     * org.apache.tiles.Attribute, Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        Attribute attribute = new Attribute("myValue");
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Request request = createMock(Request.class);
        Writer writer = createMock(Writer.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(request.getContext("request")).andReturn(requestScope);
        expect(request.getWriter()).andReturn(writer);
        container.prepare("myPreparer", request);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", request)).andReturn(attribute);
        expect(container.startContext(request)).andReturn(attributeContext);
        writer.write("myValue");
        container.endContext(request);

        replay(resolver, container, writer, request, applicationContext);
        model.execute(false, "myPreparer", "myRole", "myDefaultValue", "myDefaultValueRole", "myDefaultValueType",
                "myName", attribute, request);
        verify(resolver, container, writer, request, applicationContext);
    }

}