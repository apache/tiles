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

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DefinitionModel}.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionModelTest {

    /**
     * The model to test.
     */
    private DefinitionModel model;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        model = new DefinitionModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefinitionModel
     * #start(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, Request)}.
     */
    @Test
    public void testStart() {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getRequestScope()).andReturn(requestScope);

        replay(request);
        model.start("myName", "myTemplate", "myRole", "myExtends", "myPreparer", request);
        assertEquals(1, composeStack.size());
        Definition definition = (Definition) composeStack.peek();
        assertEquals("myName", definition.getName());
        assertEquals("myTemplate", definition.getTemplateAttribute().getValue());
        assertEquals("myRole", definition.getTemplateAttribute().getRole());
        assertEquals("myPreparer", definition.getPreparer());
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefinitionModel
     * #end(org.apache.tiles.mgmt.MutableTilesContainer, Request)}.
     */
    @Test
    public void testEnd() {
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Request request = createMock(Request.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Definition definition = new Definition();
        composeStack.push(definition);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getRequestScope()).andReturn(requestScope);
        container.register(definition, request);

        replay(container, request);
        model.end(container, request);
        verify(container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefinitionModel
     * #end(org.apache.tiles.mgmt.MutableTilesContainer, Request)}.
     */
    @Test
    public void testEndInAttribute() {
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Request request = createMock(Request.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Attribute attribute = new Attribute();
        composeStack.push(attribute);
        Definition definition = new Definition();
        composeStack.push(definition);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getRequestScope()).andReturn(requestScope);
        container.register(definition, request);

        replay(container, request);
        model.end(container, request);
        assertEquals(1, composeStack.size());
        attribute = (Attribute) composeStack.peek();
        assertEquals(definition.getName(), attribute.getValue());
        assertEquals("definition", attribute.getRenderer());
        verify(container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefinitionModel
     * #execute(org.apache.tiles.mgmt.MutableTilesContainer, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, Request)}.
     */
    @Test
    public void testExecute() {
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Request request = createMock(Request.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Attribute attribute = new Attribute();
        composeStack.push(attribute);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getRequestScope()).andReturn(requestScope);
        container.register((Definition) notNull(), eq(request));

        replay(container, request);
        model.execute(container, "myName", "myTemplate", "myRole",
                "myExtends", "myPreparer", request);
        assertEquals(1, composeStack.size());
        attribute = (Attribute) composeStack.peek();
        assertEquals("definition", attribute.getRenderer());
        verify(container, request);
    }

}
