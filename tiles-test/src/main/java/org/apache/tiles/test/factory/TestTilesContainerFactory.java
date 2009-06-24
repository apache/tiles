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
package org.apache.tiles.test.factory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.compat.definition.digester.CompatibilityDigesterDefinitionsReader;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.el.ELAttributeEvaluator;
import org.apache.tiles.evaluator.el.TilesContextBeanELResolver;
import org.apache.tiles.evaluator.el.TilesContextELResolver;
import org.apache.tiles.evaluator.mvel.MVELAttributeEvaluator;
import org.apache.tiles.evaluator.mvel.TilesContextBeanVariableResolverFactory;
import org.apache.tiles.evaluator.mvel.TilesContextVariableResolverFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.freemarker.context.FreeMarkerTilesRequestContextFactory;
import org.apache.tiles.freemarker.renderer.FreeMarkerAttributeRenderer;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.test.evaluator.el.MultiversionExpressionFactoryFactory;
import org.apache.tiles.test.renderer.ReverseStringAttributeRenderer;
import org.apache.tiles.velocity.context.VelocityTilesRequestContextFactory;
import org.apache.tiles.velocity.renderer.VelocityAttributeRenderer;
import org.mvel2.integration.VariableResolverFactory;


/**
 * Test Tiles container factory to customize Tiles behaviour.
 *
 * @version $Rev$ $Date$
 */
public class TestTilesContainerFactory extends BasicTilesContainerFactory {

    /**
     * The number of registered factories.
     */
    private static final int FACTORY_SIZE = 4;

    /**
     * The number of URLs to load..
     */
    private static final int URL_COUNT = 7;

    /** {@inheritDoc} */
    @Override
    protected BasicTilesContainer instantiateContainer(
            TilesApplicationContext applicationContext) {
        return new CachingTilesContainer();
    }
    /**
     * Register elements of a chained request context factory.
     *
     * @param contextFactory The request context factory to use.
     * @since 2.1.1
     */
    @Override
    protected void registerChainedRequestContextFactories(
            ChainedTilesRequestContextFactory contextFactory) {
        List<TilesRequestContextFactory> factories = new ArrayList<TilesRequestContextFactory>(
                FACTORY_SIZE);
        registerRequestContextFactory(
                "org.apache.tiles.servlet.context.ServletTilesRequestContextFactory",
                factories, contextFactory);
        registerRequestContextFactory(
                "org.apache.tiles.jsp.context.JspTilesRequestContextFactory",
                factories, contextFactory);
        registerRequestContextFactory(
                FreeMarkerTilesRequestContextFactory.class.getName(),
                factories, contextFactory);
        registerRequestContextFactory(
                VelocityTilesRequestContextFactory.class.getName(),
                factories, contextFactory);
        contextFactory.setFactories(factories);
    }

    /** {@inheritDoc} */
    @Override
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        super.registerAttributeRenderers(rendererFactory, applicationContext, contextFactory,
                container, attributeEvaluatorFactory);
        ReverseStringAttributeRenderer renderer = new ReverseStringAttributeRenderer();
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        renderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        rendererFactory.registerRenderer("reversed", renderer);

        FreeMarkerAttributeRenderer freemarkerRenderer = new FreeMarkerAttributeRenderer();
        freemarkerRenderer.setApplicationContext(applicationContext);
        freemarkerRenderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        freemarkerRenderer.setRequestContextFactory(contextFactory);
        freemarkerRenderer.setParameter("TemplatePath", "/");
        freemarkerRenderer.setParameter("NoCache", "true");
        freemarkerRenderer.setParameter("ContentType", "text/html");
        freemarkerRenderer.setParameter("template_update_delay", "0");
        freemarkerRenderer.setParameter("default_encoding", "ISO-8859-1");
        freemarkerRenderer.setParameter("number_format", "0.##########");
        freemarkerRenderer.commit();
        rendererFactory.registerRenderer("freemarker", freemarkerRenderer);

        VelocityAttributeRenderer velocityRenderer = new VelocityAttributeRenderer();
        velocityRenderer.setApplicationContext(applicationContext);
        velocityRenderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        velocityRenderer.setRequestContextFactory(contextFactory);
        velocityRenderer.setParameter("org.apache.velocity.toolbox", "/WEB-INF/tools.xml");
        velocityRenderer.setParameter("org.apache.velocity.properties", "/WEB-INF/velocity.properties");
        velocityRenderer.commit();
        rendererFactory.registerRenderer("velocity", velocityRenderer);
    }

    /** {@inheritDoc} */
    @Override
    protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
        BasicAttributeEvaluatorFactory attributeEvaluatorFactory = new BasicAttributeEvaluatorFactory(
                createELEvaluator(applicationContext));
        attributeEvaluatorFactory.registerAttributeEvaluator("MVEL",
                createMVELEvaluator());

        return attributeEvaluatorFactory;
    }

    /**
     * Creates the EL evaluator.
     *
     * @param applicationContext The Tiles application context.
     * @return The EL evaluator.
     */
    private ELAttributeEvaluator createELEvaluator(
            TilesApplicationContext applicationContext) {
        ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
        evaluator.setApplicationContext(applicationContext);
        MultiversionExpressionFactoryFactory efFactory = new MultiversionExpressionFactoryFactory();
        efFactory.setApplicationContext(applicationContext);
        evaluator.setExpressionFactory(efFactory.getExpressionFactory());
        ELResolver elResolver = new CompositeELResolver() {
            {
                add(new TilesContextELResolver());
                add(new TilesContextBeanELResolver());
                add(new ArrayELResolver(false));
                add(new ListELResolver(false));
                add(new MapELResolver(false));
                add(new ResourceBundleELResolver());
                add(new BeanELResolver(false));
            }
        };
        evaluator.setResolver(elResolver);
        return evaluator;
    }

    /**
     * Creates the MVEL evaluator.
     *
     * @return The MVEL evaluator.
     */
    private MVELAttributeEvaluator createMVELEvaluator() {
        TilesRequestContextHolder requestHolder = new TilesRequestContextHolder();
        VariableResolverFactory variableResolverFactory = new TilesContextVariableResolverFactory(
                requestHolder);
        variableResolverFactory
                .setNextFactory(new TilesContextBeanVariableResolverFactory(
                        requestHolder));
        MVELAttributeEvaluator mvelEvaluator = new MVELAttributeEvaluator(requestHolder,
                variableResolverFactory);
        return mvelEvaluator;
    }

    /** {@inheritDoc} */
    @Override
    protected List<URL> getSourceURLs(TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory) {
        List<URL> urls = new ArrayList<URL>(URL_COUNT);
        try {
            Set<URL> urlSet = applicationContext
                    .getResources("/WEB-INF/**/tiles-defs*.xml");
            for (URL url : urlSet) {
                String externalForm = url.toExternalForm();
                if (externalForm.indexOf('_', externalForm.lastIndexOf("/")) < 0) {
                    urls.add(url);
                }
            }
            urls.add(applicationContext.getResource(
                    "classpath:/org/apache/tiles/classpath-defs.xml"));
            urls.add(applicationContext.getResource(
                    "classpath:/org/apache/tiles/freemarker-classpath-defs.xml"));
            urls.add(applicationContext.getResource(
                "classpath:/org/apache/tiles/velocity-classpath-defs.xml"));
        } catch (IOException e) {
            throw new DefinitionsFactoryException(
                    "Cannot load definition URLs", e);
        }
        return urls;
    }

    /** {@inheritDoc} */
    @Override
    protected DefinitionsReader createDefinitionsReader(TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory) {
        return new CompatibilityDigesterDefinitionsReader();
    }
}
