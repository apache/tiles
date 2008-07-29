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

package org.apache.tiles.definition.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.tiles.Definition;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.MockDefinitionsReader;
import org.apache.tiles.definition.RefreshMonitor;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.easymock.EasyMock;

/**
 * Tests {@link LocaleUrlDefinitionDAO}.
 *
 * @version $Rev$ $Date$
 */
public class LocaleUrlDefinitionDAOTest extends TestCase {

    /**
     * The time (in milliseconds) to wait to be sure that the system updates the
     * modify date of a file.
     */
    private static final int SLEEP_MILLIS = 30000;

    /**
     * The object to test.
     */
    private LocaleUrlDefinitionDAO definitionDao;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        definitionDao = new LocaleUrlDefinitionDAO();
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#getDefinition(String, Locale)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testGetDefinition() throws IOException {
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

        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        Set<URL> urlSet = new HashSet<URL>();
        urlSet.add(url1);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs1.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url2);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs2.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url3);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs3.xml"))
                .andReturn(urlSet);
        EasyMock.replay(applicationContext);
        definitionDao.setApplicationContext(applicationContext);

        Map<String, String> params = new HashMap<String, String>();
        params.put(DefinitionsFactory.DEFINITIONS_CONFIG,
                "org/apache/tiles/config/defs1.xml,org/apache/tiles/config/defs2.xml,"
                        + "org/apache/tiles/config/defs3.xml");
        definitionDao.init(params);

        assertNotNull("test.def1 definition not found.", definitionDao
                .getDefinition("test.def1", null));
        assertNotNull("test.def2 definition not found.", definitionDao
                .getDefinition("test.def2", null));
        assertNotNull("test.def3 definition not found.", definitionDao
                .getDefinition("test.def3", null));
        assertNotNull("test.common definition not found.", definitionDao
                .getDefinition("test.common", null));
        assertNotNull("test.common definition in US locale not found.",
                definitionDao.getDefinition("test.common", Locale.US));
        assertNotNull("test.common definition in FRENCH locale not found.",
                definitionDao.getDefinition("test.common", Locale.FRENCH));
        assertNotNull("test.common definition in CHINA locale not found.",
                definitionDao.getDefinition("test.common", Locale.CHINA));
        assertNotNull(
                "test.common.french definition in FRENCH locale not found.",
                definitionDao.getDefinition("test.common.french",
                        Locale.FRENCH));
        assertNotNull(
                "test.common.french definition in CANADA_FRENCH locale not found.",
                definitionDao.getDefinition("test.common.french",
                        Locale.CANADA_FRENCH));
        assertNotNull("test.def.toextend definition not found.", definitionDao
                .getDefinition("test.def.toextend", null));
        assertNotNull("test.def.overridden definition not found.",
                definitionDao.getDefinition("test.def.overridden", null));
        assertNotNull(
                "test.def.overridden definition in FRENCH locale not found.",
                definitionDao.getDefinition("test.def.overridden",
                        Locale.FRENCH));

        assertEquals("Incorrect default country value", "default",
                definitionDao.getDefinition("test.def1", null).getAttribute(
                        "country").getValue());
        assertEquals("Incorrect US country value", "US", definitionDao
                .getDefinition("test.def1", Locale.US).getAttribute("country")
                .getValue());
        assertEquals("Incorrect France country value", "France", definitionDao
                .getDefinition("test.def1", Locale.FRENCH).getAttribute(
                        "country").getValue());
        assertEquals("Incorrect Chinese country value (should be default)",
                "default", definitionDao.getDefinition("test.def1",
                        Locale.CHINA).getAttribute("country").getValue());
        assertEquals("Incorrect default country value", "default",
                definitionDao.getDefinition("test.def.overridden", null)
                        .getAttribute("country").getValue());
        assertEquals("Incorrect default title value",
                "Definition to be overridden", definitionDao.getDefinition(
                        "test.def.overridden", null).getAttribute("title")
                        .getValue());
        assertEquals("Incorrect France country value", "France", definitionDao
                .getDefinition("test.def.overridden", Locale.FRENCH)
                .getAttribute("country").getValue());
        assertNull("Definition in French not found", definitionDao
                .getDefinition("test.def.overridden", Locale.FRENCH)
                .getAttribute("title"));
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#getDefinitions(Locale)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testGetDefinitions() throws IOException {
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

        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        Set<URL> urlSet = new HashSet<URL>();
        urlSet.add(url1);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs1.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url2);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs2.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url3);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs3.xml"))
                .andReturn(urlSet);
        EasyMock.replay(applicationContext);
        definitionDao.setApplicationContext(applicationContext);

        Map<String, String> params = new HashMap<String, String>();
        params.put(DefinitionsFactory.DEFINITIONS_CONFIG,
                "org/apache/tiles/config/defs1.xml,org/apache/tiles/config/defs2.xml,"
                        + "org/apache/tiles/config/defs3.xml");
        definitionDao.init(params);

        Map<String, Definition> defaultDefinitions = definitionDao
                .getDefinitions(null);
        Map<String, Definition> usDefinitions = definitionDao
                .getDefinitions(Locale.US);
        Map<String, Definition> frenchDefinitions = definitionDao
                .getDefinitions(Locale.FRENCH);
        Map<String, Definition> chinaDefinitions = definitionDao
                .getDefinitions(Locale.CHINA);
        Map<String, Definition> canadaFrenchDefinitions = definitionDao
                .getDefinitions(Locale.CANADA_FRENCH);

        assertNotNull("test.def1 definition not found.", defaultDefinitions
                .get("test.def1"));
        assertNotNull("test.def2 definition not found.", defaultDefinitions
                .get("test.def2"));
        assertNotNull("test.def3 definition not found.", defaultDefinitions
                .get("test.def3"));
        assertNotNull("test.common definition not found.", defaultDefinitions
                .get("test.common"));
        assertNotNull("test.common definition in US locale not found.",
                usDefinitions.get("test.common"));
        assertNotNull("test.common definition in FRENCH locale not found.",
                frenchDefinitions.get("test.common"));
        assertNotNull("test.common definition in CHINA locale not found.",
                chinaDefinitions.get("test.common"));
        assertNotNull(
                "test.common.french definition in FRENCH locale not found.",
                frenchDefinitions.get("test.common.french"));
        assertNotNull(
                "test.common.french definition in CANADA_FRENCH locale not found.",
                canadaFrenchDefinitions.get("test.common.french"));
        assertNotNull("test.def.toextend definition not found.",
                defaultDefinitions.get("test.def.toextend"));
        assertNotNull("test.def.overridden definition not found.",
                defaultDefinitions.get("test.def.overridden"));
        assertNotNull(
                "test.def.overridden definition in FRENCH locale not found.",
                frenchDefinitions.get("test.def.overridden"));

        assertEquals("Incorrect default country value", "default",
                defaultDefinitions.get("test.def1").getAttribute("country")
                        .getValue());
        assertEquals("Incorrect US country value", "US", usDefinitions.get(
                "test.def1").getAttribute("country").getValue());
        assertEquals("Incorrect France country value", "France",
                frenchDefinitions.get("test.def1").getAttribute("country")
                        .getValue());
        assertEquals("Incorrect Chinese country value (should be default)",
                "default", chinaDefinitions.get("test.def1").getAttribute(
                        "country").getValue());
        assertEquals("Incorrect default country value", "default",
                defaultDefinitions.get("test.def.overridden").getAttribute(
                        "country").getValue());
        assertEquals("Incorrect default title value",
                "Definition to be overridden", defaultDefinitions.get(
                        "test.def.overridden").getAttribute("title").getValue());
        assertEquals("Incorrect France country value", "France",
                frenchDefinitions.get("test.def.overridden").getAttribute(
                        "country").getValue());
        assertNull("Definition in French not found", frenchDefinitions.get(
                "test.def.overridden").getAttribute("title"));
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#setSourceURLs(List)}.
     */
    public void testSetSourceURLs() {
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
        List<URL> sourceURLs = new ArrayList<URL>();
        sourceURLs.add(url1);
        sourceURLs.add(url2);
        sourceURLs.add(url3);
        definitionDao.setSourceURLs(sourceURLs);
        assertEquals("The source URLs are not set correctly", sourceURLs,
                definitionDao.sourceURLs);
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#setReader(DefinitionsReader)}.
     */
    public void testSetReader() {
        DefinitionsReader reader = EasyMock.createMock(DefinitionsReader.class);
        definitionDao.setReader(reader);
        assertEquals("There reader has not been set correctly", reader,
                definitionDao.reader);
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#addSourceURL(URL)}.
     */
    public void testAddSourceURL() {
        // Set up multiple data sources.
        URL url1 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs1.xml");
        assertNotNull("Could not load defs1 file.", url1);
        URL url2 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs2.xml");
        assertTrue("The source URLs is not empty", definitionDao.sourceURLs
                .isEmpty());
        definitionDao.addSourceURL(url1);
        assertEquals("The size of the source URLs is not correct", 1,
                definitionDao.sourceURLs.size());
        assertEquals("The URL is not correct", url1, definitionDao.sourceURLs
                .get(0));
        List<URL> sourceURLs = new ArrayList<URL>();
        sourceURLs.add(url2);
        definitionDao.setSourceURLs(sourceURLs);
        definitionDao.addSourceURL(url1);
        assertEquals("The size of the source URLs is not correct", 2,
                definitionDao.sourceURLs.size());
        assertEquals("The URL is not correct", url2, definitionDao.sourceURLs
                .get(0));
        assertEquals("The URL is not correct", url1, definitionDao.sourceURLs
                .get(1));
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#setApplicationContext(TilesApplicationContext)}.
     */
    public void testSetApplicationContext() {
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        definitionDao.setApplicationContext(applicationContext);
        assertEquals("The application context has not been set",
                applicationContext, definitionDao.applicationContext);
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#init(Map)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testInit() throws IOException {
        URL url1 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs1.xml");
        URL url2 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs2.xml");
        URL url3 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs3.xml");
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        Set<URL> urlSet = new HashSet<URL>();
        urlSet.add(url1);
        EasyMock.expect(applicationContext.getResources("/WEB-INF/tiles.xml"))
                .andReturn(urlSet);
        EasyMock.replay(applicationContext);
        Map<String, String> params = new HashMap<String, String>();
        definitionDao.setApplicationContext(applicationContext);
        definitionDao.init(params);
        assertEquals("The reader is not of the correct class",
                DigesterDefinitionsReader.class, definitionDao.reader
                        .getClass());
        List<URL> sourceURLs = new ArrayList<URL>();
        sourceURLs.add(url1);
        assertEquals("The source URLs are not correct", sourceURLs,
                definitionDao.sourceURLs);
        EasyMock.reset(applicationContext);

        applicationContext = EasyMock.createMock(TilesApplicationContext.class);
        urlSet = new HashSet<URL>();
        urlSet.add(url1);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs1.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url2);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs2.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url3);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs3.xml"))
                .andReturn(urlSet);
        EasyMock.replay(applicationContext);
        params.clear();
        params.put(DefinitionsFactory.READER_IMPL_PROPERTY,
                MockDefinitionsReader.class.getName());
        params.put(DefinitionsFactory.DEFINITIONS_CONFIG,
                "org/apache/tiles/config/defs1.xml,"
                        + "org/apache/tiles/config/defs2.xml,"
                        + "org/apache/tiles/config/defs3.xml");
        definitionDao.setApplicationContext(applicationContext);
        definitionDao.setSourceURLs(new ArrayList<URL>());
        definitionDao.init(params);
        assertEquals("The reader is not of the correct class",
                MockDefinitionsReader.class, definitionDao.reader.getClass());
        sourceURLs = new ArrayList<URL>();
        sourceURLs.add(url1);
        sourceURLs.add(url2);
        sourceURLs.add(url3);
        assertEquals("The source URLs are not correct", sourceURLs,
                definitionDao.sourceURLs);
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#identifySources(Map)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testIdentifySources() throws IOException {
        URL url1 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs1.xml");
        URL url2 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs2.xml");
        URL url3 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs3.xml");
        URL url4 = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/defs1_en_US.xml");
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        Set<URL> urlSet = new HashSet<URL>();
        urlSet.add(url1);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs1.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url2);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs2.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url3);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs3.xml"))
                .andReturn(urlSet);
        urlSet = new HashSet<URL>();
        urlSet.add(url4);
        EasyMock.expect(
                applicationContext
                        .getResources("org/apache/tiles/config/defs1_en_US.xml"))
                .andReturn(urlSet);
        EasyMock.replay(applicationContext);
        Map<String, String> params = new HashMap<String, String>();
        params.put(DefinitionsFactory.DEFINITIONS_CONFIG,
                "org/apache/tiles/config/defs1.xml,"
                        + "org/apache/tiles/config/defs2.xml,"
                        + "org/apache/tiles/config/defs3.xml,"
                        + "org/apache/tiles/config/defs1_en_US.xml");
        definitionDao.setApplicationContext(applicationContext);
        definitionDao.setSourceURLs(new ArrayList<URL>());
        definitionDao.identifySources(params);
        List<URL> sourceURLs = new ArrayList<URL>();
        sourceURLs.add(url1);
        sourceURLs.add(url2);
        sourceURLs.add(url3);
        assertEquals("The source URLs are not correct", sourceURLs,
                definitionDao.sourceURLs);
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#getResourceString(Map)}.
     */
    public void testGetResourceString() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(DefinitionsFactory.DEFINITIONS_CONFIG, "The string value");
        assertEquals("The resource string has not been got correctly",
                "The string value", definitionDao.getResourceString(params));
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#getResourceNames(String)}.
     */
    public void testGetResourceNames() {
        String toSplit = "This,will,be,split";
        String[] splitted = toSplit.split(",");
        String[] result = definitionDao.getResourceNames(toSplit);
        for (int i = 0; i < splitted.length; i++) {
            assertEquals("The string has not been split correctly", splitted[i],
                    result[i]);
        }
    }

    /**
     * Tests {@link LocaleUrlDefinitionDAO#refreshRequired()}.
     *
     * @throws IOException If something goes wrong during I/O.
     * @throws InterruptedException If the "sleep" instruction fails.
     * @throws URISyntaxException If the URIs are not correct.
     */
    public void testRefreshRequired() throws IOException, InterruptedException,
            URISyntaxException {
        // Set up multiple data sources.
        URL url = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/temp-defs.xml");

        URI uri = null;
        String urlPath = null;

        // The following madness is necessary b/c of the way Windows hanndles
        // URLs.
        // We must add a slash to the protocol if Windows does not. But we
        // cannot
        // add a slash to Unix paths b/c they already have one.
        if (url.getPath().startsWith("/")) {
            urlPath = "file:" + url.getPath();
        } else {
            urlPath = "file:/" + url.getPath();
        }

        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        Set<URL> urlSet = new HashSet<URL>();
        urlSet.add(url);
        EasyMock.expect(applicationContext.getResources(urlPath)).andReturn(
                urlSet);
        EasyMock.replay(applicationContext);
        ((TilesApplicationContextAware) definitionDao)
                .setApplicationContext(applicationContext);

        // The following second madness is necessary b/c sometimes spaces
        // are encoded as '%20', sometimes they are not. For example in
        // Windows 2000 under Eclipse they are encoded, under the prompt of
        // Windows 2000 they are not.
        // It seems to be in the different behaviour of
        // sun.misc.Launcher$AppClassLoader (called under Eclipse) and
        // java.net.URLClassLoader (under maven).
        // And an URL accepts spaces while URIs need '%20'.
        try {
            uri = new URI(urlPath);
        } catch (URISyntaxException e) {
            uri = new URI(urlPath.replaceAll(" ", "%20"));
        }

        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n"
                + "<!DOCTYPE tiles-definitions PUBLIC "
                + "\"-//Apache Software Foundation//DTD Tiles Configuration 2.0//EN\" "
                + "\"http://tiles.apache.org/dtds/tiles-config_2_0.dtd\">\n\n"
                + "<tiles-definitions>"
                + "<definition name=\"rewrite.test\" template=\"/test.jsp\">"
                + "<put-attribute name=\"testparm\" value=\"testval\"/>"
                + "</definition>" + "</tiles-definitions>";

        File file = new File(uri);
        FileOutputStream fileOut = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                fileOut));
        writer.write(xml);
        writer.close();

        Map<String, String> params = new HashMap<String, String>();
        params.put(DefinitionsFactory.DEFINITIONS_CONFIG, urlPath);
        definitionDao.init(params);
        TilesRequestContext context = EasyMock
                .createMock(TilesRequestContext.class);
        EasyMock.expect(context.getSessionScope()).andReturn(
                new HashMap<String, Object>()).anyTimes();
        EasyMock.expect(context.getRequestLocale()).andReturn(null).anyTimes();
        EasyMock.replay(context);

        Definition definition = definitionDao.getDefinition("rewrite.test",
                null);
        assertNotNull("rewrite.test definition not found.", definition);
        assertEquals("Incorrect initial template value", "/test.jsp",
                definition.getTemplate());

        RefreshMonitor reloadable = (RefreshMonitor) definitionDao;
        assertEquals("Factory should be fresh.", false, reloadable
                .refreshRequired());

        // Make sure the system actually updates the timestamp.
        Thread.sleep(SLEEP_MILLIS);

        // Set up multiple data sources.
        xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n"
                + "<!DOCTYPE tiles-definitions PUBLIC "
                + "\"-//Apache Software Foundation//DTD Tiles Configuration 2.0//EN\" "
                + "\"http://tiles.apache.org/dtds/tiles-config_2_0.dtd\">\n\n"
                + "<tiles-definitions>"
                + "<definition name=\"rewrite.test\" template=\"/newtest.jsp\">"
                + "<put-attribute name=\"testparm\" value=\"testval\"/>"
                + "</definition>" + "</tiles-definitions>";

        file = new File(uri);
        fileOut = new FileOutputStream(file);
        writer = new BufferedWriter(new OutputStreamWriter(fileOut));
        writer.write(xml);
        writer.close();
        file = new File(uri);

        assertEquals("Factory should be stale.", true, reloadable
                .refreshRequired());
    }
}
