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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ImportAttributeModel}.
 *
 * @version $Rev$ $Date$
 */
public class ImportAttributeModelTest {

    /**
     * The size of the attributes collection.
     */
    private static final int ATTRIBUTES_SIZE = 3;

    /**
     * The model to test.
     */
    private ImportAttributeModel model;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        model = new ImportAttributeModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, java.lang.Object[])}.
     */
    @Test
    public void testGetImportedAttributesSingle() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute();

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        expect(container.evaluate(attribute, requestItem)).andReturn("myEvaluatedValue");

        replay(container, attributeContext);
        Map<String, Object> attributes = model.getImportedAttributes(container, "myName", null, false, requestItem);
        assertEquals(1, attributes.size());
        assertEquals("myEvaluatedValue", attributes.get("myName"));
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, java.lang.Object[])}.
     */
    @Test
    public void testGetImportedAttributesSingleToName() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute();

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        expect(container.evaluate(attribute, requestItem)).andReturn("myEvaluatedValue");

        replay(container, attributeContext);
        Map<String, Object> attributes = model.getImportedAttributes(container,
                "myName", "myToName", false, requestItem);
        assertEquals(1, attributes.size());
        assertEquals("myEvaluatedValue", attributes.get("myToName"));
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, java.lang.Object[])}.
     */
    @Test
    public void testGetImportedAttributesAll() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute1 = new Attribute("myValue1");
        Attribute attribute2 = new Attribute("myValue2");
        Attribute attribute3 = new Attribute("myValue3");
        Set<String> cascadedNames = new HashSet<String>();
        cascadedNames.add("myName1");
        cascadedNames.add("myName2");
        Set<String> localNames = new HashSet<String>();
        localNames.add("myName1");
        localNames.add("myName3");

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(cascadedNames);
        expect(attributeContext.getLocalAttributeNames()).andReturn(localNames);
        expect(attributeContext.getAttribute("myName1")).andReturn(attribute1).times(2);
        expect(attributeContext.getAttribute("myName2")).andReturn(attribute2);
        expect(attributeContext.getAttribute("myName3")).andReturn(attribute3);
        expect(container.evaluate(attribute1, requestItem)).andReturn("myEvaluatedValue1").times(2);
        expect(container.evaluate(attribute2, requestItem)).andReturn("myEvaluatedValue2");
        expect(container.evaluate(attribute3, requestItem)).andReturn("myEvaluatedValue3");

        replay(container, attributeContext);
        Map<String, Object> attributes = model.getImportedAttributes(container, null, null, false, requestItem);
        assertEquals(ATTRIBUTES_SIZE, attributes.size());
        assertEquals("myEvaluatedValue1", attributes.get("myName1"));
        assertEquals("myEvaluatedValue2", attributes.get("myName2"));
        assertEquals("myEvaluatedValue3", attributes.get("myName3"));
        verify(container, attributeContext);
    }
}
