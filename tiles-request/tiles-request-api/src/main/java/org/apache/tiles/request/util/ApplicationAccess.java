package org.apache.tiles.request.util;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.scope.ContextResolver;

public final class ApplicationAccess {

    public static final String APPLICATION_CONTEXT_ATTRIBUTE =
        ApplicationContext.class.getName() + ".ATTRIBUTE";

    public static final String CONTEXT_RESOLVER_ATTRIBUTE =
        ContextResolver.class.getName() + ".ATTRIBUTE";

    private ApplicationAccess() {
    }

    public static void register(ApplicationContext applicationContext) {
        applicationContext.getApplicationScope().put(
                APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    }

    public static void registerContextResolver(ContextResolver contextResolver,
            ApplicationContext applicationContext) {
        applicationContext.getApplicationScope().put(
                CONTEXT_RESOLVER_ATTRIBUTE, contextResolver);
    }

    public static ContextResolver getContextResolver(
            ApplicationContext applicationContext) {
        return (ContextResolver) applicationContext.getApplicationScope().get(
                CONTEXT_RESOLVER_ATTRIBUTE);
    }
}
