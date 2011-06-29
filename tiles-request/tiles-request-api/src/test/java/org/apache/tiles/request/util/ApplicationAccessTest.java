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
package org.apache.tiles.request.util;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Map;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.scope.ContextResolver;
import org.junit.Test;

/**
 * Tests {@link ApplicationAccess}.
 *
 * @version $Rev$ $Date$
 */
public class ApplicationAccessTest {

    /**
     * Test method for {@link ApplicationAccess#register(ApplicationContext)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRegister() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = createMock(Map.class);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(applicationScope.put(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE, applicationContext)).andReturn(null);

        replay(applicationContext, applicationScope);
        ApplicationAccess.register(applicationContext);
        verify(applicationContext, applicationScope);
    }

    /**
     * Test method for {@link ApplicationAccess#registerContextResolver(ContextResolver, ApplicationContext)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRegisterContextResolver() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = createMock(Map.class);
        ContextResolver contextResolver = createMock(ContextResolver.class);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(applicationScope.put(ApplicationAccess
                .CONTEXT_RESOLVER_ATTRIBUTE, contextResolver)).andReturn(null);

        replay(applicationContext, applicationScope, contextResolver);
        ApplicationAccess.registerContextResolver(contextResolver, applicationContext);
        verify(applicationContext, applicationScope, contextResolver);
    }

    /**
     * Test method for {@link ApplicationAccess#getContextResolver(ApplicationContext)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetContextResolver() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = createMock(Map.class);
        ContextResolver contextResolver = createMock(ContextResolver.class);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(applicationScope.get(ApplicationAccess
                .CONTEXT_RESOLVER_ATTRIBUTE)).andReturn(contextResolver);

        replay(applicationContext, applicationScope, contextResolver);
        assertEquals(contextResolver, ApplicationAccess.getContextResolver(applicationContext));
        verify(applicationContext, applicationScope, contextResolver);
    }

}
