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
package org.apache.tiles.request.velocity.autotag;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.Writer;

import org.apache.tiles.request.velocity.autotag.VelocityModelBody;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.junit.Test;

/**
 * Tests {@link VelocityModelBody}.
 *
 * @version $Rev$ $Date$
 */
public class VelocityModelBodyTest {

    /**
     * Test method for {@link org.apache.tiles.request.velocity.autotag.VelocityModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEvaluateWriter() throws IOException {
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        ASTBlock body = createMock(ASTBlock.class);
        Writer writer = createMock(Writer.class);
        expect(body.render(internalContextAdapter, writer)).andReturn(true);

        replay(internalContextAdapter, body, writer);
        VelocityModelBody modelBody = createMockBuilder(VelocityModelBody.class)
                .withConstructor(internalContextAdapter, body, writer)
                .createMock();
        replay(modelBody);
        modelBody.evaluate(writer);
        verify(internalContextAdapter, body, writer, modelBody);
    }

}
