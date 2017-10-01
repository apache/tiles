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

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.RefreshMonitor;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.locale.PostfixedApplicationResource;
import org.apache.tiles.request.locale.URLApplicationResource;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link BaseLocaleUrlDefinitionDAO}.
 *
 * @version $Rev$ $Date$
 */
public class BaseLocaleUrlDefinitionDAOTest {

    private static final class MutableApplicationResource extends PostfixedApplicationResource {
        private long lastModified = System.currentTimeMillis();
        private String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n"
                + "<!DOCTYPE tiles-definitions PUBLIC "
                + "\"-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN\" "
                + "\"http://tiles.apache.org/dtds/tiles-config_3_0.dtd\">\n\n" + "<tiles-definitions>"
                + "<definition name=\"rewrite.test\" template=\"/test.jsp\">"
                + "<put-attribute name=\"testparm\" value=\"testval\"/>" + "</definition>" //
                + "</tiles-definitions>";

        private MutableApplicationResource(String localePath) {
            super(localePath);
        }

        public void modify(String xml) {
            lastModified = System.currentTimeMillis();
            this.xml = xml;
        }

        @Override
        public long getLastModified() {
            return lastModified;
        }

        @Override
        public InputStream getInputStream() throws IOException {

            return new ByteArrayInputStream(xml.getBytes("ISO-8859-1"));
        }
    }

    /**
     * The time (in milliseconds) to wait to be sure that the system updates the
     * modify date of a file.
     */
    private static final int SLEEP_MILLIS = 2000;

    /**
     * The dao to test.
     */
    private ApplicationContext applicationContext;

    private BaseLocaleUrlDefinitionDAO dao;
    private MutableApplicationResource resource;

    /**
     * Sets up the test.
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        resource = new MutableApplicationResource("org/apache/tiles/config/temp-defs.xml");
        applicationContext = createMock(ApplicationContext.class);
        expect(applicationContext.getResource("org/apache/tiles/config/temp-defs.xml")).andReturn(resource).anyTimes();
        replay(applicationContext);
        dao = createMockBuilder(BaseLocaleUrlDefinitionDAO.class).withConstructor(applicationContext).createMock();
    }

    /**
     * Test method for {@link org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO#refreshRequired()}.
     * @throws URISyntaxException If something goes wrong.
     * @throws IOException If something goes wrong.
     * @throws InterruptedException If something goes wrong.
     */
    @Test
    public void testRefreshRequired() throws URISyntaxException, IOException, InterruptedException {
        // Set up multiple data sources.
        Map<String, Attribute> attribs = new HashMap<String, Attribute>();
        attribs.put("testparm", new Attribute("testval"));
        Definition rewriteTest = new Definition("rewrite.test", Attribute.createTemplateAttribute("/test.jsp"), attribs);
        expect(dao.getDefinition("rewrite.test", null)).andReturn(rewriteTest);

        replay(dao);

        List<ApplicationResource> sources = new ArrayList<ApplicationResource>();
        sources.add(resource);
        dao.setSources(sources);
        DefinitionsReader reader = new DigesterDefinitionsReader();
        dao.setReader(reader);

        Request context = createMock(Request.class);
        expect(context.getContext("session")).andReturn(new HashMap<String, Object>()).anyTimes();
        expect(context.getRequestLocale()).andReturn(null).anyTimes();
        replay(context);

        Definition definition = dao.getDefinition("rewrite.test", null);
        assertNotNull("rewrite.test definition not found.", definition);
        assertEquals("Incorrect initial template value", "/test.jsp", definition.getTemplateAttribute().getValue());

        RefreshMonitor reloadable = dao;
        dao.loadDefinitionsFromResource(resource);
        assertEquals("Factory should be fresh.", false, reloadable.refreshRequired());

        // Make sure the system actually updates the timestamp.
        Thread.sleep(SLEEP_MILLIS);

        // Set up multiple data sources.
        resource.modify("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" + "<!DOCTYPE tiles-definitions PUBLIC "
                + "\"-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN\" "
                + "\"http://tiles.apache.org/dtds/tiles-config_3_0.dtd\">\n\n" + "<tiles-definitions>"
                + "<definition name=\"rewrite.test\" template=\"/newtest.jsp\">"
                + "<put-attribute name=\"testparm\" value=\"testval\"/>" + "</definition>" //
                + "</tiles-definitions>");

        assertEquals("Factory should be stale.", true, reloadable.refreshRequired());

        verify(context, dao);
    }

    /**
     * Test method for {@link BaseLocaleUrlDefinitionDAO#loadDefinitionsFromURL(URL)}.
     * @throws MalformedURLException If something goes wrong.
     */
    @Test
    public void testLoadDefinitionsFromURLFileNotFound() throws MalformedURLException {
        URLApplicationResource resource = new URLApplicationResource("/hello/there.txt", new URL(
                "file:///hello/there.txt"));
        replay(dao);
        DefinitionsReader reader = createMock(DefinitionsReader.class);
        replay(reader);

        dao.setReader(reader);
        assertNull(dao.loadDefinitionsFromResource(resource));
        verify(dao, reader);
    }

}
