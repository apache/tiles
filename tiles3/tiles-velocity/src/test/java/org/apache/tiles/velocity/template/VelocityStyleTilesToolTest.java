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

package org.apache.tiles.velocity.template;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.Expression;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.scope.ContextResolver;
import org.apache.tiles.request.util.ApplicationAccess;
import org.apache.tiles.velocity.context.VelocityTilesRequestContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.Renderable;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VelocityStyleTilesTool}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class VelocityStyleTilesToolTest {

    /**
     * The tool to test.
     */
    private VelocityStyleTilesTool tool;

    /**
     * The request object.
     */
    private HttpServletRequest request;

    /**
     * The response object.
     */
    private HttpServletResponse response;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * The current velocity context.
     */
    private Context velocityContext;

    /**
     * Sets up the tool to test.
     *
     * @since 2.2.0
     */
    @Before
    public void setUp() {
        tool = new VelocityStyleTilesTool();
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        velocityContext = createMock(Context.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#getAttribute(java.lang.String)}.
     */
    @Test
    public void testGetAttribute() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Attribute attribute = new Attribute("myValue");
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
        expect(resolver.getContext(isA(VelocityTilesRequestContext.class),
        		eq("request"))).andReturn(requestScope).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
        expect(container.getAttributeContext(isA(VelocityTilesRequestContext.class)))
                .andReturn(attributeContext);
        expect(attributeContext.getAttribute("myAttribute")).andReturn(attribute);

        replay(velocityContext, request, response, servletContext, container, attributeContext, applicationContext, resolver);
        initializeTool();
        assertEquals(attribute, tool.getAttribute("myAttribute"));
        verify(velocityContext, request, response, servletContext, container, attributeContext, applicationContext, resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#createAttribute()}.
     */
    @Test
    public void testCreateAttribute() {
        replay(velocityContext, request, response, servletContext);
        initializeTool();
        Attribute attribute =  tool.createAttribute();
        assertNull(attribute.getValue());
        assertNull(attribute.getRenderer());
        assertNull(attribute.getExpressionObject());
        verify(velocityContext, request, response, servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool
     * #cloneAttribute(org.apache.tiles.Attribute)}.
     */
    @Test
    public void testCloneAttribute() {
        Attribute attribute = new Attribute("myValue", Expression
                .createExpression("myExpression", null), "myRole",
                "myRendererName");

        replay(velocityContext, request, response, servletContext);
        initializeTool();
        assertEquals(attribute, tool.cloneAttribute(attribute));
        verify(velocityContext, request, response, servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool
     * #createTemplateAttribute(java.lang.String)}.
     */
    @Test
    public void testCreateTemplateAttribute() {
        replay(velocityContext, request, response, servletContext);
        initializeTool();
        Attribute attribute = tool.createTemplateAttribute("myTemplate");
        assertEquals("myTemplate", attribute.getValue());
        assertEquals("template", attribute.getRenderer());
        verify(velocityContext, request, response, servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool
     * #renderAttribute(org.apache.tiles.Attribute)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderAttribute() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        StringWriter writer = new StringWriter();
        Attribute attribute = new Attribute("myValue");
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
        expect(resolver.getContext(isA(VelocityTilesRequestContext.class),
        		eq("request"))).andReturn(requestScope).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
        container.render(eq(attribute), isA(VelocityTilesRequestContext.class));

		replay(velocityContext, request, response, servletContext, container,
				internalContextAdapter, applicationContext, resolver);
        initializeTool();
        Renderable renderable = tool.renderAttribute(attribute);
        renderable.render(internalContextAdapter, writer);
		verify(velocityContext, request, response, servletContext, container,
				internalContextAdapter, applicationContext, resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool
     * #renderDefinition(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderDefinition() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        StringWriter writer = new StringWriter();
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
        expect(resolver.getContext(isA(VelocityTilesRequestContext.class),
        		eq("request"))).andReturn(requestScope).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
        container.render(eq("myDefinition"), isA(VelocityTilesRequestContext.class));

		replay(velocityContext, request, response, servletContext, container,
				internalContextAdapter, applicationContext, resolver);
        initializeTool();
        Renderable renderable = tool.renderDefinition("myDefinition");
        renderable.render(internalContextAdapter, writer);
		verify(velocityContext, request, response, servletContext, container,
				internalContextAdapter, applicationContext, resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#renderAttributeContext()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderAttributeContext() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        StringWriter writer = new StringWriter();
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
        expect(resolver.getContext(isA(VelocityTilesRequestContext.class),
        		eq("request"))).andReturn(requestScope).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
        container.renderContext(isA(VelocityTilesRequestContext.class));

		replay(velocityContext, request, response, servletContext, container,
				internalContextAdapter, applicationContext, resolver);
        initializeTool();
        Renderable renderable = tool.renderAttributeContext();
        renderable.render(internalContextAdapter, writer);
		verify(velocityContext, request, response, servletContext, container,
				internalContextAdapter, applicationContext, resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#startAttributeContext()}.
     */
    @Test
    public void testStartAttributeContext() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
        expect(resolver.getContext(isA(VelocityTilesRequestContext.class),
        		eq("request"))).andReturn(requestScope).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
        expect(container.startContext(isA(VelocityTilesRequestContext.class)))
                .andReturn(attributeContext);

		replay(velocityContext, request, response, servletContext, container,
				attributeContext, applicationContext, resolver);
        initializeTool();
        assertEquals(attributeContext, tool.startAttributeContext());
		verify(velocityContext, request, response, servletContext, container,
				attributeContext, applicationContext, resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#endAttributeContext()}.
     */
    @Test
    public void testEndAttributeContext() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
        expect(resolver.getContext(isA(VelocityTilesRequestContext.class),
        		eq("request"))).andReturn(requestScope).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
        container.endContext(isA(VelocityTilesRequestContext.class));

		replay(velocityContext, request, response, servletContext, container,
				attributeContext, applicationContext, resolver);
        initializeTool();
        tool.endAttributeContext();
		verify(velocityContext, request, response, servletContext, container,
				attributeContext, applicationContext, resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#getAttributeContext()}.
     */
    @Test
    public void testGetAttributeContext() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
        expect(resolver.getContext(isA(VelocityTilesRequestContext.class),
        		eq("request"))).andReturn(requestScope).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();
        expect(container.getAttributeContext(isA(VelocityTilesRequestContext.class)))
                .andReturn(attributeContext);

		replay(velocityContext, request, response, servletContext, container,
				attributeContext, applicationContext, resolver);
        initializeTool();
        assertEquals(attributeContext, tool.getAttributeContext());
		verify(velocityContext, request, response, servletContext, container,
				attributeContext, applicationContext, resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool
     * #setCurrentContainer(java.lang.String)}.
     */
    @Test
    public void testSetCurrentContainer() {
        TilesContainer container = createMock(TilesContainer.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("myKey", container);
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, resolver);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
        expect(resolver.getContext(isA(VelocityTilesRequestContext.class),
        		eq("request"))).andReturn(requestScope).anyTimes();
        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext)
                .anyTimes();

        replay(velocityContext, request, response, servletContext, container, applicationContext, resolver);
        initializeTool();
        assertEquals(tool, tool.setCurrentContainer("myKey"));
        assertEquals(container, requestScope.get(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME));
        verify(velocityContext, request, response, servletContext, container, applicationContext, resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("", tool.toString());
    }

    /**
     * Initializes the tool for the test.
     */
    private void initializeTool() {
        tool.setRequest(request);
        tool.setResponse(response);
        tool.setServletContext(servletContext);
        tool.setVelocityContext(velocityContext);
    }
}
