package org.apache.tiles.freemarker.context;

import org.apache.tiles.request.RequestException;

public class FreemarkerRequestException extends RequestException {

    public FreemarkerRequestException() {
    }

    public FreemarkerRequestException(String message) {
        super(message);
    }

    public FreemarkerRequestException(Throwable cause) {
        super(cause);
    }

    public FreemarkerRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
