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
package org.apache.tiles.extras.renderer;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.tiles.Attribute;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.locale.PostfixedApplicationResource;
import org.apache.tiles.request.render.StringRenderer;
import org.junit.Test;

/**
 * Tests {@link OptionsRenderer}.
 *
 * @version $Rev$ $Date$
 */
public final class OptionsRendererTest {

    private final BasicTilesContainer container = new BasicTilesContainer();

    private final Map<String, Object> appScope = new HashMap<String, Object>(){{
        put(TilesAccess.CONTAINER_ATTRIBUTE, container);
    }};

    private final Map<String, Object> requestScope = new HashMap<String, Object>();

    private final ApplicationResource template = new PostfixedApplicationResource("Result"){
        @Override
        public InputStream getInputStream() throws IOException {
            throw new AssertionError("Shouldn't be called.");
        }
        @Override
        public long getLastModified() throws IOException {
            return 0;
        }
    };

    private final ApplicationContext context = new ApplicationContext(){
        @Override
        public Object getContext() {
            throw new AssertionError("Shouldn't be called.");
        }
        @Override
        public Map<String, Object> getApplicationScope() {
            return appScope;
        }
        @Override
        public Map<String, String> getInitParams() {
            throw new AssertionError("Shouldn't be called.");
        }
        @Override
        public ApplicationResource getResource(String string) {
            return template;
        }
        @Override
        public ApplicationResource getResource(ApplicationResource ar, Locale locale) {
            throw new AssertionError("Shouldn't be called.");
        }
        @Override
        public Collection<ApplicationResource> getResources(String string) {
            throw new AssertionError("Shouldn't be called.");
        }
    };

    /**
     * Tests
     * {@link OptionsRenderer#render(String, Request)}.
     *
     * @throws IOException If something goes wrong during rendition.
     */
    @Test
    public void testWrite() throws IOException {
        StringWriter writer = new StringWriter();
        Request request = createMock(Request.class);

        requestScope.put(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);

        expect(request.getContext(matches("request"))).andReturn(requestScope).anyTimes();
        expect(request.getApplicationContext()).andReturn(context);
        expect(request.getWriter()).andReturn(writer);
        replay(request);

        container
                .getAttributeContext(request)
                .putAttribute("test-fallback", new ListAttribute(){{
                    add(new Attribute("Result"));
                }});

        OptionsRenderer renderer = new OptionsRenderer(context, new StringRenderer());
        renderer.render("{options[test-fallback]}", request);
        writer.close();
        assertEquals("Not written 'Result'", "Result", writer.toString());
        verify(request);
    }

    /**
     * Tests
     * {@link OptionsRenderer#isRenderable(String, Request)}.
     */
    @Test
    public void testIsRenderable() {
        Request requestContext = createMock(Request.class);
        OptionsRenderer renderer = new OptionsRenderer(context, new StringRenderer());
        assertTrue(renderer.isRenderable("any-string", requestContext));
    }
}
