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
import java.util.Locale;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.Refreshable;
import org.apache.tiles.definition.UnresolvingLocaleDefinitionsFactory;
import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.DefinitionDAO;
import org.apache.tiles.definition.dao.ResolvingLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.definition.pattern.BasicPatternDefinitionResolver;
import org.apache.tiles.definition.pattern.PatternDefinitionResolver;
import org.apache.tiles.definition.pattern.PatternDefinitionResolverAware;
import org.apache.tiles.definition.pattern.wildcard.WildcardDefinitionPatternMatcherFactory;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
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
import org.apache.tiles.renderer.impl.DefinitionAttributeRenderer;
import org.apache.tiles.renderer.impl.StringAttributeRenderer;
import org.apache.tiles.renderer.impl.TemplateAttributeRenderer;
import org.apache.tiles.request.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory that builds a standard Tiles container using only Java code.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class BasicTilesContainerFactory extends AbstractTilesContainerFactory {

    /**
     * The string renderer name.
     */
    protected static final String STRING_RENDERER_NAME = "string";

    /**
     * The template renderer name.
     */
    protected static final String TEMPLATE_RENDERER_NAME = "template";

    /**
     * The definition renderer name.
     */
    protected static final String DEFINITION_RENDERER_NAME = "definition";
    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(BasicTilesContainerFactory.class);

    /** {@inheritDoc} */
    @Override
    public TilesContainer createContainer(ApplicationContext applicationContext) {
        BasicTilesContainer container = instantiateContainer(applicationContext);
        container.setApplicationContext(applicationContext);
        LocaleResolver resolver = createLocaleResolver(applicationContext);
        container.setDefinitionsFactory(createDefinitionsFactory(applicationContext,
                resolver));
        AttributeEvaluatorFactory attributeEvaluatorFactory = createAttributeEvaluatorFactory(
                applicationContext, resolver);
        container.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        container.setPreparerFactory(createPreparerFactory(applicationContext));
        container.setRendererFactory(createRendererFactory(applicationContext,
                container, attributeEvaluatorFactory));
        return container;
    }

    /**
     * Instantiate the container, without initialization.
     *
     * @param context The Tiles application context object.
     * @return The instantiated container.
     * @since 2.1.1
     */
    protected BasicTilesContainer instantiateContainer(
            ApplicationContext context) {
        return new BasicTilesContainer();
    }

    /**
     * Creates the definitions factory. By default it creates a
     * {@link UnresolvingLocaleDefinitionsFactory} with default dependencies.
     *
     * @param applicationContext The Tiles application context.
     * @param resolver The locale resolver.
     * @return The definitions factory.
     * @since 2.1.1
     */
    protected DefinitionsFactory createDefinitionsFactory(ApplicationContext applicationContext,
            LocaleResolver resolver) {
        UnresolvingLocaleDefinitionsFactory factory = instantiateDefinitionsFactory(
                applicationContext, resolver);
        factory.setApplicationContext(applicationContext);
        factory.setLocaleResolver(resolver);
        factory.setDefinitionDAO(createLocaleDefinitionDao(applicationContext,
                resolver));
        if (factory instanceof Refreshable) {
            ((Refreshable) factory).refresh();
        }
        return factory;
    }

    /**
     * Instantiate a new definitions factory based on Locale.
     * @param applicationContext The Tiles application context.
     * @param resolver The locale resolver.
     * @return The definitions factory.
     * @since 2.2.1
     */
    protected UnresolvingLocaleDefinitionsFactory instantiateDefinitionsFactory(
            ApplicationContext applicationContext,
            LocaleResolver resolver) {
        return new UnresolvingLocaleDefinitionsFactory();
    }


    /**
     * Instantiate (and does not initialize) a Locale-based definition DAO.
     * @param applicationContext The Tiles application context.
     * @param resolver The locale resolver.
     * @return The definition DAO.
     * @since 2.1.1
     */
    protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(ApplicationContext applicationContext,
            LocaleResolver resolver) {
        ResolvingLocaleUrlDefinitionDAO dao = new ResolvingLocaleUrlDefinitionDAO();
        return dao;
    }

    /**
     * Creates a Locale-based definition DAO.
     * @param applicationContext The Tiles application context.
     * @param resolver The locale resolver.
     * @return The definition DAO.
     * @since 2.1.1
     */
    @SuppressWarnings("unchecked")
    protected DefinitionDAO<Locale> createLocaleDefinitionDao(ApplicationContext applicationContext,
            LocaleResolver resolver) {
        BaseLocaleUrlDefinitionDAO definitionDao = instantiateLocaleDefinitionDao(
                applicationContext, resolver);
        definitionDao.setReader(createDefinitionsReader(applicationContext));
        definitionDao.setSourceURLs(getSourceURLs(applicationContext));
        definitionDao.setApplicationContext(applicationContext);
        if (definitionDao instanceof PatternDefinitionResolverAware) {
            ((PatternDefinitionResolverAware<Locale>) definitionDao)
                    .setPatternDefinitionResolver(createPatternDefinitionResolver(Locale.class));
        }
        return definitionDao;
    }

    /**
     * Creates the locale resolver. By default it creates a
     * {@link DefaultLocaleResolver}.
     * @param applicationContext The Tiles application context.
     * @return The locale resolver.
     * @since 2.1.1
     */
    protected LocaleResolver createLocaleResolver(ApplicationContext applicationContext) {
        return new DefaultLocaleResolver();
    }

    /**
     * Creates the definitions reader. By default it creates a
     * {@link DigesterDefinitionsReader}.
     * @param applicationContext The Tiles application context.
     * @return The definitions reader.
     * @since 2.1.1
     */
    protected DefinitionsReader createDefinitionsReader(
            ApplicationContext applicationContext) {
        return new DigesterDefinitionsReader();
    }

    /**
     * Returns a list containing the URLs to be parsed. By default, it returns a
     * list containing the URL point to "/WEB-INF/tiles.xml".
     * @param applicationContext The Tiles application context.
     * @return The source URLs.
     * @since 2.1.1
     */
    protected List<URL> getSourceURLs(ApplicationContext applicationContext) {
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
     * Creates the attribute evaluator factory to use. By default it returns a
     * {@link BasicAttributeEvaluatorFactory} containing the
     * {@link DirectAttributeEvaluator} as the default evaluator.
     *
     * @param applicationContext The Tiles application context.
     * @param resolver The locale resolver.
     * @return The evaluator factory.
     * @since 2.2.0
     */
    protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(
            ApplicationContext applicationContext,
            LocaleResolver resolver) {
        return new BasicAttributeEvaluatorFactory(new DirectAttributeEvaluator());
    }

    /**
     * Creates the preparer factory to use. By default it returns a
     * {@link BasicPreparerFactory}.
     * @param applicationContext The Tiles application context.
     * @return The preparer factory.
     * @since 2.1.1
     */
    protected PreparerFactory createPreparerFactory(ApplicationContext applicationContext) {
        return new BasicPreparerFactory();
    }

    /**
     * Creates a renderer factory. By default it returns a
     * {@link BasicRendererFactory}, composed of an
     * {@link UntypedAttributeRenderer} as default, and
     * {@link StringAttributeRenderer}, {@link TemplateAttributeRenderer} and
     * {@link DefinitionAttributeRenderer}.
     * @param applicationContext The Tiles application context.
     * @param container The container.
     * @param attributeEvaluatorFactory The attribute evaluator factory.
     * @return The renderer factory.
     * @since 2.2.0
     */
    protected RendererFactory createRendererFactory(ApplicationContext applicationContext,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        BasicRendererFactory retValue = new BasicRendererFactory();
        retValue.setApplicationContext(applicationContext);
        retValue.setContainer(container);
        retValue.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        registerAttributeRenderers(retValue, applicationContext, container,
                attributeEvaluatorFactory);
        retValue.setDefaultRenderer(createDefaultAttributeRenderer(retValue,
                applicationContext, container, attributeEvaluatorFactory));
        return retValue;
    }

    /**
     * Creates the default attribute renderer. By default it is an
     * {@link ChainedDelegateAttributeRenderer}.
     *
     * @param rendererFactory The renderer factory to configure.
     * @param applicationContext The Tiles application context.
     * @param container The container.
     * @param attributeEvaluatorFactory The attribute evaluator factory.
     * @return The default attribute renderer.
     * @since 2.2.1
     */
    protected AttributeRenderer createDefaultAttributeRenderer(
            BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        ChainedDelegateAttributeRenderer retValue = new ChainedDelegateAttributeRenderer();
        retValue.addAttributeRenderer((TypeDetectingAttributeRenderer) rendererFactory
                .getRenderer(DEFINITION_RENDERER_NAME));
        retValue.addAttributeRenderer((TypeDetectingAttributeRenderer) rendererFactory
                .getRenderer(TEMPLATE_RENDERER_NAME));
        retValue.addAttributeRenderer((TypeDetectingAttributeRenderer) rendererFactory
                .getRenderer(STRING_RENDERER_NAME));
        retValue.setApplicationContext(applicationContext);
        retValue.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        return retValue;
    }

    /**
     * Creates a new pattern definition resolver. By default, it instantiate a
     * {@link BasicPatternDefinitionResolver} with
     * {@link WildcardDefinitionPatternMatcherFactory} to manage wildcard
     * substitution.
     *
     * @param <T> The type of the customization key.
     * @param customizationKeyClass The customization key class.
     * @return The pattern definition resolver.
     * @since 2.2.0
     */
    protected <T> PatternDefinitionResolver<T> createPatternDefinitionResolver(
            Class<T> customizationKeyClass) {
        WildcardDefinitionPatternMatcherFactory definitionPatternMatcherFactory =
            new WildcardDefinitionPatternMatcherFactory();
        return new BasicPatternDefinitionResolver<T>(
                definitionPatternMatcherFactory,
                definitionPatternMatcherFactory);
    }

    /**
     * Registers attribute renderers in a {@link BasicRendererFactory}. By
     * default, it registers a {@link StringAttributeRenderer}, a
     * {@link TemplateAttributeRenderer} and a
     * {@link DefinitionAttributeRenderer}.
     *
     * @param rendererFactory The renderer factory to configure.
     * @param applicationContext The Tiles application context.
     * @param container The container.
     * @param attributeEvaluatorFactory The attribute evaluator factory.
     * @since 2.2.0
     */
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        rendererFactory.registerRenderer(STRING_RENDERER_NAME,
                createStringAttributeRenderer(rendererFactory,
                        applicationContext, container, attributeEvaluatorFactory));
        rendererFactory.registerRenderer(TEMPLATE_RENDERER_NAME,
                createTemplateAttributeRenderer(rendererFactory,
                        applicationContext, container, attributeEvaluatorFactory));
        rendererFactory.registerRenderer(DEFINITION_RENDERER_NAME,
                createDefinitionAttributeRenderer(rendererFactory,
                        applicationContext, container, attributeEvaluatorFactory));
    }

    /**
     * Creates a {@link StringAttributeRenderer}.
     *
     * @param rendererFactory The renderer factory to configure.
     * @param applicationContext The Tiles application context.
     * @param container The container.
     * @param attributeEvaluatorFactory The attribute evaluator factory.
     * @return The renderer.
     * @since 2.2.1
     */
    protected AttributeRenderer createStringAttributeRenderer(
            BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        StringAttributeRenderer stringRenderer = new StringAttributeRenderer();
        stringRenderer.setApplicationContext(applicationContext);
        stringRenderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        return stringRenderer;
    }

    /**
     * Creates a {@link TemplateAttributeRenderer}.
     *
     * @param rendererFactory The renderer factory to configure.
     * @param applicationContext The Tiles application context.
     * @param container The container.
     * @param attributeEvaluatorFactory The attribute evaluator factory.
     * @return The renderer.
     * @since 2.2.1
     */
    protected AttributeRenderer createTemplateAttributeRenderer(
            BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        TemplateAttributeRenderer templateRenderer = new TemplateAttributeRenderer();
        templateRenderer.setApplicationContext(applicationContext);
        templateRenderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        return templateRenderer;
    }

    /**
     * Creates a {@link DefinitionAttributeRenderer}.
     *
     * @param rendererFactory The renderer factory to configure.
     * @param applicationContext The Tiles application context.
     * @param container The container.
     * @param attributeEvaluatorFactory The attribute evaluator factory.
     * @return The renderer.
     * @since 2.2.1
     */
    protected AttributeRenderer createDefinitionAttributeRenderer(
            BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        DefinitionAttributeRenderer definitionRenderer = new DefinitionAttributeRenderer();
        definitionRenderer.setApplicationContext(applicationContext);
        definitionRenderer.setContainer(container);
        definitionRenderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        return definitionRenderer;
    }
}
