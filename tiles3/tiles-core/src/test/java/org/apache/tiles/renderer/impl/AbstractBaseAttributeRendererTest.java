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
package org.apache.tiles.renderer.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.easymock.EasyMock;

/**
 * Tests {@link AbstractBaseAttributeRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractBaseAttributeRendererTest extends TestCase {

    /**
     * The renderer, no more abstract.
     */
    private MockAttributeRenderer renderer;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        renderer = new MockAttributeRenderer();
        renderer.setAttributeEvaluatorFactory(new BasicAttributeEvaluatorFactory(
                new DirectAttributeEvaluator()));
    }

    /**
     * Tests
     * {@link AbstractBaseAttributeRenderer#setRequestContextFactory(TilesRequestContextFactory)}.
     */
    public void testSetContextFactory() {
        TilesRequestContextFactory contextFactory = EasyMock
                .createMock(TilesRequestContextFactory.class);
        EasyMock.replay(contextFactory);
        renderer.setRequestContextFactory(contextFactory);
        assertNotNull("The context factory is null", renderer.contextFactory);
    }

    /**
     * Tests
     * {@link AbstractBaseAttributeRenderer#setApplicationContext(TilesApplicationContext)}.
     */
    public void testSetApplicationContext() {
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        EasyMock.replay(applicationContext);
        renderer.setApplicationContext(applicationContext);
        assertNotNull("The application context is null", renderer.applicationContext);
    }

    /**
     * Tests
     * {@link AbstractBaseAttributeRenderer#render(Attribute, TilesRequestContext)}.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    public void testRender() throws IOException {
        Attribute attribute = new Attribute();
        StringWriter writer = new StringWriter();
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        TilesRequestContextFactory contextFactory = EasyMock
                .createMock(TilesRequestContextFactory.class);
        TilesRequestContext requestContext = EasyMock
                .createMock(TilesRequestContext.class);
        EasyMock.expect(contextFactory.createRequestContext(applicationContext))
                .andReturn(requestContext);
        EasyMock.expect(requestContext.getWriter()).andReturn(writer);
        EasyMock.replay(applicationContext, contextFactory, requestContext);
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        renderer.render(attribute, requestContext);
        writer.close();
        assertEquals("Wrongly written", "wrote", writer.toString());
    }

    /**
     * Tests {@link AbstractBaseAttributeRenderer#getRequestContext(Object...)}.
     */
    public void testGetRequestContext() {
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        TilesRequestContextFactory contextFactory = EasyMock
                .createMock(TilesRequestContextFactory.class);
        TilesRequestContext requestContext = EasyMock
                .createMock(TilesRequestContext.class);
        EasyMock.expect(contextFactory.createRequestContext(applicationContext))
                .andReturn(requestContext);
        EasyMock.replay(applicationContext, contextFactory, requestContext);
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        assertTrue("This is not the expected request",
                requestContext == renderer.getRequestContext());
    }

    /**
     * Tests
     * {@link AbstractBaseAttributeRenderer#isPermitted(TilesRequestContext, Set)}.
     */
    public void testIsPermitted() {
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        TilesRequestContextFactory contextFactory = EasyMock
                .createMock(TilesRequestContextFactory.class);
        TilesRequestContext requestContext = EasyMock
                .createMock(TilesRequestContext.class);
        EasyMock.expect(contextFactory.createRequestContext(applicationContext))
                .andReturn(requestContext);
        EasyMock.expect(requestContext.isUserInRole("first")).andReturn(
                Boolean.TRUE).anyTimes();
        EasyMock.expect(requestContext.isUserInRole("second")).andReturn(
                Boolean.FALSE).anyTimes();
        EasyMock.replay(applicationContext, contextFactory, requestContext);
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        Set<String> roles = new HashSet<String>();
        roles.add("first");
        assertTrue("The role is not permitted", renderer.isPermitted(
                requestContext, roles));
        roles.clear();
        roles.add("second");
        assertFalse("The role is not permitted", renderer.isPermitted(
                requestContext, roles));
    }

    /**
     * The renderer with "write" implementation.
     */
    private static class MockAttributeRenderer extends AbstractBaseAttributeRenderer {

        /** {@inheritDoc} */
        @Override
        public void write(Object value, Attribute attribute,
                TilesRequestContext request)
                throws IOException {
            request.getWriter().write("wrote");
        }
    }
}
