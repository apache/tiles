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
package org.apache.tiles.servlet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import servletunit.ServletConfigSimulator;

import org.apache.tiles.definition.MockComponentDefinitions;
import org.apache.tiles.definition.MockDefinitionsReader;
import org.apache.tiles.servlet.TilesServlet;
import org.apache.tiles.definition.DefinitionsFactory;

import javax.servlet.ServletException;

/**
 * Verifies the functionality of the TilesServlet
 *
 * @version $Rev$ $Date$
 */
public class TestTilesServlet extends TestCase {

    /**
     * Creates a new instance of TestTilesServlet
     */
    public TestTilesServlet(String name) {
        super(name);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        TestRunner.main(
                new String[]{TestTilesServlet.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestTilesServlet.class);
    }

    /**
     * Executes the servlet init() method.
     */
    public void testInitTilesServlet() {
        try {
            ServletConfigSimulator servletConfig = new ServletConfigSimulator();
            servletConfig.setInitParameter("definitions-config",
                    "org/apache/tiles/config/tiles-defs.xml");

            TilesServlet servlet = new TilesServlet();
            servlet.init(servletConfig);
        } catch (Exception e) {
            fail("Exception initializing servlet: " + e);
        }
    }

    /**
     * Executes the servlet init() method with a custom definitions reader and
     * a custom component definitions.
     */
    public void testCustomizedInitTilesServlet() throws ServletException {
        int readerInstanceCount = MockDefinitionsReader.getInstanceCount();
        int defsInstanceCount = MockComponentDefinitions.getInstanceCount();

        ServletConfigSimulator servletConfig = new ServletConfigSimulator();
        servletConfig.setInitParameter("definitions-config",
                "org/apache/tiles/config/tiles-defs.xml");
        servletConfig.setInitParameter(
                DefinitionsFactory.READER_IMPL_PROPERTY,
                "org.apache.tiles.definition.MockDefinitionsReader");

        TilesServlet servlet = new TilesServlet();
        servlet.init(servletConfig);

        assertEquals("MockDefinitionsReader not used.",
                readerInstanceCount + 1,
                MockDefinitionsReader.getInstanceCount());
    }
}
