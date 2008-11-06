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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.awareness.TilesApplicationContextFactoryAware;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.context.ChainedTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesApplicationContextFactory;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.LocaleDefinitionsFactory;
import org.apache.tiles.definition.Refreshable;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.DefinitionDAO;
import org.apache.tiles.definition.dao.ResolvingLocaleUrlDefinitionDAO;
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

/**
 * Factory that builds a standard Tiles container using only Java code.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class BasicTilesContainerFactory extends AbstractTilesContainerFactory {

    /**
     * The count of elements in the Tiles context factory chain.
     */
    private static final int CONTEXT_FACTORY_CHAIN_COUNT = 3;

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory.getLog(BasicTilesContainerFactory.class);

    /** {@inheritDoc} */
    @Override
    public TilesContainer createContainer(Object context) {
        BasicTilesContainer container = instantiateContainer(context);
        TilesApplicationContextFactory contextFactory = createApplicationContextFactory(context);
        TilesApplicationContext applicationContext = contextFactory
                .createApplicationContext(context);
        TilesRequestContextFactory requestContextFactory = createRequestContextFactory();
        container.setRequestContextFactory(requestContextFactory);
        container.setApplicationContext(applicationContext);
        LocaleResolver resolver = createLocaleResolver(context,
                applicationContext, requestContextFactory);
        container.setDefinitionsFactory(createDefinitionsFactory(context,
                applicationContext, requestContextFactory, resolver));
        AttributeEvaluator evaluator = createEvaluator(context,
                applicationContext, requestContextFactory, resolver);
        container.setEvaluator(evaluator);
        container.setPreparerFactory(createPreparerFactory(context,
                applicationContext, requestContextFactory));
        container.setRendererFactory(createRendererFactory(context,
                applicationContext, requestContextFactory, container,
                evaluator));
        return container;
    }

    /**
     * Instantiate the container, without initialization.
     *
     * @param context The context object.
     * @return The instantiated container.
     * @since 2.1.0
     */
    protected BasicTilesContainer instantiateContainer(Object context) {
        return new BasicTilesContainer();
    }

    /**
     * Create a Tiles applicaitoncontext factory. By default it creates a
     * {@link ChainedTilesApplicationContextFactory}.
     *
     * @param context The context.
     * @return The application context factory.
     * @since 2.1.1
     */
    protected TilesApplicationContextFactory createApplicationContextFactory(
            Object context) {
        ChainedTilesApplicationContextFactory contextFactory = new ChainedTilesApplicationContextFactory();
        registerChainedApplicationContextFactories(context, contextFactory);

        return contextFactory;
    }

    /**
     * Create a Tiles request context factory. By default it creates a
     * {@link ChainedTilesRequestContextFactory}.
     *
     * @return The request context factory.
     * @since 2.1.1
     */
    protected TilesRequestContextFactory createRequestContextFactory() {
        ChainedTilesRequestContextFactory contextFactory = new ChainedTilesRequestContextFactory();
        registerChainedRequestContextFactories(contextFactory);

        return contextFactory;
    }

    /**
     * Register elements of a chained application context factory.
     *
     * @param context The context.
     * @param contextFactory The application context factory to use.
     * @since 2.1.1
     */
    protected void registerChainedApplicationContextFactories(Object context,
            ChainedTilesApplicationContextFactory contextFactory) {
        List<TilesApplicationContextFactory> factories = new ArrayList<TilesApplicationContextFactory>(
                CONTEXT_FACTORY_CHAIN_COUNT);
        registerApplicationContextFactory(
                "org.apache.tiles.servlet.context.ServletTilesApplicationContextFactory",
                factories, contextFactory);
        registerApplicationContextFactory(
                "org.apache.tiles.portlet.context.PortletTilesApplicationContextFactory",
                factories, contextFactory);
        contextFactory.setFactories(factories);
    }

    /**
     * Register elements of a chained request context factory.
     *
     * @param contextFactory The request context factory to use.
     * @since 2.1.1
     */
    protected void registerChainedRequestContextFactories(
            ChainedTilesRequestContextFactory contextFactory) {
        List<TilesRequestContextFactory> factories = new ArrayList<TilesRequestContextFactory>(
                CONTEXT_FACTORY_CHAIN_COUNT);
        registerRequestContextFactory(
                "org.apache.tiles.servlet.context.ServletTilesRequestContextFactory",
                factories, contextFactory);
        registerRequestContextFactory(
                "org.apache.tiles.portlet.context.PortletTilesRequestContextFactory",
                factories, contextFactory);
        registerRequestContextFactory(
                "org.apache.tiles.jsp.context.JspTilesRequestContextFactory",
                factories, contextFactory);
        contextFactory.setFactories(factories);
    }

    /**
     * Registers a {@link TilesApplicationContextFactory} specifying its
     * classname.
     *
     * @param className The name of the class to instantiate.
     * @param factories The list of factories to add to.
     * @param parent The parent {@link TilesApplicationContextFactory}. If null
     * it won't be considered.
     * @since 2.1.1
     */
    protected void registerApplicationContextFactory(String className,
            List<TilesApplicationContextFactory> factories,
            TilesApplicationContextFactory parent) {
        TilesApplicationContextFactory retValue = null;
        try {
            Class<? extends TilesApplicationContextFactory> clazz = Class
                    .forName(className).asSubclass(
                            TilesApplicationContextFactory.class);
            retValue = clazz.newInstance();
            if (parent != null
                    && retValue instanceof TilesApplicationContextFactoryAware) {
                ((TilesApplicationContextFactoryAware) retValue)
                        .setApplicationContextFactory(parent);
            }
        } catch (ClassNotFoundException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cannot find JspTilesContextFactory, ignoring problem", e);
            }
        } catch (InstantiationException e) {
            throw new TilesContainerFactoryException(
                    "Cannot instantiate JspTilesContextFactory", e);
        } catch (IllegalAccessException e) {
            throw new TilesContainerFactoryException(
                    "Cannot access default constructor JspTilesContextFactory",
                    e);
        }
        if (retValue != null) {
            factories.add(retValue);
        }
    }

    /**
     * Registers a {@link TilesRequestContextFactory} specifying its
     * classname.
     *
     * @param className The name of the class to instantiate.
     * @param factories The list of factories to add to.
     * @param parent The parent {@link TilesRequestContextFactory}. If null
     * it won't be considered.
     * @since 2.1.1
     */
    protected void registerRequestContextFactory(String className,
            List<TilesRequestContextFactory> factories,
            TilesRequestContextFactory parent) {
        TilesRequestContextFactory retValue = null;
        try {
            Class<? extends TilesRequestContextFactory> clazz = Class
                    .forName(className).asSubclass(
                            TilesRequestContextFactory.class);
            retValue = clazz.newInstance();
            if (parent != null
                    && retValue instanceof TilesRequestContextFactoryAware) {
                ((TilesRequestContextFactoryAware) retValue)
                        .setRequestContextFactory(parent);
            }
        } catch (ClassNotFoundException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cannot find JspTilesContextFactory, ignoring problem", e);
            }
        } catch (InstantiationException e) {
            throw new TilesContainerFactoryException(
                    "Cannot instantiate JspTilesContextFactory", e);
        } catch (IllegalAccessException e) {
            throw new TilesContainerFactoryException(
                    "Cannot access default constructor JspTilesContextFactory",
                    e);
        }
        if (retValue != null) {
            factories.add(retValue);
        }
    }

    /**
     * Creates the definitions factory. By default it creates a
     * {@link UrlDefinitionsFactory} with default dependencies.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @param resolver The locale resolver.
     * @return The definitions factory.
     * @since 2.1.1
     */
    protected DefinitionsFactory createDefinitionsFactory(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
        LocaleDefinitionsFactory factory = instantiateDefinitionsFactory(
                context, applicationContext, contextFactory, resolver);
        factory.setApplicationContext(applicationContext);
        factory.setLocaleResolver(resolver);
        factory.setDefinitionDAO(createLocaleDefinitionDao(context,
                applicationContext, contextFactory, resolver));
        if (factory instanceof Refreshable) {
            ((Refreshable) factory).refresh();
        }
        return factory;
    }

    /**
     * Instantiate a new definitions factory based on Locale.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @param resolver The locale resolver.
     * @return The definitions factory.
     * @since 2.1.1
     */
    protected LocaleDefinitionsFactory instantiateDefinitionsFactory(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
        return new UrlDefinitionsFactory();
    }


    /**
     * Instantiate (and does not initialize) a Locale-based definition DAO.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @param resolver The locale resolver.
     * @return The definition DAO.
     * @since 2.1.1
     */
    protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
        return new ResolvingLocaleUrlDefinitionDAO();
    }

    /**
     * Creates a Locale-based definition DAO.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @param resolver The locale resolver.
     * @return The definition DAO.
     * @since 2.1.1
     */
    protected DefinitionDAO<Locale> createLocaleDefinitionDao(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
        BaseLocaleUrlDefinitionDAO definitionDao = instantiateLocaleDefinitionDao(
                context, applicationContext, contextFactory, resolver);
        definitionDao.setReader(createDefinitionsReader(context, applicationContext,
                contextFactory));
        definitionDao.setSourceURLs(getSourceURLs(context, applicationContext,
                contextFactory));
        definitionDao.setApplicationContext(applicationContext);
        return definitionDao;
    }

    /**
     * Creates the locale resolver. By default it creates a
     * {@link DefaultLocaleResolver}.
     *
     * @param context The context.
     * @param applicationContext The Tiles application context.
     * @param contextFactory The Tiles context factory.
     * @return The locale resolver.
     * @since 2.1.1
     */
    protected LocaleResolver createLocaleResolver(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory) {
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
     * @since 2.1.1
     */
    protected DefinitionsReader createDefinitionsReader(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory) {
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
     * @since 2.1.1
     */
    protected List<URL> getSourceURLs(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory) {
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
     * @param resolver The locale resolver.
     * @return The evaluator.
     */
    protected AttributeEvaluator createEvaluator(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
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
     * @since 2.1.1
     */
    protected PreparerFactory createPreparerFactory(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory) {
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
     * @since 2.1.1
     */
    protected RendererFactory createRendererFactory(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, TilesContainer container,
            AttributeEvaluator evaluator) {
        BasicRendererFactory retValue = new BasicRendererFactory();
        retValue.setApplicationContext(applicationContext);
        retValue.setRequestContextFactory(contextFactory);
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
     * @since 2.1.1
     */
    protected AttributeRenderer createDefaultAttributeRenderer(Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory,
            TilesContainer container, AttributeEvaluator evaluator) {
        UntypedAttributeRenderer retValue = new UntypedAttributeRenderer();
        retValue.setApplicationContext(applicationContext);
        retValue.setContainer(container);
        retValue.setRequestContextFactory(contextFactory);
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
     * @since 2.1.1
     */
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory, Object context,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, TilesContainer container,
            AttributeEvaluator evaluator) {
        StringAttributeRenderer stringRenderer = new StringAttributeRenderer();
        stringRenderer.setApplicationContext(applicationContext);
        stringRenderer.setRequestContextFactory(contextFactory);
        stringRenderer.setEvaluator(evaluator);
        rendererFactory.registerRenderer("string", stringRenderer);

        TemplateAttributeRenderer templateRenderer = new TemplateAttributeRenderer();
        templateRenderer.setApplicationContext(applicationContext);
        templateRenderer.setRequestContextFactory(contextFactory);
        templateRenderer.setEvaluator(evaluator);
        rendererFactory.registerRenderer("template", templateRenderer);

        DefinitionAttributeRenderer definitionRenderer = new DefinitionAttributeRenderer();
        definitionRenderer.setApplicationContext(applicationContext);
        definitionRenderer.setContainer(container);
        definitionRenderer.setRequestContextFactory(contextFactory);
        definitionRenderer.setEvaluator(evaluator);
        rendererFactory.registerRenderer("definition", definitionRenderer);
    }
}
