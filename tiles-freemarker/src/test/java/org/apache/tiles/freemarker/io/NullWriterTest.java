/**
 * 
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
        writer.write("Hello there".toCharArray(), 0, 15);
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
