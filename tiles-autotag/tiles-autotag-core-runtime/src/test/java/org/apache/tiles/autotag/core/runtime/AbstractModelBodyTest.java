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
package org.apache.tiles.autotag.core.runtime;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.tiles.autotag.core.runtime.util.NullWriter;
import org.junit.Test;

/**
 * Tests {@link AbstractModelBody}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractModelBodyTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.core.runtime.AbstractModelBody#evaluate()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEvaluate() throws IOException {
        Writer writer = createMock(Writer.class);
        AbstractModelBody modelBody = createMockBuilder(AbstractModelBody.class).withConstructor(writer).createMock();

        modelBody.evaluate(writer);

        replay(writer, modelBody);
        modelBody.evaluate();
        verify(writer, modelBody);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.core.runtime.AbstractModelBody#evaluateAsString()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEvaluateAsString() throws IOException {
        AbstractModelBody modelBody = new MockModelBody(null, "return me");
        assertEquals("return me", modelBody.evaluateAsString());

        modelBody = new MockModelBody(null, "\n   \n");
        assertNull(modelBody.evaluateAsString());
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.core.runtime.AbstractModelBody#evaluateAsString()}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testEvaluateAsStringException() throws IOException {
        Writer writer = createMock(Writer.class);
        AbstractModelBody modelBody = createMockBuilder(AbstractModelBody.class).withConstructor(writer).createMock();

        modelBody.evaluate(isA(StringWriter.class));
        expectLastCall().andThrow(new IOException());

        replay(writer, modelBody);
        try {
            modelBody.evaluateAsString();
        } finally {
            verify(writer, modelBody);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.core.runtime.AbstractModelBody#evaluateWithoutWriting()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEvaluateWithoutWriting() throws IOException {
        Writer writer = createMock(Writer.class);
        AbstractModelBody modelBody = createMockBuilder(AbstractModelBody.class).withConstructor(writer).createMock();

        modelBody.evaluate(isA(NullWriter.class));

        replay(writer, modelBody);
        modelBody.evaluateWithoutWriting();
        verify(writer, modelBody);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.core.runtime.AbstractModelBody#evaluateWithoutWriting()}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testEvaluateWithoutWritingException() throws IOException {
        Writer writer = createMock(Writer.class);
        AbstractModelBody modelBody = createMockBuilder(AbstractModelBody.class).withConstructor(writer).createMock();

        modelBody.evaluate(isA(NullWriter.class));
        expectLastCall().andThrow(new IOException());

        replay(writer, modelBody);
        try {
            modelBody.evaluateWithoutWriting();
        } finally {
            verify(writer, modelBody);
        }
    }

    /**
     * A mock model body.
     *
     * @version $Rev$ $Date$
     */
    public static class MockModelBody extends AbstractModelBody {

        /**
         * The result to return.
         */
        private String toReturn;

        /**
         * Constructor.
         *
         * @param defaultWriter The default writer.
         * @param toReturn The result to return.
         */
        public MockModelBody(Writer defaultWriter, String toReturn) {
            super(defaultWriter);
            this.toReturn = toReturn;
        }

        @Override
        public void evaluate(Writer writer) throws IOException {
            writer.write(toReturn);
        }

    }
}
