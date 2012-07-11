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
import java.net.URL;

import junit.framework.TestCase;

import org.apache.tiles.Attribute;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.locale.URLApplicationResource;
import org.apache.tiles.request.render.CannotRenderException;
import org.easymock.EasyMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version $Rev$ $Date$
 */
public class BasicTilesContainerTest extends TestCase {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(BasicTilesContainerTest.class);

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
        ApplicationContext context = EasyMock
                .createMock(ApplicationContext.class);
        URL url = getClass().getResource("/org/apache/tiles/factory/test-defs.xml");
        URLApplicationResource resource = new URLApplicationResource("/WEB-INF/tiles.xml", url);

        EasyMock.expect(context.getResource("/WEB-INF/tiles.xml"))
                .andReturn(resource);
        EasyMock.replay(context);
        AbstractTilesContainerFactory factory = new BasicTilesContainerFactory();
        container = (BasicTilesContainer) factory.createContainer(context);
    }

    /**
     * Tests basic Tiles container initialization.
     */
    public void testInitialization() {
        assertNotNull(container);
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
        Request request = EasyMock.createMock(Request.class);
        EasyMock.replay(request);
        boolean exceptionFound = false;

        attribute.setValue(new Integer(SAMPLE_INT)); // A simple object
        try {
            container.render(attribute, request);
        } catch (CannotRenderException e) {
            log.debug("Intercepted a TilesException, it is correct", e);
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
        Request request = EasyMock.createMock(Request.class);
        EasyMock.expect(request.isUserInRole("myrole")).andReturn(Boolean.TRUE);
        StringWriter writer = new StringWriter();
        EasyMock.expect(request.getWriter()).andReturn(writer);
        EasyMock.replay(request);
        Attribute attribute = new Attribute("This is the value", "myrole");
        attribute.setRenderer("string");
        container.render(attribute, request);
        writer.close();
        assertEquals("The attribute should have been rendered",
                "This is the value", writer.toString());
        EasyMock.reset(request);
        request = EasyMock.createMock(Request.class);
        EasyMock.expect(request.isUserInRole("myrole")).andReturn(Boolean.FALSE);
        EasyMock.replay(request);
        writer = new StringWriter();
        container.render(attribute, request);
        writer.close();
        assertNotSame("The attribute should have not been rendered",
                "This is the value", writer);
    }

    /**
     * Tests {@link BasicTilesContainer#evaluate(Attribute, Request)}.
     */
    public void testEvaluate() {
        Request request = EasyMock.createMock(Request.class);
        EasyMock.replay(request);
        Attribute attribute = new Attribute("This is the value");
        Object value = container.evaluate(attribute, request);
        assertEquals("The attribute has not been evaluated correctly",
                "This is the value", value);
    }
}
