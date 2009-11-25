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

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.mvel.TilesContextBeanVariableResolverFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.UnresolveablePropertyException;
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
     * Test method for {@link TilesContextBeanVariableResolverFactory#createVariable(String, Object)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariableStringObject() {
        replay(request, applicationContext);
        factory.createVariable("myName", "myValue");
        verify(request, applicationContext);
    }

    /**
     * Test method for {@link TilesContextBeanVariableResolverFactory#createVariable(String, Object, Class)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariableStringObjectClassOfQ() {
        replay(request, applicationContext);
        factory.createVariable("myName", "myValue", String.class);
        verify(request, applicationContext);
    }

    /**
     * Test method for {@link TilesContextBeanVariableResolverFactory#isResolveable(String)}.
     */
    @Test
    public void testIsResolveable() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        expect(request.getContext("request")).andReturn(requestScope).times(
                EXPECTED_REQUEST_CALLS);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);
        expect(request.getApplicationContext()).andReturn(applicationContext)
                .times(2);
        expect(applicationContext.getApplicationScope()).andReturn(
                applicationScope).times(2);
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        sessionScope.put("three", "three");
        expect(request.getContext("session")).andReturn(sessionScope).times(
                EXPECTED_SESSION_CALLS);
        replay(request, applicationContext);

        assertTrue(factory.isResolveable("one"));
        assertTrue(factory.isResolveable("two"));
        assertTrue(factory.isResolveable("three"));
        assertFalse(factory.isResolveable("four"));
        verify(request, applicationContext);
    }

    /**
     * Test method for {@link TilesContextBeanVariableResolverFactory#getVariableResolver(String)}.
     */
    @Test
    public void testGetVariableResolverString() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        expect(request.getContext("request")).andReturn(requestScope).anyTimes();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);
        expect(request.getApplicationContext()).andReturn(applicationContext)
                .anyTimes();
        expect(applicationContext.getApplicationScope()).andReturn(
                applicationScope).anyTimes();
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        sessionScope.put("three", "three");
        expect(request.getContext("session")).andReturn(sessionScope).anyTimes();
        replay(request, applicationContext);

        VariableResolver resolver = factory.getVariableResolver("one");
        assertEquals(1, resolver.getValue());
        resolver = factory.getVariableResolver("two");
        assertEquals(2, resolver.getValue());
        resolver = factory.getVariableResolver("three");
        assertEquals("three", resolver.getValue());
        verify(request, applicationContext);
    }

    /**
     * Test method for {@link TilesContextBeanVariableResolverFactory#getVariableResolver(String)}.
     */
    @Test(expected = UnresolveablePropertyException.class)
    public void testGetVariableResolverStringException() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        expect(request.getContext("request")).andReturn(requestScope).anyTimes();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);
        expect(request.getApplicationContext()).andReturn(applicationContext)
                .anyTimes();
        expect(applicationContext.getApplicationScope()).andReturn(
                applicationScope).anyTimes();
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        sessionScope.put("three", "three");
        expect(request.getContext("session")).andReturn(sessionScope).anyTimes();
        replay(request, applicationContext);

        factory.getVariableResolver("four");
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
        expect(request.getApplicationContext()).andReturn(applicationContext)
                .times(2);
        expect(applicationContext.getApplicationScope()).andReturn(
                applicationScope).times(2);
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        sessionScope.put("three", "three");
        expect(request.getContext("session")).andReturn(sessionScope).times(
                EXPECTED_SESSION_CALLS);
        replay(request, applicationContext);

        assertTrue(factory.isTarget("one"));
        assertTrue(factory.isTarget("two"));
        assertTrue(factory.isTarget("three"));
        assertFalse(factory.isTarget("four"));
        verify(request, applicationContext);
    }
}
