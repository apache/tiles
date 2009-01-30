/**
 * 
 */
package org.apache.tiles.freemarker.io;

import java.io.IOException;
import java.io.Writer;

public class NullWriter extends Writer {

    @Override
    public void close() throws IOException {
        // Does nothing
    }

    @Override
    public void flush() throws IOException {
        // Does nothing
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        // Does nothing
    }
    
}