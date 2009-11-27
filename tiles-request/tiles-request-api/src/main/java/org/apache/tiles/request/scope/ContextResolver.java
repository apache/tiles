package org.apache.tiles.request.scope;

import java.util.Map;

import org.apache.tiles.request.Request;

public interface ContextResolver {

    public Map<String, Object> getContext(Request request, String scope);

    public String[] getAvailableScopes(Request request);
}
