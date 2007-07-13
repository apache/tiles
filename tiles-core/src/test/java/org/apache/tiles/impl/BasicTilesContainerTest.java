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
package org.apache.tiles.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.tiles.Attribute;
import org.apache.tiles.TilesException;
import org.apache.tiles.factory.TilesContainerFactory;
import org.easymock.EasyMock;


/**
 * @version $Rev$ $Date$
 */
public class BasicTilesContainerTest extends TestCase {

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory
            .getLog(BasicTilesContainerTest.class);

    /**
     * A sample integer value to check object rendering.
     */
    private static final int SAMPLE_INT = 15;

    /**
     * The container.
     */
    private BasicTilesContainer container;

    /** {@inheritDoc} */
    @Override
    public void setUp() {
        ServletContext context = EasyMock.createMock(ServletContext.class);
        URL url = getClass().getResource("/org/apache/tiles/factory/test-defs.xml");

        Vector<String> v = new Vector<String>();

        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.DEFINITIONS_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(EasyMock.isA(String.class))).andReturn(null).anyTimes();
        EasyMock.expect(context.getInitParameterNames()).andReturn(v.elements()).anyTimes();
        try {
            EasyMock.expect(context.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error getting Tiles configuration URL",
                    e);
        }
        EasyMock.replay(context);
        try {
            TilesContainerFactory factory = TilesContainerFactory.getFactory(context);
            container = (BasicTilesContainer) factory.createContainer(context);
        } catch (TilesException e) {
            throw new RuntimeException("Error initializing factory", e);
        }
    }

    /**
     * Tests basic Tiles container initialization.
     */
    public void testInitialization() {
        assertNotNull(container);
        assertNotNull(container.getContextFactory());
        assertNotNull(container.getPreparerFactory());
        assertNotNull(container.getDefinitionsFactory());
    }

    /**
     * Tests that attributes of type "object" won't be rendered.
     *
     * @throws IOException If something goes wrong, but it's not a Tiles
     * exception.
     */
    public void testObjectAttribute() throws IOException {
        Attribute attribute = new Attribute();
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        boolean exceptionFound = false;

        attribute.setValue(new Integer(SAMPLE_INT)); // A simple object
        try {
            container.render(attribute, null, request, response);
        } catch (TilesException e) {
            LOG.debug("Intercepted a TilesException, it is correct", e);
            exceptionFound = true;
        }

        assertTrue("An attribute of 'object' type cannot be rendered",
                exceptionFound);
    }

    /**
     * Tests is attributes are rendered correctly according to users roles.
     *
     * @throws TilesException If a problem arises during rendering.
     * @throws IOException If a problem arises during rendering or writing in the writer.
     */
    public void testAttributeCredentials() throws TilesException, IOException {
        RoleMockHttpServletRequest request = new RoleMockHttpServletRequest("myrole");
        MockHttpSession session = new MockHttpSession();
        request.setHttpSession(session);
        MockHttpServletResponse response = new MockHttpServletResponse();
        Attribute attribute = new Attribute((Object) "This is the value", "myrole");
        StringWriter writer = new StringWriter();
        container.render(attribute, writer, request, response);
        writer.close();
        assertEquals("The attribute should have been rendered", writer
                .toString(), "This is the value");
        request = new RoleMockHttpServletRequest();
        writer = new StringWriter();
        container.render(attribute, writer, request, response);
        writer.close();
        assertNotSame("The attribute should have not been rendered", writer
                .toString(), "This is the value");
    }

    /**
     * Servlet request mock class that allows to choose the user roles.
     */
    private static class RoleMockHttpServletRequest extends MockHttpServletRequest {

        /**
         * Set containing the allowed roles.
         */
        private Set<String> roleSet;

        /**
         * Constructor.
         *
         * @param roles The roles to be allowed.
         */
        public RoleMockHttpServletRequest(String... roles) {
            roleSet = new HashSet<String>();
            for (String role : roles) {
                roleSet.add(role);
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean isUserInRole(String role) {
            return roleSet.contains(role);
        }
    }
}
