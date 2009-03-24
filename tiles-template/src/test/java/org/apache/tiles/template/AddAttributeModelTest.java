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

import java.util.List;
import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.ListAttribute;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AddAttributeModel}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
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
     * Test method for {@link org.apache.tiles.template.AddAttributeModel#start(java.util.Stack)}.
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
     * Test method for {@link org.apache.tiles.template.AddAttributeModel#end(java.util.Stack, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEnd() {
        Stack<Object> composeStack = new Stack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        Attribute attribute = new Attribute();
        composeStack.push(listAttribute);
        composeStack.push(attribute);
        
        model.end(composeStack, "myValue", "myExpression", "myBody", "myRole", "myType");
        assertEquals(1, ((List<Attribute>)listAttribute.getValue()).size());
        assertEquals("myValue", attribute.getValue());
        assertEquals("myExpression", attribute.getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());

        composeStack = new Stack<Object>();
        listAttribute = new ListAttribute();
        attribute = new Attribute();
        composeStack.push(listAttribute);
        composeStack.push(attribute);
        
        model.end(composeStack, null, "myExpression", "myBody", "myRole", "myType");
        assertEquals(1, ((List<Attribute>)listAttribute.getValue()).size());
        assertEquals("myBody", attribute.getValue());
        assertEquals("myExpression", attribute.getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());
    }

    /**
     * Test method for {@link org.apache.tiles.template.AddAttributeModel#execute(java.util.Stack, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testExecute() {
        Stack<Object> composeStack = new Stack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        Attribute attribute;
        composeStack.push(listAttribute);
        
        model.execute(composeStack, "myValue", "myExpression", "myBody", "myRole", "myType");
        List<Attribute> attributes = (List<Attribute>)listAttribute.getValue();
        assertEquals(1, attributes.size());
        attribute = attributes.iterator().next();
        assertEquals("myValue", attribute.getValue());
        assertEquals("myExpression", attribute.getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());

        composeStack = new Stack<Object>();
        listAttribute = new ListAttribute();
        attribute = new Attribute();
        composeStack.push(listAttribute);
        composeStack.push(attribute);
        
        model.execute(composeStack, null, "myExpression", "myBody", "myRole", "myType");
        attributes = (List<Attribute>)listAttribute.getValue();
        assertEquals(1, attributes.size());
        attribute = attributes.iterator().next();
        assertEquals("myBody", attribute.getValue());
        assertEquals("myExpression", attribute.getExpression());
        assertEquals("myRole", attribute.getRole());
        assertEquals("myType", attribute.getRenderer());
    }

}
