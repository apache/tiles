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
package org.apache.tiles.factory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.ChainedTilesContextFactory;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.locale.impl.DefaultLocaleResolver;
import org.apache.tiles.mock.RepeaterTilesContextFactory;
import org.apache.tiles.preparer.BasicPreparerFactory;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererFactory;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.renderer.impl.DefinitionAttributeRenderer;
import org.apache.tiles.renderer.impl.StringAttributeRenderer;
import org.apache.tiles.renderer.impl.TemplateAttributeRenderer;
import org.apache.tiles.renderer.impl.UntypedAttributeRenderer;
import org.easymock.EasyMock;

/**
 * Tests {@link BasicTilesContainerFactory}.
 *
 * @version $Rev$ $Date$
 */
public class BasicTilesContainerFactoryTest extends TestCase {

    /**
     * The factory to test.
     */
    private BasicTilesContainerFactory factory;

    /**
     * The context object.
     */
    private TilesApplicationContext context;

    /**
     * The URL to load.
     */
    private URL url;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        context = EasyMock.createMock(TilesApplicationContext.class);
        url = getClass().getResource("/org/apache/tiles/config/tiles-defs.xml");
        EasyMock.expect(context.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        EasyMock.replay(context);
        factory = new CustomBasicTilesContainerFactory(context);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createContainer(Object)}.
     */
    public void testCreateContainer() {
        TilesContainer container = factory.createContainer(context);
        assertTrue("The class of the container is not correct",
                container instanceof BasicTilesContainer);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createContextFactory(Object)}.
     */
    public void testCreateContextFactory() {
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        assertTrue("The class of the context factory is not correct",
                contextFactory instanceof ChainedTilesContextFactory);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createDefinitionsFactory(Object,
     * TilesApplicationContext, TilesContextFactory, LocaleResolver)}.
     */
    public void testCreateDefinitionsFactory() {
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        LocaleResolver resolver = factory.createLocaleResolver(context,
                applicationContext, contextFactory);
        DefinitionsFactory defsFactory = factory.createDefinitionsFactory(
                context, applicationContext, contextFactory, resolver);
        assertTrue("The class of the definitions factory is not correct",
                defsFactory instanceof UrlDefinitionsFactory);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createLocaleResolver(Object,
     * TilesApplicationContext, TilesContextFactory)}.
     */
    public void testCreateLocaleResolver() {
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        LocaleResolver localeResolver = factory.createLocaleResolver(context,
                applicationContext, contextFactory);
        assertTrue("The class of the locale resolver is not correct",
                localeResolver instanceof DefaultLocaleResolver);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createDefinitionsReader(Object,
     * TilesApplicationContext, TilesContextFactory)}.
     */
    public void testCreateDefinitionsReader() {
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        DefinitionsReader reader = factory.createDefinitionsReader(context,
                applicationContext, contextFactory);
        assertTrue("The class of the reader is not correct",
                reader instanceof DigesterDefinitionsReader);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#getSourceURLs(Object, TilesApplicationContext, TilesContextFactory)}.
     */
    public void testGetSourceURLs() {
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        List<URL> urls = factory.getSourceURLs(context, applicationContext,
                contextFactory);
        assertEquals("The urls list is not one-sized", 1, urls.size());
        assertEquals("The URL is not correct", url, urls.get(0));
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createEvaluator(Object,
     * TilesApplicationContext, TilesContextFactory, LocaleResolver)}.
     */
    public void testCreateEvaluator() {
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        LocaleResolver resolver = factory.createLocaleResolver(context,
                applicationContext, contextFactory);
        AttributeEvaluator evaluator = factory.createEvaluator(context,
                applicationContext, contextFactory, resolver);
        assertTrue("The class of the evaluator is not correct",
                evaluator instanceof DirectAttributeEvaluator);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createPreparerFactory(Object, TilesApplicationContext, TilesContextFactory)}.
     */
    public void testCreatePreparerFactory() {
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        PreparerFactory preparerFactory = factory.createPreparerFactory(
                context, applicationContext, contextFactory);
        assertTrue("The class of the preparer factory is not correct",
                preparerFactory instanceof BasicPreparerFactory);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createRendererFactory(Object,
     * TilesApplicationContext, TilesContextFactory, TilesContainer, AttributeEvaluator)}.
     */
    public void testCreateRendererFactory() {
        TilesContainer container = factory.createContainer(context);
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        LocaleResolver resolver = factory.createLocaleResolver(context,
                applicationContext, contextFactory);
        AttributeEvaluator evaluator = factory.createEvaluator(context,
                applicationContext, contextFactory, resolver);
        RendererFactory rendererFactory = factory.createRendererFactory(
                context, applicationContext, contextFactory, container,
                evaluator);
        assertTrue("The class of the renderer factory is not correct",
                rendererFactory instanceof BasicRendererFactory);
        AttributeRenderer renderer = rendererFactory.getRenderer("string");
        assertNotNull("The string renderer is null", renderer);
        assertTrue("The string renderer class is not correct",
                renderer instanceof StringAttributeRenderer);
        renderer = rendererFactory.getRenderer("template");
        assertNotNull("The template renderer is null", renderer);
        assertTrue("The template renderer class is not correct",
                renderer instanceof TemplateAttributeRenderer);
        renderer = rendererFactory.getRenderer("definition");
        assertNotNull("The definition renderer is null", renderer);
        assertTrue("The definition renderer class is not correct",
                renderer instanceof DefinitionAttributeRenderer);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createDefaultAttributeRenderer(Object,
     * TilesApplicationContext, TilesContextFactory, TilesContainer, AttributeEvaluator)}.
     */
    public void testCreateDefaultAttributeRenderer() {
        TilesContainer container = factory.createContainer(context);
        TilesContextFactory contextFactory = factory
                .createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        LocaleResolver resolver = factory.createLocaleResolver(context,
                applicationContext, contextFactory);
        AttributeEvaluator evaluator = factory.createEvaluator(context,
                applicationContext, contextFactory, resolver);
        AttributeRenderer renderer = factory.createDefaultAttributeRenderer(
                context, applicationContext, contextFactory, container,
                evaluator);
        assertTrue("The default renderer class is not correct",
                renderer instanceof UntypedAttributeRenderer);
    }

    /**
     * A test Tiles container factory.
     */
    public static class CustomBasicTilesContainerFactory extends BasicTilesContainerFactory {

        /**
         * The application context.
         */
        private TilesApplicationContext applicationContext;

        /**
         * Constructor.
         *
         * @param applicationContext The Tiles application context.
         */
        public CustomBasicTilesContainerFactory(TilesApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        /** {@inheritDoc} */
        @Override
        protected void registerChainedContextFactories(Object context,
                ChainedTilesContextFactory contextFactory) {
            List<TilesContextFactory> factories =
                new ArrayList<TilesContextFactory>(1);
            TilesContextFactory factory = new RepeaterTilesContextFactory(applicationContext);
            factories.add(factory);
            contextFactory.setFactories(factories);
        }

    }
}
