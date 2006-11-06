/*
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
 *
 */

package org.apache.tiles.definition;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.tiles.definition.ComponentDefinitionsImpl;
import org.apache.tiles.definition.ComponentDefinitions;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.ComponentAttribute;

/**
 * Tests the ComponentDefinitionsImpl class.
 *
 * @version $Rev$ $Date$ 
 */
public class TestComponentDefinitions extends TestCase {
    
    /** Creates a new instance of TestComponentDefinitions */
    public TestComponentDefinitions(String name) {
        super(name);
    }
    
    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.textui.TestRunner.main(
            new String[] { TestComponentDefinitions.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestComponentDefinitions.class);
    }

    /**
     * Tests the inheritance properties of ComponentDefinition objects.
     */
    public void testResolveInheritances() {
        Map defs = new HashMap();
        
        ComponentDefinition def = new ComponentDefinition();
        def.setName("parent.def1");
        def.setTemplate("/test1.jsp");
        ComponentAttribute attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("value1");
        def.addAttribute(attr);
        defs.put(def.getName(), def);
        
        def = new ComponentDefinition();
        def.setName("child.def1");
        def.setExtends("parent.def1");
        attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("New value");
        def.addAttribute(attr);
        defs.put(def.getName(), def);
        
        ComponentDefinitions definitions = new ComponentDefinitionsImpl();
        try {
            definitions.addDefinitions(defs);
        } catch (NoSuchDefinitionException e) {
            fail("Test failure: " + e);
        }
        
        assertNotNull("Couldn't get parent.", 
                definitions.getDefinition("parent.def1"));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("parent.def1").getTemplate());
        assertEquals("Incorrect attr1 value", "value1",
                definitions.getDefinition("parent.def1").getAttribute("attr1"));
        
        assertNotNull("Couldn't get child.", 
                definitions.getDefinition("child.def1"));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("child.def1").getTemplate());
        assertEquals("Incorrect attr1 value", "New value",
                definitions.getDefinition("child.def1").getAttribute("attr1"));
    }
    
    /**
     * Tests the inheritance with localized definitions.
     */
    public void testLocalizedResolveInheritances() {
        Map defs = new HashMap();
        ComponentDefinition def = new ComponentDefinition();
        def.setName("parent.def1");
        def.setTemplate("/test1.jsp");
        ComponentAttribute attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("value1");
        def.addAttribute(attr);
        defs.put(def.getName(), def);
        
        def = new ComponentDefinition();
        def.setName("child.def1");
        def.setExtends("parent.def1");
        attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("New value");
        def.addAttribute(attr);
        defs.put(def.getName(), def);

        Map localDefs = new HashMap();
        def = new ComponentDefinition();
        def.setName("child.def1");
        def.setExtends("parent.def1");
        attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("US Value");
        def.addAttribute(attr);
        localDefs.put(def.getName(), def);

        ComponentDefinitions definitions = new ComponentDefinitionsImpl();
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
        assertEquals("Incorrect attr1 value", "value1",
                definitions.getDefinition("parent.def1").getAttribute("attr1"));
        
        assertNotNull("Couldn't get child.", 
                definitions.getDefinition("child.def1"));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("child.def1").getTemplate());
        assertEquals("Incorrect attr1 value", "New value",
                definitions.getDefinition("child.def1").getAttribute("attr1"));
        
        assertNotNull("Couldn't get parent.", 
                definitions.getDefinition("parent.def1", Locale.US));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("parent.def1", Locale.US).getTemplate());
        assertEquals("Incorrect attr1 value", "value1",
                definitions.getDefinition("parent.def1", Locale.US).getAttribute("attr1"));
        
        assertNotNull("Couldn't get child.", 
                definitions.getDefinition("child.def1", Locale.US));
        assertEquals("Incorrect template value." , "/test1.jsp",
                definitions.getDefinition("child.def1", Locale.US).getTemplate());
        assertEquals("Incorrect attr1 value", "US Value",
                definitions.getDefinition("child.def1", Locale.US).getAttribute("attr1"));
    }
    
    /**
     * Tests the reset method.
     */
    public void testReset() {
        Map defs = new HashMap();
        
        ComponentDefinition def = new ComponentDefinition();
        def.setName("parent.def1");
        def.setTemplate("/test1.jsp");
        ComponentAttribute attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("value1");
        def.addAttribute(attr);
        defs.put(def.getName(), def);
        
        def = new ComponentDefinition();
        def.setName("child.def1");
        def.setExtends("parent.def1");
        attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("New value");
        def.addAttribute(attr);
        defs.put(def.getName(), def);
        
        ComponentDefinitions definitions = new ComponentDefinitionsImpl();
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
     * A Component (tile) can have an attribute that points to another component.
     * This test verifies that the <code>resolveAttributes</code> method is
     * executed and attribute dependencies are calculated.
     */
    public void testResolveAttributeDependencies() {
        Map defs = new HashMap();
        
        ComponentDefinition def = new ComponentDefinition();
        def.setName("parent.def1");
        def.setTemplate("/test1.jsp");
        ComponentAttribute attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("tiles.def2");
        attr.setType("definition");
        def.addAttribute(attr);
        defs.put(def.getName(), def);
        
        def = new ComponentDefinition();
        def.setName("parent.notype.def1");
        def.setTemplate("/test1.jsp");
        attr = new ComponentAttribute();
        attr.setName("attr1");
        attr.setValue("tiles.def2");
        // Don't set the type
        def.addAttribute(attr);
        defs.put(def.getName(), def);
        
        def = new ComponentDefinition();
        def.setName("tiles.def2");
        defs.put(def.getName(), def);
        
        ComponentDefinitions definitions = new ComponentDefinitionsImpl();
        try {
            definitions.addDefinitions(defs);
            definitions.addDefinitions(defs, Locale.ITALIAN);
        } catch (NoSuchDefinitionException e) {
            fail("Test failure: " + e);
        }
        
        ComponentDefinition newDef = definitions.getDefinition("parent.def1");
        assertNotNull("Parent definition not found.", newDef);
        
        Object newAttr = newDef.getAttribute("attr1");
        assertNotNull("Dependent attribute not found.", newAttr);
        assertTrue("Dependent attribute incorrect type.", 
                newAttr instanceof ComponentDefinition);
        
        newDef = definitions.getDefinition("parent.notype.def1");
        assertNotNull("Parent definition not found.", newDef);
        
        newAttr = newDef.getAttribute("attr1");
        assertNotNull("Dependent attribute not found.", newAttr);
        assertTrue("Dependent attribute incorrect type.", 
                newAttr instanceof ComponentDefinition);
        
        assertEquals("Incorrect dependent attribute name.", "tiles.def2",
                ((ComponentDefinition) newAttr).getName());
        
        // Part of the test for locale-specific definitions.
        newDef = definitions.getDefinition("parent.def1", Locale.ITALIAN);
        assertNotNull("Parent definition not found.", newDef);
        
        newAttr = newDef.getAttribute("attr1");
        assertNotNull("Dependent attribute not found.", newAttr);
        assertTrue("Dependent attribute incorrect type.", 
                newAttr instanceof ComponentDefinition);
        
        newDef = definitions.getDefinition("parent.notype.def1",
        		Locale.ITALIAN);
        assertNotNull("Parent definition not found.", newDef);
        
        newAttr = newDef.getAttribute("attr1");
        assertNotNull("Dependent attribute not found.", newAttr);
        assertTrue("Dependent attribute incorrect type.", 
                newAttr instanceof ComponentDefinition);
        
        assertEquals("Incorrect dependent attribute name.", "tiles.def2",
                ((ComponentDefinition) newAttr).getName());
    }
}
