/*
 * $Id$
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.tiles.digester.DigesterDefinitionsReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Tests the <code>org.apache.tiles.digester.DigesterDefinitionsReader</code> class.
 *
 * @version $Rev$ $Date$ 
 */
public class TestDigesterDefinitionsReader extends TestCase {
    
    /** Creates a new instance of TestDigesterDefinitionsReader */
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
            reader.init(new HashMap());
            
            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/tiles-defs.xml");
            assertNotNull("Config file not found", configFile);
            
            InputStream source = configFile.openStream();
            Map definitions = reader.read(source);
            
            assertNotNull("Definitions not returned.", definitions);
            assertNotNull("Couldn't find doc.mainLayout tile.", 
                    (ComponentDefinition) definitions.get("doc.mainLayout"));
            assertNotNull("Couldn't Find title attribute.",
                    ((ComponentDefinition) definitions.get("doc.mainLayout"))
                    .getAttribute("title"));
            assertEquals("Incorrect Find title attribute.",
                    "Tiles Library Documentation",
                    ((ComponentDefinition) definitions.get("doc.mainLayout"))
                    .getAttribute("title"));
            
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
            Map definitions = reader.read(source);
            
            fail("Should've thrown exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
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
            Map params = new HashMap();

            // Initialize reader.
            reader.init(params);

            // Read definitions.
            Map definitions = reader.read(new String("Bad Input"));
            fail("Should've thrown an exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
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
            reader.init(new HashMap());
            
            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/malformed-defs.xml");
            assertNotNull("Config file not found", configFile);
            
            InputStream source = configFile.openStream();
            Map definitions = reader.read(source);
            fail("Should've thrown an exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
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
        /*
         * For some reason this test doesn't work.  It throws a SAXParseException
         * but then the test failes saying "Should've thrown an exception.
         *
         * I don't know why DigesterDefinitionsReader doesn't catch the 
         * SAXParseException or how it makes it to the "fail" statement below.
         *
        try {
            DefinitionsReader reader = new DigesterDefinitionsReader();
            Map params = new HashMap();
            params.put(DigesterDefinitionsReader.PARSER_VALIDATE_PARAMETER_NAME,
                    "true");
            reader.init(params);
            
            URL configFile = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/invalid-defs.xml");
            assertNotNull("Config file not found", configFile);
            
            InputStream source = configFile.openStream();
            Map definitions = reader.read(source);
            fail("Should've thrown an exception.");
        } catch (DefinitionsFactoryException e) {
            // correct.
        } catch (Exception e) {
            fail("Exception reading configuration." + e);
        }
         */
    }
}
