package org.apache.tiles.request.render;

public class RenderException extends RuntimeException {

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
