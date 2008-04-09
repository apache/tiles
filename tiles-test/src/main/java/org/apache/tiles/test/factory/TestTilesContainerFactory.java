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

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.compat.definition.digester.CompatibilityDigesterDefinitionsReader;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.enhanced.EnhancedContextFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.el.ELAttributeEvaluator;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.test.renderer.ReverseStringAttributeRenderer;

import de.odysseus.el.ExpressionFactoryImpl;

/**
 * Test Tiles container factory to customize Tiles behaviour.
 *
 * @version $Rev$ $Date$
 */
public class TestTilesContainerFactory extends BasicTilesContainerFactory {

    /**
     * The number of URLs to load..
     */
    private static final int URL_COUNT = 3;

    /** {@inheritDoc} */
    @Override
    protected BasicTilesContainer instantiateContainer(Object context) {
        return new CachingTilesContainer();
    }

    /** {@inheritDoc} */
    @Override
    protected TilesContextFactory createContextFactory(Object context) {
        EnhancedContextFactory factory = new  EnhancedContextFactory();
        registerChainContextFactories(context, factory);
        return factory;
    }

    /** {@inheritDoc} */
    @Override
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory, Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory, TilesContainer container,
            AttributeEvaluator evaluator) {
        super.registerAttributeRenderers(rendererFactory, context, applicationContext,
                contextFactory, container, evaluator);
        ReverseStringAttributeRenderer renderer = new ReverseStringAttributeRenderer();
        renderer.setApplicationContext(applicationContext);
        renderer.setContextFactory(contextFactory);
        renderer.setEvaluator(evaluator);
        rendererFactory.registerRenderer("reversed", renderer);
    }

    /** {@inheritDoc} */
    @Override
    protected AttributeEvaluator createEvaluator(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory, LocaleResolver resolver) {
        ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
        evaluator.setApplicationContext(applicationContext);
        evaluator.setExpressionFactory(new ExpressionFactoryImpl());
        evaluator.init(null);
        return evaluator;
    }

    /** {@inheritDoc} */
    @Override
    protected List<URL> getSourceURLs(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory) {
        List<URL> urls = new ArrayList<URL>(URL_COUNT);
        try {
            urls.add(applicationContext.getResource("/WEB-INF/tiles-defs.xml"));
            urls.add(applicationContext.getResource("/org/apache/tiles/classpath-defs.xml"));
            urls.add(applicationContext.getResource("/WEB-INF/tiles-defs-1.1.xml"));
        } catch (IOException e) {
            throw new DefinitionsFactoryException(
                    "Cannot load definition URLs", e);
        }
        return urls;
    }

    /** {@inheritDoc} */
    @Override
    protected DefinitionsReader createDefinitionsReader(Object context,
            TilesApplicationContext applicationContext,
            TilesContextFactory contextFactory) {
        return new CompatibilityDigesterDefinitionsReader();
    }
}
