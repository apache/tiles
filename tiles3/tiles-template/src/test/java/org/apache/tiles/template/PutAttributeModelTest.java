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

import org.apache.tiles.ArrayStack;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.Request;
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
     * Test method for {@link org.apache.tiles.template.PutAttributeModel#start(ArrayStack)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStart() {
        ArrayStack<Object> composeStack = createMock(ArrayStack.class);
        Attribute attribute = new Attribute();

        expect(composeStack.push(isA(Attribute.class))).andReturn(attribute);

        replay(composeStack);
        model.start(composeStack);
        verify(composeStack);
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutAttributeModel
     * #end(org.apache.tiles.TilesContainer, ArrayStack, String, Object, String,
     * String, String, String, boolean, Request)}.
     */
    @Test
    public void testEnd() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Attribute attribute = new Attribute();
        composeStack.push(attribute);

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        attributeContext.putAttribute("myName", attribute, false);

        replay(container, attributeContext, request);
        model.end(container, composeStack, "myName", "myValue", "myExpression",
                "myBody", "myRole", "myType", false, request);
        assertEquals("myValue", attribute.getValue());
        assertEquals("myExpression", attribute.getExpressionObject()
                .getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutAttributeModel
     * #end(org.apache.tiles.TilesContainer, ArrayStack, String, Object, String,
     * String, String, String, boolean, Request)}.
     */
    @Test
    public void testEndBody() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Attribute attribute = new Attribute();
        composeStack.push(attribute);

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        attributeContext.putAttribute("myName", attribute, false);

        replay(container, attributeContext, request);
        model.end(container, composeStack, "myName", "myValue", "myExpression",
                "myBody", "myRole", "myType", false, request);
        assertEquals("myValue", attribute.getValue());
        assertEquals("myExpression", attribute.getExpressionObject()
                .getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutAttributeModel
     * #execute(org.apache.tiles.TilesContainer, ArrayStack, String, Object, String,
     * String, String, String, boolean, Request)}.
     */
    @Test
    public void testExecuteListAttribute() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        composeStack.push(listAttribute);

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        attributeContext.putAttribute(eq("myName"), (Attribute) notNull(), eq(false));

        replay(container, attributeContext, request);
        model.execute(container, composeStack, "myName", "myValue",
                "myExpression", "myBody", "myRole", "myType", false,
                request);
        verify(container, attributeContext, request);
    }
}
