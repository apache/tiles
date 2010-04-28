package org.apache.tiles.request.portlet.delegate;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public interface ResponseDelegate {

    OutputStream getOutputStream() throws IOException;

    PrintWriter getPrintWriter() throws IOException;

    Writer getWriter() throws IOException;

    void setContentType(String contentType);

    boolean isResponseCommitted();
}
