package org.apache.tiles.request.portlet.delegate;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import javax.portlet.MimeResponse;

public class MimeResponseDelegate implements ResponseDelegate {

    private MimeResponse response;

    public MimeResponseDelegate(MimeResponse response) {
        this.response = response;
    }

    /** {@inheritDoc} */
    public OutputStream getOutputStream() throws IOException {
        return response.getPortletOutputStream();
    }

    /** {@inheritDoc} */
    public PrintWriter getPrintWriter() throws IOException {
        return response.getWriter();
    }

    /** {@inheritDoc} */
    public Writer getWriter() throws IOException {
        return response.getWriter();
    }

    /** {@inheritDoc} */
    public boolean isResponseCommitted() {
        return response.isCommitted();
    }

    /** {@inheritDoc} */
    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }
}
