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
package org.apache.tiles.renderer.impl;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.tiles.Attribute;
import org.apache.tiles.Expression;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.renderer.AttributeRenderer;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link UntypedAttributeRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class UntypedDelegateAttributeRendererTest {

    /**
     * The renderer.
     */
    private UntypedDelegateAttributeRenderer renderer;

    /**
     * A mock container.
     */
    private TilesContainer container;

    /**
     * A mock string attribute renderer.
     */
    private AttributeRenderer stringRenderer;

    /**
     * A mock template attribute renderer.
     */
    private AttributeRenderer templateRenderer;

    /**
     * A mock definition attribute renderer.
     */
    private AttributeRenderer definitionRenderer;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        container = createMock(TilesContainer.class);
        stringRenderer = createMock(AttributeRenderer.class);
        templateRenderer = createMock(AttributeRenderer.class);
        definitionRenderer = createMock(AttributeRenderer.class);
        renderer = new UntypedDelegateAttributeRenderer(container,
                stringRenderer, templateRenderer, definitionRenderer);
        renderer.setAttributeEvaluatorFactory(new BasicAttributeEvaluatorFactory(
                new DirectAttributeEvaluator()));
    }

    /**
     * Tests
     * {@link UntypedDelegateAttributeRenderer#render(Attribute, TilesRequestContext)}
     * writing a definition.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWriteDefinition() throws IOException {
        StringWriter writer = new StringWriter();
        Attribute attribute = new Attribute("my.definition", (Expression) null,
                null, "definition");
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        TilesRequestContextFactory contextFactory = EasyMock
                .createMock(TilesRequestContextFactory.class);
        TilesRequestContext requestContext = EasyMock
                .createMock(TilesRequestContext.class);
        Object[] requestObjects = new Object[0];
        expect(requestContext.getRequestObjects()).andReturn(requestObjects);
        expect(container.isValidDefinition("my.definition", requestObjects))
                .andReturn(Boolean.TRUE);
        definitionRenderer.render(attribute, requestContext);

        replay(applicationContext, contextFactory, requestContext, container,
                stringRenderer, templateRenderer, definitionRenderer);
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        renderer.render(attribute, requestContext);
        writer.close();
        verify(applicationContext, contextFactory, requestContext,
                container, stringRenderer, templateRenderer, definitionRenderer);
    }

    /**
     * Tests
     * {@link UntypedDelegateAttributeRenderer#render(Attribute, TilesRequestContext)}
     * writing a string.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWriteString() throws IOException {
        Attribute attribute = new Attribute("Result", (Expression) null, null,
                "string");
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        TilesRequestContextFactory contextFactory = EasyMock
                .createMock(TilesRequestContextFactory.class);
        TilesRequestContext requestContext = EasyMock
                .createMock(TilesRequestContext.class);
        Object[] requestObjects = new Object[0];
        expect(requestContext.getRequestObjects()).andReturn(requestObjects);
        expect(container.isValidDefinition("Result", requestObjects))
                .andReturn(Boolean.FALSE);
        stringRenderer.render(attribute, requestContext);

        replay(applicationContext, contextFactory, requestContext, container,
                stringRenderer, templateRenderer, definitionRenderer);
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        renderer.render(attribute, requestContext);
        verify(applicationContext, contextFactory, requestContext, container,
                stringRenderer, templateRenderer, definitionRenderer);
    }

    /**
     * Tests
     * {@link UntypedDelegateAttributeRenderer#render(Attribute, TilesRequestContext)}
     * writing a template.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWriteTemplate() throws IOException {
        StringWriter writer = new StringWriter();
        Attribute attribute = new Attribute("/myTemplate.jsp",
                (Expression) null, null, "template");
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        TilesRequestContextFactory contextFactory = EasyMock
                .createMock(TilesRequestContextFactory.class);
        TilesRequestContext requestContext = EasyMock
                .createMock(TilesRequestContext.class);
        Object[] requestObjects = new Object[0];
        expect(requestContext.getRequestObjects()).andReturn(requestObjects);
        expect(container.isValidDefinition("/myTemplate.jsp", requestObjects))
                .andReturn(Boolean.FALSE);
        templateRenderer.render(attribute, requestContext);

        replay(applicationContext, contextFactory, requestContext, container,
                stringRenderer, templateRenderer, definitionRenderer);
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        renderer.render(attribute, requestContext);
        writer.close();
        verify(applicationContext, contextFactory, requestContext, container,
                stringRenderer, templateRenderer, definitionRenderer);
    }
}
