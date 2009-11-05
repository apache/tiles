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
package org.apache.tiles.access;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;

/**
 * Tests {@link TilesAccess}.
 *
 * @version $Rev$ $Date$
 */
public class TilesAccessTest {

    /**
     * Tests {@link TilesAccess#setContainer(TilesApplicationContext, TilesContainer)}.
     */
    @Test
    public void testSetContainer() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> attribs = new HashMap<String, Object>();
        expect(context.getApplicationScope()).andReturn(attribs);
        replay(context, container);
        TilesAccess.setContainer(context, container);
        assertEquals(attribs.size(), 1);
        assertEquals(attribs.get(TilesAccess.CONTAINER_ATTRIBUTE), container);
        verify(context, container);
    }

    /**
     * Tests {@link TilesAccess#setContainer(TilesApplicationContext, TilesContainer, String)}.
     */
    @Test
    public void testSetContainerWithKey() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> attribs = new HashMap<String, Object>();
        expect(context.getApplicationScope()).andReturn(attribs).anyTimes();
        replay(context, container);
        TilesAccess.setContainer(context, container, "myKey");
        assertEquals(1, attribs.size());
        assertEquals(container, attribs.get("myKey"));

        TilesAccess.setContainer(context, null, "myKey");
        assertEquals(0, attribs.size());

        TilesAccess.setContainer(context, container, null);
        assertEquals(1, attribs.size());
        assertEquals(container, attribs.get(TilesAccess.CONTAINER_ATTRIBUTE));
        verify(context, container);
    }

    /**
     * Tests {@link TilesAccess#getContainer(TilesApplicationContext)}.
     */
    @Test
    public void testGetContainer() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> attribs = new HashMap<String, Object>();
        expect(context.getApplicationScope()).andReturn(attribs).anyTimes();

        replay(context, container);
        attribs.put(TilesAccess.CONTAINER_ATTRIBUTE, container);
        assertEquals(container, TilesAccess.getContainer(context));
        verify(context, container);
    }

    /**
     * Tests {@link TilesAccess#getContainer(TilesApplicationContext, String))}.
     */
    @Test
    public void testGetContainerWithKey() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> attribs = new HashMap<String, Object>();
        expect(context.getApplicationScope()).andReturn(attribs).anyTimes();

        replay(context, container);
        attribs.put(TilesAccess.CONTAINER_ATTRIBUTE, container);
        attribs.put("myKey", container);
        assertEquals(container, TilesAccess.getContainer(context, null));
        assertEquals(container, TilesAccess.getContainer(context, "myKey"));
        verify(context, container);
    }
}
