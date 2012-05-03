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
package org.apache.tiles.request.freemarker.autotag;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.Writer;

import org.apache.tiles.request.freemarker.autotag.FreemarkerModelBody;
import org.junit.Test;

import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

/**
 * Tests {@link FreemarkerModelBody}.
 *
 * @version $Rev$ $Date$
 */
public class FreemarkerModelBodyTest {

    /**
     * Test method for {@link org.apache.tiles.request.freemarker.autotag.FreemarkerModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testEvaluateWriter() throws TemplateException, IOException {
        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Writer writer = createMock(Writer.class);

        body.render(writer);

        replay(body, writer);
        FreemarkerModelBody modelBody = new FreemarkerModelBody(null, body);
        modelBody.evaluate(writer);
        verify(body, writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.freemarker.autotag.FreemarkerModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testEvaluateWriterNull() throws TemplateException, IOException {
        Writer writer = createMock(Writer.class);

        replay(writer);
        FreemarkerModelBody modelBody = new FreemarkerModelBody(null, null);
        modelBody.evaluate(writer);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.freemarker.autotag.FreemarkerModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testEvaluateWriterException() throws TemplateException, IOException {
        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Writer writer = createMock(Writer.class);

        body.render(writer);
        expectLastCall().andThrow(new TemplateException(null));

        replay(body, writer);
        try {
            FreemarkerModelBody modelBody = new FreemarkerModelBody(null, body);
            modelBody.evaluate(writer);
        } finally {
            verify(body, writer);
        }
    }
}
