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
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link TemplateAttributeRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class TemplateAttributeRendererTest {

    /**
     * The renderer.
     */
    private TemplateAttributeRenderer renderer;

    /** {@inheritDoc} */
    @Before
    public void setUp() throws Exception {
        renderer = new TemplateAttributeRenderer();
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
        Attribute attribute = new Attribute("/myTemplate.jsp",
                (Expression) null, null, "template");
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        TilesRequestContextFactory contextFactory = createMock(TilesRequestContextFactory.class);
        Request requestContext = createMock(Request.class);
        requestContext.dispatch("/myTemplate.jsp");
        replay(applicationContext, contextFactory, requestContext);
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        renderer.render(attribute, requestContext);
        verify(applicationContext, contextFactory, requestContext);
    }

    /**
     * Tests
     * {@link StringAttributeRenderer#isRenderable(Object, Attribute, Request)}.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testIsRenderable() throws IOException {
        Attribute attribute = new Attribute("/myTemplate.jsp",
                (Expression) null, null, "template");
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        TilesRequestContextFactory contextFactory = createMock(TilesRequestContextFactory.class);
        Request requestContext = createMock(Request.class);
        replay(applicationContext, contextFactory, requestContext);
        renderer.setApplicationContext(applicationContext);
        renderer.setRequestContextFactory(contextFactory);
        assertTrue(renderer.isRenderable("/myTemplate.jsp", attribute,
                requestContext));
        verify(applicationContext, contextFactory, requestContext);
    }
}