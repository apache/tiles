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

package org.apache.tiles.mvel;

import java.util.Arrays;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.integration.VariableResolver;

/**
 * Tests {@link TilesContextBeanVariableResolverFactory}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class TilesContextBeanVariableResolverFactoryTest {

    /**
     * The expected session scope calls.
     */
    private static final int EXPECTED_SESSION_CALLS = 3;

    /**
     * The expected request scope calls.
     */
    private static final int EXPECTED_REQUEST_CALLS = 4;

    /**
     * The Tiles request.
     */
    private Request request;

    /**
     * The Tiles application context.
     */
    private ApplicationContext applicationContext;

    /**
     * The object to test.
     */
    private TilesContextBeanVariableResolverFactory factory;

    /**
     * Sets up the object.
     */
    @Before
    public void setUp() {
        request = createMock(Request.class);
        TilesRequestContextHolder holder = new TilesRequestContextHolder();
        holder.setTilesRequestContext(request);
        applicationContext = createMock(ApplicationContext.class);
        factory = new TilesContextBeanVariableResolverFactory(holder);
    }

    /**
     * Test method for {@link TilesContextBeanVariableResolverFactory#createVariableResolver(String)}.
     */
    @Test
    public void testCreateVariableResolver() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        expect(request.getContext("request")).andReturn(requestScope).anyTimes();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        sessionScope.put("three", "three");
        expect(request.getContext("session")).andReturn(sessionScope).anyTimes();
        expect(request.getAvailableScopes()).andReturn(
                Arrays.asList(new String[] { "request", "session", "application" }))
                .anyTimes();
        expect(request.getContext("application")).andReturn(applicationScope).anyTimes();
        replay(request, applicationContext);

        VariableResolver resolver = factory.createVariableResolver("one");
        assertEquals(1, resolver.getValue());
        assertEquals(Integer.class, resolver.getType());
        resolver = factory.createVariableResolver("two");
        assertEquals(2, resolver.getValue());
        resolver = factory.createVariableResolver("three");
        assertEquals("three", resolver.getValue());
        resolver = factory.createVariableResolver("four");
        assertEquals(Object.class, resolver.getType());
        assertNull(resolver.getValue());
        verify(request, applicationContext);
    }

    /**
     * Test method for {@link TilesContextBeanVariableResolverFactory#isTarget(String)}.
     */
    @Test
    public void testIsTarget() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        expect(request.getContext("request")).andReturn(requestScope).times(
                EXPECTED_REQUEST_CALLS);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        sessionScope.put("three", "three");
        expect(request.getContext("session")).andReturn(sessionScope).times(
                EXPECTED_SESSION_CALLS);
        expect(request.getAvailableScopes()).andReturn(
                Arrays.asList(new String[] { "request", "session", "application" }))
                .anyTimes();
        expect(request.getContext("application")).andReturn(applicationScope).anyTimes();
        replay(request, applicationContext);

        assertTrue(factory.isTarget("one"));
        assertTrue(factory.isTarget("two"));
        assertTrue(factory.isTarget("three"));
        assertFalse(factory.isTarget("four"));
        verify(request, applicationContext);
    }
}
