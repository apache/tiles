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

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.mgmt.MutableTilesContainer;
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
     * #start(java.util.Stack, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)}.
     */
    @Test
    public void testStart() {
        Stack<Object> composeStack = new Stack<Object>();
        model.start(composeStack, "myName", "myTemplate", "myRole", "myExtends", "myPreparer");
        assertEquals(1, composeStack.size());
        Definition definition = (Definition) composeStack.peek();
        assertEquals("myName", definition.getName());
        assertEquals("myTemplate", definition.getTemplateAttribute().getValue());
        assertEquals("myRole", definition.getTemplateAttribute().getRole());
        assertEquals("myPreparer", definition.getPreparer());
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefinitionModel
     * #end(org.apache.tiles.mgmt.MutableTilesContainer, java.util.Stack, java.lang.Object[])}.
     */
    @Test
    public void testEnd() {
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Stack<Object> composeStack = new Stack<Object>();
        Definition definition = new Definition();
        composeStack.push(definition);
        Integer requestItem = new Integer(1);

        container.register(definition, requestItem);

        replay(container);
        model.end(container, composeStack, requestItem);
        verify(container);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefinitionModel
     * #end(org.apache.tiles.mgmt.MutableTilesContainer, java.util.Stack, java.lang.Object[])}.
     */
    @Test
    public void testEndInAttribute() {
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Stack<Object> composeStack = new Stack<Object>();
        Attribute attribute = new Attribute();
        composeStack.push(attribute);
        Definition definition = new Definition();
        composeStack.push(definition);
        Integer requestItem = new Integer(1);

        container.register(definition, requestItem);

        replay(container);
        model.end(container, composeStack, requestItem);
        assertEquals(1, composeStack.size());
        attribute = (Attribute) composeStack.peek();
        assertEquals(definition.getName(), attribute.getValue());
        assertEquals("definition", attribute.getRenderer());
        verify(container);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefinitionModel
     * #execute(org.apache.tiles.mgmt.MutableTilesContainer, java.util.Stack,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testExecute() {
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Stack<Object> composeStack = new Stack<Object>();
        Attribute attribute = new Attribute();
        composeStack.push(attribute);
        Integer requestItem = new Integer(1);

        container.register((Definition) notNull(), eq(requestItem));

        replay(container);
        model.execute(container, composeStack, "myName", "myTemplate",
                "myRole", "myExtends", "myPreparer", requestItem);
        assertEquals(1, composeStack.size());
        attribute = (Attribute) composeStack.peek();
        assertEquals("definition", attribute.getRenderer());
        verify(container);
    }

}
