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
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
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
    private Request request;

    /**
     * The Tiles application context.
     */
    private ApplicationContext applicationContext;

    /**
     * The object to test.
     */
    private TilesContextVariableResolverFactory factory;

    /**
     * Sets up the object.
     */
    @Before
    public void setUp() {
        request = createMock(Request.class);
        TilesRequestContextHolder holder = new TilesRequestContextHolder();
        holder.setTilesRequestContext(request);
        applicationContext = createMock(ApplicationContext.class);
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
     * Test method for {@link TilesContextVariableResolverFactory#isTarget(String)}.
     */
    @Test
    public void testIsTarget() {
        replay(request, applicationContext);
        assertTrue(factory.isTarget("header"));
        assertFalse(factory.isTarget("requestScope"));
        assertTrue(factory.isTarget("applicationScope"));
        assertFalse(factory.isTarget("blah"));
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#createVariableResolver(String)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCreateVariableResolver() {
        Map<String, String> header = createMock(Map.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);

        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(request.getHeader()).andReturn(header);

        replay(request, applicationContext, header);

        VariableResolver resolver = factory.createVariableResolver("header");
        assertEquals(Map.class, resolver.getType());
        assertEquals(header, resolver.getValue());
        resolver = factory.createVariableResolver("applicationScope");
        assertEquals(applicationScope, resolver.getValue());
        assertEquals(Map.class, resolver.getType());
        verify(header);
    }
}
