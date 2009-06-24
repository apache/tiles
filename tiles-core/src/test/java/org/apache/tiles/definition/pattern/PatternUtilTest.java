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

package org.apache.tiles.definition.pattern;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.junit.Test;

/**
 * Tests {@link PatternUtil}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class PatternUtilTest {

    /**
     * Test method for
     * {@link PatternUtil#replacePlaceholders(Definition, String, Object[])}.
     */
    @Test
    public void testReplacePlaceholders() {
        Map<String, Attribute> attributes = new HashMap<String, Attribute>();
        attributes.put("attrib1", new Attribute("value{2}"));
        attributes.put("attrib2", new Attribute("value{2}{3}"));
        Definition definition = new Definition("definitionName", new Attribute(
                "template{1}"), attributes);
        Definition nudef = PatternUtil.replacePlaceholders(definition, "nudef",
                "value0", "value1", "value2", "value3");
        assertEquals("nudef", nudef.getName());
        Attribute attribute = nudef.getTemplateAttribute();
        assertEquals("templatevalue1", attribute.getValue());
        attribute = nudef.getAttribute("attrib1");
        assertEquals("valuevalue2", attribute.getValue());
        attribute = nudef.getAttribute("attrib2");
        assertEquals("valuevalue2value3", attribute.getValue());
    }
}
