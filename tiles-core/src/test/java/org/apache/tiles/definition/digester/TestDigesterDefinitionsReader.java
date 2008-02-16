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

package org.apache.tiles.definition.digester;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.context.ListAttribute;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;

/**
 * Tests the <code>org.apache.tiles.definition.digester.DigesterDefinitionsReader</code> class.
 *
 * @version $Rev$ $Date$
 */
public class TestDigesterDefinitionsReader extends TestCase {

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory
            .getLog(TestDigesterDefinitionsReader.class);

    /**
     * Creates a new instance of TestDigesterDefinitionsReader.
     *
     * @param name The name of the test.
     */
    public TestDigesterDefinitionsReader(String name) {
        super(name);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.textui.TestRunner.main(
            new String[] { TestDigesterDefinitionsReader.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestDigesterDefinitionsReader.class);
    }

    /**
     * Tests the read method under normal conditions.
     */
    public void testRead() {
        try {
            DefinitionsReader reader = new DigesterDefinitionsReader();
            reader.init(new HashMap<String, String>());

            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/tiles-defs.xml");
            assertNotNull("Config file not found", configFile);

            InputStream source = configFile.openStream();
            Map<String, Definition> definitions = reader.read(source);

            assertNotNull("Definitions not returned.", definitions);
            assertNotNull("Couldn't find doc.mainLayout tile.",
                    definitions.get("doc.mainLayout"));
            assertNotNull("Couldn't Find title attribute.", definitions.get(
                    "doc.mainLayout").getAttribute("title").getValue());
            assertEquals("Incorrect Find title attribute.",
                    "Tiles Library Documentation", definitions.get(
                            "doc.mainLayout").getAttribute("title").getValue());

            Definition def = definitions.get("doc.role.test");
            assertNotNull("Couldn't find doc.role.test tile.", def);
            Attribute attribute = def.getAttribute("title");
            assertNotNull("Couldn't Find title attribute.", attribute
                    .getValue());
            assertEquals("Role 'myrole' expected", attribute.getRole(),
                    "myrole");

            def = definitions.get("doc.listattribute.test");
            assertNotNull("Couldn't find doc.listattribute.test tile.", def);
            attribute = def.getAttribute("items");
            assertNotNull("Couldn't Find items attribute.", attribute);
            assertTrue("The class of the attribute is not right",
                    attribute instanceof ListAttribute);
            assertTrue("The class of value of the attribute is not right",
                    attribute.getValue() instanceof List);
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }
    }

    /**
     * Tests calling read without calling init.
     */
    public void testNoInit() {
        try {
            DefinitionsReader reader = new DigesterDefinitionsReader();

            // What happens if we don't call init?
            // reader.init(new HashMap());

            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/tiles-defs.xml");
            assertNotNull("Config file not found", configFile);

            InputStream source = configFile.openStream();
            reader.read(source);

            fail("Should've thrown exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
            if (LOG.isDebugEnabled()) {
                LOG.debug("Exception caught, it is OK", e);
            }
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }
    }

    /**
     * Tests read with bad input source.
     */
    public void testBadSource() {
        try {
            // Create Digester Reader.
            DefinitionsReader reader = new DigesterDefinitionsReader();
            Map<String, String> params = new HashMap<String, String>();

            // Initialize reader.
            reader.init(params);

            // Read definitions.
            reader.read(new String("Bad Input"));
            fail("Should've thrown an exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
            if (LOG.isDebugEnabled()) {
                LOG.debug("Exception caught, it is OK", e);
            }
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }
    }

    /**
     * Tests read with bad XML source.
     */
    public void testBadXml() {
        try {
            DefinitionsReader reader = new DigesterDefinitionsReader();
            reader.init(new HashMap<String, String>());

            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/malformed-defs.xml");
            assertNotNull("Config file not found", configFile);

            InputStream source = configFile.openStream();
            reader.read(source);
            fail("Should've thrown an exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
            if (LOG.isDebugEnabled()) {
                LOG.debug("Exception caught, it is OK", e);
            }
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }
    }

    /**
     * Tests the validating input parameter.
     *
     * This test case enables Digester's validating property then passes in a
     * configuration file with invalid XML.
     */
    public void testValidatingParameter() {
        // Testing with default (validation ON).
        try {
            DefinitionsReader reader = new DigesterDefinitionsReader();
            Map<String, String> params = new HashMap<String, String>();
            reader.init(params);

            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/invalid-defs.xml");
            assertNotNull("Config file not found", configFile);

            InputStream source = configFile.openStream();
            reader.read(source);
            fail("Should've thrown an exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
            if (LOG.isDebugEnabled()) {
                LOG.debug("Exception caught, it is OK", e);
            }
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }

        // Testing with validation OFF.
        try {
            DefinitionsReader reader = new DigesterDefinitionsReader();
            Map<String, String> params = new HashMap<String, String>();
            params.put(DigesterDefinitionsReader.PARSER_VALIDATE_PARAMETER_NAME,
                    "false");
            reader.init(params);

            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/invalid-defs.xml");
            assertNotNull("Config file not found", configFile);

            InputStream source = configFile.openStream();
            reader.read(source);
        } catch (DefinitionsFactoryException e) {
            fail("Should not have thrown an exception." + e);
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }
    }
}
