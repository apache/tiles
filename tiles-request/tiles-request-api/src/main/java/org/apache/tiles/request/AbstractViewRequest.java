package org.apache.tiles.request;

import java.io.IOException;

import org.apache.tiles.request.util.RequestWrapper;

public class AbstractViewRequest extends RequestWrapper {

    public AbstractViewRequest(Request request) {
        super(request);
    }

    @Override
    public void dispatch(String path) throws IOException {
        setForceInclude(true);
        doInclude(path);
    }

    @Override
    public void include(String path) throws IOException {
        setForceInclude(true);
        doInclude(path);
    }

    protected void doInclude(String path) throws IOException {
        getWrappedRequest().include(path);
    }
}
