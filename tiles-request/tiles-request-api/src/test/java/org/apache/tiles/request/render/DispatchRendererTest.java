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
package org.apache.tiles.request.render;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.DispatchRenderer;
import org.apache.tiles.request.render.InvalidTemplateException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DispatchRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class DispatchRendererTest {

    /**
     * The renderer.
     */
    private DispatchRenderer renderer;

    /** {@inheritDoc} */
    @Before
    public void setUp() {
        renderer = new DispatchRenderer();
    }

    /**
     * Tests
     * {@link DispatchRenderer#render(String, Request)}.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWrite() throws IOException {
        Request requestContext = createMock(Request.class);
        requestContext.dispatch("/myTemplate.jsp");
        replay(requestContext);
        renderer.render("/myTemplate.jsp", requestContext);
        verify(requestContext);
    }

    /**
     * Tests
     * {@link DispatchRenderer#render(String, Request)}.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test(expected=InvalidTemplateException.class)
    public void testWriteNull() throws IOException {
        Request requestContext = createMock(Request.class);
        replay(requestContext);
        renderer.render(null, requestContext);
        verify(requestContext);
    }

    /**
     * Tests
     * {@link DispatchRenderer#isRenderable(String, Request)}.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testIsRenderable() {
        Request requestContext = createMock(Request.class);
        replay(requestContext);
        assertTrue(renderer.isRenderable("/myTemplate.jsp", requestContext));
        verify(requestContext);
    }
}
