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

package org.apache.tiles.extras.complete;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;

import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.compat.definition.digester.CompatibilityDigesterDefinitionsReader;
import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.pattern.DefinitionPatternMatcherFactory;
import org.apache.tiles.definition.pattern.PatternDefinitionResolver;
import org.apache.tiles.definition.pattern.PrefixedPatternDefinitionResolver;
import org.apache.tiles.definition.pattern.regexp.RegexpDefinitionPatternMatcherFactory;
import org.apache.tiles.definition.pattern.wildcard.WildcardDefinitionPatternMatcherFactory;
import org.apache.tiles.el.ELAttributeEvaluator;
import org.apache.tiles.el.JspExpressionFactoryFactory;
import org.apache.tiles.el.ScopeELResolver;
import org.apache.tiles.el.TilesContextBeanELResolver;
import org.apache.tiles.el.TilesContextELResolver;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.factory.TilesContainerFactoryException;
import org.apache.tiles.freemarker.TilesSharedVariableFactory;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.mvel.MVELAttributeEvaluator;
import org.apache.tiles.mvel.ScopeVariableResolverFactory;
import org.apache.tiles.mvel.TilesContextBeanVariableResolverFactory;
import org.apache.tiles.mvel.TilesContextVariableResolverFactory;
import org.apache.tiles.ognl.AnyScopePropertyAccessor;
import org.apache.tiles.ognl.DelegatePropertyAccessor;
import org.apache.tiles.ognl.NestedObjectDelegatePropertyAccessor;
import org.apache.tiles.ognl.OGNLAttributeEvaluator;
import org.apache.tiles.ognl.PropertyAccessorDelegateFactory;
import org.apache.tiles.ognl.ScopePropertyAccessor;
import org.apache.tiles.ognl.TilesApplicationContextNestedObjectExtractor;
import org.apache.tiles.ognl.TilesContextPropertyAccessorDelegateFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.freemarker.render.FreemarkerRenderer;
import org.apache.tiles.request.freemarker.render.FreemarkerRendererBuilder;
import org.apache.tiles.request.freemarker.servlet.SharedVariableLoaderFreemarkerServlet;
import org.apache.tiles.request.mustache.MustacheRenderer;
import org.apache.tiles.request.render.BasicRendererFactory;
import org.apache.tiles.request.render.ChainedDelegateRenderer;
import org.apache.tiles.request.render.Renderer;
import org.apache.tiles.request.servlet.ServletUtil;
import org.apache.tiles.request.velocity.render.VelocityRenderer;
import org.apache.tiles.request.velocity.render.VelocityRendererBuilder;
import org.mvel2.integration.VariableResolverFactory;

/**
 * Tiles container factory that:
 * <ul>
 * <li>create supporting objects for Velocity and FreeMarker;</li>
 * <li>create renderers for Velocity, FreeMarker, and Mustache templates;</li>
 * <li>allows using EL, MVEL and OGNL as attribute expressions;</li>
 * <li>allows using Wildcards and Regular Expressions in definition names;</li>
 * <li>loads Tiles 1.x definition files;</li>
 * <li>loads all the definition files that have the "tiles*.xml" pattern under
 * <code>/WEB-INF</code> directory (and subdirectories) and under
 * <code>META-INF</code> directories (and subdirectories) in every jar.</li>
 * </ul>
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class CompleteAutoloadTilesContainerFactory extends BasicTilesContainerFactory {

    /**
     * The freemarker renderer name.
     */
    private static final String FREEMARKER_RENDERER_NAME = "freemarker";

    /**
     * The velocity renderer name.
     */
    private static final String VELOCITY_RENDERER_NAME = "velocity";

    /**
     * The mustache renderer name.
     */
    private static final String MUSTACHE_RENDERER_NAME = "mustache";

    /** {@inheritDoc} */
    @Override
    public TilesContainer createDecoratedContainer(TilesContainer originalContainer,
            ApplicationContext applicationContext) {
        return new CachingTilesContainer(originalContainer);
    }

    /** {@inheritDoc} */
    @Override
    protected void registerAttributeRenderers(
            final BasicRendererFactory rendererFactory,
            final ApplicationContext applicationContext,
            final TilesContainer container,
            final AttributeEvaluatorFactory attributeEvaluatorFactory) {
        super.registerAttributeRenderers(rendererFactory, applicationContext, container, attributeEvaluatorFactory);

        FreemarkerRenderer freemarkerRenderer = FreemarkerRendererBuilder
                .createInstance()
                .setApplicationContext(applicationContext)
                .setParameter("TemplatePath", "/")
                .setParameter("NoCache", "true")
                .setParameter("ContentType", "text/html")
                .setParameter("template_update_delay", "0")
                .setParameter("default_encoding", "ISO-8859-1")
                .setParameter("number_format", "0.##########")
                .setParameter(SharedVariableLoaderFreemarkerServlet.CUSTOM_SHARED_VARIABLE_FACTORIES_INIT_PARAM,
                        "tiles," + TilesSharedVariableFactory.class.getName()).build();
        rendererFactory.registerRenderer(FREEMARKER_RENDERER_NAME, freemarkerRenderer);

        VelocityRenderer velocityRenderer = VelocityRendererBuilder.createInstance()
                .setApplicationContext(applicationContext).build();
        rendererFactory.registerRenderer(VELOCITY_RENDERER_NAME, velocityRenderer);

        MustacheRenderer mustacheRenderer = new MustacheRenderer();
        mustacheRenderer.setAcceptPattern(Pattern.compile(".+\\.mustache"));
        rendererFactory.registerRenderer(MUSTACHE_RENDERER_NAME, mustacheRenderer);
    }

    /** {@inheritDoc} */
    @Override
    protected Renderer createDefaultAttributeRenderer(BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext, TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {

        ChainedDelegateRenderer retValue = new ChainedDelegateRenderer();
        retValue.addAttributeRenderer(rendererFactory.getRenderer(DEFINITION_RENDERER_NAME));
        retValue.addAttributeRenderer(rendererFactory.getRenderer(VELOCITY_RENDERER_NAME));
        retValue.addAttributeRenderer(rendererFactory.getRenderer(FREEMARKER_RENDERER_NAME));
        retValue.addAttributeRenderer(rendererFactory.getRenderer(MUSTACHE_RENDERER_NAME));
        retValue.addAttributeRenderer(rendererFactory.getRenderer(TEMPLATE_RENDERER_NAME));
        retValue.addAttributeRenderer(rendererFactory.getRenderer(STRING_RENDERER_NAME));
        return retValue;
    }

    /** {@inheritDoc} */
    @Override
    protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(ApplicationContext applicationContext,
            LocaleResolver resolver) {
        BasicAttributeEvaluatorFactory attributeEvaluatorFactory = new BasicAttributeEvaluatorFactory(
                createELEvaluator(applicationContext));
        attributeEvaluatorFactory.registerAttributeEvaluator("MVEL", createMVELEvaluator());
        attributeEvaluatorFactory.registerAttributeEvaluator("OGNL", createOGNLEvaluator());

        return attributeEvaluatorFactory;
    }

    /** {@inheritDoc} */
    @Override
    protected <T> PatternDefinitionResolver<T> createPatternDefinitionResolver(Class<T> customizationKeyClass) {
        DefinitionPatternMatcherFactory wildcardFactory = new WildcardDefinitionPatternMatcherFactory();
        DefinitionPatternMatcherFactory regexpFactory = new RegexpDefinitionPatternMatcherFactory();
        PrefixedPatternDefinitionResolver<T> resolver = new PrefixedPatternDefinitionResolver<T>();
        resolver.registerDefinitionPatternMatcherFactory("WILDCARD", wildcardFactory);
        resolver.registerDefinitionPatternMatcherFactory("REGEXP", regexpFactory);
        return resolver;
    }

    /** {@inheritDoc} */
    @Override
    protected List<ApplicationResource> getSources(ApplicationContext applicationContext) {
        Collection<ApplicationResource> webINFSet = applicationContext.getResources("/WEB-INF/**/tiles*.xml");
        Collection<ApplicationResource> metaINFSet = applicationContext
                .getResources("classpath*:META-INF/**/tiles*.xml");

        List<ApplicationResource> filteredResources = new ArrayList<ApplicationResource>();
        if (webINFSet != null) {
            for (ApplicationResource resource : webINFSet) {
                if (Locale.ROOT.equals(resource.getLocale())) {
                    filteredResources.add(resource);
                }
            }
        }
        if (metaINFSet != null) {
            for (ApplicationResource resource : metaINFSet) {
                if (Locale.ROOT.equals(resource.getLocale())) {
                    filteredResources.add(resource);
                }
            }
        }
        return filteredResources;
    }

    /** {@inheritDoc} */
    @Override
    protected DefinitionsReader createDefinitionsReader(ApplicationContext applicationContext) {
        return new CompatibilityDigesterDefinitionsReader();
    }

    /**
     * Creates the EL evaluator.
     *
     * @param applicationContext The Tiles application context.
     * @return The EL evaluator.
     */
    private ELAttributeEvaluator createELEvaluator(ApplicationContext applicationContext) {
        ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
        JspExpressionFactoryFactory efFactory = new JspExpressionFactoryFactory();
        efFactory.setApplicationContext(applicationContext);
        evaluator.setExpressionFactory(efFactory.getExpressionFactory());
        ELResolver elResolver = new CompositeELResolver() {
            {
                BeanELResolver beanElResolver = new BeanELResolver(false);
                add(new ScopeELResolver());
                add(new TilesContextELResolver(beanElResolver));
                add(new TilesContextBeanELResolver());
                add(new ArrayELResolver(false));
                add(new ListELResolver(false));
                add(new MapELResolver(false));
                add(new ResourceBundleELResolver());
                add(beanElResolver);
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
        VariableResolverFactory variableResolverFactory = new ScopeVariableResolverFactory(requestHolder);
        variableResolverFactory.setNextFactory(new TilesContextVariableResolverFactory(requestHolder));
        variableResolverFactory.setNextFactory(new TilesContextBeanVariableResolverFactory(requestHolder));
        MVELAttributeEvaluator mvelEvaluator = new MVELAttributeEvaluator(requestHolder, variableResolverFactory);
        return mvelEvaluator;
    }

    /**
     * Creates the OGNL evaluator.
     *
     * @return The OGNL evaluator.
     */
    private OGNLAttributeEvaluator createOGNLEvaluator() {
        try {
            PropertyAccessor objectPropertyAccessor = OgnlRuntime.getPropertyAccessor(Object.class);
            PropertyAccessor applicationContextPropertyAccessor = new NestedObjectDelegatePropertyAccessor<Request>(
                    new TilesApplicationContextNestedObjectExtractor(), objectPropertyAccessor);
            PropertyAccessor anyScopePropertyAccessor = new AnyScopePropertyAccessor();
            PropertyAccessor scopePropertyAccessor = new ScopePropertyAccessor();
            PropertyAccessorDelegateFactory<Request> factory = new TilesContextPropertyAccessorDelegateFactory(
                    objectPropertyAccessor, applicationContextPropertyAccessor, anyScopePropertyAccessor,
                    scopePropertyAccessor);
            PropertyAccessor tilesRequestAccessor = new DelegatePropertyAccessor<Request>(factory);
            OgnlRuntime.setPropertyAccessor(Request.class, tilesRequestAccessor);
            return new OGNLAttributeEvaluator();
        } catch (OgnlException e) {
            throw new TilesContainerFactoryException("Cannot initialize OGNL evaluator", e);
        }
    }
}
