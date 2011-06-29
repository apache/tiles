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
package org.apache.tiles.request.portlet;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.portlet.PortletContext;

import org.apache.tiles.request.collection.ReadOnlyEnumerationMap;
import org.apache.tiles.request.collection.ScopeMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link PortletApplicationContext}.
 *
 * @version $Rev$ $Date$
 */
public class PortletApplicationContextTest {

    /**
     * The portlet context.
     */
    private PortletContext portletContext;

    /**
     * The application context.
     */
    private PortletApplicationContext context;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        portletContext = createMock(PortletContext.class);
        context = new PortletApplicationContext(portletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.PortletApplicationContext#getContext()}.
     */
    @Test
    public void testGetContext() {
        replay(portletContext);
        assertEquals(portletContext, context.getContext());
        verify(portletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.PortletApplicationContext#getPortletContext()}.
     */
    @Test
    public void testGetPortletContext() {
        replay(portletContext);
        assertEquals(portletContext, context.getPortletContext());
        verify(portletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.PortletApplicationContext#getApplicationScope()}.
     */
    @Test
    public void testGetApplicationScope() {
        replay(portletContext);
        assertTrue(context.getApplicationScope() instanceof ScopeMap);
        verify(portletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.PortletApplicationContext#getInitParams()}.
     */
    @Test
    public void testGetInitParams() {
        replay(portletContext);
        assertTrue(context.getInitParams() instanceof ReadOnlyEnumerationMap);
        verify(portletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.PortletApplicationContext#getResource(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetResource() throws IOException {
        URL url = new URL("http://tiles.apache.org/");
        expect(portletContext.getResource("/my/path")).andReturn(url);

        replay(portletContext);
        assertEquals(url, context.getResource("/my/path"));
        verify(portletContext);
    }

    /**
     * Test method for {@link PortletApplicationContext#getResources(String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetResources() throws IOException {
        URL url = new URL("http://tiles.apache.org/");
        expect(portletContext.getResource("/my/path")).andReturn(url);

        replay(portletContext);
        Set<URL> urls = context.getResources("/my/path");
        assertEquals(1, urls.size());
        assertTrue(urls.contains(url));
        verify(portletContext);
    }
}
