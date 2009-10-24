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

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class TilesAccessTest extends TestCase {

    /**
     * The servlet context to use.
     */
    private TilesApplicationContext context;

    /** {@inheritDoc} */
    @Override
	public void setUp() {
        context = EasyMock.createMock(TilesApplicationContext.class);
    }

    /**
     * Tests the setting of the context.
     */
    public void testSetContainer() {
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        Map<String, Object> attribs = new HashMap<String, Object>();
        EasyMock.expect(context.getApplicationScope()).andReturn(attribs);
        EasyMock.replay(context);
        TilesAccess.setContainer(context, container);
        assertEquals(attribs.size(), 1);
        assertEquals(attribs.get(TilesAccess.CONTAINER_ATTRIBUTE), container);
        EasyMock.verify(context);
    }

    /**
     * Tests the setting of the context.
     */
    public void testSetContainerWithKey() {
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        Map<String, Object> attribs = new HashMap<String, Object>();
        EasyMock.expect(context.getApplicationScope()).andReturn(attribs);
        EasyMock.replay(context);
        TilesAccess.setContainer(context, container, "myKey");
        assertEquals(attribs.size(), 1);
        assertEquals(attribs.get("myKey"), container);
        EasyMock.verify(context);
    }
}
