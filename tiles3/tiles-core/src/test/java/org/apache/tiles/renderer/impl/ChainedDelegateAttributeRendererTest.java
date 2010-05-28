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
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.renderer.RendererException;
import org.apache.tiles.renderer.TypeDetectingAttributeRenderer;
import org.apache.tiles.request.Request;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ChainedDelegateAttributeRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class ChainedDelegateAttributeRendererTest {

    /**
     * The renderer.
     */
    private ChainedDelegateAttributeRenderer renderer;

    /**
     * A mock string attribute renderer.
     */
    private TypeDetectingAttributeRenderer stringRenderer;

    /**
     * A mock template attribute renderer.
     */
    private TypeDetectingAttributeRenderer templateRenderer;

    /**
     * A mock definition attribute renderer.
     */
    private TypeDetectingAttributeRenderer definitionRenderer;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        stringRenderer = createMock(TypeDetectingAttributeRenderer.class);
        templateRenderer = createMock(TypeDetectingAttributeRenderer.class);
        definitionRenderer = createMock(TypeDetectingAttributeRenderer.class);
        renderer = new ChainedDelegateAttributeRenderer();
        renderer.setAttributeEvaluatorFactory(new BasicAttributeEvaluatorFactory(
                new DirectAttributeEvaluator()));
        renderer.addAttributeRenderer(definitionRenderer);
        renderer.addAttributeRenderer(templateRenderer);
        renderer.addAttributeRenderer(stringRenderer);
    }

    /**
     * Tests
     * {@link ChainedDelegateAttributeRenderer#render(Attribute, Request)}
     * writing a definition.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWriteDefinition() throws IOException {
        StringWriter writer = new StringWriter();
        Attribute attribute = new Attribute("my.definition", (Expression) null,
                null, "definition");
        Request requestContext = EasyMock
                .createMock(Request.class);

        expect(
                definitionRenderer.isRenderable("my.definition", attribute,
                        requestContext)).andReturn(Boolean.TRUE);
        definitionRenderer.render(attribute, requestContext);

        replay(requestContext, stringRenderer, templateRenderer,
                definitionRenderer);
        renderer.render(attribute, requestContext);
        writer.close();
        verify(requestContext, stringRenderer, templateRenderer,
                definitionRenderer);
    }

    /**
     * Tests
     * {@link ChainedDelegateAttributeRenderer#render(Attribute, Request)}
     * writing a definition.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test(expected=NullPointerException.class)
    public void testWriteNull() throws IOException {
        StringWriter writer = new StringWriter();
        Attribute attribute = new Attribute("my.definition", (Expression) null,
                null, "definition");
        Request requestContext = EasyMock
                .createMock(Request.class);

        replay(requestContext, stringRenderer, templateRenderer,
                definitionRenderer);
        try {
            renderer.write(null, attribute, requestContext);
        } finally {
            writer.close();
            verify(requestContext, stringRenderer, templateRenderer,
                    definitionRenderer);
        }
    }

    /**
     * Tests
     * {@link ChainedDelegateAttributeRenderer#render(Attribute, Request)}
     * writing a definition.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test(expected=RendererException.class)
    public void testWriteNotRenderable() throws IOException {
        StringWriter writer = new StringWriter();
        Attribute attribute = new Attribute("my.definition", (Expression) null,
                null, "definition");
        Request requestContext = EasyMock
                .createMock(Request.class);

        expect(
                definitionRenderer.isRenderable("Result", attribute,
                        requestContext)).andReturn(Boolean.FALSE);
        expect(
                templateRenderer.isRenderable("Result", attribute,
                        requestContext)).andReturn(Boolean.FALSE);
        expect(stringRenderer.isRenderable("Result", attribute, requestContext))
                .andReturn(Boolean.FALSE);

        replay(requestContext, stringRenderer, templateRenderer,
                definitionRenderer);
        try {
            renderer.write("Result", attribute, requestContext);
        } finally {
            writer.close();
            verify(requestContext, stringRenderer, templateRenderer,
                    definitionRenderer);
        }
    }

    /**
     * Tests
     * {@link ChainedDelegateAttributeRenderer#render(Attribute, Request)}
     * writing a string.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWriteString() throws IOException {
        Attribute attribute = new Attribute("Result", (Expression) null, null,
                "string");
        Request requestContext = EasyMock
                .createMock(Request.class);
        expect(
                definitionRenderer.isRenderable("Result", attribute,
                        requestContext)).andReturn(Boolean.FALSE);
        expect(
                templateRenderer.isRenderable("Result", attribute,
                        requestContext)).andReturn(Boolean.FALSE);
        expect(
                stringRenderer.isRenderable("Result", attribute,
                        requestContext)).andReturn(Boolean.TRUE);
        stringRenderer.render(attribute, requestContext);

        replay(requestContext, stringRenderer, templateRenderer,
                definitionRenderer);
        renderer.render(attribute, requestContext);
        verify(requestContext, stringRenderer, templateRenderer,
                definitionRenderer);
    }

    /**
     * Tests
     * {@link ChainedDelegateAttributeRenderer#render(Attribute, Request)}
     * writing a template.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWriteTemplate() throws IOException {
        StringWriter writer = new StringWriter();
        Attribute attribute = new Attribute("/myTemplate.jsp",
                (Expression) null, null, "template");
        Request requestContext = EasyMock
                .createMock(Request.class);
        templateRenderer.render(attribute, requestContext);
        expect(
                definitionRenderer.isRenderable("/myTemplate.jsp", attribute,
                        requestContext)).andReturn(Boolean.FALSE);
        expect(
                templateRenderer.isRenderable("/myTemplate.jsp", attribute,
                        requestContext)).andReturn(Boolean.TRUE);

        replay(requestContext, stringRenderer, templateRenderer,
                definitionRenderer);
        renderer.render(attribute, requestContext);
        writer.close();
        verify(requestContext, stringRenderer, templateRenderer,
                definitionRenderer);
    }
}
