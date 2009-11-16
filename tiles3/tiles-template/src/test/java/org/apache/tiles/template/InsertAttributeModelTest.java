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
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link InsertAttributeModel}.
 *
 * @version $Rev$ $Date$
 */
public class InsertAttributeModelTest {

    /**
     * The mock resolver.
     */
    private AttributeResolver resolver;

    /**
     * The model to test.
     */
    private InsertAttributeModel model;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        resolver = createMock(AttributeResolver.class);
        model = new InsertAttributeModel(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel
     * #start(TilesContainer, boolean, String, String, Object, String, String,
     * String, Attribute, Request)}.
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

        expect(request.getRequestScope()).andReturn(requestScope);
        container.prepare("myPreparer", request);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", request)).andReturn(attribute);
        expect(container.startContext(request)).andReturn(attributeContext);

        replay(resolver, container, attributeContext, request);
        model.start(container, false, "myPreparer", "myRole", "myDefaultValue", "myDefaultValueRole",
                "myDefaultValueType", "myName", attribute, request);
        assertEquals(1, composeStack.size());
        assertEquals(attribute, composeStack.peek());
        verify(resolver, container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel
     * #end(TilesContainer, boolean, Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEnd() throws IOException {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Attribute attribute = new Attribute("myValue");
        composeStack.push(attribute);
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getRequestScope()).andReturn(requestScope);
        container.endContext(request);
        container.render(attribute, request);

        replay(resolver, container, request);
        model.end(container, false, request);
        verify(resolver, container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel
     * #execute(TilesContainer, boolean, String, String, Object, String, String,
     * String, Attribute, Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        Attribute attribute = new Attribute("myValue");
        AttributeContext attributeContext = createMock(AttributeContext.class);

        container.prepare("myPreparer", request);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", request)).andReturn(attribute);
        expect(container.startContext(request)).andReturn(attributeContext);
        container.endContext(request);
        container.render(attribute, request);

        replay(resolver, container, request);
        model.execute(container, false, "myPreparer", "myRole", "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", "myName", attribute, request);
        verify(resolver, container, request);
    }

}
