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
import static org.easymock.classextension.EasyMock.*;
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
 * Tests {@link ScopeVariableResolverFactory}.
 *
 * @version $Rev$ $Date$
 */
public class ScopeVariableResolverFactoryTest {

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
    private ScopeVariableResolverFactory factory;

    /**
     * Sets up the object.
     */
    @Before
    public void setUp() {
        request = createMock(Request.class);
        expect(request.getAvailableScopes()).andReturn(
                Arrays.asList(new String[]{"request", "session", "application"})).anyTimes();
        TilesRequestContextHolder holder = new TilesRequestContextHolder();
        holder.setTilesRequestContext(request);
        applicationContext = createMock(ApplicationContext.class);
        factory = new ScopeVariableResolverFactory(holder);
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#isTarget(String)}.
     */
    @Test
    public void testIsTarget() {
        replay(request, applicationContext);
        assertFalse(factory.isTarget("header"));
        assertTrue(factory.isTarget("requestScope"));
        assertTrue(factory.isTarget("applicationScope"));
        assertFalse(factory.isTarget("blah"));
        verify(request, applicationContext);
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#createVariableResolver(String)}.
     */
    @Test
    public void testCreateVariableResolver() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        expect(request.getContext("request")).andReturn(requestScope).times(2);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);
        expect(request.getContext("application")).andReturn(applicationScope);

        replay(request, applicationContext);
        VariableResolver resolver = factory.createVariableResolver("requestScope");
        assertEquals(0, resolver.getFlags());
        assertEquals("requestScope", resolver.getName());
        assertEquals(Map.class, resolver.getType());
        assertEquals(requestScope, resolver.getValue());
        resolver.setStaticType(Object.class); // To complete coverage
        assertEquals(Map.class, resolver.getType());
        resolver = factory.createVariableResolver("requestScope"); // again to test caching
        assertEquals(requestScope, resolver.getValue());
        resolver = factory.createVariableResolver("applicationScope");
        assertEquals(applicationScope, resolver.getValue());
        verify(request, applicationContext);
    }
}
