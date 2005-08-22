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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.tiles.definition.UrlDefinitionsFactory;

/**
 * Tests the reloadable definitions factory.
 *
 * @version $Rev$ $Date$ 
 */
public class TestReloadableDefinitionsFactory extends TestCase {
    
    /** Creates a new instance of TestReloadableDefinitionsFactory */
    public TestReloadableDefinitionsFactory(String name) {
        super(name);
    }
  
    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.awtui.TestRunner.main(
            new String[] { TestReloadableDefinitionsFactory.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        return new TestSuite(TestReloadableDefinitionsFactory.class);
    }

    /**
     * Tests reloading definitions factory.
     */
    public void testReloadableDefinitionsFactory() {
        try {
            DefinitionsFactory factory = new UrlDefinitionsFactory();

            // Set up multiple data sources.
            URL url = this.getClass().getClassLoader().getResource(
                    "org/apache/tiles/config/temp-defs.xml");
	    URI uri = new URI(url.toExternalForm());
            
            String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
             "<!DOCTYPE tiles-definitions PUBLIC " +
                   "\"-//Apache Software Foundation//DTD Tiles Configuration 1.1//EN\" " +
                   "\"http://struts.apache.org/dtds/tiles-config_1_1.dtd\">\n\n" +
            "<tiles-definitions>" +
            "<definition name=\"rewrite.test\" path=\"/test.jsp\">" +
                      "<put name=\"testparm\" value=\"testval\"/>" +
              "</definition>" +
            "</tiles-definitions>";
            
	    File file = new File(uri);
	    FileOutputStream fileOut = new FileOutputStream(file);
	    BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(fileOut)); 
	    writer.write(xml);
	    writer.close();

            factory.init(null);
            factory.addSource(url);

            // Parse files.
            ComponentDefinitions definitions = factory.readDefinitions();
            
            assertNotNull("rewrite.test definition not found.", 
                    definitions.getDefinition("rewrite.test"));
            assertEquals("Incorrect initial path value", "/test.jsp",
			 definitions.getDefinition("rewrite.test").getPath());

	    ReloadableDefinitionsFactory reloadable = (ReloadableDefinitionsFactory) factory;
	    assertEquals("Factory should be fresh.", false, 
                    reloadable.refreshRequired());

	    // Make sure the system actually updates the timestamp.
	    Thread.sleep(30000);

            // Set up multiple data sources.
            xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
             "<!DOCTYPE tiles-definitions PUBLIC " +
                   "\"-//Apache Software Foundation//DTD Tiles Configuration 1.1//EN\" " +
                   "\"http://struts.apache.org/dtds/tiles-config_1_1.dtd\">\n\n" +
            "<tiles-definitions>" +
            "<definition name=\"rewrite.test\" path=\"/newtest.jsp\">" +
                      "<put name=\"testparm\" value=\"testval\"/>" +
              "</definition>" +
            "</tiles-definitions>";
            
	    file = new File(uri);
	    fileOut = new FileOutputStream(file);
	    writer = new BufferedWriter(new OutputStreamWriter(fileOut)); 
	    writer.write(xml);
	    writer.close();


	    assertEquals("Factory should be stale.", true, 
                    reloadable.refreshRequired());
	    definitions = factory.readDefinitions();
            assertNotNull("rewrite.test definition not found.", 
                    definitions.getDefinition("rewrite.test"));
            assertEquals("Incorrect initial path value", "/newtest.jsp",
			 definitions.getDefinition("rewrite.test").getPath());
        } catch (Exception e) {
            fail("Error running test: " + e);
        }
        
    }
}
