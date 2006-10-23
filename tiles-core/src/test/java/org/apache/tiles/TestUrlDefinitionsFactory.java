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

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.mock.MockComponentDefinitions;
import org.apache.tiles.mock.MockPublicUrlDefinitionsFactory;
import org.apache.tiles.mock.MockDefinitionsReader;
import org.apache.tiles.mock.MockOnlyLocaleTilesContext;

/**
 * Tests the UrlDefinitionsFactory component.
 *
 * @version $Rev$ $Date$ 
 */
public class TestUrlDefinitionsFactory extends TestCase {
    
    /** Creates a new instance of TestUrlDefinitionsFactory */
    public TestUrlDefinitionsFactory(String name) {
        super(name);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.awtui.TestRunner.main(
            new String[] { TestUrlDefinitionsFactory.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestUrlDefinitionsFactory.class);
    }

    /**
     * Tests the readDefinitions method under normal conditions.
     */
    public void testReadDefinitions() {
        try {
            DefinitionsFactory factory = new UrlDefinitionsFactory();

            // Set up multiple data sources.
            URL url1 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs1.xml");
            assertNotNull("Could not load defs1 file.", url1);
            URL url2 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs2.xml");
            assertNotNull("Could not load defs2 file.", url2);
            URL url3 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs3.xml");
            assertNotNull("Could not load defs3 file.", url3);

            factory.init(null);
            factory.addSource(url1);
            factory.addSource(url2);
            factory.addSource(url3);

            // Parse files.
            ComponentDefinitions definitions = factory.readDefinitions();
            
            assertNotNull("test.def1 definition not found.", definitions.getDefinition("test.def1"));
            assertNotNull("test.def2 definition not found.", definitions.getDefinition("test.def1"));
            assertNotNull("test.def3 definition not found.", definitions.getDefinition("test.def1"));
        } catch (Exception e) {
            fail("Error running test: " + e);
        }
    }
    
    /**
     * Tests addSource with a bad source object type.
     */
    public void testBadSourceType() {
        try {
            DefinitionsFactory factory = new UrlDefinitionsFactory();

            factory.init(null);
            factory.addSource("Bad object.");
            
            fail("Should've thrown exception.");
        } catch (DefinitionsFactoryException e) {
            // success.
        } catch (Exception e) {
            fail("Error running test: " + e);
        }
    }
    
    /**
     * Tests the addDefinitions method under normal
     * circumstances.
     */
    public void testReadByLocale() {
        try {
            MockPublicUrlDefinitionsFactory factory = new MockPublicUrlDefinitionsFactory();

            // Set up multiple data sources.
            URL url1 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs1.xml");
            assertNotNull("Could not load defs1 file.", url1);
            URL url2 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs2.xml");
            assertNotNull("Could not load defs2 file.", url2);
            URL url3 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs3.xml");
            assertNotNull("Could not load defs3 file.", url3);

            factory.init(null);
            factory.addSource(url1);
            factory.addSource(url2);
            factory.addSource(url3);

            // Parse files.
            ComponentDefinitions definitions = factory.readDefinitions();
            factory.addDefinitions(definitions,
                    new MockOnlyLocaleTilesContext(Locale.US));
            factory.addDefinitions(definitions,
                    new MockOnlyLocaleTilesContext(Locale.FRENCH));
            
            assertNotNull("test.def1 definition not found.", definitions.getDefinition("test.def1"));
            assertNotNull("test.def1 US definition not found.", definitions.getDefinition("test.def1", Locale.US));
            assertNotNull("test.def1 France definition not found.", definitions.getDefinition("test.def1", Locale.FRENCH));
            assertNotNull("test.def1 China should return default.", definitions.getDefinition("test.def1", Locale.CHINA));
            
            assertEquals("Incorrect default country value", "default",
                    definitions.getDefinition("test.def1").getAttribute("country"));
            assertEquals("Incorrect US country value", "US",
                    definitions.getDefinition("test.def1", Locale.US).getAttribute("country"));
            assertEquals("Incorrect France country value", "France",
                    definitions.getDefinition("test.def1", Locale.FRENCH).getAttribute("country"));
            assertEquals("Incorrect Chinese country value (should default)", "default",
                    definitions.getDefinition("test.def1", Locale.CHINA).getAttribute("country"));
        } catch (Exception e) {
            fail("Error running test: " + e);
        }
        
    }
    
    /**
     * Tests the isLocaleProcessed method.
     */
    public void testIsLocaleProcessed() {
        try {
            MockPublicUrlDefinitionsFactory factory = new MockPublicUrlDefinitionsFactory();

            // Set up multiple data sources.
            URL url1 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs1.xml");
            assertNotNull("Could not load defs1 file.", url1);

            factory.init(null);
            factory.addSource(url1);

            // Parse files.
            ComponentDefinitions definitions = factory.readDefinitions();
            TilesRequestContext tilesContext =
                    new MockOnlyLocaleTilesContext(Locale.US);
            assertFalse("Locale should not be processed.", 
                    factory.isContextProcessed(tilesContext));
            
            factory.addDefinitions(definitions, tilesContext);
            assertTrue("Locale should be processed.",
                    factory.isContextProcessed(tilesContext));
            
        } catch (Exception e) {
            fail("Error running test: " + e);
        }
    }
    
    /**
     * Tests the reader init param.
     */
    public void testReaderParam() {
        Map params = new HashMap();
        params.put(DefinitionsFactory.READER_IMPL_PROPERTY, 
                "org.apache.tiles.mock.MockDefinitionsReader");

        int instanceCount = MockDefinitionsReader.getInstanceCount();
        
        try {
            DefinitionsFactory factory = new UrlDefinitionsFactory();

            // Set up multiple data sources.
            URL url1 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs1.xml");
            assertNotNull("Could not load defs1 file.", url1);

            factory.init(params);
            factory.addSource(url1);

            assertEquals("MockDefinitionsReader not used.",  
                    instanceCount + 1,
                    MockDefinitionsReader.getInstanceCount());
        } catch (Exception e) {
            fail("Error running test: " + e);
        }
    }
    
    /**
     * Tests the ComponentDefinitions init param.
     */
    public void testComponentDefinitionsParam() {
        Map params = new HashMap();
        params.put(DefinitionsFactory.DEFINITIONS_IMPL_PROPERTY, 
                "org.apache.tiles.mock.MockComponentDefinitions");

        try {
            DefinitionsFactory factory = new UrlDefinitionsFactory();

            // Set up multiple data sources.
            URL url1 = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/defs1.xml");
            assertNotNull("Could not load defs1 file.", url1);

            factory.init(params);
            factory.addSource(url1);
            ComponentDefinitions defs = factory.readDefinitions();

            assertNotNull("Definitions not read.", defs);
            assertTrue("Incorrect definitions type.", 
                    defs instanceof MockComponentDefinitions);
        } catch (Exception e) {
            fail("Error running test: " + e);
        }
    }
}
