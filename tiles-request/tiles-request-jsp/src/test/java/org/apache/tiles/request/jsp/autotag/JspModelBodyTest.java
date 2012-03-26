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
package org.apache.tiles.request.jsp.autotag;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.tiles.request.jsp.autotag.JspModelBody;
import org.junit.Test;

/**
 * Tests {@link JspModelBody}.
 *
 * @version $Rev$ $Date$
 */
public class JspModelBodyTest {

    /**
     * Test method for {@link org.apache.tiles.request.jsp.autotag.freemarker.runtime.JspModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     * @throws JspException If something goes wrong.
     */
    @Test
    public void testEvaluateWriter() throws JspException, IOException {
        JspFragment body = createMock(JspFragment.class);
        PageContext pageContext = createMock(PageContext.class);
        JspWriter writer = createMock(JspWriter.class);

        expect(pageContext.getOut()).andReturn(null);
        body.invoke(writer);

        replay(body, pageContext, writer);
        JspModelBody modelBody = new JspModelBody(body, pageContext);
        modelBody.evaluate(writer);
        verify(body, pageContext, writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.autotag.freemarker.runtime.JspModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     * @throws JspException If something goes wrong.
     */
    @Test
    public void testEvaluateWriterNull() throws JspException, IOException {
        PageContext pageContext = createMock(PageContext.class);
        Writer writer = createMock(Writer.class);

        expect(pageContext.getOut()).andReturn(null);

        replay(writer, pageContext);
        JspModelBody modelBody = new JspModelBody(null, pageContext);
        modelBody.evaluate(writer);
        verify(writer, pageContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.autotag.freemarker.runtime.JspModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     * @throws JspException If something goes wrong.
     */
    @Test(expected = IOException.class)
    public void testEvaluateWriterException() throws JspException, IOException {
        PageContext pageContext = createMock(PageContext.class);
        JspFragment body = createMock(JspFragment.class);
        JspWriter writer = createMock(JspWriter.class);

        expect(pageContext.getOut()).andReturn(null);
        body.invoke(writer);
        expectLastCall().andThrow(new JspException());

        replay(body, pageContext, writer);
        try {
            JspModelBody modelBody = new JspModelBody(body, pageContext);
            modelBody.evaluate(writer);
        } finally {
            verify(body, pageContext, writer);
        }
    }
}
