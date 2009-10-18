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

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.Expression;
import org.apache.tiles.TilesContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DefaultAttributeResolver}.
 *
 * @version $Rev$ $Date$
 */
public class DefaultAttributeResolverTest {

    /**
     * The resolver to test.
     */
    private DefaultAttributeResolver resolver;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        resolver = new DefaultAttributeResolver();
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver
     * #computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute,
     * java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String,
     * java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testComputeAttributeInContext() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute("myValue", Expression
                .createExpression("myExpression", null), "myRole", "myRenderer");
        Integer requestItem = new Integer(1);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);

        replay(container, attributeContext);
        assertEquals(attribute, resolver.computeAttribute(container, null,
                "myName", null, false, null, null, null, requestItem));
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver
     * #computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute,
     * java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String,
     * java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testComputeAttributeInCall() {
        TilesContainer container = createMock(TilesContainer.class);
        Attribute attribute = new Attribute("myValue", Expression
                .createExpression("myExpression", null), "myRole", "myRenderer");
        Integer requestItem = new Integer(1);

        replay(container);
        assertEquals(attribute, resolver.computeAttribute(container, attribute,
                null, null, false, null, null, null, requestItem));
        verify(container);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver
     * #computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute,
     * java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String,
     * java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testComputeAttributeDefault() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Integer requestItem = new Integer(1);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(null);

        replay(container, attributeContext);
        Attribute attribute = resolver.computeAttribute(container, null,
                "myName", null, false, "defaultValue", "defaultRole",
                "defaultType", requestItem);
        assertEquals("defaultValue", attribute.getValue());
        assertEquals("defaultRole", attribute.getRole());
        assertEquals("defaultType", attribute.getRenderer());
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver
     * #computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute,
     * java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String,
     * java.lang.String, java.lang.Object[])}.
     */
    @Test(expected = NoSuchAttributeException.class)
    public void testComputeAttributeException() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Integer requestItem = new Integer(1);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(null);

        replay(container, attributeContext);
        resolver.computeAttribute(container, null, "myName", null, false, null,
                "defaultRole", "defaultType", requestItem);
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver
     * #computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute,
     * java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String,
     * java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testComputeAttributeIgnore() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Integer requestItem = new Integer(1);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(null);

        replay(container, attributeContext);
        assertNull(resolver.computeAttribute(container, null, "myName", null, true, null,
                "defaultRole", "defaultType", requestItem));
        verify(container, attributeContext);
    }
}
