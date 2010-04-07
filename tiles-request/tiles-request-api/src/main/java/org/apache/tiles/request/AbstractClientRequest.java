package org.apache.tiles.request;

import java.io.IOException;
import java.util.Map;

import org.apache.tiles.request.scope.ContextResolver;
import org.apache.tiles.request.util.ApplicationAccess;

public abstract class AbstractClientRequest extends AbstractRequest {

    private ApplicationContext applicationContext;

    public AbstractClientRequest(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public void dispatch(String path) throws IOException {
        if (isForceInclude()) {
            doInclude(path);
        } else {
            setForceInclude(true);
            doForward(path);
        }
    }

    @Override
    public void include(String path) throws IOException {
        setForceInclude(true);
        doInclude(path);
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

    protected abstract void doForward(String path) throws IOException;

    protected abstract void doInclude(String path) throws IOException;

}
