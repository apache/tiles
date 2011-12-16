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
package org.apache.tiles.request.scope;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.util.DefaultRequestWrapper;
import org.apache.tiles.request.util.RequestWrapper;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ReflectionContextResolver}.
 *
 * @version $Rev$ $Date$
 */
public class ReflectionContextResolverTest {

    /**
     * The scopes.
     */
    private static final String [] SCOPES = new String[] {"one", "two", "three"};

    /**
     * The resolver to test.
     */
    private ReflectionContextResolver resolver;

    /**
     * One scope.
     */
    private Map<String, Object> oneScope;
    /**
     * Two scope.
     */
    private Map<String, Object> twoScope;
    /**
     * Three scope.
     */
    private Map<String, Object> threeScope;

    /**
     * The request.
     */
    private Request request;

    /**
     * Initializes the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        resolver = new ReflectionContextResolver();
        oneScope = createMock(Map.class);
        twoScope = createMock(Map.class);
        threeScope = createMock(Map.class);
        request = new SampleRequest(oneScope, twoScope, threeScope);
    }

    /**
     * Test method for {@link ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test
    public void testGetContext() {
        replay(oneScope, twoScope, threeScope);
        assertEquals(oneScope, resolver.getContext(request, "one"));
        assertEquals(twoScope, resolver.getContext(request, "two"));
        assertEquals(threeScope, resolver.getContext(request, "three"));
        verify(oneScope, twoScope, threeScope);
    }

    /**
     * Test method for {@link ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test
    public void testGetContextWrapped() {
        replay(oneScope, twoScope, threeScope);
        RequestWrapper wrapper = new DefaultRequestWrapper(request);
        assertEquals(oneScope, resolver.getContext(wrapper, "one"));
        assertEquals(twoScope, resolver.getContext(wrapper, "two"));
        assertEquals(threeScope, resolver.getContext(wrapper, "three"));
        verify(oneScope, twoScope, threeScope);
    }

    /**
     * Test method for {@link ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test(expected = NoSuchScopeException.class)
    public void testGetContextException() {
        resolver.getContext(request, "none");
    }

    /**
     * Test method for {@link ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test(expected = NoSuchScopeException.class)
    public void testGetContextException2() {
        resolver.getContext(request, "private");
    }

    /**
     * Test method for {@link ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test(expected = NoSuchScopeException.class)
    public void testGetContextException3() {
        resolver.getContext(request, "unavailable");
    }

    /**
     * Test method for {@link ReflectionContextResolver#getAvailableScopes(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetAvailableScopes() {
        replay(oneScope, twoScope, threeScope);
        assertArrayEquals(SCOPES, resolver.getAvailableScopes(request));
        verify(oneScope, twoScope, threeScope);
    }

    /**
     * Test method for {@link ReflectionContextResolver#getAvailableScopes(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetAvailableScopesWrapped() {
        replay(oneScope, twoScope, threeScope);
        RequestWrapper wrapper = new DefaultRequestWrapper(request);
        assertArrayEquals(SCOPES, resolver.getAvailableScopes(wrapper));
        verify(oneScope, twoScope, threeScope);
    }

    /**
     * A sample request.
     */
    public static class SampleRequest implements Request {

        /**
         * The scopes.
         */
        private static final String [] SCOPES = new String[] {"one", "two", "three"};

        /**
         * The scope maps.
         */
        private Map<String, Object> oneScope, twoScope, threeScope;

        /**
         * Constructor.
         *
         * @param oneScope Scope one.
         * @param twoScope Scope two.
         * @param threeScope Scope three.
         */
        public SampleRequest(Map<String, Object> oneScope,
                Map<String, Object> twoScope, Map<String, Object> threeScope) {
            this.oneScope = oneScope;
            this.twoScope = twoScope;
            this.threeScope = threeScope;
        }

        @Override
        public String[] getNativeScopes() {
            return SCOPES;
        }

        @Override
        public String[] getAvailableScopes() {
            return SCOPES;
        }

        /**
         * Returns one scope.
         *
         * @return One scope.
         */
        public Map<String, Object> getOneScope() {
            return oneScope;
        }

        /**
         * Returns two scope.
         *
         * @return Two scope.
         */
        public Map<String, Object> getTwoScope() {
            return twoScope;
        }

        /**
         * Returns three scope.
         *
         * @return Three scope.
         */
        public Map<String, Object> getThreeScope() {
            return threeScope;
        }

        /**
         * Returns a private scope.
         *
         * @return A private, unused, scope.
         */
        @SuppressWarnings("unused")
        private Map<String, Object> getPrivateScope() {
            return null;
        }

        /**
         * Returns an unavailable scope.
         *
         * @return The unavailable scope, raising exception.
         */
        public Map<String, Object> getUnavailableScope() {
            throw new UnsupportedOperationException("No way!");
        }

        @Override
        public ApplicationContext getApplicationContext() {
            return null;
        }

        @Override
        public Map<String, Object> getContext(String scope) {
            return null;
        }

        @Override
        public Map<String, String> getHeader() {
            return null;
        }

        @Override
        public Map<String, String[]> getHeaderValues() {
            return null;
        }

        @Override
        public OutputStream getOutputStream() {
            return null;
        }

        @Override
        public Map<String, String> getParam() {
            return null;
        }

        @Override
        public Map<String, String[]> getParamValues() {
            return null;
        }

        @Override
        public PrintWriter getPrintWriter() {
            return null;
        }

        @Override
        public Locale getRequestLocale() {
            return null;
        }

        @Override
        public Writer getWriter() {
            return null;
        }

        @Override
        public boolean isResponseCommitted() {
            return false;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }
    }
}
