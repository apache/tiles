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
package org.apache.tiles.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link SimpleMenuItem}.
 *
 * @version $Rev$ $Date$
 */
public class SimpleMenuItemTest {

    /**
     * The item to test.
     */
    private SimpleMenuItem item;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        item = new SimpleMenuItem();
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#setValue(java.lang.String)}.
     */
    @Test
    public void testSetValue() {
        item.setValue("value");
        assertEquals("value", item.getValue());
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#setLink(java.lang.String)}.
     */
    @Test
    public void testSetLink() {
        item.setLink("value");
        assertEquals("value", item.getLink());
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#setIcon(java.lang.String)}.
     */
    @Test
    public void testSetIcon() {
        item.setIcon("value");
        assertEquals("value", item.getIcon());
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#setTooltip(java.lang.String)}.
     */
    @Test
    public void testSetTooltip() {
        item.setTooltip("value");
        assertEquals("value", item.getTooltip());
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#toString()}.
     */
    @Test
    public void testToString() {
        item.setIcon("icon");
        item.setLink("link");
        item.setTooltip("tooltip");
        item.setValue("value");
        assertEquals(
                "SimpleMenuItem[value=value, link=link, tooltip=tooltip, icon=icon, ]",
                item.toString());
    }

}
