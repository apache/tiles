package org.apache.tiles.request.util;

import org.apache.tiles.request.ApplicationContext;

public final class ApplicationContextUtil {

    public static final String APPLICATION_CONTEXT_ATTRIBUTE =
        ApplicationContext.class.getName() + ".ATTRIBUTE";

    private ApplicationContextUtil() {
    }

    public static void register(ApplicationContext applicationContext) {
        applicationContext.getApplicationScope().put(
                APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    }
}
