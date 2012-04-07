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
package org.apache.tiles.impl;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.BasicAttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.preparer.factory.NoSuchPreparerException;
import org.apache.tiles.preparer.factory.PreparerFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.CannotRenderException;
import org.apache.tiles.request.render.NoSuchRendererException;
import org.apache.tiles.request.render.Renderer;
import org.apache.tiles.request.render.RendererFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link BasicTilesContainer}.
 *
 * @version $Rev$ $Date$
 */
public class BasicTilesContainerUnitTest {

    /**
     * Name used to store attribute context stack.
     */
    private static final String ATTRIBUTE_CONTEXT_STACK =
        "org.apache.tiles.AttributeContext.STACK";

    /**
     * The application context.
     */
    private ApplicationContext applicationContext;

    /**
     * The definitions factory.
     */
    private DefinitionsFactory definitionsFactory;

    /**
     * The preparer factory.
     */
    private PreparerFactory preparerFactory;

    /**
     * The renderer factory.
     */
    private RendererFactory rendererFactory;

    /**
     * The evaluator factory.
     */
    private AttributeEvaluatorFactory attributeEvaluatorFactory;

    /**
     * The container to test.
     */
    private BasicTilesContainer container;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        applicationContext = createMock(ApplicationContext.class);
        definitionsFactory = createMock(DefinitionsFactory.class);
        preparerFactory = createMock(PreparerFactory.class);
        rendererFactory = createMock(RendererFactory.class);
        attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        container = new BasicTilesContainer();
        container.setApplicationContext(applicationContext);
        container.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        container.setDefinitionsFactory(definitionsFactory);
        container.setPreparerFactory(preparerFactory);
        container.setRendererFactory(rendererFactory);
    }

    /**
     * Test method for {@link BasicTilesContainer#startContext(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStartContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        deque.push(isA(BasicAttributeContext.class));

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertTrue(container.startContext(request) instanceof BasicAttributeContext);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link BasicTilesContainer#endContext(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEndContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.pop()).andReturn(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        container.endContext(request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link BasicTilesContainer#renderContext(Request)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRenderContext() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        Attribute templateAttribute = createMock(Attribute.class);
        Renderer renderer = createMock(Renderer.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn("/mytemplate.jsp");
        expect(templateAttribute.isPermitted(request)).andReturn(true);
        renderer.render("/mytemplate.jsp", request);

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, templateAttribute, renderer);
        container.renderContext(request);
        verify(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, templateAttribute, renderer);
    }

    /**
     * Test method for {@link BasicTilesContainer#getApplicationContext()}.
     */
    @Test
    public void testGetApplicationContext() {
        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
        assertEquals(applicationContext, container.getApplicationContext());
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
    }

    /**
     * Test method for {@link BasicTilesContainer#getAttributeContext(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAttributeContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertEquals(attributeContext, container.getAttributeContext(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link BasicTilesContainer#getAttributeContext(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAttributeContextNew() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope).times(2);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque).times(2);
        expect(deque.isEmpty()).andReturn(true);
        deque.push(isA(BasicAttributeContext.class));

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertTrue(container.getAttributeContext(request) instanceof BasicAttributeContext);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link BasicTilesContainer#getDefinitionsFactory()}.
     */
    @Test
    public void testGetDefinitionsFactory() {
        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
        assertEquals(definitionsFactory, container.getDefinitionsFactory());
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
    }

    /**
     * Test method for {@link BasicTilesContainer#getPreparerFactory()}.
     */
    @Test
    public void testGetPreparerFactory() {
        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
        assertEquals(preparerFactory, container.getPreparerFactory());
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
    }

    /**
     * Test method for {@link BasicTilesContainer#prepare(java.lang.String, Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrepare() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);

        expect(preparerFactory.getPreparer("preparer", request)).andReturn(preparer);
        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        preparer.execute(request, attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer);
        container.prepare("preparer", request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer);
    }

    /**
     * Test method for {@link BasicTilesContainer#prepare(java.lang.String, Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = NoSuchPreparerException.class)
    public void testPrepareException() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(preparerFactory.getPreparer("preparer", request)).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        try {
            container.prepare("preparer", request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, requestScope, deque, attributeContext);
        }
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRenderStringRequest() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        Renderer renderer = createMock(Renderer.class);
        Definition definition = createMock(Definition.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        Attribute templateAttribute = Attribute.createTemplateAttribute("/my/template.jsp");

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(definition);
        expect(request.getContext("request")).andReturn(requestScope).times(3);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque).times(3);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(attributeContext.getLocalAttributeNames()).andReturn(null);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        expect(definition.getTemplateAttribute()).andReturn(templateAttribute);
        expect(rendererFactory.getRenderer("template")).andReturn(renderer);
        deque.push(isA(BasicAttributeContext.class));
        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn("/my/template.jsp");
        renderer.render("/my/template.jsp", request);
        expect(deque.pop()).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
        container.render("definition", request);
        verify(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, java.lang.String)}.
     */
    @Test(expected = NoSuchDefinitionException.class)
    public void testRenderStringRequestException() {
        Request request = createMock(Request.class);

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
        try {
            container.render("definition", request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory);
        }
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, org.apache.tiles.Attribute)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderAttributeRequest() throws IOException {
        Request request = createMock(Request.class);
        Attribute templateAttribute = createMock(Attribute.class);
        Renderer renderer = createMock(Renderer.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn("/mytemplate.jsp");
        expect(templateAttribute.isPermitted(request)).andReturn(true);
        renderer.render("/mytemplate.jsp", request);

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute, renderer);
        container.render(templateAttribute, request);
        verify(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute, renderer);
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, org.apache.tiles.Attribute)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = CannotRenderException.class)
    public void testRenderAttributeRequestException1() throws IOException {
        Request request = createMock(Request.class);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
        try {
            container.render((Attribute) null, request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request);
        }
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, org.apache.tiles.Attribute)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = NoSuchRendererException.class)
    public void testRenderAttributeRequestException2() throws IOException {
        Request request = createMock(Request.class);
        Attribute templateAttribute = createMock(Attribute.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(templateAttribute.isPermitted(request)).andReturn(true);
        expect(rendererFactory.getRenderer("renderer")).andThrow(new NoSuchRendererException());

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute);
        try {
            container.render(templateAttribute, request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory, evaluator,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, templateAttribute);
        }
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, org.apache.tiles.Attribute)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = CannotRenderException.class)
    public void testRenderAttributeRequestException3() throws IOException {
        Request request = createMock(Request.class);
        Attribute templateAttribute = createMock(Attribute.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);
        Renderer renderer = createMock(Renderer.class);

        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(templateAttribute.isPermitted(request)).andReturn(true);
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn(new Integer(1));

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute);
        try {
            container.render(templateAttribute, request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory, evaluator,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, templateAttribute);
        }
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, org.apache.tiles.Attribute)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = NoSuchRendererException.class)
    public void testRenderAttributeRequestException() throws IOException {
        Request request = createMock(Request.class);
        Attribute templateAttribute = createMock(Attribute.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(templateAttribute.isPermitted(request)).andReturn(true);
        expect(rendererFactory.getRenderer("renderer")).andThrow(new NoSuchRendererException());

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute);
        try {
            container.render(templateAttribute, request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory, evaluator,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, templateAttribute);
        }
    }

    /**
     * Test method for {@link BasicTilesContainer#evaluate(org.apache.tiles.Attribute, Request)}.
     */
    @Test
    public void testEvaluate() {
        Request request = createMock(Request.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);
        Attribute templateAttribute = createMock(Attribute.class);

        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn(new Integer(1));

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute, evaluator);
        assertEquals(new Integer(1), container.evaluate(templateAttribute, request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute, evaluator);
    }

    /**
     * Test method for {@link BasicTilesContainer#isValidDefinition(java.lang.String, Request)}.
     */
    @Test
    public void testIsValidDefinition() {
        Request request = createMock(Request.class);
        Definition definition = createMock(Definition.class);

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(definition);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request, definition);
        assertTrue(container.isValidDefinition("definition", request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request, definition);
    }

    /**
     * Test method for {@link BasicTilesContainer#isValidDefinition(java.lang.String, Request)}.
     */
    @Test
    public void testIsValidDefinitionNull() {
        Request request = createMock(Request.class);

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
        assertFalse(container.isValidDefinition("definition", request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
    }

    /**
     * Test method for {@link BasicTilesContainer#isValidDefinition(java.lang.String, Request)}.
     */
    @Test
    public void testIsValidDefinitionException() {
        Request request = createMock(Request.class);

        expect(definitionsFactory.getDefinition("definition", request))
                .andThrow(new NoSuchDefinitionException());

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
        assertFalse(container.isValidDefinition("definition", request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
    }

    /**
     * Test method for {@link BasicTilesContainer#getDefinition(java.lang.String, Request)}.
     */
    @Test
    public void testGetDefinition() {
        Request request = createMock(Request.class);
        Definition definition = createMock(Definition.class);

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(definition);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request, definition);
        assertEquals(definition, container.getDefinition("definition", request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request, definition);
    }

    /**
     * Test method for {@link BasicTilesContainer#getContextStack(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetContextStack() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque);
        assertEquals(deque, container.getContextStack(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque);
    }

    /**
     * Test method for {@link BasicTilesContainer#getContextStack(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetContextStackNew() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(null);
        expect(requestScope.put(eq(ATTRIBUTE_CONTEXT_STACK), isA(LinkedList.class))).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope);
        assertTrue(container.getContextStack(request) instanceof LinkedList);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope);
    }

    /**
     * Test method for {@link BasicTilesContainer#pushContext(org.apache.tiles.AttributeContext, Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPushContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        deque.push(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        container.pushContext(attributeContext, request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link BasicTilesContainer#popContext(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPopContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.pop()).andReturn(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertEquals(attributeContext, container.popContext(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link BasicTilesContainer#getContext(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertEquals(attributeContext, container.getContext(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link BasicTilesContainer#getContext(Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetContextNull() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(true);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque);
        assertNull(container.getContext(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque);
    }

    /**
     * Test method for {@link BasicTilesContainer#render(org.apache.tiles.Definition, Request)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRenderRequestDefinition() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        Renderer renderer = createMock(Renderer.class);
        Definition definition = createMock(Definition.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        Attribute templateAttribute = Attribute.createTemplateAttribute("/my/template.jsp");

        expect(request.getContext("request")).andReturn(requestScope).times(3);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque).times(3);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(attributeContext.getLocalAttributeNames()).andReturn(null);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        expect(definition.getTemplateAttribute()).andReturn(templateAttribute);
        expect(rendererFactory.getRenderer("template")).andReturn(renderer);
        deque.push(isA(BasicAttributeContext.class));
        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn("/my/template.jsp");
        renderer.render("/my/template.jsp", request);
        expect(deque.pop()).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
        container.render(definition, request);
        verify(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
    }

    /**
     * Test method for {@link BasicTilesContainer#render(org.apache.tiles.Definition, Request)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = CannotRenderException.class)
    public void testRenderRequestDefinitionException() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        Renderer renderer = createMock(Renderer.class);
        Definition definition = createMock(Definition.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        Attribute templateAttribute = Attribute.createTemplateAttribute("/my/template.jsp");

        expect(request.getContext("request")).andReturn(requestScope).times(3);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque).times(3);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(attributeContext.getLocalAttributeNames()).andReturn(null);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        expect(definition.getTemplateAttribute()).andReturn(templateAttribute);
        expect(rendererFactory.getRenderer("template")).andReturn(renderer);
        deque.push(isA(BasicAttributeContext.class));
        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn("/mytemplate.jsp");
        renderer.render("/mytemplate.jsp", request);
        expectLastCall().andThrow(new IOException());
        expect(deque.pop()).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
        try {
            container.render(definition, request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory, evaluator,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, requestScope, deque, attributeContext, preparer,
                    renderer, definition);
        }
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, org.apache.tiles.AttributeContext)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRenderRequestAttributeContext() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        Attribute templateAttribute = createMock(Attribute.class);
        Renderer renderer = createMock(Renderer.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn("/mytemplate.jsp");
        expect(templateAttribute.isPermitted(request)).andReturn(true);
        renderer.render("/mytemplate.jsp", request);

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, templateAttribute, renderer);
        container.render(request, attributeContext);
        verify(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, templateAttribute, renderer);
    }

    /**
     * Test method for {@link BasicTilesContainer#render(Request, org.apache.tiles.AttributeContext)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = CannotRenderException.class)
    public void testRenderRequestAttributeContextException() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute templateAttribute = createMock(Attribute.class);
        Renderer renderer = createMock(Renderer.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);

        expect(attributeContext.getPreparer()).andReturn("preparer").times(2);
        expect(preparerFactory.getPreparer("preparer", request)).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn("/mytemplate.jsp");
        expect(templateAttribute.isPermitted(request)).andReturn(true);
        renderer.render("/mytemplate.jsp", request);
        expectLastCall().andThrow(new IOException());

        replay(applicationContext, attributeEvaluatorFactory, evaluator,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, templateAttribute, renderer);
        try {
            container.render(request, attributeContext);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory, evaluator,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, requestScope, deque, attributeContext,
                    templateAttribute, renderer);
        }
    }
}
