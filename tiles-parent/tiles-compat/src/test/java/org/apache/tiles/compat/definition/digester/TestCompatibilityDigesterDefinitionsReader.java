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

package org.apache.tiles.compat.definition.digester;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the <code>org.apache.tiles.definition.digester.DigesterDefinitionsReader</code> class.
 *
 * @version $Rev$ $Date$
 */
public class TestCompatibilityDigesterDefinitionsReader extends TestCase {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(TestCompatibilityDigesterDefinitionsReader.class);

    /**
     * The definitions reader.
     */
    private DefinitionsReader reader;

    /**
     * Creates a new instance of TestDigesterDefinitionsReader.
     *
     * @param name The name of the test.
     */
    public TestCompatibilityDigesterDefinitionsReader(String name) {
        super(name);
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestCompatibilityDigesterDefinitionsReader.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        reader = new CompatibilityDigesterDefinitionsReader();
    }

    /**
     * Tests the read method to read Tiles 1.1 files.
     *
     * @throws DefinitionsFactoryException If the definitions factory fails.
     * @throws IOException If an I/O exception happens.
     */
    public void testReadOldFormat() throws IOException {
        URL configFile = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/tiles-defs-1.1.xml");
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
    }

    /**
     * Tests the read method to read Tiles 2.0 files.
     *
     * @throws IOException If an I/O exception happens.
     */
    public void testReadNewFormat() throws IOException {
        URL configFile = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/tiles-defs-2.0.xml");
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
    }

    /**
     * Tests the read method under normal conditions for the new features in 2.1
     * version of the DTD.
     */
    public void testRead21Version() {
        try {
            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/tiles-defs-2.1.xml");
            assertNotNull("Config file not found", configFile);

            InputStream source = configFile.openStream();
            Map<String, Definition> definitions = reader.read(source);

            assertNotNull("Definitions not returned.", definitions);
            Definition def = definitions.get("doc.cascaded.test");

            assertNotNull("Couldn't find doc.role.test tile.", def);
            Attribute attribute = def.getLocalAttribute("title");
            assertNotNull("Couldn't Find title local attribute.", attribute);
            attribute = def.getCascadedAttribute("title2");
            assertNotNull("Couldn't Find title2 cascaded attribute.", attribute);
            attribute = def.getLocalAttribute("items1");
            assertNotNull("Couldn't Find items1 local attribute.", attribute);
            attribute = def.getCascadedAttribute("items2");
            assertNotNull("Couldn't Find items2 cascaded attribute.", attribute);
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }
    }

    /**
     * Tests read with bad input source.
     */
    public void testBadSource() {
        try {
            // Read definitions.
            reader.read(new String("Bad Input"));
            fail("Should've thrown an exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
            if (log.isDebugEnabled()) {
                log.debug("Exception caught, it is OK", e);
            }
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }
    }
}
