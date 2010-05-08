package org.apache.tiles.request.freemarker;

import org.apache.tiles.request.NotAvailableFeatureException;

public class NotAvailableFreemarkerServletException extends
        NotAvailableFeatureException {

    public NotAvailableFreemarkerServletException() {
    }

    public NotAvailableFreemarkerServletException(String message) {
        super(message);
    }

    public NotAvailableFreemarkerServletException(Throwable e) {
        super(e);
    }

    public NotAvailableFreemarkerServletException(String message, Throwable e) {
        super(message, e);
    }
}
