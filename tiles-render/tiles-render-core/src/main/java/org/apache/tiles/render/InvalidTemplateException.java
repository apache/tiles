package org.apache.tiles.render;

public class InvalidTemplateException extends RenderException {

    public InvalidTemplateException() {
        super();
    }

    public InvalidTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTemplateException(String message) {
        super(message);
    }

    public InvalidTemplateException(Throwable cause) {
        super(cause);
    }

}
