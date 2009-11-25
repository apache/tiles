package org.apache.tiles.request.scope;

import org.apache.tiles.request.RequestException;

public class NoSuchScopeException extends RequestException {

    public NoSuchScopeException() {
    }

    public NoSuchScopeException(String message) {
        super(message);
    }

    public NoSuchScopeException(Throwable cause) {
        super(cause);
    }

    public NoSuchScopeException(String message, Throwable cause) {
        super(message, cause);
    }

}
