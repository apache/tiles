package org.apache.tiles.request;

import java.util.Map;

import org.apache.tiles.request.scope.ContextResolver;
import org.apache.tiles.request.util.ApplicationAccess;

public abstract class AbstractRequest implements Request{

    private ApplicationContext applicationContext;

    public AbstractRequest(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public Map<String, Object> getContext(String scope) {
        ContextResolver resolver = ApplicationAccess.getContextResolver(applicationContext);
        return resolver.getContext(this, scope);
    }

    @Override
    public String[] getAvailableScopes() {
        ContextResolver resolver = ApplicationAccess.getContextResolver(applicationContext);
        return resolver.getAvailableScopes(this);
    }

    public Map<String, Object> getApplicationScope() {
        return applicationContext.getApplicationScope();
    }
}
