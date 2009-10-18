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

package org.apache.tiles.startup;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.util.Map;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AbstractTilesInitializer}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractTilesInitializerTest {

    /**
     * A mock Tiles container factory.
     */
    private AbstractTilesContainerFactory containerFactory;

    /**
     * The object to test.
     */
    private AbstractTilesInitializer initializer;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        containerFactory = createMock(AbstractTilesContainerFactory.class);
        initializer = new AbstractTilesInitializer() {

            @Override
            protected AbstractTilesContainerFactory createContainerFactory(
                    TilesApplicationContext context) {
                return containerFactory;
            }
        };
    }

    /**
     * Test method for {@link AbstractTilesInitializer#initialize(TilesApplicationContext)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInitialize() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> scope = createMock(Map.class);

        expect(containerFactory.createContainer(context)).andReturn(container);
        expect(context.getApplicationScope()).andReturn(scope).times(2);
        expect(scope.put(TilesAccess.CONTAINER_ATTRIBUTE, container)).andReturn(null);
        expect(scope.remove(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container);

        replay(containerFactory, context, container, scope);
        initializer.initialize(context);
        initializer.destroy();
        verify(containerFactory, context, container, scope);
    }

    /**
     * Test method for {@link AbstractTilesInitializer#createTilesApplicationContext(TilesApplicationContext)}.
     */
    @Test
    public void testCreateTilesApplicationContext() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);
        replay(containerFactory, context);
        assertEquals(context, initializer.createTilesApplicationContext(context));
        verify(containerFactory, context);
    }

    /**
     * Test method for {@link AbstractTilesInitializer#getContainerKey(TilesApplicationContext)}.
     */
    @Test
    public void testGetContainerKey() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);
        replay(containerFactory, context);
        assertNull(initializer.getContainerKey(context));
        verify(containerFactory, context);
    }

    /**
     * Test method for {@link AbstractTilesInitializer#createContainer(TilesApplicationContext)}.
     */
    @Test
    public void testCreateContainer() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);

        expect(containerFactory.createContainer(context)).andReturn(container);

        replay(containerFactory, context, container);
        assertEquals(container, initializer.createContainer(context));
        verify(containerFactory, context, container);
    }
}
