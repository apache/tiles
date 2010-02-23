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

package org.apache.tiles.autotag.core.runtime.composition;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.tiles.autotag.core.runtime.composition.ComposeStackUtil;
import org.junit.Test;

/**
 * Tests {@link ComposeStackUtil}.
 *
 * @version $Rev$ $Date$
 */
public class ComposeStackUtilTest {

    /**
     * An integer value.
     */
    private static final int INT_VALUE = 3;

    /**
     * A long value.
     */
    private static final long LONG_VALUE = 2L;

    /**
     * Test method for {@link org.apache.tiles.autotag.core.runtime.composition.ComposeStackUtil
     * #findAncestorWithClass(java.util.Stack, java.lang.Class)}.
     */
    @Test
    public void testFindAncestorWithClass() {
        Deque<Object> composeStack = new ArrayDeque<Object>();
        Integer integerValue = new Integer(1);
        Long longValue = new Long(LONG_VALUE);
        String stringValue = "my value";
        Integer integerValue2 = new Integer(INT_VALUE);
        composeStack.push(integerValue);
        composeStack.push(longValue);
        composeStack.push(stringValue);
        composeStack.push(integerValue2);
        assertEquals(integerValue2, ComposeStackUtil.findAncestorWithClass(composeStack, Integer.class));
        assertEquals(longValue, ComposeStackUtil.findAncestorWithClass(composeStack, Long.class));
        assertEquals(stringValue, ComposeStackUtil.findAncestorWithClass(composeStack, String.class));
        assertEquals(integerValue2, ComposeStackUtil.findAncestorWithClass(composeStack, Object.class));
    }

}
