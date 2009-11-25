package org.apache.tiles.request;

public class RequestException extends RuntimeException {

    public RequestException() {
    }

    public RequestException(String message) {
        super(message);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
