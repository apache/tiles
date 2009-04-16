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

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.junit.Test;

/**
 * Tests {@link AbstractDefaultToStringRenderable}.
 */
public class AbstractDefaultToStringRenderableTest {

    /**
     * Test method for {@link org.apache.tiles.velocity.template.AbstractDefaultToStringRenderable
     * #AbstractDefaultToStringRenderable(org.apache.velocity.context.Context,
     * java.util.Map, javax.servlet.http.HttpServletResponse, javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    public void testAbstractDefaultToStringRenderable() {
        Context velociContext = createMock(Context.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Map<String, Object> params = new HashMap<String, Object>();

        replay(velociContext, request, response);
        DefaultRenderable renderable = new DefaultRenderable(velociContext,
                params, response, request);
        assertEquals(velociContext, renderable.getVelocityContext());
        assertEquals(request, renderable.getRequest());
        assertEquals(response, renderable.getResponse());
        assertEquals(params, renderable.getParams());
        verify(velociContext, request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.AbstractDefaultToStringRenderable#toString()}.
     */
    @Test
    public void testToString() {
        Context velociContext = createMock(Context.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("one", "value1");

        replay(velociContext, request, response);
        DefaultRenderable renderable = new DefaultRenderable(velociContext,
                params, response, request);

        assertEquals("Hello!", renderable.toString());
        assertTrue(renderable.getWriter() instanceof StringWriter);
        assertNull(renderable.getInternalContextAdapter());
        verify(velociContext, request, response);
    }

    /**
     * Mock class for AbstractDefaultToStringRenderable.
     */
    private static class DefaultRenderable extends AbstractDefaultToStringRenderable {

        /**
         * The internal context.
         */
        private InternalContextAdapter internalContextAdapter;

        /**
         * The writer.
         */
        private Writer writer;

        /**
         * Constructor.
         *
         * @param velocityContext The Velocity context.
         * @param params The parameters used in the current tool call.
         * @param response The HTTP response.
         * @param request The HTTP request.
         */
        public DefaultRenderable(Context velocityContext,
                Map<String, Object> params, HttpServletResponse response,
                HttpServletRequest request) {
            super(velocityContext, params, response, request);
        }

        /** {@inheritDoc} */
        public boolean render(InternalContextAdapter context, Writer writer)
                throws IOException {
            this.internalContextAdapter = context;
            this.writer = writer;
            writer.write("Hello!");
            return true;
        }

        /**
         * Returns the Velocity context.
         *
         * @return The velocity context.
         */
        public Context getVelocityContext() {
            return velocityContext;
        }

        /**
         * Returns the parameters.
         *
         * @return The parameters.
         */
        public Map<String, Object> getParams() {
            return params;
        }

        /**
         * Returns the request.
         *
         * @return The request.
         */
        public HttpServletRequest getRequest() {
            return request;
        }

        /**
         * Returns the respnse.
         *
         * @return The response.
         */
        public HttpServletResponse getResponse() {
            return response;
        }

        /**
         * Returns the internal context.
         *
         * @return The internal context.
         */
        public InternalContextAdapter getInternalContextAdapter() {
            return internalContextAdapter;
        }

        /**
         * Returns the writer.
         *
         * @return The writer.
         */
        public Writer getWriter() {
            return writer;
        }
    }
}
