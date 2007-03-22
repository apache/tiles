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
 *
 */

package org.apache.tiles.definition;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.tiles.definition.ComponentDefinition;

/**
 * Tests the ComponentDefinition class.
 *
 * @version $Rev$ $Date$
 */
public class TestComponentDefinition extends TestCase{
    
    /**
     * Creates a new instance of TestComponentDefinition
     *
     * @param name The name of the test.
     */
    public TestComponentDefinition(String name) {
        super(name);
    }
    
    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.textui.TestRunner.main(
            new String[] { TestComponentDefinition.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestComponentDefinition.class);
    }
    
    /**
     * Verifies the put Attribute functionality.
     *
     * Attributes are added or replaced in the component definition.
     */
    public void testPutAttribute() {
        ComponentDefinition def = new ComponentDefinition();
        def.setName("test1");
        def.setTemplate("/page1.jsp");
        def.put("attr1", new ComponentDefinition(), "definition", null);
        
        Object attr1 = def.getAttribute("attr1");
        assertNotNull("Null attribute.", attr1);
        assertTrue("Wrong attribute type", attr1 instanceof ComponentDefinition);
    }
    
    
}
