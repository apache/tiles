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


import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.servlet.ServletRequest;
import org.junit.Test;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link MustacheRenderer}.
 *
 * @version $Rev: 1066788 $ $Date: 2011-02-03 11:49:11 +0000 (Thu, 03 Feb 2011) $
 */
public final class MustacheRendererTest {

    /**
     * Tests {@link MustacheRenderer#render(String, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRender() throws IOException {
        ServletRequest request = createMock(ServletRequest.class);
        Writer writer = createMock(Writer.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        ApplicationResource applicationResource = createMock(ApplicationResource.class);
        expect(applicationResource.getInputStream()).andReturn(getClass().getResource("/test.html").openStream());

        Map<String,Object> context = Collections.singletonMap("testKey", (Object)"test value");

        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getResource(isA(String.class))).andReturn(applicationResource).anyTimes();
        expect(request.getAvailableScopes()).andReturn(Arrays.asList("request", "session", "application"));
        expect(request.getContext("request")).andReturn(context);
        expect(request.getContext("session")).andReturn(Collections.<String,Object>emptyMap());
        expect(request.getContext("application")).andReturn(Collections.<String,Object>emptyMap());
        expect(request.getWriter()).andReturn(writer);
        writer.write("test template with test value");
        writer.flush();

        replay(request, applicationContext, applicationResource, writer);
        Renderer renderer = new MustacheRenderer();
        renderer.render("/test.html", request);
        verify(request, applicationContext, applicationResource, writer);
    }

    /**
     * Tests {@link MustacheRenderer#render(String, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = CannotRenderException.class)
    public void testRenderException() throws IOException {
        ServletRequest request = createMock(ServletRequest.class);
        replay(request);
        Renderer renderer = new MustacheRenderer();
        try {
            renderer.render(null, request);
        } finally {
            verify(request);
        }
    }

    /**
     * Test method for
     * {@link MustacheRenderer#isRenderable(String, org.apache.tiles.request.Request)}
     * .
     */
    @Test
    public void testIsRenderable() {
        MustacheRenderer renderer = new MustacheRenderer();
        final Pattern pattern = Pattern.compile("/.*");
        renderer.setAcceptPattern(pattern);

        assertTrue(renderer.isRenderable("/my/template.html", null));
        assertTrue(renderer.isRenderable("/my/template.any", null));
        assertFalse(renderer.isRenderable("my/template.html", null));
        assertFalse(renderer.isRenderable(null, null));
    }
}
