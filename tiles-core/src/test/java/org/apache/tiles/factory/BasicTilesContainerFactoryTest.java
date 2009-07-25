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
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.LocaleDefinitionsFactory;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.locale.impl.DefaultLocaleResolver;
import org.apache.tiles.mock.RepeaterTilesRequestContextFactory;
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
    private TilesApplicationContext applicationContext;

    /**
     * The URL to load.
     */
    private URL url;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        applicationContext = EasyMock.createMock(TilesApplicationContext.class);
        url = getClass().getResource("/org/apache/tiles/config/tiles-defs.xml");
        EasyMock.expect(applicationContext.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        EasyMock.replay(applicationContext);
        factory = new CustomBasicTilesContainerFactory();
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createContainer(TilesApplicationContext)}.
     */
    public void testCreateContainer() {
        TilesContainer container = factory.createContainer(applicationContext);
        assertTrue("The class of the container is not correct",
                container instanceof BasicTilesContainer);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createRequestContextFactory(TilesApplicationContext)}.
     */
    public void testCreateRequestContextFactory() {
        TilesRequestContextFactory contextFactory = factory
                .createRequestContextFactory(null);
        assertTrue("The class of the context factory is not correct",
                contextFactory instanceof ChainedTilesRequestContextFactory);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createDefinitionsFactory(
     * TilesApplicationContext, TilesRequestContextFactory, LocaleResolver)}.
     */
    public void testCreateDefinitionsFactory() {
        TilesRequestContextFactory requestContextFactory = factory
                .createRequestContextFactory(applicationContext);
        LocaleResolver resolver = factory.createLocaleResolver(applicationContext,
                requestContextFactory);
        DefinitionsFactory defsFactory = factory.createDefinitionsFactory(
                applicationContext, requestContextFactory, resolver);
        assertTrue("The class of the definitions factory is not correct",
                defsFactory instanceof LocaleDefinitionsFactory);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createLocaleResolver(
     * TilesApplicationContext, TilesRequestContextFactory)}.
     */
    public void testCreateLocaleResolver() {
        TilesRequestContextFactory requestContextFactory = factory
                .createRequestContextFactory(applicationContext);
        LocaleResolver localeResolver = factory.createLocaleResolver(applicationContext,
                requestContextFactory);
        assertTrue("The class of the locale resolver is not correct",
                localeResolver instanceof DefaultLocaleResolver);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createDefinitionsReader(
     * TilesApplicationContext, TilesRequestContextFactory)}.
     */
    public void testCreateDefinitionsReader() {
        TilesRequestContextFactory requestContextFactory = factory
                .createRequestContextFactory(applicationContext);
        DefinitionsReader reader = factory.createDefinitionsReader(applicationContext,
                requestContextFactory);
        assertTrue("The class of the reader is not correct",
                reader instanceof DigesterDefinitionsReader);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#getSourceURLs(TilesApplicationContext, TilesRequestContextFactory)}.
     */
    public void testGetSourceURLs() {
        TilesRequestContextFactory requestContextFactory = factory
                .createRequestContextFactory(applicationContext);
        List<URL> urls = factory.getSourceURLs(applicationContext, requestContextFactory);
        assertEquals("The urls list is not one-sized", 1, urls.size());
        assertEquals("The URL is not correct", url, urls.get(0));
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createAttributeEvaluatorFactory(
     * TilesApplicationContext, TilesRequestContextFactory, LocaleResolver)}.
     */
    public void testCreateAttributeEvaluatorFactory() {
        TilesRequestContextFactory requestContextFactory = factory
                .createRequestContextFactory(applicationContext);
        LocaleResolver resolver = factory.createLocaleResolver(applicationContext,
                requestContextFactory);
        AttributeEvaluatorFactory attributeEvaluatorFactory = factory
                .createAttributeEvaluatorFactory(applicationContext,
                        requestContextFactory, resolver);
        assertTrue(
                "The class of the evaluator is not correct",
                attributeEvaluatorFactory.getAttributeEvaluator((String) null) instanceof DirectAttributeEvaluator);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createPreparerFactory(TilesApplicationContext, TilesRequestContextFactory)}.
     */
    public void testCreatePreparerFactory() {
        TilesRequestContextFactory requestContextFactory = factory
                .createRequestContextFactory(applicationContext);
        PreparerFactory preparerFactory = factory.createPreparerFactory(
                applicationContext, requestContextFactory);
        assertTrue("The class of the preparer factory is not correct",
                preparerFactory instanceof BasicPreparerFactory);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createRendererFactory(
     * TilesApplicationContext, TilesRequestContextFactory, TilesContainer, AttributeEvaluatorFactory)}.
     */
    public void testCreateRendererFactory() {
        TilesContainer container = factory.createContainer(applicationContext);
        TilesRequestContextFactory requestContextFactory = factory
                .createRequestContextFactory(applicationContext);
        LocaleResolver resolver = factory.createLocaleResolver(applicationContext,
                requestContextFactory);
        AttributeEvaluatorFactory attributeEvaluatorFactory = factory
                .createAttributeEvaluatorFactory(applicationContext,
                        requestContextFactory, resolver);
        RendererFactory rendererFactory = factory.createRendererFactory(
                applicationContext, requestContextFactory, container,
                attributeEvaluatorFactory);
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
     * Tests
     * {@link BasicTilesContainerFactory#createDefaultAttributeRenderer(TilesApplicationContext,
     * TilesRequestContextFactory, TilesContainer, AttributeEvaluatorFactory)}.
     */
    public void testCreateDefaultAttributeRenderer() {
        TilesContainer container = factory.createContainer(applicationContext);
        TilesRequestContextFactory requestContextFactory = factory
                .createRequestContextFactory(applicationContext);
        LocaleResolver resolver = factory.createLocaleResolver(applicationContext,
                requestContextFactory);
        AttributeEvaluatorFactory attributeEvaluatorFactory = factory
        .createAttributeEvaluatorFactory(applicationContext,
                requestContextFactory, resolver);
        AttributeRenderer renderer = factory.createDefaultAttributeRenderer(
                applicationContext, requestContextFactory, container,
                attributeEvaluatorFactory);
        assertTrue("The default renderer class is not correct",
                renderer instanceof UntypedAttributeRenderer);
    }

    /**
     * A test Tiles container factory.
     */
    public static class CustomBasicTilesContainerFactory extends BasicTilesContainerFactory {

        /** {@inheritDoc} */
        @Override
        protected void registerChainedRequestContextFactories(
                ChainedTilesRequestContextFactory contextFactory) {
            List<TilesRequestContextFactory> factories =
                new ArrayList<TilesRequestContextFactory>(1);
            RepeaterTilesRequestContextFactory factory = new RepeaterTilesRequestContextFactory();
            factory.setRequestContextFactory(contextFactory);
            factories.add(factory);
            contextFactory.setFactories(factories);
        }
    }
}
