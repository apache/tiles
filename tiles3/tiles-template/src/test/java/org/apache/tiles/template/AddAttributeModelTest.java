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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.Attribute;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AddAttributeModel}.
 *
 * @version $Rev$ $Date$
 */
public class AddAttributeModelTest {

    /**
     * The model to test.
     */
    private AddAttributeModel model;

    /** Sets up the test. */
    @Before
    public void setUp() {
        model = new AddAttributeModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.AddAttributeModel#start(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStart() {
        ArrayStack<Object> composeStack = createMock(ArrayStack.class);
        Request request = createMock(Request.class);
        Attribute attribute = new Attribute();
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(composeStack.push(isA(Attribute.class))).andReturn(attribute);
        expect(request.getContext("request")).andReturn(requestScope);

        replay(composeStack, request);
        model.start(request);
        verify(composeStack, request);
    }

    /**
     * Test method for
     * {@link org.apache.tiles.template.AddAttributeModel
     * #end(java.lang.Object, java.lang.String, java.lang.String, java.lang.String, java.lang.String, Request)}
     * .
     */
    @Test
    public void testEnd() {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Request request = createMock(Request.class);
        ListAttribute listAttribute = new ListAttribute();
        Attribute attribute = new Attribute();
        composeStack.push(listAttribute);
        composeStack.push(attribute);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getContext("request")).andReturn(requestScope).times(2);

        replay(request);
        model.end("myValue", "myExpression", "myBody", "myRole", "myType",
                request);
        assertEquals(1, listAttribute.getValue().size());
        assertEquals("myValue", attribute.getValue());
        assertEquals("myExpression", attribute.getExpressionObject()
                .getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());

        composeStack.clear();
        listAttribute = new ListAttribute();
        attribute = new Attribute();
        composeStack.push(listAttribute);
        composeStack.push(attribute);

        model.end(null, "myExpression", "myBody", "myRole", "myType",
                request);
        assertEquals(1, listAttribute.getValue().size());
        assertEquals("myBody", attribute.getValue());
        assertEquals("myExpression", attribute.getExpressionObject()
                .getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.AddAttributeModel
     * #execute(java.lang.Object, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, Request)}.
     */
    @Test
    public void testExecute() {
        Request request = createMock(Request.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        Attribute attribute;
        composeStack.push(listAttribute);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getContext("request")).andReturn(requestScope).times(2);

        replay(request);
        model.execute("myValue", "myExpression", "myBody", "myRole",
                "myType", request);
        List<Attribute> attributes = listAttribute.getValue();
        assertEquals(1, attributes.size());
        attribute = attributes.iterator().next();
        assertEquals("myValue", attribute.getValue());
        assertEquals("myExpression", attribute.getExpressionObject().getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());

        composeStack.clear();
        listAttribute = new ListAttribute();
        attribute = new Attribute();
        composeStack.push(listAttribute);
        composeStack.push(attribute);

        model.execute(null, "myExpression", "myBody", "myRole", "myType",
                request);
        attributes = listAttribute.getValue();
        assertEquals(1, attributes.size());
        attribute = attributes.iterator().next();
        assertEquals("myBody", attribute.getValue());
        assertEquals("myExpression", attribute.getExpressionObject()
                .getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());
        verify(request);
    }

}
