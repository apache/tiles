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

package org.apache.tiles.definition.digester;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link DigesterDefinitionsReaderException}.
 *
 * @version $Rev$ $Date$
 */
public class DigesterDefinitionsReaderExceptionTest {

    /**
     * Test method for {@link DigesterDefinitionsReaderException#DigesterDefinitionsReaderException()}.
     */
    @Test
    public void testDigesterDefinitionsReaderException() {
        DigesterDefinitionsReaderException exception = new DigesterDefinitionsReaderException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Test method for {@link DigesterDefinitionsReaderException#DigesterDefinitionsReaderException(java.lang.String)}.
     */
    @Test
    public void testDigesterDefinitionsReaderExceptionString() {
        DigesterDefinitionsReaderException exception = new DigesterDefinitionsReaderException("my message");
        assertEquals("my message", exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Test method for {@link DigesterDefinitionsReaderException#DigesterDefinitionsReaderException(Throwable)}.
     */
    @Test
    public void testDigesterDefinitionsReaderExceptionThrowable() {
        Throwable cause = new Throwable();
        DigesterDefinitionsReaderException exception = new DigesterDefinitionsReaderException(cause);
        assertEquals(cause.toString(), exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    /**
     * Test method for {@link DigesterDefinitionsReaderException#DigesterDefinitionsReaderException(String, Throwable)}.
     */
    @Test
    public void testDigesterDefinitionsReaderExceptionStringThrowable() {
        Throwable cause = new Throwable();
        DigesterDefinitionsReaderException exception = new DigesterDefinitionsReaderException("my message", cause);
        assertEquals("my message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
