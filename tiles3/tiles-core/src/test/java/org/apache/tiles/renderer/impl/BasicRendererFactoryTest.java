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

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.request.ApplicationContext;
import org.easymock.EasyMock;

import junit.framework.TestCase;

/**
 * Basic renderer factory implementation.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class BasicRendererFactoryTest extends TestCase {

    /**
     * The renderer factory.
     */
    private BasicRendererFactory rendererFactory;

    /** {@inheritDoc} */
    @Override
    public void setUp() throws Exception {
        rendererFactory = new BasicRendererFactory();
        ApplicationContext applicationContext = EasyMock
                .createMock(ApplicationContext.class);
        TilesRequestContextFactory contextFactory = EasyMock
                .createMock(TilesRequestContextFactory.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        rendererFactory.setApplicationContext(applicationContext);
        rendererFactory.setRequestContextFactory(contextFactory);
        rendererFactory.setContainer(container);
        EasyMock.replay(applicationContext, contextFactory, container);
    }

    /**
     * Tests execution and
     * {@link BasicRendererFactory#getRenderer(String)}.
     */
    public void testInitAndGetRenderer() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(BasicRendererFactory.TYPE_RENDERERS_INIT_PARAM, "test,"
                + StringAttributeRenderer.class.getName() + ";test2,"
                + StringAttributeRenderer.class.getName());
        rendererFactory.registerRenderer("string", new StringAttributeRenderer());
        rendererFactory.registerRenderer("test", new StringAttributeRenderer());
        rendererFactory.registerRenderer("test2", new StringAttributeRenderer());
        AttributeRenderer renderer = rendererFactory.getRenderer("string");
        assertNotNull("The renderer is null", renderer);
        assertTrue("The class of the renderer is wrong", renderer instanceof StringAttributeRenderer);
        renderer = rendererFactory.getRenderer("test");
        assertNotNull("The renderer is null", renderer);
        assertTrue("The class of the renderer is wrong", renderer instanceof StringAttributeRenderer);
        renderer = rendererFactory.getRenderer("test2");
        assertNotNull("The renderer is null", renderer);
        assertTrue("The class of the renderer is wrong", renderer instanceof StringAttributeRenderer);
        renderer = rendererFactory.getRenderer(StringAttributeRenderer.class
                .getName());
        assertNotNull("The renderer is null", renderer);
        assertTrue("The class of the renderer is wrong", renderer instanceof StringAttributeRenderer);
    }

    /**
     * Tests {@link BasicRendererFactory#setContainer(TilesContainer)}.
     */
    public void testSetContainer() {
        assertNotNull("The container is null", rendererFactory.container);
    }

    /**
     * Tests {@link BasicRendererFactory#setRequestContextFactory(TilesRequestContextFactory)}.
     */
    public void testSetContextFactory() {
        assertNotNull("The context factory is null",
                rendererFactory.contextFactory);
    }

    /**
     * Tests
     * {@link BasicRendererFactory#setApplicationContext(ApplicationContext)}.
     */
    public void testSetApplicationContext() {
        assertNotNull("The application context is null",
                rendererFactory.applicationContext);
    }

    /**
     * Tests {@link BasicRendererFactory#initializeRenderer(AttributeRenderer)}.
     */
    public void testInitializeRenderer() {
        DefinitionAttributeRenderer renderer = new DefinitionAttributeRenderer();
        rendererFactory.initializeRenderer(renderer);
        assertNotNull("The container is null", renderer.container);
        assertNotNull("The context factory is null", renderer.contextFactory);
        assertNotNull("The application context is null",
                renderer.applicationContext);
    }
}
