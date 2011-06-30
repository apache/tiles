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
package org.apache.tiles.jsp.taglib;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link UseAttributeTag}.
 *
 * @version $Rev$ $Date$
 */
public class UseAttributeTagTest {

    /**
     * The tag to test.
     */
    private UseAttributeTag tag;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        tag = new UseAttributeTag();
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.taglib.UseAttributeTag#execute(org.apache.tiles.request.Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testExecute() {
        Request request = createMock(Request.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Map<String, Object> scope = createMock(Map.class);
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = createMock(Attribute.class);

        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("name")).andReturn(attribute);
        expect(container.evaluate(attribute, request)).andReturn(new Integer(1));
        expect(request.getContext("scope")).andReturn(scope);
        Map<String, Object> toImport = new HashMap<String, Object>();
        toImport.put("id", new Integer(1));
        scope.putAll(toImport);

        replay(request, applicationContext, requestScope, container, attributeContext, attribute, scope);
        tag.setName("name");
        tag.setScope("scope");
        tag.setId("id");
        tag.setIgnore(false);
        tag.execute(request);
        verify(request, applicationContext, requestScope, container, attributeContext, attribute, scope);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.taglib.UseAttributeTag#setId(java.lang.String)}.
     */
    @Test
    public void testSetId() {
        tag.setId("id");
        assertEquals("id", tag.getId());
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.taglib.UseAttributeTag#getScope()}.
     */
    @Test
    public void testGetScope() {
        tag.setScope("scope");
        assertEquals("scope", tag.getScope());
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.taglib.UseAttributeTag#setName(java.lang.String)}.
     */
    @Test
    public void testSetName() {
        tag.setName("name");
        assertEquals("name", tag.getName());
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.taglib.UseAttributeTag#setIgnore(boolean)}.
     */
    @Test
    public void testSetIgnore() {
        tag.setIgnore(true);
        assertTrue(tag.isIgnore());
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.taglib.UseAttributeTag#setClassname(java.lang.String)}.
     */
    @Test
    public void testSetClassname() {
        tag.setClassname("classname");
        assertEquals("classname", tag.getClassname());
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.taglib.UseAttributeTag#getScriptingVariable()}.
     */
    @Test
    public void testGetScriptingVariable() {
        tag.setName("name");
        assertEquals("name", tag.getScriptingVariable());
        tag.setId("id");
        assertEquals("id", tag.getScriptingVariable());
    }

    /**
     * Tests {@link UseAttributeTag.Tei}.
     */
    @Test
    public void testTei() {
        TagData tagData = createMock(TagData.class);

        expect(tagData.getAttributeString("classname")).andReturn("my.Clazz");
        expect(tagData.getAttributeString("id")).andReturn("id");

        replay(tagData);
        UseAttributeTag.Tei tei = new UseAttributeTag.Tei();
        VariableInfo[] infos = tei.getVariableInfo(tagData);
        assertEquals(1, infos.length);
        VariableInfo info = infos[0];
        assertEquals("id", info.getVarName());
        assertEquals("my.Clazz", info.getClassName());
        assertTrue(info.getDeclare());
        assertEquals(VariableInfo.AT_END, info.getScope());
        verify(tagData);
    }

    /**
     * Tests {@link UseAttributeTag.Tei}.
     */
    @Test
    public void testTeiDefaults() {
        TagData tagData = createMock(TagData.class);

        expect(tagData.getAttributeString("classname")).andReturn(null);
        expect(tagData.getAttributeString("id")).andReturn(null);
        expect(tagData.getAttributeString("name")).andReturn("name");

        replay(tagData);
        UseAttributeTag.Tei tei = new UseAttributeTag.Tei();
        VariableInfo[] infos = tei.getVariableInfo(tagData);
        assertEquals(1, infos.length);
        VariableInfo info = infos[0];
        assertEquals("name", info.getVarName());
        assertEquals("java.lang.Object", info.getClassName());
        assertTrue(info.getDeclare());
        assertEquals(VariableInfo.AT_END, info.getScope());
        verify(tagData);
    }
}
