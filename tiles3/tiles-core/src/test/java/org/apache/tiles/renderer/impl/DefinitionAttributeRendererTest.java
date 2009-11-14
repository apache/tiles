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
import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.tiles.Attribute;
import org.apache.tiles.Expression;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DefinitionAttributeRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionAttributeRendererTest {

    /**
     * The renderer.
     */
    private DefinitionAttributeRenderer renderer;

    /** {@inheritDoc} */
    @Before
    public void setUp() throws Exception {
        renderer = new DefinitionAttributeRenderer();
        renderer.setAttributeEvaluatorFactory(new BasicAttributeEvaluatorFactory(
                new DirectAttributeEvaluator()));
    }

    /**
     * Tests
     * {@link StringAttributeRenderer#write(Object, Attribute, Request)}.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWrite() throws IOException {
        Attribute attribute = new Attribute("my.definition", (Expression) null,
                null, "definition");
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Request requestContext = createMock(Request.class);
        container.render("my.definition", requestContext);
		replay(applicationContext, requestContext, container);
        renderer.setApplicationContext(applicationContext);
        renderer.setContainer(container);
        renderer.render(attribute, requestContext);
		verify(applicationContext, requestContext, container);
    }

    /**
     * Tests
     * {@link DefinitionAttributeRenderer#isRenderable(Object, Attribute, Request)}
     * .
     */
    @Test
    public void testIsRenderable() {
        Attribute attribute = new Attribute("my.definition", (Expression) null,
                null, "definition");
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Request requestContext = createMock(Request.class);
        expect(container.isValidDefinition("my.definition", requestContext)).andReturn(Boolean.TRUE);
		replay(applicationContext, requestContext, container);
        renderer.setApplicationContext(applicationContext);
        renderer.setContainer(container);
        assertTrue(renderer.isRenderable("my.definition", attribute, requestContext));
		verify(applicationContext, requestContext, container);
    }
}
