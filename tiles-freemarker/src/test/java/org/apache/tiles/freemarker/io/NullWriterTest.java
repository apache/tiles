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
package org.apache.tiles.freemarker.io;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 */
public class NullWriterTest {

    /**
     * A dummy size.
     */
    private static final int DUMMY_SIZE = 15;
    /**
     * The object to test.
     */
    private NullWriter writer;

    /**
     * @throws java.lang.Exception If something goes wrong.
     */
    @Before
    public void setUp() throws Exception {
        writer = new NullWriter();
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.io.NullWriter#write(char[], int, int)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testWriteCharArrayIntInt() throws IOException {
        writer.write("Hello there".toCharArray(), 0, DUMMY_SIZE);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.io.NullWriter#flush()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testFlush() throws IOException {
        writer.flush();
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.io.NullWriter#close()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testClose() throws IOException {
        writer.close();
    }

}
