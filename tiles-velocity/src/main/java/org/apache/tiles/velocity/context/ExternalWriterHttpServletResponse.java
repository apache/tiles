package org.apache.tiles.velocity.context;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ExternalWriterHttpServletResponse extends
        HttpServletResponseWrapper {

    private PrintWriter writer;
    
    public ExternalWriterHttpServletResponse(HttpServletResponse response, PrintWriter writer) {
        super(response);
        this.writer = writer;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }
}
