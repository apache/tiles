package org.apache.tiles.request.scope;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.util.TilesRequestContextWrapper;

public class ReflectionContextResolver implements ContextResolver {

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getContext(Request request, String scope) {
        String methodName = "get" + Character.toUpperCase(scope.charAt(0))
                + scope.substring(1) + "Scope";
        Method method;
        try {
            method = request.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            if (request instanceof TilesRequestContextWrapper) {
                TilesRequestContextWrapper wrapper = (TilesRequestContextWrapper) request;
                return getContext(wrapper.getWrappedRequest(), scope);
            }
            throw new NoSuchScopeException("No accessor method for '" + scope
                    + "' scope.", e);
        }
        try {
            return (Map<String, Object>) method.invoke(request);
        } catch (IllegalAccessException e) {
            throw new NoSuchScopeException("No accessible method for '" + scope
                    + "' scope.", e);
        } catch (InvocationTargetException e) {
            throw new NoSuchScopeException(
                    "Exception during execution of accessor method for '"
                            + scope + "' scope.", e);
        }
    }
}
