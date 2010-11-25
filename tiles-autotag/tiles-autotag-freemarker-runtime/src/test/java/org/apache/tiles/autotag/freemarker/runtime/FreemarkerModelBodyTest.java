/**
 *
 */
package org.apache.tiles.autotag.freemarker.runtime;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.Writer;

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
     * Test method for {@link org.apache.tiles.autotag.freemarker.runtime.FreemarkerModelBody#evaluate(java.io.Writer)}.
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
     * Test method for {@link org.apache.tiles.autotag.freemarker.runtime.FreemarkerModelBody#evaluate(java.io.Writer)}.
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
     * Test method for {@link org.apache.tiles.autotag.freemarker.runtime.FreemarkerModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test(expected=IOException.class)
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
