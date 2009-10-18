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

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.mvel.TilesContextVariableResolverFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.integration.VariableResolver;

/**
 * Tests {@link TilesContextVariableResolverFactory}.
 *
 * @version $Rev$ $Date$
 */
public class TilesContextVariableResolverFactoryTest {

    /**
     * The Tiles request.
     */
    private TilesRequestContext request;

    /**
     * The Tiles application context.
     */
    private TilesApplicationContext applicationContext;

    /**
     * The object to test.
     */
    private TilesContextVariableResolverFactory factory;

    /**
     * Sets up the object.
     */
    @Before
    public void setUp() {
        request = createMock(TilesRequestContext.class);
        TilesRequestContextHolder holder = new TilesRequestContextHolder();
        holder.setTilesRequestContext(request);
        applicationContext = createMock(TilesApplicationContext.class);
        factory = new TilesContextVariableResolverFactory(holder);
    }

    /**
     * Tears down the object.
     */
    @After
    public void tearDown() {
        verify(request, applicationContext);
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#createVariable(String, Object)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariableStringObject() {
        replay(request, applicationContext);
        factory.createVariable("myName", "myValue");
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#createVariable(String, Object, Class)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariableStringObjectClassOfQ() {
        replay(request, applicationContext);
        factory.createVariable("myName", "myValue", String.class);
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#isResolveable(String)}.
     */
    @Test
    public void testIsResolveable() {
        replay(request, applicationContext);
        assertTrue(factory.isResolveable("header"));
        assertTrue(factory.isResolveable("requestScope"));
        assertTrue(factory.isResolveable("applicationScope"));
        assertFalse(factory.isResolveable("blah"));
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#isTarget(String)}.
     */
    @Test
    public void testIsTarget() {
        replay(request, applicationContext);
        assertTrue(factory.isTarget("header"));
        assertTrue(factory.isTarget("requestScope"));
        assertTrue(factory.isTarget("applicationScope"));
        assertFalse(factory.isTarget("blah"));
    }

    /**
     * Test method for {@link org.mvel2.integration.impl.BaseVariableResolverFactory#getVariableResolver(String)}.
     */
    @Test
    public void testGetVariableResolver() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        expect(request.getRequestScope()).andReturn(requestScope);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);
        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        replay(request, applicationContext);

        VariableResolver resolver = factory.getVariableResolver("requestScope");
        assertEquals(requestScope, resolver.getValue());
        resolver = factory.getVariableResolver("applicationScope");
        assertEquals(applicationScope, resolver.getValue());
    }
}
