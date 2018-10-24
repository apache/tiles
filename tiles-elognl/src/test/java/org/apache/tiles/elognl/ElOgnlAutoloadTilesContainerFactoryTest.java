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

package org.apache.tiles.elognl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.el.ExpressionFactory;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.renderer.DefinitionRenderer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.freemarker.render.FreemarkerRenderer;
import org.apache.tiles.request.render.BasicRendererFactory;
import org.apache.tiles.request.render.ChainedDelegateRenderer;
import org.apache.tiles.request.render.DispatchRenderer;
import org.apache.tiles.request.render.Renderer;
import org.apache.tiles.request.render.StringRenderer;
import org.apache.tiles.request.servlet.ServletApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ElOgnlTilesContainerFactory}.
 *
 * @version $Rev$ $Date$
 */
public class ElOgnlAutoloadTilesContainerFactoryTest {

    /**
     * The object to test.
     */
    private ElOgnlTilesContainerFactory factory;

    /**
     * Initializes the object.
     */
    @Before
    public void setUp() {
        factory = new ElOgnlTilesContainerFactory();
    }

   

    /**
     * Test method for
     * {@link ElOgnlTilesContainerFactory
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

        
        rendererFactory.registerRenderer(eq("string"), isA(StringRenderer.class));
        rendererFactory.registerRenderer(eq("template"), isA(DispatchRenderer.class));
        rendererFactory.registerRenderer(eq("definition"), isA(DefinitionRenderer.class));
        rendererFactory.registerRenderer(eq("freemarker"), isA(FreemarkerRenderer.class));

        expect(applicationContext.getContext()).andReturn(servletContext).anyTimes();
//        expect(servletContext.getResourceAsStream(VelocityView.DEPRECATED_USER_TOOLS_PATH)).andReturn(null);
        servletContext.log((String) anyObject());
        expectLastCall().anyTimes();

        replay(rendererFactory, applicationContext, container, attributeEvaluatorFactory, servletContext);
        factory.registerAttributeRenderers(rendererFactory, applicationContext, container, attributeEvaluatorFactory);
        verify(rendererFactory, applicationContext, container, attributeEvaluatorFactory, servletContext);
    }

    /**
     * Tests
     * {@link ElOgnlTilesContainerFactory#createDefaultAttributeRenderer(BasicRendererFactory,
     * ApplicationContext, TilesContainer, AttributeEvaluatorFactory)}.
     */
    @Test
    public void testCreateDefaultAttributeRenderer() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        BasicRendererFactory rendererFactory = createMock(BasicRendererFactory.class);
        Renderer stringRenderer = createMock(Renderer.class);
        Renderer templateRenderer = createMock(Renderer.class);
        Renderer definitionRenderer = createMock(Renderer.class);
        Renderer freemarkerRenderer = createMock(Renderer.class);

        expect(rendererFactory.getRenderer("string")).andReturn(stringRenderer);
        expect(rendererFactory.getRenderer("template")).andReturn(templateRenderer);
        expect(rendererFactory.getRenderer("definition")).andReturn(definitionRenderer);
        expect(rendererFactory.getRenderer("freemarker")).andReturn(freemarkerRenderer);

        replay(container, attributeEvaluatorFactory, rendererFactory, applicationContext);
        Renderer renderer = factory.createDefaultAttributeRenderer(rendererFactory, applicationContext, container,
                attributeEvaluatorFactory);
        assertTrue("The default renderer class is not correct", renderer instanceof ChainedDelegateRenderer);
        verify(container, attributeEvaluatorFactory, rendererFactory, applicationContext);
    }

    /**
     * Test method for
     * {@link ElOgnlTilesContainerFactory
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

        replay(applicationContext, resolver, servletContext, jspFactory, jspApplicationContext, expressionFactory);
        JspFactory.setDefaultFactory(jspFactory);
        AttributeEvaluatorFactory attributeEvaluatorFactory = factory.createAttributeEvaluatorFactory(
                applicationContext, resolver);
        assertTrue(attributeEvaluatorFactory instanceof BasicAttributeEvaluatorFactory);
        assertNotNull(attributeEvaluatorFactory.getAttributeEvaluator("EL"));
        assertNotNull(attributeEvaluatorFactory.getAttributeEvaluator("OGNL"));
        verify(applicationContext, resolver, servletContext, jspFactory, jspApplicationContext, expressionFactory);
    }

    
    /**
     * Test method for
     * {@link ElOgnlTilesContainerFactory#getSources(ApplicationContext)}
     * .
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetSources() throws IOException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        ApplicationResource resource1 = createMock(ApplicationResource.class);
        expect(resource1.getLocale()).andReturn(Locale.ROOT);

        Collection<ApplicationResource> resourceSet1 = new HashSet<ApplicationResource>();
        resourceSet1.add(resource1);

        expect(applicationContext.getResources("/WEB-INF/tiles.xml")).andReturn(resourceSet1);

        replay(applicationContext, resource1);
        List<ApplicationResource> urls = factory.getSources(applicationContext);
        assertEquals(1, urls.size());
        assertTrue(urls.contains(resource1));
        verify(applicationContext, resource1);
    }
    
   
    @Test
    public void testEmptyReturn() throws IOException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(applicationContext.getResources("/WEB-INF/tiles.xml")).andReturn(null);

        replay(applicationContext);
        List<ApplicationResource> resources = factory.getSources(applicationContext);
        assertEquals(0, resources.size());
        verify(applicationContext);
    }

}
