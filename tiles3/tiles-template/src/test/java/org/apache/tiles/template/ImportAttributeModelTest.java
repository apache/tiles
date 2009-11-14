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
import org.apache.tiles.request.Request;
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
     * java.lang.String, boolean, Request)}.
     */
    @Test
    public void testGetImportedAttributesSingle() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute();
        Request request = createMock(Request.class);

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        expect(container.evaluate(attribute, request)).andReturn("myEvaluatedValue");

        replay(container, attributeContext, request);
        Map<String, Object> attributes = model.getImportedAttributes(container, "myName", null, false, request);
        assertEquals(1, attributes.size());
        assertEquals("myEvaluatedValue", attributes.get("myName"));
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, Request)}.
     */
    @Test
    public void testGetImportedAttributesSingleToName() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute();

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        expect(container.evaluate(attribute, request)).andReturn("myEvaluatedValue");

        replay(container, attributeContext, request);
        Map<String, Object> attributes = model.getImportedAttributes(container,
                "myName", "myToName", false, request);
        assertEquals(1, attributes.size());
        assertEquals("myEvaluatedValue", attributes.get("myToName"));
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, Request)}.
     */
    @Test
    public void testGetImportedAttributesAll() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
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

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(cascadedNames);
        expect(attributeContext.getLocalAttributeNames()).andReturn(localNames);
        expect(attributeContext.getAttribute("myName1")).andReturn(attribute1).times(2);
        expect(attributeContext.getAttribute("myName2")).andReturn(attribute2);
        expect(attributeContext.getAttribute("myName3")).andReturn(attribute3);
        expect(container.evaluate(attribute1, request)).andReturn("myEvaluatedValue1").times(2);
        expect(container.evaluate(attribute2, request)).andReturn("myEvaluatedValue2");
        expect(container.evaluate(attribute3, request)).andReturn("myEvaluatedValue3");

        replay(container, attributeContext, request);
        Map<String, Object> attributes = model.getImportedAttributes(container, null, null, false, request);
        assertEquals(ATTRIBUTES_SIZE, attributes.size());
        assertEquals("myEvaluatedValue1", attributes.get("myName1"));
        assertEquals("myEvaluatedValue2", attributes.get("myName2"));
        assertEquals("myEvaluatedValue3", attributes.get("myName3"));
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, Request)}.
     */
    @Test(expected = NoSuchAttributeException.class)
    public void testGetImportedAttributesSingleNullAttributeException() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(null);

        replay(container, attributeContext, request);
        try {
            model.getImportedAttributes(container, "myName", null, false, request);
        } finally {
            verify(container, attributeContext, request);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, Request)}.
     */
    @Test(expected = NoSuchAttributeException.class)
    public void testGetImportedAttributesSingleNullAttributeValueException() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute();

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        expect(container.evaluate(attribute, request)).andReturn(null);

        replay(container, attributeContext, request);
        try {
            model.getImportedAttributes(container, "myName", null, false, request);
        } finally {
            verify(container, attributeContext, request);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, Request)}.
     */
    @Test(expected = RuntimeException.class)
    public void testGetImportedAttributesSingleRuntimeException() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute();

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        expect(container.evaluate(attribute, request)).andThrow(new RuntimeException());

        replay(container, attributeContext, request);
        try {
            model.getImportedAttributes(container, "myName", null, false, request);
        } finally {
            verify(container, attributeContext, request);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, Request)}.
     */
    @Test
    public void testGetImportedAttributesSingleNullAttributeIgnore() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(null);

        replay(container, attributeContext, request);
        model.getImportedAttributes(container, "myName", null, true, request);
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, Request)}.
     */
    @Test
    public void testGetImportedAttributesSingleNullAttributeValueIgnore() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute();

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        expect(container.evaluate(attribute, request)).andReturn(null);

        replay(container, attributeContext, request);
        model.getImportedAttributes(container, "myName", null, true, request);
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.ImportAttributeModel
     * #getImportedAttributes(org.apache.tiles.TilesContainer, java.lang.String,
     * java.lang.String, boolean, Request)}.
     */
    @Test
    public void testGetImportedAttributesSingleRuntimeIgnore() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute();

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        expect(container.evaluate(attribute, request)).andThrow(new RuntimeException());

        replay(container, attributeContext, request);
        model.getImportedAttributes(container, "myName", null, true, request);
        verify(container, attributeContext, request);
    }
}
