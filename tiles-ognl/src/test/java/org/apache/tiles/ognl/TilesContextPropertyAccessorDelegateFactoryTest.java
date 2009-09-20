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

package org.apache.tiles.ognl;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import ognl.PropertyAccessor;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.ognl.PropertyAccessorDelegateFactory;
import org.apache.tiles.ognl.TilesContextPropertyAccessorDelegateFactory;
import org.junit.Test;

/**
 * @author antonio
 *
 * @version $Rev$ $Date$
 */
public class TilesContextPropertyAccessorDelegateFactoryTest {

    /**
     * Test method for
     * {@link TilesContextPropertyAccessorDelegateFactory#getPropertyAccessor(String, TilesRequestContext)}
     * .
     */
    @Test
    public void testGetPropertyAccessorRequest() {
        PropertyAccessor objectPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationContextPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor requestScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor sessionScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationScopePropertyAccessor = createMock(PropertyAccessor.class);
        TilesRequestContext request = createMock(TilesRequestContext.class);

        replay(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request);
        PropertyAccessorDelegateFactory<TilesRequestContext> factory = new TilesContextPropertyAccessorDelegateFactory(
                objectPropertyAccessor, applicationContextPropertyAccessor,
                requestScopePropertyAccessor, sessionScopePropertyAccessor,
                applicationScopePropertyAccessor);
        assertEquals(objectPropertyAccessor, factory.getPropertyAccessor("writer", request));

        verify(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request);
    }

    /**
     * Test method for
     * {@link TilesContextPropertyAccessorDelegateFactory#getPropertyAccessor(String, TilesRequestContext)}
     * .
     */
    @Test
    public void testGetPropertyAccessorApplication() {
        PropertyAccessor objectPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationContextPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor requestScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor sessionScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationScopePropertyAccessor = createMock(PropertyAccessor.class);
        TilesRequestContext request = createMock(TilesRequestContext.class);

        replay(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request);
        PropertyAccessorDelegateFactory<TilesRequestContext> factory = new TilesContextPropertyAccessorDelegateFactory(
                objectPropertyAccessor, applicationContextPropertyAccessor,
                requestScopePropertyAccessor, sessionScopePropertyAccessor,
                applicationScopePropertyAccessor);
        assertEquals(applicationContextPropertyAccessor, factory.getPropertyAccessor("initParams", request));

        verify(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request);
    }

    /**
     * Test method for
     * {@link TilesContextPropertyAccessorDelegateFactory#getPropertyAccessor(String, TilesRequestContext)}
     * .
     */
    @Test
    public void testGetPropertyAccessorRequestScope() {
        PropertyAccessor objectPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationContextPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor requestScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor sessionScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationScopePropertyAccessor = createMock(PropertyAccessor.class);
        TilesRequestContext request = createMock(TilesRequestContext.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("attribute", 1);
        expect(request.getRequestScope()).andReturn(map);

        replay(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request);
        PropertyAccessorDelegateFactory<TilesRequestContext> factory = new TilesContextPropertyAccessorDelegateFactory(
                objectPropertyAccessor, applicationContextPropertyAccessor,
                requestScopePropertyAccessor, sessionScopePropertyAccessor,
                applicationScopePropertyAccessor);
        assertEquals(requestScopePropertyAccessor, factory.getPropertyAccessor("attribute", request));

        verify(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request);
    }

    /**
     * Test method for
     * {@link TilesContextPropertyAccessorDelegateFactory#getPropertyAccessor(String, TilesRequestContext)}
     * .
     */
    @Test
    public void testGetPropertyAccessorSessionScope() {
        PropertyAccessor objectPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationContextPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor requestScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor sessionScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationScopePropertyAccessor = createMock(PropertyAccessor.class);
        TilesRequestContext request = createMock(TilesRequestContext.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("attribute", 1);
        Map<String, Object> emptyMap = new HashMap<String, Object>();
        expect(request.getRequestScope()).andReturn(emptyMap);
        expect(request.getSessionScope()).andReturn(map);

        replay(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request);
        PropertyAccessorDelegateFactory<TilesRequestContext> factory = new TilesContextPropertyAccessorDelegateFactory(
                objectPropertyAccessor, applicationContextPropertyAccessor,
                requestScopePropertyAccessor, sessionScopePropertyAccessor,
                applicationScopePropertyAccessor);
        assertEquals(sessionScopePropertyAccessor, factory.getPropertyAccessor("attribute", request));

        verify(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request);
    }

    /**
     * Test method for
     * {@link TilesContextPropertyAccessorDelegateFactory#getPropertyAccessor(String, TilesRequestContext)}
     * .
     */
    @Test
    public void testGetPropertyAccessorApplicationScope() {
        PropertyAccessor objectPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationContextPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor requestScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor sessionScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationScopePropertyAccessor = createMock(PropertyAccessor.class);
        TilesRequestContext request = createMock(TilesRequestContext.class);
        TilesApplicationContext applicationContext = createMock(TilesApplicationContext.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("attribute", 1);
        Map<String, Object> emptyMap = new HashMap<String, Object>();
        expect(request.getRequestScope()).andReturn(emptyMap);
        expect(request.getSessionScope()).andReturn(emptyMap);
        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getApplicationScope()).andReturn(map);

        replay(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request, applicationContext);
        PropertyAccessorDelegateFactory<TilesRequestContext> factory = new TilesContextPropertyAccessorDelegateFactory(
                objectPropertyAccessor, applicationContextPropertyAccessor,
                requestScopePropertyAccessor, sessionScopePropertyAccessor,
                applicationScopePropertyAccessor);
        assertEquals(applicationScopePropertyAccessor, factory.getPropertyAccessor("attribute", request));

        verify(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request, applicationContext);
    }

    /**
     * Test method for
     * {@link TilesContextPropertyAccessorDelegateFactory#getPropertyAccessor(String, TilesRequestContext)}
     * .
     */
    @Test
    public void testGetPropertyAccessorRequestScopeDefault() {
        PropertyAccessor objectPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationContextPropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor requestScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor sessionScopePropertyAccessor = createMock(PropertyAccessor.class);
        PropertyAccessor applicationScopePropertyAccessor = createMock(PropertyAccessor.class);
        TilesRequestContext request = createMock(TilesRequestContext.class);
        TilesApplicationContext applicationContext = createMock(TilesApplicationContext.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("anotherAttribute", 1);
        Map<String, Object> emptyMap = new HashMap<String, Object>();
        expect(request.getRequestScope()).andReturn(map);
        expect(request.getSessionScope()).andReturn(emptyMap);
        expect(request.getApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getApplicationScope()).andReturn(emptyMap);

        replay(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request, applicationContext);
        PropertyAccessorDelegateFactory<TilesRequestContext> factory = new TilesContextPropertyAccessorDelegateFactory(
                objectPropertyAccessor, applicationContextPropertyAccessor,
                requestScopePropertyAccessor, sessionScopePropertyAccessor,
                applicationScopePropertyAccessor);
        assertEquals(requestScopePropertyAccessor, factory.getPropertyAccessor("attribute", request));

        verify(objectPropertyAccessor, applicationContextPropertyAccessor, requestScopePropertyAccessor,
                sessionScopePropertyAccessor, applicationScopePropertyAccessor, request, applicationContext);
    }
}
