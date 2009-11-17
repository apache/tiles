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

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.Request;
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
     * Test method for {@link org.apache.tiles.template.PutListAttributeModel#start(String, boolean, Request)}.
     */
    @Test
    public void testStart() {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Request request = createMock(Request.class);
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getRequestScope()).andReturn(requestScope);

        replay(request);
        model.start("myRole", false, request);
        assertEquals(1, composeStack.size());
        ListAttribute listAttribute = (ListAttribute) composeStack.peek();
        assertEquals("myRole", listAttribute.getRole());
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutListAttributeModel
     * #end(org.apache.tiles.TilesContainer, String, boolean, Request)}.
     */
    @Test
    public void testEnd() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Request request = createMock(Request.class);
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        composeStack.push(listAttribute);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);

        expect(request.getRequestScope()).andReturn(requestScope);
        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        attributeContext.putAttribute("myName", listAttribute, false);

        replay(container, attributeContext, request);
        model.end(container, "myName", false, request);
        assertEquals(0, composeStack.size());
        verify(container, attributeContext, request);
    }

}
