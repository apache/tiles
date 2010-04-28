package org.apache.tiles.request.portlet.delegate;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class StateAwareResponseDelegate implements ResponseDelegate {

    @Override
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException(
                "No outputstream available for state-aware response");
    }

    @Override
    public PrintWriter getPrintWriter() {
        throw new UnsupportedOperationException(
                "No outputstream available for state-aware response");
    }

    @Override
    public Writer getWriter() {
        throw new UnsupportedOperationException(
                "No outputstream available for state-aware response");
    }

    @Override
    public boolean isResponseCommitted() {
        return false;
    }

    @Override
    public void setContentType(String contentType) {
        throw new UnsupportedOperationException(
                "No outputstream available for state-aware response");
    }
}
