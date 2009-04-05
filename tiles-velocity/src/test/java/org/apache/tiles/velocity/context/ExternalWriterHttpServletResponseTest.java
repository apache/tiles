/**
 * 
 */
package org.apache.tiles.velocity.context;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

/**
 * @author antonio
 *
 */
public class ExternalWriterHttpServletResponseTest {

    /**
     * Test method for {@link org.apache.tiles.velocity.context.ExternalWriterHttpServletResponse#getWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetWriter() throws IOException {
        HttpServletResponse wrappedResponse = createMock(HttpServletResponse.class);
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        replay(wrappedResponse);
        ExternalWriterHttpServletResponse response = new ExternalWriterHttpServletResponse(
                wrappedResponse, printWriter);
        assertEquals(printWriter, response.getWriter());
        verify(wrappedResponse);
    }

}
