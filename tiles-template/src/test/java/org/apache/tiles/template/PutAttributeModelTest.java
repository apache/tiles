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

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link PutAttributeModel}.
 *
 * @version $Rev$ $Date$
 */
public class PutAttributeModelTest {

    /**
     * The model to test.
     */
    private PutAttributeModel model;

    /** Sets up the test. */
    @Before
    public void setUp() {
        model = new PutAttributeModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutAttributeModel#start(Stack)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStart() {
        Stack<Object> composeStack = createMock(Stack.class);
        Attribute attribute = new Attribute();

        expect(composeStack.push(isA(Attribute.class))).andReturn(attribute);

        replay(composeStack);
        model.start(composeStack);
        verify(composeStack);
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutAttributeModel
     * #end(org.apache.tiles.TilesContainer, Stack, String, Object, String,
     * String, String, String, boolean, Object...)}.
     */
    @Test
    public void testEnd() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Stack<Object> composeStack = new Stack<Object>();
        Attribute attribute = new Attribute();
        Integer requestItem = new Integer(1);
        composeStack.push(attribute);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        attributeContext.putAttribute("myName", attribute, false);

        replay(container, attributeContext);
        model.end(container, composeStack, "myName", "myValue", "myExpression",
                "myBody", "myRole", "myType", false, requestItem);
        assertEquals("myValue", attribute.getValue());
        assertEquals("myExpression", attribute.getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutAttributeModel
     * #end(org.apache.tiles.TilesContainer, Stack, String, Object, String,
     * String, String, String, boolean, Object...)}.
     */
    @Test
    public void testEndBody() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Stack<Object> composeStack = new Stack<Object>();
        Attribute attribute = new Attribute();
        Integer requestItem = new Integer(1);
        composeStack.push(attribute);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        attributeContext.putAttribute("myName", attribute, false);

        replay(container, attributeContext);
        model.end(container, composeStack, "myName", "myValue", "myExpression",
                "myBody", "myRole", "myType", false, requestItem);
        assertEquals("myValue", attribute.getValue());
        assertEquals("myExpression", attribute.getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutAttributeModel
     * #execute(org.apache.tiles.TilesContainer, Stack, String, Object, String,
     * String, String, String, boolean, Object...)}.
     */
    @Test
    public void testExecuteListAttribute() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Stack<Object> composeStack = new Stack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        Integer requestItem = new Integer(1);
        composeStack.push(listAttribute);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        attributeContext.putAttribute(eq("myName"), (Attribute) notNull(), eq(false));

        replay(container, attributeContext);
        model.execute(container, composeStack, "myName", "myValue",
                "myExpression", "myBody", "myRole", "myType", false,
                requestItem);
        verify(container, attributeContext);
    }
}
