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

package org.apache.tiles;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests {@link ListAttribute}.
 *
 * @version $Rev$ $Date$
 */
public class ListAttributeTest {

    /**
     * The list size.
     */
    private static final int LIST_SIZE = 3;

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#hashCode()}.
     */
    @Test
    public void testHashCode() {
        ListAttribute attribute = new ListAttribute();
        List<Attribute> list = new ArrayList<Attribute>();
        list.add(new Attribute("value1"));
        list.add(new Attribute("value2"));
        attribute.setValue(list);
        attribute.setInherit(true);
        assertEquals(list.hashCode() + Boolean.TRUE.hashCode(), attribute.hashCode());
    }

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        ListAttribute attribute = new ListAttribute();
        List<Attribute> list = new ArrayList<Attribute>();
        list.add(new Attribute("value1"));
        list.add(new Attribute("value2"));
        attribute.setValue(list);
        attribute.setInherit(true);
        ListAttribute toCheck = new ListAttribute(attribute);
        assertTrue(attribute.equals(toCheck));
        toCheck = new ListAttribute(attribute);
        toCheck.setInherit(false);
        assertFalse(attribute.equals(toCheck));
        toCheck = new ListAttribute(attribute);
        toCheck.add(new Attribute("value3"));
        assertFalse(attribute.equals(toCheck));
    }

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#ListAttribute(java.util.List)}.
     */
    @Test
    public void testListAttributeListOfAttribute() {
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("value1"));
        attributes.add(new Attribute("value2"));
        ListAttribute attribute = new ListAttribute(attributes);
        assertEquals(attributes, attribute.getValue());
    }

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#ListAttribute(org.apache.tiles.ListAttribute)}.
     */
    @Test
    public void testListAttributeListAttribute() {
        ListAttribute attribute = new ListAttribute();
        List<Attribute> list = new ArrayList<Attribute>();
        list.add(new Attribute("value1"));
        list.add(new Attribute("value2"));
        list.add(null);
        attribute.setValue(list);
        attribute.setInherit(true);
        ListAttribute toCheck = new ListAttribute(attribute);
        assertEquals(attribute, toCheck);
    }

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#setValue(java.util.List)}.
     */
    @Test
    public void testSetValue() {
        ListAttribute attribute = new ListAttribute();
        List<Attribute> list = new ArrayList<Attribute>();
        list.add(new Attribute("value1"));
        list.add(new Attribute("value2"));
        attribute.setValue(list);
        assertEquals(list, attribute.getValue());
    }

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#add(org.apache.tiles.Attribute)}.
     */
    @Test
    public void testAdd() {
        List<Attribute> list = new ArrayList<Attribute>();
        Attribute attribute1 = new Attribute("value1");
        list.add(attribute1);
        Attribute attribute2 = new Attribute("value2");
        list.add(attribute2);
        ListAttribute attribute = new ListAttribute(list);
        Attribute attribute3 = new Attribute("value3");
        attribute.add(attribute3);
        list = attribute.getValue();
        assertEquals(LIST_SIZE, list.size());
        assertEquals(attribute1, list.get(0));
        assertEquals(attribute2, list.get(1));
        assertEquals(attribute3, list.get(2));
    }

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#setInherit(boolean)}.
     */
    @Test
    public void testSetInherit() {
        ListAttribute attribute = new ListAttribute();
        attribute.setInherit(true);
        assertTrue(attribute.isInherit());
        attribute.setInherit(false);
        assertFalse(attribute.isInherit());
    }

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#inherit(org.apache.tiles.ListAttribute)}.
     */
    @Test
    public void testInherit() {
        List<Attribute> list = new ArrayList<Attribute>();
        Attribute attribute1 = new Attribute("value1");
        list.add(attribute1);
        Attribute attribute2 = new Attribute("value2");
        list.add(attribute2);
        ListAttribute parent = new ListAttribute(list);
        Attribute attribute3 = new Attribute("value3");
        ListAttribute child = new ListAttribute();
        child.add(attribute3);
        child.inherit(parent);
        list = child.getValue();
        assertEquals(LIST_SIZE, list.size());
        assertEquals(attribute1, list.get(0));
        assertEquals(attribute2, list.get(1));
        assertEquals(attribute3, list.get(2));
    }

    /**
     * Test method for {@link org.apache.tiles.ListAttribute#clone()}.
     */
    @Test
    public void testClone() {
        ListAttribute attribute = new ListAttribute();
        List<Attribute> list = new ArrayList<Attribute>();
        list.add(new Attribute("value1"));
        list.add(new Attribute("value2"));
        attribute.setValue(list);
        attribute.setInherit(true);
        ListAttribute toCheck = attribute.clone();
        assertEquals(attribute, toCheck);
    }
}
