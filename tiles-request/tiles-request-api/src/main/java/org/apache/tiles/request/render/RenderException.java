package org.apache.tiles.request.render;

import org.apache.tiles.request.RequestException;

public class RenderException extends RequestException {

    public RenderException() {
    }

    public RenderException(String message) {
        super(message);
    }

    public RenderException(Throwable cause) {
        super(cause);
    }

    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
