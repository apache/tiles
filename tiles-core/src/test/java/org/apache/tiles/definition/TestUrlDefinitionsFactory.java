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

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Collections;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.context.TilesRequestContext;

/**
 * Tests the UrlDefinitionsFactory.
 *
 * @version $Rev$ $Date$
 */
public class TestUrlDefinitionsFactory extends TestCase {

    /**
     * The logging object.
     */
    private static final Log LOG =
        LogFactory.getLog(TestUrlDefinitionsFactory.class);

    /**
     * The number of foreseen URLs with postfixes.
     */
    private static final int POSTFIX_COUNT = 3;

    /**
     * Creates a new instance of TestUrlDefinitionsFactory.
     *
     * @param name The name of the test.
     */
    public TestUrlDefinitionsFactory(String name) {
        super(name);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.textui.TestRunner.main(
                new String[]{TestUrlDefinitionsFactory.class.getName()});
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
     *
     * @throws Exception If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    public void testReadDefinitions() throws Exception {
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

        factory.init(Collections.EMPTY_MAP);
        factory.addSource(url1);
        factory.addSource(url2);
        factory.addSource(url3);

        // Parse files.
        Definitions definitions = factory.readDefinitions();

        assertNotNull("test.def1 definition not found.", definitions.getDefinition("test.def1"));
        assertNotNull("test.def2 definition not found.", definitions.getDefinition("test.def2"));
        assertNotNull("test.def3 definition not found.", definitions.getDefinition("test.def3"));
    }

    /**
     * Tests the getDefinition method.
     *
     * @throws Exception If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    public void testGetDefinition() throws Exception {
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

        factory.addSource(url1);
        factory.addSource(url2);
        factory.addSource(url3);
        factory.init(Collections.EMPTY_MAP);

        TilesRequestContext emptyContext = new MockOnlyLocaleTilesContext(null);
        TilesRequestContext usContext = new MockOnlyLocaleTilesContext(Locale.US);
        TilesRequestContext frenchContext = new MockOnlyLocaleTilesContext(Locale.FRENCH);
        TilesRequestContext chinaContext = new MockOnlyLocaleTilesContext(Locale.CHINA);
        TilesRequestContext canadaFrenchContext = new MockOnlyLocaleTilesContext(Locale.CANADA_FRENCH);

        assertNotNull("test.def1 definition not found.", factory.getDefinition("test.def1", emptyContext));
        assertNotNull("test.def2 definition not found.", factory.getDefinition("test.def2", emptyContext));
        assertNotNull("test.def3 definition not found.", factory.getDefinition("test.def3", emptyContext));
        assertNotNull("test.common definition not found.", factory.getDefinition("test.common", emptyContext));
        assertNotNull("test.common definition in US locale not found.", factory
                .getDefinition("test.common", usContext));
        assertNotNull("test.common definition in FRENCH locale not found.",
                factory.getDefinition("test.common", frenchContext));
        assertNotNull("test.common definition in CHINA locale not found.",
                factory.getDefinition("test.common", chinaContext));
        assertNotNull(
                "test.common.french definition in FRENCH locale not found.",
                factory.getDefinition("test.common.french", frenchContext));
        assertNotNull(
                "test.common.french definition in CANADA_FRENCH locale not found.",
                factory
                        .getDefinition("test.common.french",
                                canadaFrenchContext));
        assertNotNull("test.def.toextend definition not found.", factory
                .getDefinition("test.def.toextend", emptyContext));
        assertNotNull("test.def.overridden definition not found.", factory
                .getDefinition("test.def.overridden", emptyContext));
        assertNotNull(
                "test.def.overridden definition in FRENCH locale not found.",
                factory.getDefinition("test.def.overridden", frenchContext));

        assertEquals("Incorrect default country value", "default", factory
                .getDefinition("test.def1", emptyContext).getAttribute(
                        "country").getValue());
        assertEquals("Incorrect US country value", "US", factory.getDefinition(
                "test.def1", usContext).getAttribute("country").getValue());
        assertEquals("Incorrect France country value", "France", factory
                .getDefinition("test.def1", frenchContext).getAttribute(
                        "country").getValue());
        assertEquals("Incorrect Chinese country value (should be default)",
                "default", factory.getDefinition("test.def1", chinaContext)
                        .getAttribute("country").getValue());
        assertEquals("Incorrect default country value", "default", factory
                .getDefinition("test.def.overridden", emptyContext)
                .getAttribute("country").getValue());
        assertEquals("Incorrect default title value",
                "Definition to be overridden", factory.getDefinition(
                        "test.def.overridden", emptyContext).getAttribute(
                        "title").getValue());
        assertEquals("Incorrect France country value", "France", factory
                .getDefinition("test.def.overridden", frenchContext)
                .getAttribute("country").getValue());
        assertEquals("Incorrect France title value",
                "Definition to be extended", factory.getDefinition(
                        "test.def.overridden", frenchContext).getAttribute(
                        "title").getValue());
    }

    /**
     * Tests addSource with a bad source object type.
     *
     * @throws Exception If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    public void testBadSourceType() throws Exception {
        try {
            DefinitionsFactory factory = new UrlDefinitionsFactory();

            factory.init(Collections.EMPTY_MAP);
            factory.addSource("Bad object.");

            fail("Should've thrown exception.");
        } catch (DefinitionsFactoryException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Bad class name intercepted, it is ok", e);
            }
            // success.
        }
    }

    /**
     * Tests the addDefinitions method under normal
     * circumstances.
     *
     * @throws Exception If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    public void testReadByLocale() throws Exception {
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

        factory.init(Collections.EMPTY_MAP);
        factory.addSource(url1);
        factory.addSource(url2);
        factory.addSource(url3);

        // Parse files.
        Definitions definitions = factory.readDefinitions();
        factory.addDefinitions(definitions,
                new MockOnlyLocaleTilesContext(Locale.US));
        factory.addDefinitions(definitions,
                new MockOnlyLocaleTilesContext(Locale.FRENCH));

        assertNotNull("test.def1 definition not found.", definitions.getDefinition("test.def1"));
        assertNotNull("test.def1 US definition not found.", definitions.getDefinition("test.def1", Locale.US));
        assertNotNull("test.def1 France definition not found.", definitions.getDefinition("test.def1", Locale.FRENCH));
        assertNotNull("test.def1 China should return default.", definitions.getDefinition("test.def1", Locale.CHINA));

        assertEquals("Incorrect default country value", "default", definitions
                .getDefinition("test.def1").getAttribute("country").getValue());
        assertEquals("Incorrect US country value", "US", definitions
                .getDefinition("test.def1", Locale.US).getAttribute("country")
                .getValue());
        assertEquals("Incorrect France country value", "France", definitions
                .getDefinition("test.def1", Locale.FRENCH).getAttribute(
                        "country").getValue());
        assertEquals("Incorrect Chinese country value (should default)",
                "default", definitions.getDefinition("test.def1", Locale.CHINA)
                        .getAttribute("country").getValue());
    }

    /**
     * Tests the isContextProcessed method.
     *
     * @throws Exception If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    public void testIsContextProcessed() throws Exception {
        MockPublicUrlDefinitionsFactory factory = new MockPublicUrlDefinitionsFactory();

        // Set up multiple data sources.
        URL url1 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs1.xml");
        assertNotNull("Could not load defs1 file.", url1);

        factory.init(Collections.EMPTY_MAP);
        factory.addSource(url1);

        // Parse files.
        Definitions definitions = factory.readDefinitions();
        TilesRequestContext tilesContext =
                new MockOnlyLocaleTilesContext(Locale.US);
        assertFalse("Locale should not be processed.",
                factory.isContextProcessed(tilesContext));

        factory.addDefinitions(definitions, tilesContext);
        assertTrue("Locale should be processed.",
                factory.isContextProcessed(tilesContext));
    }

    /**
     * Tests the reader init param.
     *
     * @throws Exception If something goes wrong.
     */
    public void testReaderParam() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put(DefinitionsFactory.READER_IMPL_PROPERTY,
                "org.apache.tiles.definition.MockDefinitionsReader");

        int instanceCount = MockDefinitionsReader.getInstanceCount();

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
    }

    /**
     * Tests the calculatePostfixes method.
     */
    public void testCalculatePostfixes() {
        Locale locale = Locale.US;

        List<String> posts = UrlDefinitionsFactory.calculatePostfixes(locale);
        assertEquals(POSTFIX_COUNT, posts.size());
        assertTrue(posts.contains("_en_US"));
        assertTrue(posts.contains("_en"));

        locale = Locale.ENGLISH;
        posts = UrlDefinitionsFactory.calculatePostfixes(locale);
        assertEquals(2, posts.size());
        assertTrue(posts.contains("_en"));
    }

    /**
     * Tests the concatPostfix method.
     */
    public void testConcatPostfix() {
        UrlDefinitionsFactory factory = new UrlDefinitionsFactory();
        String postfix = "_en_US";
        assertEquals("a_en_US", factory.concatPostfix("a", postfix));
        assertEquals("a_en_US.jsp", factory.concatPostfix("a.jsp", postfix));
        assertEquals("file_en_US.jsp", factory.concatPostfix("file.jsp", postfix));
        assertEquals("./path/file_en_US.jsp", factory.concatPostfix("./path/file.jsp", postfix));
    }
}
