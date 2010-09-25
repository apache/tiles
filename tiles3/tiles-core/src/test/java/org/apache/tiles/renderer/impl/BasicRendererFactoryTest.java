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

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.util.ApplicationContextAware;
import org.junit.Before;
import org.junit.Test;

/**
 * Basic renderer factory implementation.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class BasicRendererFactoryTest {

    /**
     * The renderer factory.
     */
    private BasicRendererFactory rendererFactory;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        rendererFactory = new BasicRendererFactory();
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        rendererFactory.setApplicationContext(applicationContext);
        rendererFactory.setContainer(container);
        replay(applicationContext, container);
    }

    /**
     * Tests execution and
     * {@link BasicRendererFactory#getRenderer(String)}.
     */
    @Test
    public void testInitAndGetRenderer() {
        AttributeRenderer renderer1 = createMock(AttributeRenderer.class);
        AttributeRenderer renderer2 = createMock(AttributeRenderer.class);
        AttributeRenderer renderer3 = createMock(AttributeRenderer.class);
        AttributeRenderer renderer4 = createMock(AttributeRenderer.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);

        replay(renderer1, renderer2, renderer3, renderer4, applicationContext, attributeEvaluatorFactory);
        rendererFactory.registerRenderer("string", renderer1);
        rendererFactory.registerRenderer("test", renderer2);
        rendererFactory.registerRenderer("test2", renderer3);
        rendererFactory.setDefaultRenderer(renderer4);
        rendererFactory.setApplicationContext(applicationContext);
        rendererFactory.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        AttributeRenderer renderer = rendererFactory.getRenderer("string");
        assertSame(renderer1, renderer);
        renderer = rendererFactory.getRenderer("test");
        assertSame(renderer2, renderer);
        renderer = rendererFactory.getRenderer("test2");
        assertSame(renderer3, renderer);
        renderer = rendererFactory.getRenderer(ExtendedStringAttributeRenderer.class
                .getName());
        assertSame(applicationContext, ((ExtendedStringAttributeRenderer) renderer).applicationContext);
        renderer = rendererFactory.getRenderer(null);
        assertSame(renderer4, renderer);
        verify(renderer1, renderer2, renderer3, renderer4, applicationContext, attributeEvaluatorFactory);
    }

    /**
     * Tests {@link BasicRendererFactory#setContainer(TilesContainer)}.
     */
    @Test
    public void testSetContainer() {
        assertNotNull("The container is null", rendererFactory.container);
    }

    /**
     * Tests
     * {@link BasicRendererFactory#setApplicationContext(ApplicationContext)}.
     */
    @Test
    public void testSetApplicationContext() {
        assertNotNull("The application context is null",
                rendererFactory.applicationContext);
    }

    /**
     * Tests {@link BasicRendererFactory#initializeRenderer(AttributeRenderer)}.
     */
    @Test
    public void testInitializeRenderer() {
        // TODO This will be removed in future, only named renderers should be available.
    }

    /**
     * Mock renderer.
     *
     * @version $Rev$ $Date$
     */
    public static class ExtendedStringAttributeRenderer extends AbstractBaseAttributeRenderer implements ApplicationContextAware {

        /**
         * The application context.
         */
        private ApplicationContext applicationContext;

        /** {@inheritDoc} */
        @Override
        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        /** {@inheritDoc} */
        @Override
        public void write(Object value, Attribute attribute,
                Request request)
                throws IOException {
            request.getWriter().write(value.toString());
        }

        /** {@inheritDoc} */
        public boolean isRenderable(Object value, Attribute attribute,
                Request request) {
            return value instanceof String;
        }
    }
}
