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
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.el.ELAttributeEvaluator;
import org.apache.tiles.evaluator.el.TilesContextBeanELResolver;
import org.apache.tiles.evaluator.el.TilesContextELResolver;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.freemarker.context.FreeMarkerTilesRequestContextFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.test.evaluator.el.MultiversionExpressionFactoryFactory;
import org.apache.tiles.test.renderer.ReverseStringAttributeRenderer;
import org.apache.tiles.velocity.VelocityContextFactory;


/**
 * Test Tiles container factory to customize Tiles behaviour.
 *
 * @version $Rev$ $Date$
 */
public class TestTilesContainerFactory extends BasicTilesContainerFactory {

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
    protected void registerChainedRequestContextFactories(
            ChainedTilesRequestContextFactory contextFactory) {
        List<TilesRequestContextFactory> factories = new ArrayList<TilesRequestContextFactory>(
                3);
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
                VelocityContextFactory.class.getName(),
                factories, contextFactory);
        contextFactory.setFactories(factories);
    }

    /** {@inheritDoc} */
    @Override
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory, TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory,
            TilesContainer container, AttributeEvaluator evaluator) {
        super.registerAttributeRenderers(rendererFactory, applicationContext, contextFactory,
                container, evaluator);
        ReverseStringAttributeRenderer renderer = new ReverseStringAttributeRenderer();
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        renderer.setEvaluator(evaluator);
        rendererFactory.registerRenderer("reversed", renderer);
    }

    /** {@inheritDoc} */
    @Override
    protected AttributeEvaluator createEvaluator(TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory,
            LocaleResolver resolver) {
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
