/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tiles.request.scope;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.util.RequestWrapper;

/**
 * Resolves scopes and contexts by using reflection.
 * To expose a scope, add a <code>get&lt;name_of_the_scope&gt;Scope</code> without parameters
 * that returns a <code>Map&lt;String, Object&gt;</code>.
 *
 * @version $Rev$ $Date$
 */
public class ReflectionContextResolver implements ContextResolver {

    /**
     * Maps a request class to all available scopes.
     */
    private Map<Class<? extends Request>, Set<String>> class2scopes =
        new HashMap<Class<? extends Request>, Set<String>>();

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getContext(Request request, String scope) {
        String methodName = "get" + Character.toUpperCase(scope.charAt(0))
                + scope.substring(1) + "Scope";
        Method method;
        try {
            method = request.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            if (request instanceof RequestWrapper) {
                RequestWrapper wrapper = (RequestWrapper) request;
                return getContext(wrapper.getWrappedRequest(), scope);
            }
            throw new NoSuchScopeException("No accessor method for '" + scope
                    + "' scope.", e);
        }
        try {
            return (Map<String, Object>) method.invoke(request);
        } catch (IllegalAccessException e) {
            // Should not ever happen, since method is public.
            throw new NoSuchScopeException("No accessible method for '" + scope
                    + "' scope.", e);
        } catch (InvocationTargetException e) {
            throw new NoSuchScopeException(
                    "Exception during execution of accessor method for '"
                            + scope + "' scope.", e);
        }
    }

    @Override
    public String[] getAvailableScopes(Request request) {
        Set<String> scopes = new LinkedHashSet<String>();
        boolean finished = false;
        do {
            scopes.addAll(getSpecificScopeSet(request));
            if (request instanceof RequestWrapper) {
                request = ((RequestWrapper) request)
                        .getWrappedRequest();
            } else {
                finished = true;
            }
        } while(!finished);
        String[] retValue = new String[scopes.size()];
        return scopes.toArray(retValue);
    }

    /**
     * Returns the scopes for a single class. To be used in an iteration to get the scopes
     * of all the hierarchy of the request.
     *
     * @param request The request to analyze.
     * @return The scope set.
     */
    private Set<String> getSpecificScopeSet(Request request) {
        Set<String> scopes = class2scopes.get(request.getClass());
        if (scopes == null) {
            scopes = new LinkedHashSet<String>();
            String[] nativeScopes = request.getNativeScopes();
            if (nativeScopes != null) {
                for (String scopeName : nativeScopes) {
                    scopes.add(scopeName);
                }
            }
            class2scopes.put(request.getClass(), scopes);
        }
        return scopes;
    }
}
