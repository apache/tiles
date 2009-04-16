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

import org.apache.tiles.AttributeContext;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link PutListAttributeModel}.
 *
 * @version $Rev$ $Date$
 */
public class PutListAttributeModelTest {

    /**
     * The model to test.
     */
    private PutListAttributeModel model;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        model = new PutListAttributeModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutListAttributeModel#start(Stack, String, boolean)}.
     */
    @Test
    public void testStart() {
        Stack<Object> composeStack = new Stack<Object>();
        model.start(composeStack, "myRole", false);
        assertEquals(1, composeStack.size());
        ListAttribute listAttribute = (ListAttribute) composeStack.peek();
        assertEquals("myRole", listAttribute.getRole());
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutListAttributeModel
     * #end(org.apache.tiles.TilesContainer, Stack, String, boolean, Object...)}.
     */
    @Test
    public void testEnd() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Stack<Object> composeStack = new Stack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        Integer requestItem = new Integer(1);
        composeStack.push(listAttribute);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        attributeContext.putAttribute("myName", listAttribute, false);

        replay(container, attributeContext);
        model.end(container, composeStack, "myName", false, requestItem);
        assertEquals(0, composeStack.size());
        verify(container, attributeContext);
    }

}
