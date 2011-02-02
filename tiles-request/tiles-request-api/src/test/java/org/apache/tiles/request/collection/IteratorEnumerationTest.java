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
package org.apache.tiles.request.collection;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link IteratorEnumeration}.
 *
 * @version $Rev$ $Date$
 */
public class IteratorEnumerationTest {

    /**
     * The iterator to use.
     */
    private Iterator<Integer> iterator;

    /**
     * The object to test.
     */
    private IteratorEnumeration<Integer> enumeration;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        iterator = createMock(Iterator.class);
        enumeration = new IteratorEnumeration<Integer>(iterator);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.IteratorEnumeration#hasMoreElements()}.
     */
    @Test
    public void testHasMoreElements() {
        expect(iterator.hasNext()).andReturn(true);

        replay(iterator);
        assertTrue(enumeration.hasMoreElements());
        verify(iterator);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.IteratorEnumeration#nextElement()}.
     */
    @Test
    public void testNextElement() {
        expect(iterator.next()).andReturn(1);

        replay(iterator);
        assertEquals(new Integer(1), enumeration.nextElement());
        verify(iterator);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.IteratorEnumeration#getIterator()}.
     */
    @Test
    public void testGetIterator() {
        replay(iterator);
        assertEquals(iterator, enumeration.getIterator());
        verify(iterator);
    }

}
