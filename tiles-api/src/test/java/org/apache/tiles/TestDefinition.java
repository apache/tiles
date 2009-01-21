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


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the Definition class.
 *
 * @version $Rev$ $Date$
 */
public class TestDefinition extends TestCase {

    /**
     * Creates a new instance of TestDefinition.
     *
     * @param name The name of the test.
     */
    public TestDefinition(String name) {
        super(name);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.textui.TestRunner.main(
            new String[] { TestDefinition.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestDefinition.class);
    }

    /**
     * Verifies the put Attribute functionality.
     *
     * Attributes are added or replaced in the definition.
     */
    public void testPutAttribute() {
        Definition def = new Definition();
        def.setName("test1");
        def.setTemplateAttribute(Attribute
                .createTemplateAttribute("/page1.jsp"));
        Attribute attr1 = new Attribute("test.definition.name", null,
                null, "definition");
        def.putAttribute("attr1",  attr1);

        attr1 = def.getAttribute("attr1");
        assertNotNull("Null attribute.", attr1);
        assertTrue("Wrong attribute type", "definition".equals(attr1
                .getRenderer()));
    }

    /**
     * Tests the {@link Definition#inherit(Definition)} method.
     */
    public void testInherit() {
        Definition toCopy = new Definition();
        toCopy.putAttribute("name1", new Attribute("value1"), true);
        toCopy.putAttribute("name2", new Attribute("value2"), true);
        toCopy.putAttribute("name3", new Attribute("value3"), false);
        toCopy.putAttribute("name4", new Attribute("value4"), false);
        Definition context = new Definition();
        toCopy.putAttribute("name1", new Attribute("newValue1"), true);
        toCopy.putAttribute("name3", new Attribute("newValue3"), false);
        context.inherit(toCopy);
        Attribute attribute = context.getCascadedAttribute("name1");
        assertNotNull("Attribute name1 not found", attribute);
        assertEquals("Attribute name1 has not been set correctly", "newValue1",
                attribute.getValue());
        attribute = context.getCascadedAttribute("name2");
        assertNotNull("Attribute name2 not found", attribute);
        assertEquals("Attribute name2 has not been set correctly", "value2",
                attribute.getValue());
        attribute = context.getLocalAttribute("name3");
        assertNotNull("Attribute name3 not found", attribute);
        assertEquals("Attribute name3 has not been set correctly", "newValue3",
                attribute.getValue());
        attribute = context.getLocalAttribute("name4");
        assertNotNull("Attribute name4 not found", attribute);
        assertEquals("Attribute name4 has not been set correctly", "value4",
                attribute.getValue());

        toCopy = new Definition();
        toCopy.setPreparer("ExtendedPreparer");
        Attribute templateAttribute = new Attribute("extendedTemplate.jsp", "expression", "extendedRole", "template");
        toCopy.setTemplateAttribute(templateAttribute);
        context = new Definition();
        context.inherit(toCopy);
        assertEquals("Preparer not inherited", "ExtendedPreparer", context
                .getPreparer());
        assertNotNull("Roles not inherited", context.getTemplateAttribute()
                .getRoles());
        assertEquals("Roles not inherited", context.getTemplateAttribute()
                .getRoles().size(), 1);
        assertTrue("Roles not inherited", context.getTemplateAttribute()
                .getRoles().contains(
                "extendedRole"));
        assertEquals("Template not inherited", "extendedTemplate.jsp", context
                .getTemplateAttribute().getValue());
        assertEquals("Template expression not inherited", "expression", context
                .getTemplateAttribute().getExpression());
        context = new Definition();
        context.setPreparer("LocalPreparer");
        templateAttribute = new Attribute("localTemplate.jsp",
                "localExpression", "localRole", "template");
        context.setTemplateAttribute(templateAttribute);
        assertEquals("Preparer inherited", "LocalPreparer", context
                .getPreparer());
        assertNotNull("Roles not correct", context.getTemplateAttribute()
                .getRoles());
        assertEquals("Roles not correct", context.getTemplateAttribute()
                .getRoles().size(), 1);
        assertTrue("Roles inherited", context.getTemplateAttribute().getRoles()
                .contains("localRole"));
        assertEquals("Template inherited", "localTemplate.jsp", context
                .getTemplateAttribute().getValue());
        assertEquals("Template expression inherited", "localExpression",
                context.getTemplateAttribute().getExpression());
    }
}
