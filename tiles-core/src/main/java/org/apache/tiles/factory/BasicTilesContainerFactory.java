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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
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
import org.apache.tiles.preparer.BasicPreparerFactory;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererFactory;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.renderer.impl.DefinitionAttributeRenderer;
import org.apache.tiles.renderer.impl.StringAttributeRenderer;
import org.apache.tiles.renderer.impl.TemplateAttributeRenderer;
import org.apache.tiles.renderer.impl.UntypedAttributeRenderer;
import org.apache.tiles.servlet.context.ServletTilesContextFactory;

/**
 * Factory that builds a standard Tiles container using only Java code.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class BasicTilesContainerFactory extends AbstractTilesContainerFactory {

    /** {@inheritDoc} */
    @Override
    public TilesContainer createContainer(Object context) {
        BasicTilesContainer container = new BasicTilesContainer();
        TilesContextFactory contextFactory = createContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        container.setContextFactory(contextFactory);
        container.setApplicationContext(applicationContext);
        container.setDefinitionsFactory(createDefinitionsFactory(context,
                applicationContext, contextFactory));
        AttributeEvaluator evaluator = createEvaluator(context,
                applicationContext, contextFactory);
        container.setEvaluator(evaluator);
        container.setPreparerFactory(createPreparerFactory(context,
                applicationContext, contextFactory));
        container.setRendererFactory(createRendererFactory(context,
                applicationContext, contextFactory, container, evaluator));
        return container;
    }

    /**
     * Create a Tiles context factory. By default it creates a
     * {@link ServletTilesContextFactory}.
     *
     * @param context The context.
     * @return The context factory.
     */
    protected TilesContextFactory createContextFactory(Object context) {
        return new ServletTilesContextFactory();
    }

    /**
     * Creates the definitions factory. By default it creates a
     * {@link UrlDefinitionsFactory} with default dependencies.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @return The definitions factory.
     */
    protected DefinitionsFactory createDefinitionsFactory(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory) {
        UrlDefinitionsFactory factory = new UrlDefinitionsFactory();
        factory.setApplicationContext(applicationContext);
        factory.setLocaleResolver(createLocaleResolver(context,
                applicationContext, contextFactory));
        factory.setReader(createDefinitionsReader(context, applicationContext,
                contextFactory));
        factory.setSourceURLs(getSourceURLs(context, applicationContext,
                contextFactory));
        factory.refresh();
        return factory;
    }

    /**
     * Creates the locale resolver. By default it creates a
     * {@link DefaultLocaleResolver}.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @return The locale resolver.
     */
    protected LocaleResolver createLocaleResolver(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory) {
        return new DefaultLocaleResolver();
    }

    /**
     * Creates the definitions reader. By default it creates a
     * {@link DigesterDefinitionsReader}.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @return The definitions reader.
     */
    protected DefinitionsReader createDefinitionsReader(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory) {
        return new DigesterDefinitionsReader();
    }

    /**
     * Returns a list containing the URLs to be parsed. By default, it returns a
     * list containing the URL point to "/WEB-INF/tiles.xml".
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @return The source URLs.
     */
    protected List<URL> getSourceURLs(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory) {
        List<URL> retValue = new ArrayList<URL>(1);
        try {
            retValue.add(applicationContext.getResource("/WEB-INF/tiles.xml"));
        } catch (IOException e) {
            throw new TilesContainerFactoryException(
                    "Cannot get URL: /WEB-INF/tiles.xml", e);
        }
        return retValue;
    }

    /**
     * Creates the attribute evaluator to use. By default it returns a {@link DirectAttributeEvaluator}.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @return The evaluator.
     */
    protected AttributeEvaluator createEvaluator(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory) {
        return new DirectAttributeEvaluator();
    }

    /**
     * Creates the preparer factory to use. By default it returns a
     * {@link BasicPreparerFactory}.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @return The preparer factory.
     */
    protected PreparerFactory createPreparerFactory(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory) {
        return new BasicPreparerFactory();
    }

    /**
     * Creates a renderer factory. By default it returns a
     * {@link BasicRendererFactory}, composed of an
     * {@link UntypedAttributeRenderer} as default, and
     * {@link StringAttributeRenderer}, {@link TemplateAttributeRenderer} and
     * {@link DefinitionAttributeRenderer}.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @param container The container.
     * @param evaluator The evaluator.
     * @return The renderer factory.
     */
    protected RendererFactory createRendererFactory(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory, TilesContainer container,
            AttributeEvaluator evaluator) {
        BasicRendererFactory retValue = new BasicRendererFactory();
        retValue.setApplicationContext(applicationContext);
        retValue.setContextFactory(contextFactory);
        retValue.setContainer(container);
        retValue.setEvaluator(evaluator);
        retValue.setDefaultRenderer(createDefaultAttributeRenderer(context,
                applicationContext, contextFactory, container, evaluator));
        registerAttributeRenderers(retValue, context, applicationContext,
                contextFactory, container, evaluator);
        return retValue;
    }

    /**
     * Creates the default attribute renderer. By default it is an
     * {@link UntypedAttributeRenderer}.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @param container The container.
     * @param evaluator The evaluator.
     * @return The default attribute renderer.
     */
    protected AttributeRenderer createDefaultAttributeRenderer(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory, TilesContainer container,
            AttributeEvaluator evaluator) {
        UntypedAttributeRenderer retValue = new UntypedAttributeRenderer();
        retValue.setApplicationContext(applicationContext);
        retValue.setContainer(container);
        retValue.setContextFactory(contextFactory);
        retValue.setEvaluator(evaluator);
        return retValue;
    }

    /**
     * Registers attribute renderers in a {@link BasicRendererFactory}. By
     * default, it registers a {@link StringAttributeRenderer}, a
     * {@link TemplateAttributeRenderer} and a
     * {@link DefinitionAttributeRenderer}.
     *
     * @param rendererFactory The renderer factory to configure.
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @param container The container.
     * @param evaluator The evaluator.
     */
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory, Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory, TilesContainer container,
            AttributeEvaluator evaluator) {
        StringAttributeRenderer stringRenderer = new StringAttributeRenderer();
        stringRenderer.setApplicationContext(applicationContext);
        stringRenderer.setContextFactory(contextFactory);
        stringRenderer.setEvaluator(evaluator);
        rendererFactory.registerRenderer("string", stringRenderer);

        TemplateAttributeRenderer templateRenderer = new TemplateAttributeRenderer();
        templateRenderer.setApplicationContext(applicationContext);
        templateRenderer.setContextFactory(contextFactory);
        templateRenderer.setEvaluator(evaluator);
        rendererFactory.registerRenderer("template", templateRenderer);

        DefinitionAttributeRenderer definitionRenderer = new DefinitionAttributeRenderer();
        definitionRenderer.setApplicationContext(applicationContext);
        definitionRenderer.setContainer(container);
        definitionRenderer.setContextFactory(contextFactory);
        definitionRenderer.setEvaluator(evaluator);
        rendererFactory.registerRenderer("definition", definitionRenderer);
    }
}
