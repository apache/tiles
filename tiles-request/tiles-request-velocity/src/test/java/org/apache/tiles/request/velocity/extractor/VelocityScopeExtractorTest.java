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
package org.apache.tiles.request.velocity.extractor;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VelocityScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class VelocityScopeExtractorTest {

    /**
     * The Velocity context.
     */
    private Context request;

    /**
     * The extractor to test.
     */
    private VelocityScopeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(Context.class);
        extractor = new VelocityScopeExtractor(request);
    }

    /**
     * Test method for {@link VelocityScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValue() {
        expect(request.remove("key")).andReturn("value");

        replay(request);
        extractor.removeValue("key");
        verify(request);
    }

    /**
     * Test method for {@link VelocityScopeExtractor#getKeys()}.
     */
    @Test
    public void testGetKeys() {
        expect(request.getKeys()).andReturn(new Object[] {"one", "two"});

        replay(request);
        Enumeration<String> keys = extractor.getKeys();
        assertTrue(keys.hasMoreElements());
        assertEquals("one", keys.nextElement());
        assertTrue(keys.hasMoreElements());
        assertEquals("two", keys.nextElement());
        assertFalse(keys.hasMoreElements());
        verify(request);
    }

    /**
     * Test method for {@link VelocityScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(request.get("key")).andReturn("value");

        replay(request);
        assertEquals("value", extractor.getValue("key"));
        verify(request);
    }

    /**
     * Test method for {@link VelocityScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        expect(request.put("key", "value")).andReturn(null);

        replay(request);
        extractor.setValue("key", "value");
        verify(request);
    }
}
