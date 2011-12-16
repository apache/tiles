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

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.attribute.Addable;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AddOnlyMap}.
 *
 * @version $Rev$ $Date$
 */
public class AddOnlyMapTest {

    /**
     * The object to test.
     */
    private AddOnlyMap<Integer> map;

    /**
     * The extractor to use.
     */
    private Addable<Integer> extractor;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        extractor = createMock(Addable.class);
        map = new AddOnlyMap<Integer>(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.AddableParameterMap#entrySet()}.
     */
    @Test
    public void testEntrySet() {
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        MapEntry<String, Integer> entry1 = new MapEntry<String, Integer>("one", 13, false);
        MapEntry<String, Integer> entry2 = new MapEntry<String, Integer>("two", 42, false);
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(2);
        entries.add(entry1);
        entries.add(entry2);

        extractor.setValue("one", 13);
        expectLastCall().times(2);
        extractor.setValue("two", 42);
        replay(extractor);
        entrySet.add(entry1);
        entrySet.addAll(entries);
        verify(extractor);
    }

    /**
     * Test method for {@link AddableParameterMap#put(String, String)}.
     */
    @Test
    public void testPut() {
        extractor.setValue("one", 42);

        replay(extractor);
        assertNull(map.put("one", 42));
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.AddableParameterMap#putAll(java.util.Map)}.
     */
    @Test
    public void testPutAll() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("one", 13);
        map.put("two", 42);

        extractor.setValue("one", 13);
        extractor.setValue("two", 42);

        replay(extractor);
        this.map.putAll(map);
        verify(extractor);
    }
}
