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

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.UnresolvingLocaleDefinitionsFactory;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.locale.impl.DefaultLocaleResolver;
import org.apache.tiles.preparer.BasicPreparerFactory;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererFactory;
import org.apache.tiles.renderer.TypeDetectingAttributeRenderer;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.renderer.impl.ChainedDelegateAttributeRenderer;
import org.apache.tiles.renderer.impl.DelegateAttributeRenderer;
import org.apache.tiles.request.ApplicationContext;
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
    private ApplicationContext applicationContext;

    /**
     * The URL to load.
     */
    private URL url;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        applicationContext = EasyMock.createMock(ApplicationContext.class);
        url = getClass().getResource("/org/apache/tiles/config/tiles-defs.xml");
        EasyMock.expect(applicationContext.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        EasyMock.replay(applicationContext);
        factory = new BasicTilesContainerFactory();
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createContainer(ApplicationContext)}.
     */
    public void testCreateContainer() {
        TilesContainer container = factory.createContainer(applicationContext);
        assertTrue("The class of the container is not correct",
                container instanceof BasicTilesContainer);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createDefinitionsFactory(
     * ApplicationContext, LocaleResolver)}.
     */
    public void testCreateDefinitionsFactory() {
        LocaleResolver resolver = factory.createLocaleResolver(applicationContext);
        DefinitionsFactory defsFactory = factory.createDefinitionsFactory(
                applicationContext, resolver);
        assertTrue("The class of the definitions factory is not correct",
                defsFactory instanceof UnresolvingLocaleDefinitionsFactory);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createLocaleResolver(
     * ApplicationContext)}.
     */
    public void testCreateLocaleResolver() {
        LocaleResolver localeResolver = factory.createLocaleResolver(applicationContext);
        assertTrue("The class of the locale resolver is not correct",
                localeResolver instanceof DefaultLocaleResolver);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createDefinitionsReader(
     * ApplicationContext)}.
     */
    public void testCreateDefinitionsReader() {
        DefinitionsReader reader = factory.createDefinitionsReader(applicationContext);
        assertTrue("The class of the reader is not correct",
                reader instanceof DigesterDefinitionsReader);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#getSourceURLs(ApplicationContext)}.
     */
    public void testGetSourceURLs() {
        List<URL> urls = factory.getSourceURLs(applicationContext);
        assertEquals("The urls list is not one-sized", 1, urls.size());
        assertEquals("The URL is not correct", url, urls.get(0));
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createAttributeEvaluatorFactory(
     * ApplicationContext, LocaleResolver)}.
     */
    public void testCreateAttributeEvaluatorFactory() {
        LocaleResolver resolver = factory.createLocaleResolver(applicationContext);
        AttributeEvaluatorFactory attributeEvaluatorFactory = factory
                .createAttributeEvaluatorFactory(applicationContext,
                        resolver);
        assertTrue(
                "The class of the evaluator is not correct",
                attributeEvaluatorFactory.getAttributeEvaluator((String) null) instanceof DirectAttributeEvaluator);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createPreparerFactory(ApplicationContext)}.
     */
    public void testCreatePreparerFactory() {
        PreparerFactory preparerFactory = factory.createPreparerFactory(
                applicationContext);
        assertTrue("The class of the preparer factory is not correct",
                preparerFactory instanceof BasicPreparerFactory);
    }

    /**
     * Tests {@link BasicTilesContainerFactory#createRendererFactory(
     * ApplicationContext, TilesContainer, AttributeEvaluatorFactory)}.
     */
    public void testCreateRendererFactory() {
        TilesContainer container = factory.createContainer(applicationContext);
        LocaleResolver resolver = factory.createLocaleResolver(applicationContext);
        AttributeEvaluatorFactory attributeEvaluatorFactory = factory
                .createAttributeEvaluatorFactory(applicationContext,
                        resolver);
        RendererFactory rendererFactory = factory.createRendererFactory(
                applicationContext, container, attributeEvaluatorFactory);
        assertTrue("The class of the renderer factory is not correct",
                rendererFactory instanceof BasicRendererFactory);
        AttributeRenderer renderer = rendererFactory.getRenderer("string");
        assertNotNull("The string renderer is null", renderer);
        assertTrue("The string renderer class is not correct",
                renderer instanceof DelegateAttributeRenderer);
        renderer = rendererFactory.getRenderer("template");
        assertNotNull("The template renderer is null", renderer);
        assertTrue("The template renderer class is not correct",
                renderer instanceof DelegateAttributeRenderer);
        renderer = rendererFactory.getRenderer("definition");
        assertNotNull("The definition renderer is null", renderer);
        assertTrue("The definition renderer class is not correct",
                renderer instanceof DelegateAttributeRenderer);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createDefaultAttributeRenderer(BasicRendererFactory,
     * ApplicationContext, TilesContainer, AttributeEvaluatorFactory)}.
     */
    public void testCreateDefaultAttributeRenderer() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        BasicRendererFactory rendererFactory = createMock(BasicRendererFactory.class);
        AttributeRenderer stringRenderer = createMock(TypeDetectingAttributeRenderer.class);
        AttributeRenderer templateRenderer = createMock(TypeDetectingAttributeRenderer.class);
        AttributeRenderer definitionRenderer = createMock(TypeDetectingAttributeRenderer.class);

        expect(rendererFactory.getRenderer("string")).andReturn(stringRenderer);
        expect(rendererFactory.getRenderer("template")).andReturn(templateRenderer);
        expect(rendererFactory.getRenderer("definition")).andReturn(definitionRenderer);

        replay(container, attributeEvaluatorFactory, rendererFactory);
        AttributeRenderer renderer = factory.createDefaultAttributeRenderer(
                rendererFactory, applicationContext, container,
                attributeEvaluatorFactory);
        assertTrue("The default renderer class is not correct",
                renderer instanceof ChainedDelegateAttributeRenderer);
        verify(container, attributeEvaluatorFactory, rendererFactory);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createStringAttributeRenderer(BasicRendererFactory,
     * ApplicationContext, TilesContainer, AttributeEvaluatorFactory)}.
     */
    public void testCreateStringAttributeRenderer() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        BasicRendererFactory rendererFactory = createMock(BasicRendererFactory.class);

        replay(container, attributeEvaluatorFactory, rendererFactory);
        AttributeRenderer renderer = factory.createStringAttributeRenderer(
                rendererFactory, applicationContext, container,
                attributeEvaluatorFactory);
        assertTrue("The renderer class is not correct",
                renderer instanceof DelegateAttributeRenderer);
        verify(container, attributeEvaluatorFactory, rendererFactory);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createTemplateAttributeRenderer(BasicRendererFactory,
     * ApplicationContext, TilesContainer, AttributeEvaluatorFactory)}.
     */
    public void testCreateTemplateAttributeRenderer() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        BasicRendererFactory rendererFactory = createMock(BasicRendererFactory.class);

        replay(container, attributeEvaluatorFactory, rendererFactory);
        AttributeRenderer renderer = factory.createTemplateAttributeRenderer(
                rendererFactory, applicationContext, container,
                attributeEvaluatorFactory);
        assertTrue("The renderer class is not correct",
                renderer instanceof DelegateAttributeRenderer);
        verify(container, attributeEvaluatorFactory, rendererFactory);
    }

    /**
     * Tests
     * {@link BasicTilesContainerFactory#createDefinitionAttributeRenderer(BasicRendererFactory,
     * ApplicationContext, TilesContainer, AttributeEvaluatorFactory)}.
     */
    public void testCreateDefinitionAttributeRenderer() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        BasicRendererFactory rendererFactory = createMock(BasicRendererFactory.class);

        replay(container, attributeEvaluatorFactory, rendererFactory);
        AttributeRenderer renderer = factory.createDefinitionAttributeRenderer(
                rendererFactory, applicationContext, container,
                attributeEvaluatorFactory);
        assertTrue("The renderer class is not correct",
                renderer instanceof DelegateAttributeRenderer);
        verify(container, attributeEvaluatorFactory, rendererFactory);
    }
}
