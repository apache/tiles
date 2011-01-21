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

import org.apache.tiles.ArrayStack;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
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
     * #start(ArrayStack, TilesContainer, boolean, String, String, Object, String,
     * String, String, Attribute, Object...)}.
     */
    @Test
    public void testStart() {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        Attribute attribute = new Attribute();
        AttributeContext attributeContext = createMock(AttributeContext.class);

        container.prepare("myPreparer", requestItem);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", requestItem)).andReturn(attribute);
        expect(container.startContext(requestItem)).andReturn(attributeContext);

        replay(resolver, container, attributeContext);
        model.start(composeStack, container, false, "myPreparer", "myRole", "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", "myName", attribute, requestItem);
        assertEquals(1, composeStack.size());
        assertEquals(attribute, composeStack.peek());
        verify(resolver, container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel
     * #end(ArrayStack, TilesContainer, boolean, Object...)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEnd() throws IOException {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Attribute attribute = new Attribute("myValue");
        composeStack.push(attribute);
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);

        container.endContext(requestItem);
        container.render(attribute, requestItem);

        replay(resolver, container);
        model.end(composeStack, container, false, requestItem);
        assertEquals(0, composeStack.size());
        verify(resolver, container);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel
     * #end(ArrayStack, TilesContainer, boolean, Object...)} when ignore flag is set.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEndIgnore() throws IOException {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Attribute attribute = null;
        composeStack.push(attribute);
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);

        container.endContext(requestItem);

        replay(resolver, container);
        model.end(composeStack, container, true, requestItem);
        assertEquals(0, composeStack.size());
        verify(resolver, container);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel
     * #execute(TilesContainer, boolean, String, String, Object, String, String,
     * String, Attribute, Object...)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        Attribute attribute = new Attribute("myValue");
        AttributeContext attributeContext = createMock(AttributeContext.class);

        container.prepare("myPreparer", requestItem);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", requestItem)).andReturn(attribute);
        expect(container.startContext(requestItem)).andReturn(attributeContext);
        container.endContext(requestItem);
        container.render(attribute, requestItem);

        replay(resolver, container);
        model.execute(container, false, "myPreparer", "myRole", "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", "myName", attribute, requestItem);
        verify(resolver, container);
    }

}
