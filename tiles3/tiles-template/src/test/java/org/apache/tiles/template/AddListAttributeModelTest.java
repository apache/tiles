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
 * Tests {@link AddListAttributeModel}.
 *
 * @version $Rev$ $Date$
 */
public class AddListAttributeModelTest {

    /**
     * The model to test.
     */
    private AddListAttributeModel model;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        model = new AddListAttributeModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.AddListAttributeModel#start(java.lang.String, Request)}.
     */
    @Test
    public void testStart() {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();

        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        expect(request.getContext("request")).andReturn(requestScope);

        replay(request);
        model.start("myRole", request);
        assertEquals(1, composeStack.size());
        ListAttribute listAttribute = (ListAttribute) composeStack.peek();
        assertEquals("myRole", listAttribute.getRole());
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.AddListAttributeModel#end(Request)}.
     */
    @Test
    public void testEnd() {
        ArrayStack<Object> composeStack = new ArrayStack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();

        requestScope.put(ComposeStackUtil.COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        expect(request.getContext("request")).andReturn(requestScope);

        replay(request);
        ListAttribute parent = new ListAttribute();
        composeStack.push(parent);
        composeStack.push(listAttribute);
        model.end(request);
        assertEquals(1, composeStack.size());
        assertEquals(parent, composeStack.peek());
        List<Attribute> attributes = parent.getValue();
        assertEquals(1, attributes.size());
        assertEquals(listAttribute, attributes.get(0));
        verify(request);
    }

}
