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
package org.apache.tiles.autotag.tool;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

/**
 * Tests {@link StringTool}.
 *
 * @version $Rev$ $Date$
 */
public class StringToolTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.tool.StringTool#splitOnNewlines(java.lang.String)}.
     */
    @Test
    public void testSplitOnNewlines() {
        StringTool tool = new StringTool();
        List<String> splitted = tool.splitOnNewlines("time\nto\nsplit");
        assertEquals(3, splitted.size());
        assertEquals("time", splitted.get(0));
        assertEquals("to", splitted.get(1));
        assertEquals("split", splitted.get(2));
        splitted = tool.splitOnNewlines(null);
        assertTrue(splitted.isEmpty());
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.tool.StringTool#capitalizeFirstLetter(java.lang.String)}.
     */
    @Test
    public void testCapitalizeFirstLetter() {
        StringTool tool = new StringTool();
        assertEquals("Whatever", tool.capitalizeFirstLetter("whatever"));
    }

    /**
     * Test method for {@link StringTool#getDefaultValue(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDefaultValue() {
        StringTool tool = new StringTool();
        assertEquals("0", tool.getDefaultValue("byte", null));
        assertEquals("1", tool.getDefaultValue("byte", "1"));
        assertEquals("null", tool.getDefaultValue("Whatever", null));
        assertEquals("thatsit", tool.getDefaultValue("Whatever", "thatsit"));
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.tool.StringTool#getClassToCast(java.lang.String)}.
     */
    @Test
    public void testGetClassToCast() {
        StringTool tool = new StringTool();
        assertEquals(Byte.class.getName(), tool.getClassToCast("byte"));
        assertEquals("Whatever", tool.getClassToCast("Whatever"));
    }

}
