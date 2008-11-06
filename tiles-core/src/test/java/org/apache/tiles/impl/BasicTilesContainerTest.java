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
import java.util.Vector;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Attribute;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.ChainedTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.TilesContainerFactory;
import org.apache.tiles.mock.RepeaterTilesApplicationContextFactory;
import org.apache.tiles.mock.RepeaterTilesRequestContextFactory;
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
    @SuppressWarnings("deprecation")
    @Override
    public void setUp() {
        ServletContext context = EasyMock.createMock(ServletContext.class);
        URL url = getClass().getResource("/org/apache/tiles/factory/test-defs.xml");

        Vector<String> v = new Vector<String>();
        v.add(ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES);
        v.add(ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES);

        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParameter(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(null);
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
        AbstractTilesContainerFactory factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        container = (BasicTilesContainer) factory.createContainer(context);
    }

    /**
     * Tests basic Tiles container initialization.
     */
    public void testInitialization() {
        assertNotNull(container);
        assertNotNull(container.getRequestContextFactory());
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
        TilesRequestContext request = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.replay(request);
        boolean exceptionFound = false;

        attribute.setValue(new Integer(SAMPLE_INT)); // A simple object
        try {
            container.render(attribute, null, request);
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
     * @throws IOException If a problem arises during rendering or writing in the writer.
     */
    public void testAttributeCredentials() throws IOException {
        TilesRequestContext request = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.expect(request.isUserInRole("myrole")).andReturn(Boolean.TRUE);
        EasyMock.replay(request);
        Attribute attribute = new Attribute((Object) "This is the value", "myrole");
        attribute.setRenderer("string");
        StringWriter writer = new StringWriter();
        container.render(attribute, writer, request);
        writer.close();
        assertEquals("The attribute should have been rendered",
                "This is the value", writer.toString());
        EasyMock.reset(request);
        request = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.expect(request.isUserInRole("myrole")).andReturn(Boolean.FALSE);
        EasyMock.replay(request);
        writer = new StringWriter();
        container.render(attribute, writer, request);
        writer.close();
        assertNotSame("The attribute should have not been rendered",
                "This is the value", writer);
    }

    /**
     * Tests {@link BasicTilesContainer#evaluate(Attribute, Object...)}.
     */
    public void testEvaluate() {
        TilesRequestContext request = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.replay(request);
        Attribute attribute = new Attribute((Object) "This is the value");
        Object value = container.evaluate(attribute, request);
        assertEquals("The attribute has not been evaluated correctly",
                "This is the value", value);
    }
}
