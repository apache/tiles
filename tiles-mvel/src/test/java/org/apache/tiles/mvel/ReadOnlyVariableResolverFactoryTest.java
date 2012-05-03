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

import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.UnresolveablePropertyException;
import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.VariableResolverFactory;


/**
 * Tests {@link ReadOnlyVariableResolverFactory}.
 *
 * @version $Rev$ $Date$
 */
public class ReadOnlyVariableResolverFactoryTest {

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
    private ReadOnlyVariableResolverFactory factory;

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
        factory = createMockBuilder(ReadOnlyVariableResolverFactory.class).withConstructor(holder).createMock();
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#createVariable(String, Object)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariableStringObject() {
        replay(factory, request, applicationContext);
        try {
            factory.createVariable("myName", "myValue");
        } finally {
            verify(factory, request, applicationContext);
        }
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#createVariable(String, Object)}.
     */
    @Test
    public void testCreateVariableStringObjectNextFactory() {
        VariableResolverFactory nextFactory = createMock(VariableResolverFactory.class);
        VariableResolver resolver = createMock(VariableResolver.class);

        expect(nextFactory.createVariable("myName", "myValue")).andReturn(resolver);

        replay(factory, request, applicationContext, nextFactory, resolver);
        factory.setNextFactory(nextFactory);
        assertEquals(resolver, factory.createVariable("myName", "myValue"));
        verify(factory, request, applicationContext, nextFactory, resolver);
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#createVariable(String, Object, Class)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariableStringObjectClassOfQ() {
        replay(factory, request, applicationContext);
        try {
            factory.createVariable("myName", "myValue", String.class);
        } finally {
            verify(factory, request, applicationContext);
        }
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#createVariable(String, Object, Class)}.
     */
    @Test
    public void testCreateVariableStringObjectClassNextFactory() {
        VariableResolverFactory nextFactory = createMock(VariableResolverFactory.class);
        VariableResolver resolver = createMock(VariableResolver.class);

        expect(nextFactory.createVariable("myName", "myValue", String.class)).andReturn(resolver);

        replay(factory, request, applicationContext, nextFactory, resolver);
        factory.setNextFactory(nextFactory);
        assertEquals(resolver, factory.createVariable("myName", "myValue", String.class));
        verify(factory, request, applicationContext, nextFactory, resolver);
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#isResolveable(String)}.
     */
    @Test
    public void testIsResolveable() {
        expect(factory.isTarget("whatever")).andReturn(true);

        replay(factory, request, applicationContext);
        assertTrue(factory.isResolveable("whatever"));
        verify(factory, request, applicationContext);
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#getVariableResolver(String)}.
     */
    @Test
    public void testGetVariableResolver() {
        VariableResolverFactory nextFactory = createMock(VariableResolverFactory.class);
        VariableResolver nextResolver = createMock(VariableResolver.class);
        VariableResolver requestResolver = createMock(VariableResolver.class);
        VariableResolver applicationResolver = createMock(VariableResolver.class);
        expect(nextFactory.getVariableResolver("other")).andReturn(nextResolver);
        expect(nextFactory.isResolveable("other")).andReturn(true);
        expect(factory.isTarget("requestScope")).andReturn(true).anyTimes();
        expect(factory.isTarget("applicationScope")).andReturn(true).anyTimes();
        expect(factory.isTarget("other")).andReturn(false).anyTimes();
        expect(factory.createVariableResolver("requestScope")).andReturn(requestResolver);
        expect(factory.createVariableResolver("applicationScope")).andReturn(applicationResolver);

        replay(factory, request, applicationContext, nextFactory, nextResolver, requestResolver, applicationResolver);
        factory.setNextFactory(nextFactory);
        VariableResolver resolver = factory.getVariableResolver("requestScope");
        assertEquals(requestResolver, resolver);
        resolver = factory.getVariableResolver("requestScope"); // again to test caching
        assertEquals(requestResolver, resolver);
        resolver = factory.getVariableResolver("applicationScope");
        assertEquals(applicationResolver, resolver);
        resolver = factory.getVariableResolver("other");
        assertEquals(nextResolver, resolver);
        verify(factory, request, applicationContext, nextFactory, nextResolver, requestResolver, applicationResolver);
    }

    /**
     * Test method for {@link ScopeVariableResolverFactory#getVariableResolver(String)}.
     */
    @Test(expected = UnresolveablePropertyException.class)
    public void testGetVariableResolverNotResolvable() {
        VariableResolverFactory nextFactory = createMock(VariableResolverFactory.class);

        expect(factory.isTarget("other")).andReturn(false).anyTimes();
        expect(nextFactory.isResolveable("other")).andReturn(false);

        replay(factory, request, applicationContext, nextFactory);
        try {
            factory.setNextFactory(nextFactory);
            factory.getVariableResolver("other");
        } finally {
            verify(factory, request, applicationContext, nextFactory);
        }
    }
}
