/**
 *
 */
package org.apache.tiles.autotag.velocity.runtime;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.junit.Test;

/**
 * Tests {@link VelocityModelBody}.
 *
 * @version $Rev$ $Date$
 */
public class VelocityModelBodyTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.velocity.runtime.VelocityModelBody#evaluate(java.io.Writer)}.
     * @throws IOException If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     * @throws MethodInvocationException If something goes wrong.
     */
    @Test
    public void testEvaluateWriter() throws MethodInvocationException, ResourceNotFoundException, ParseErrorException, IOException {
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
