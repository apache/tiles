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
import java.io.StringWriter;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.StringRenderer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link StringRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class StringRendererTest {

    /**
     * The renderer.
     */
    private StringRenderer renderer;

    /** {@inheritDoc} */
    @Before
    public void setUp() {
        renderer = new StringRenderer();
    }

    /**
     * Tests
     * {@link StringRenderer#render(String, Request)}.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWrite() throws IOException {
        StringWriter writer = new StringWriter();
        Request requestContext = createMock(Request.class);
        expect(requestContext.getWriter()).andReturn(writer);
        replay(requestContext);
        renderer.render("Result", requestContext);
        writer.close();
        assertEquals("Not written 'Result'", "Result", writer.toString());
        verify(requestContext);
    }

    /**
     * Tests
     * {@link StringRenderer#isRenderable(String, Request)}.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testIsRenderable() {
        Request requestContext = createMock(Request.class);
        replay(requestContext);
        assertTrue(renderer.isRenderable("Result", requestContext));
        verify(requestContext);
    }
}
