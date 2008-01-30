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

package org.apache.tiles.definition;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.Attribute.AttributeType;

/**
 * Tests the DefinitionsImpl class.
 *
 * @version $Rev$ $Date$
 */
public class TestDefinitions extends TestCase {

    /**
     * Creates a new instance of TestDefinitions.
     *
     * @param name The name of the test.
     */
    public TestDefinitions(String name) {
        super(name);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.textui.TestRunner.main(
            new String[] { TestDefinitions.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestDefinitions.class);
    }

    /**
     * Tests the inheritance properties of Definition objects.
     */
    public void testResolveInheritances() {
        Map<String, Definition> defs = new HashMap<String, Definition>();

        Definition def = new Definition();
        def.setName("parent.def1");
        def.setTemplate("/test1.jsp");
        Attribute attr = new Attribute();
        attr.setValue("value1");
        def.putAttribute("attr1", attr);
        attr = new Attribute();
        attr.setValue("tiles.def1");
        // No type set
        def.putAttribute("attr2", attr);
        defs.put(def.getName(), def);
        attr = new Attribute();
        attr.setValue("tiles.def1");
        attr.setType(AttributeType.STRING);
        def.putAttribute("attr3", attr);
        defs.put(def.getName(), def);

        def = new Definition();
        def.setName("tiles.def1");
        def.setTemplate("/test2.jsp");
        defs.put(def.getName(), def);

        def = new Definition();
        def.setName("child.def1");
        def.setExtends("parent.def1");
        attr = new Attribute();
        attr.setValue("New value");
        def.putAttribute("attr1", attr);
        defs.put(def.getName(), def);

        Definitions definitions = new DefinitionsImpl();
        try {
            definitions.addDefinitions(defs);
        } catch (NoSuchDefinitionException e) {
            fail("Test failure: " + e);
        }

        def = definitions.getDefinition("parent.def1");

        assertNotNull("Couldn't get parent.", def);
        assertEquals("Incorrect template value.", "/test1.jsp", def
                .getTemplate());
        assertEquals("Incorrect attr1 value", "value1", def
                .getAttribute("attr1").getValue());

        attr = def.getAttributes().get("attr1");
        assertNotNull("Dependent attribute not found.", attr);
        attr = def.getAttributes().get("attr2");
        assertNotNull("Dependent attribute not found.", attr);
        attr = def.getAttributes().get("attr3");
        assertNotNull("Dependent attribute not found.", attr);
        assertTrue("The attribute 'attr3' should be of type STRING", attr
                .getType() == AttributeType.STRING);

        def = definitions.getDefinition("child.def1");

        assertNotNull("Couldn't get child.",
                definitions.getDefinition("child.def1"));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("child.def1").getTemplate());
        assertEquals("Incorrect attr1 value", "New value", definitions
                .getDefinition("child.def1").getAttribute("attr1").getValue());

        attr = def.getAttributes().get("attr1");
        assertNotNull("Dependent attribute not found.", attr);
        attr = def.getAttributes().get("attr2");
        assertNotNull("Dependent attribute not found.", attr);
        attr = def.getAttributes().get("attr3");
        assertNotNull("Dependent attribute not found.", attr);
        assertTrue("The attribute 'attr3' should be of type STRING", attr
                .getType() == AttributeType.STRING);
    }

    /**
     * Tests the inheritance with localized definitions.
     */
    public void testLocalizedResolveInheritances() {
        Map<String, Definition> defs = new HashMap<String, Definition>();
        Definition def = new Definition();
        def.setName("parent.def1");
        def.setTemplate("/test1.jsp");
        Attribute attr = new Attribute();
        attr.setValue("value1");
        def.putAttribute("attr1", attr);
        defs.put(def.getName(), def);

        def = new Definition();
        def.setName("child.def1");
        def.setExtends("parent.def1");
        attr = new Attribute();
        attr.setValue("New value");
        def.putAttribute("attr1", attr);
        defs.put(def.getName(), def);

        Map<String, Definition> localDefs = new HashMap<String, Definition>();
        def = new Definition();
        def.setName("child.def1");
        def.setExtends("parent.def1");
        attr = new Attribute();
        attr.setValue("US Value");
        def.putAttribute("attr1", attr);
        localDefs.put(def.getName(), def);

        Definitions definitions = new DefinitionsImpl();
        try {
            definitions.addDefinitions(defs);
            definitions.addDefinitions(localDefs, Locale.US);
        } catch (NoSuchDefinitionException e) {
            fail("Test failure: " + e);
        }

        assertNotNull("Couldn't get parent.",
                definitions.getDefinition("parent.def1"));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("parent.def1").getTemplate());
        assertEquals("Incorrect attr1 value", "value1", definitions
                .getDefinition("parent.def1").getAttribute("attr1").getValue());

        assertNotNull("Couldn't get child.",
                definitions.getDefinition("child.def1"));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("child.def1").getTemplate());
        assertEquals("Incorrect attr1 value", "New value", definitions
                .getDefinition("child.def1").getAttribute("attr1").getValue());

        assertNotNull("Couldn't get parent.",
                definitions.getDefinition("parent.def1", Locale.US));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("parent.def1", Locale.US).getTemplate());
        assertEquals("Incorrect attr1 value", "value1", definitions
                .getDefinition("parent.def1", Locale.US).getAttribute("attr1")
                .getValue());

        assertNotNull("Couldn't get child.",
                definitions.getDefinition("child.def1", Locale.US));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("child.def1", Locale.US).getTemplate());
        assertEquals("Incorrect attr1 value", "US Value", definitions
                .getDefinition("child.def1", Locale.US).getAttribute("attr1")
                .getValue());
    }

    /**
     * Tests the reset method.
     */
    public void testReset() {
        Map<String, Definition> defs = new HashMap<String, Definition>();

        Definition def = new Definition();
        def.setName("parent.def1");
        def.setTemplate("/test1.jsp");
        Attribute attr = new Attribute();
        attr.setValue("value1");
        def.putAttribute("attr1", attr);
        defs.put(def.getName(), def);

        def = new Definition();
        def.setName("child.def1");
        def.setExtends("parent.def1");
        attr = new Attribute();
        attr.setValue("New value");
        def.putAttribute("attr1", attr);
        defs.put(def.getName(), def);

        Definitions definitions = new DefinitionsImpl();
        try {
            definitions.addDefinitions(defs);
        } catch (NoSuchDefinitionException e) {
            fail("Test failure: " + e);
        }

        assertNotNull("Couldn't get parent.",
                definitions.getDefinition("parent.def1"));

        definitions.reset();
        assertNull("Definitions should be null.",
                definitions.getDefinition("parent.def1"));
    }

    /**
     * Verifies that attribute dependencies are resolved.
     *
     * A definition can have an attribute that points to another definition.
     * This test verifies that the <code>resolveAttributes</code> method is
     * executed and attribute dependencies are calculated.
     */
    public void testResolveAttributeDependencies() {
        Map<String, Definition> defs = new HashMap<String, Definition>();

        Definition def = new Definition();
        def.setName("parent.def1");
        def.setTemplate("/test1.jsp");
        Attribute attr = new Attribute();
        attr.setValue("tiles.def2");
        attr.setType(AttributeType.DEFINITION);
        def.putAttribute("attr1", attr);
        defs.put(def.getName(), def);

        def = new Definition();
        def.setName("parent.notype.def1");
        def.setTemplate("/test1.jsp");
        attr = new Attribute();
        attr.setValue("tiles.def2");
        // Don't set the type
        def.putAttribute("attr1", attr);
        defs.put(def.getName(), def);

        def = new Definition();
        def.setName("tiles.def2");
        defs.put(def.getName(), def);

        Definitions definitions = new DefinitionsImpl();
        try {
            definitions.addDefinitions(defs);
        } catch (NoSuchDefinitionException e) {
            fail("Test failure: " + e);
        }

        defs = new HashMap<String, Definition>(defs);
        def = new Definition();
        def.setName("parent.def2");
        def.setTemplate("/test1.jsp");
        attr = new Attribute();
        attr.setValue("tiles.def3");
        def.putAttribute("attr1", attr);
        defs.put(def.getName(), def);
        def = new Definition();
        def.setName("tiles.def3");
        defs.put(def.getName(), def);

        try {
            definitions.addDefinitions(defs, Locale.ITALIAN);
        } catch (NoSuchDefinitionException e) {
            fail("Test failure: " + e);
        }

        Definition newDef = definitions.getDefinition("parent.def1");
        assertNotNull("Parent definition not found.", newDef);

        Object newAttr = newDef.getAttribute("attr1").getValue();
        assertNotNull("Dependent attribute not found.", newAttr);

        newDef = definitions.getDefinition("parent.notype.def1");
        assertNotNull("Parent definition not found.", newDef);

        newAttr = newDef.getAttribute("attr1").getValue();
        assertNotNull("Dependent attribute not found.", newAttr);

        assertEquals("Incorrect dependent attribute name.", "tiles.def2",
                newAttr);

        // Part of the test for locale-specific definitions.
        newDef = definitions.getDefinition("parent.def1", Locale.ITALIAN);
        assertNotNull("Parent definition not found.", newDef);

        newAttr = newDef.getAttribute("attr1").getValue();
        assertNotNull("Dependent attribute not found.", newAttr);

        newDef = definitions.getDefinition("parent.notype.def1",
                Locale.ITALIAN);
        assertNotNull("Parent definition not found.", newDef);

        newAttr = newDef.getAttribute("attr1").getValue();
        assertNotNull("Dependent attribute not found.", newAttr);

        assertEquals("Incorrect dependent attribute name.", "tiles.def2",
                newAttr);

        newDef = definitions.getDefinition("parent.def2", Locale.ITALIAN);
        assertNotNull("Parent definition not found.", newDef);

        attr = newDef.getAttributes().get("attr1");
        assertNotNull("Dependent attribute not found.", attr);
    }
}
