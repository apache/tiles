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

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ExpressionFactory;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.compat.definition.digester.CompatibilityDigesterDefinitionsReader;
import org.apache.tiles.definition.pattern.PatternDefinitionResolver;
import org.apache.tiles.definition.pattern.PrefixedPatternDefinitionResolver;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.TypeDetectingAttributeRenderer;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.renderer.impl.ChainedDelegateAttributeRenderer;
import org.apache.tiles.renderer.impl.DelegateAttributeRenderer;
import org.apache.tiles.renderer.impl.StringAttributeRenderer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.servlet.ServletApplicationContext;
import org.apache.velocity.tools.view.VelocityView;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link CompleteAutoloadTilesContainerFactory}.
 *
 * @version $Rev$ $Date$
 */
public class CompleteAutoloadTilesContainerFactoryTest {

    /**
     * The object to test.
     */
    private CompleteAutoloadTilesContainerFactory factory;

    /**
     * Initializes the object.
     */
    @Before
    public void setUp() {
        factory = new CompleteAutoloadTilesContainerFactory();
    }

    /**
     * Test method for
     * {@link CompleteAutoloadTilesContainerFactory#createDecoratedContainer(TilesContainer, ApplicationContext)
     * .
     */
    @Test
    public void testCreateDecoratedContainer() {
        ApplicationContext applicationContext = createMock(ServletApplicationContext.class);
        TilesContainer wrapped = createMock(TilesContainer.class);

        replay(applicationContext, wrapped);
        assertSame(wrapped, ((CachingTilesContainer) factory
                .createDecoratedContainer(wrapped, applicationContext))
                .getWrappedContainer());
        verify(applicationContext, wrapped);
    }

    /**
     * Test method for
     * {@link CompleteAutoloadTilesContainerFactory
     * #registerAttributeRenderers(BasicRendererFactory, ApplicationContext,
     * TilesContainer, evaluator.AttributeEvaluatorFactory)}
     * .
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testRegisterAttributeRenderers() {
        BasicRendererFactory rendererFactory = createMock(BasicRendererFactory.class);
        ServletApplicationContext applicationContext = createMock(ServletApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        ServletContext servletContext = createMock(ServletContext.class);

        rendererFactory.registerRenderer(eq("string"),
                isA(StringAttributeRenderer.class));
        rendererFactory.registerRenderer(eq("template"),
                isA(DelegateAttributeRenderer.class));
        rendererFactory.registerRenderer(eq("definition"),
                isA(DelegateAttributeRenderer.class));
        rendererFactory.registerRenderer(eq("freemarker"),
                isA(DelegateAttributeRenderer.class));
        rendererFactory.registerRenderer(eq("velocity"),
                isA(DelegateAttributeRenderer.class));

        expect(applicationContext.getContext()).andReturn(servletContext)
                .anyTimes();
        expect(servletContext.getInitParameter(VelocityView.PROPERTIES_KEY))
                .andReturn(null);
        expect(servletContext.getInitParameter(VelocityView.TOOLS_KEY))
                .andReturn(null);
        expect(servletContext.getAttribute(VelocityView.TOOLS_KEY)).andReturn(
                null);
        expect(
                servletContext
                        .getResourceAsStream("/WEB-INF/velocity.properties"))
                .andReturn(
                        getClass().getResourceAsStream("/velocity.properties"));
        expect(
                servletContext
                        .getResourceAsStream("/WEB-INF/VM_global_library.vm"))
                .andReturn(
                        getClass().getResourceAsStream("/VM_global_library.vm"));
        expect(servletContext.getResourceAsStream("/WEB-INF/tools.xml"))
                .andReturn(getClass().getResourceAsStream("/tools.xml"));
        expect(
                servletContext
                        .getResourceAsStream(VelocityView.DEPRECATED_USER_TOOLS_PATH))
                .andReturn(null);
        servletContext.log((String) anyObject());
        expectLastCall().anyTimes();
        expect(servletContext.getRealPath("/")).andReturn(null);

        replay(rendererFactory, applicationContext, container,
                attributeEvaluatorFactory, servletContext);
        factory.registerAttributeRenderers(rendererFactory, applicationContext,
                container, attributeEvaluatorFactory);
        verify(rendererFactory, applicationContext, container,
                attributeEvaluatorFactory, servletContext);
    }

    /**
     * Tests
     * {@link CompleteAutoloadTilesContainerFactory#createDefaultAttributeRenderer(BasicRendererFactory,
     * ApplicationContext, TilesContainer, AttributeEvaluatorFactory)}.
     */
    @Test
    public void testCreateDefaultAttributeRenderer() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        BasicRendererFactory rendererFactory = createMock(BasicRendererFactory.class);
        AttributeRenderer stringRenderer = createMock(TypeDetectingAttributeRenderer.class);
        AttributeRenderer templateRenderer = createMock(TypeDetectingAttributeRenderer.class);
        AttributeRenderer definitionRenderer = createMock(TypeDetectingAttributeRenderer.class);
        AttributeRenderer velocityRenderer = createMock(TypeDetectingAttributeRenderer.class);
        AttributeRenderer freemarkerRenderer = createMock(TypeDetectingAttributeRenderer.class);

        expect(rendererFactory.getRenderer("string")).andReturn(stringRenderer);
        expect(rendererFactory.getRenderer("template")).andReturn(templateRenderer);
        expect(rendererFactory.getRenderer("definition")).andReturn(definitionRenderer);
        expect(rendererFactory.getRenderer("velocity")).andReturn(velocityRenderer);
        expect(rendererFactory.getRenderer("freemarker")).andReturn(freemarkerRenderer);

        replay(container, attributeEvaluatorFactory, rendererFactory,
                applicationContext);
        AttributeRenderer renderer = factory.createDefaultAttributeRenderer(
                rendererFactory, applicationContext, container,
                attributeEvaluatorFactory);
        assertTrue("The default renderer class is not correct",
                renderer instanceof ChainedDelegateAttributeRenderer);
        verify(container, attributeEvaluatorFactory, rendererFactory,
                applicationContext);
    }

    /**
     * Test method for
     * {@link CompleteAutoloadTilesContainerFactory
     * #createAttributeEvaluatorFactory(ApplicationContext, locale.LocaleResolver)}
     * .
     */
    @Test
    public void testCreateAttributeEvaluatorFactory() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        LocaleResolver resolver = createMock(LocaleResolver.class);
        ServletContext servletContext = createMock(ServletContext.class);
        JspFactory jspFactory = createMock(JspFactory.class);
        JspApplicationContext jspApplicationContext = createMock(JspApplicationContext.class);
        ExpressionFactory expressionFactory = createMock(ExpressionFactory.class);

        expect(applicationContext.getContext()).andReturn(servletContext);
        expect(jspFactory.getJspApplicationContext(servletContext)).andReturn(jspApplicationContext);
        expect(jspApplicationContext.getExpressionFactory()).andReturn(expressionFactory);

        replay(applicationContext, resolver, servletContext, jspFactory,
                jspApplicationContext, expressionFactory);
        JspFactory.setDefaultFactory(jspFactory);
        AttributeEvaluatorFactory attributeEvaluatorFactory = factory
                .createAttributeEvaluatorFactory(applicationContext,
                        resolver);
        assertTrue(attributeEvaluatorFactory instanceof BasicAttributeEvaluatorFactory);
        assertNotNull(attributeEvaluatorFactory.getAttributeEvaluator("EL"));
        assertNotNull(attributeEvaluatorFactory.getAttributeEvaluator("MVEL"));
        assertNotNull(attributeEvaluatorFactory.getAttributeEvaluator("OGNL"));
        verify(applicationContext, resolver, servletContext, jspFactory,
                jspApplicationContext, expressionFactory);
    }

    /**
     * Test method for
     * {@link CompleteAutoloadTilesContainerFactory#createPatternDefinitionResolver(Class)}
     * .
     */
    @Test
    public void testCreatePatternDefinitionResolver() {
        PatternDefinitionResolver<Integer> resolver = factory
                .createPatternDefinitionResolver(Integer.class);
        assertTrue(resolver instanceof PrefixedPatternDefinitionResolver);
        Definition definitionWildcard = new Definition("WILDCARD:blah*", (Attribute) null, null);
        Definition definitionRegexp = new Definition("REGEXP:what(.*)", (Attribute) null, null);
        Map<String, Definition> definitionMap = new HashMap<String, Definition>();
        definitionMap.put("WILDCARD:blah*", definitionWildcard);
        definitionMap.put("REGEXP:what(.*)", definitionRegexp);
        resolver.storeDefinitionPatterns(definitionMap, 1);
        Definition result = resolver.resolveDefinition("blahX", 1);
        assertEquals("blahX", result.getName());
        result = resolver.resolveDefinition("whatX", 1);
        assertEquals("whatX", result.getName());
    }

    /**
     * Test method for
     * {@link CompleteAutoloadTilesContainerFactory#getSourceURLs(ApplicationContext)}
     * .
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetSourceURLs() throws IOException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        URL url1 = new URL("file:///nonexistent/tiles.xml");
        URL url2 = new URL("file:///nonexistent/tiles_it.xml");
        URL url3 = new URL("file:///nonexistent2/tiles.xml");

        Set<URL> urls1 = new HashSet<URL>();
        urls1.add(url1);
        urls1.add(url2);

        Set<URL> urls2 = new HashSet<URL>();
        urls2.add(url3);

        expect(applicationContext.getResources("/WEB-INF/**/tiles*.xml")).andReturn(urls1);
        expect(applicationContext.getResources("classpath*:META-INF/**/tiles*.xml")).andReturn(urls2);

        replay(applicationContext);
        List<URL> urls = factory.getSourceURLs(applicationContext);
        assertEquals(2, urls.size());
        assertTrue(urls.contains(url1));
        assertTrue(urls.contains(url3));
        verify(applicationContext);
    }

    /**
     * Regression test for TILES-484 issue.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testTILES484first() throws IOException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        URL url3 = new URL("file:///nonexistent2/tiles.xml");

        Set<URL> urls2 = new HashSet<URL>();
        urls2.add(url3);

        expect(applicationContext.getResources("/WEB-INF/**/tiles*.xml")).andReturn(null);
        expect(applicationContext.getResources("classpath*:META-INF/**/tiles*.xml")).andReturn(urls2);

        replay(applicationContext);
        List<URL> urls = factory.getSourceURLs(applicationContext);
        assertEquals(1, urls.size());
        assertTrue(urls.contains(url3));
        verify(applicationContext);
    }

    /**
     * Regression test for TILES-484 issue.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testTILES484second() throws IOException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        URL url1 = new URL("file:///nonexistent/tiles.xml");
        URL url2 = new URL("file:///nonexistent/tiles_it.xml");

        Set<URL> urls1 = new HashSet<URL>();
        urls1.add(url1);
        urls1.add(url2);

        expect(applicationContext.getResources("/WEB-INF/**/tiles*.xml")).andReturn(urls1);
        expect(applicationContext.getResources("classpath*:META-INF/**/tiles*.xml")).andReturn(null);

        replay(applicationContext);
        List<URL> urls = factory.getSourceURLs(applicationContext);
        assertEquals(1, urls.size());
        assertTrue(urls.contains(url1));
        verify(applicationContext);
    }

    /**
     * Test method for
     * {@link CompleteAutoloadTilesContainerFactory
     * #createDefinitionsReader(ApplicationContext)}
     * .
     */
    @Test
    public void testCreateDefinitionsReader() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        replay(applicationContext);
        assertTrue(factory.createDefinitionsReader(applicationContext) instanceof CompatibilityDigesterDefinitionsReader);
        verify(applicationContext);
    }

}
