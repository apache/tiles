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


import org.apache.tiles.Attribute.AttributeType;

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
        def.setTemplate("/page1.jsp");
        def.put("attr1", "test.definition.name", AttributeType.DEFINITION,
                null);

        Attribute attr1 = def.getAttribute("attr1");
        assertNotNull("Null attribute.", attr1);
        assertTrue("Wrong attribute type",
                attr1.getType() == AttributeType.DEFINITION);
    }


}
