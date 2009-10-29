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

package org.apache.tiles;

import static org.junit.Assert.*;

import java.util.EmptyStackException;

import org.junit.Test;

/**
 * Tests {@link ArrayStack}.
 *
 * @version $Rev$ $Date$
 */
public class ArrayStackTest {

    /**
     * The stack size.
     */
    private static final int STACK_SIZE = 3;

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#empty()}.
     */
    @Test
    public void testEmpty() {
        ArrayStack<Object> stack = new ArrayStack<Object>();
        assertTrue(stack.empty());
        stack.add(new Integer(1));
        assertFalse(stack.empty());
    }

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#peek()}.
     */
    @Test
    public void testPeek() {
        ArrayStack<Object> stack = new ArrayStack<Object>(1);
        assertTrue(stack.empty());
        stack.add(new Integer(1));
        assertEquals(1, stack.peek());
        assertFalse(stack.empty());
    }

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#peek()}.
     */
    @Test(expected = EmptyStackException.class)
    public void testPeekException() {
        ArrayStack<Object> stack = new ArrayStack<Object>(1);
        assertTrue(stack.empty());
        stack.peek();
    }

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#peek(int)}.
     */
    @Test
    public void testPeekInt() {
        ArrayStack<Object> stack = new ArrayStack<Object>(1);
        stack.add(new Integer(1));
        stack.add(new Integer(2));
        assertEquals(2, stack.peek(0));
        assertEquals(1, stack.peek(1));
    }

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#peek(int)}.
     */
    @Test(expected = EmptyStackException.class)
    public void testPeekIntException() {
        ArrayStack<Object> stack = new ArrayStack<Object>(1);
        stack.add(new Integer(1));
        stack.add(new Integer(2));
        stack.peek(2);
    }

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#pop()}.
     */
    @Test
    public void testPop() {
        ArrayStack<Object> stack = new ArrayStack<Object>(1);
        assertTrue(stack.empty());
        stack.add(new Integer(1));
        assertEquals(1, stack.pop());
        assertTrue(stack.empty());
    }

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#pop()}.
     */
    @Test(expected = EmptyStackException.class)
    public void testPopException() {
        ArrayStack<Object> stack = new ArrayStack<Object>(1);
        assertTrue(stack.empty());
        stack.pop();
    }

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#push(java.lang.Object)}.
     */
    @Test
    public void testPush() {
        ArrayStack<Object> stack = new ArrayStack<Object>();
        assertTrue(stack.empty());
        stack.push(new Integer(1));
        assertFalse(stack.empty());
    }

    /**
     * Test method for {@link org.apache.tiles.ArrayStack#search(java.lang.Object)}.
     */
    @Test
    public void testSearch() {
        ArrayStack<Object> stack = new ArrayStack<Object>(1);
        stack.add(null);
        stack.add(new Integer(1));
        stack.add(new Integer(2));
        assertEquals(1, stack.search(2));
        assertEquals(2, stack.search(1));
        assertEquals(STACK_SIZE, stack.search(null));
        assertEquals(-1, stack.search(STACK_SIZE));
    }
}
