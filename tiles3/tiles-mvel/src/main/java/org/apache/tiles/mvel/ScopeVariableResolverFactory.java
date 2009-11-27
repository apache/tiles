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

package org.apache.tiles.mvel;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.request.Request;
import org.mvel2.UnresolveablePropertyException;
import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.impl.BaseVariableResolverFactory;

/**
 * Resolves beans stored in request, session and application scopes.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class ScopeVariableResolverFactory extends
        BaseVariableResolverFactory {

    /**
     * The Tiles request holder.
     */
    private TilesRequestContextHolder requestHolder;

    /**
     * Constructor.
     *
     * @param requestHolder The Tiles request holder.
     * @since 2.2.0
     */
    public ScopeVariableResolverFactory(TilesRequestContextHolder requestHolder) {
        this.requestHolder = requestHolder;
        variableResolvers = new HashMap<String, VariableResolver>();
    }

    /** {@inheritDoc} */
    public VariableResolver createVariable(String name, Object value) {
        if (nextFactory != null) {
            return nextFactory.createVariable(name, value);
        }
        throw new UnsupportedOperationException("This variable resolver factory is read only");
    }

    /** {@inheritDoc} */
    public VariableResolver createVariable(String name, Object value,
            Class<?> type) {
        variableResolvers = new HashMap<String, VariableResolver>();
        if (nextFactory != null) {
            return nextFactory.createVariable(name, value, type);
        }
        throw new UnsupportedOperationException("This variable resolver factory is read only");
    }

    /** {@inheritDoc} */
    public boolean isResolveable(String name) {
        return isTarget(name) || isNextResolveable(name);
    }

    /** {@inheritDoc} */
    @Override
    public VariableResolver getVariableResolver(String name) {
        if (isResolveable(name)) {
            if (variableResolvers != null && variableResolvers.containsKey(name)) {
                return variableResolvers.get(name);
            } else if (isTarget(name)) {
                VariableResolver variableResolver = new ScopeVariableResolver(name);
                variableResolvers.put(name, variableResolver);
                return variableResolver;
            } else if (nextFactory != null) {
                return nextFactory.getVariableResolver(name);
            }
        }

        throw new UnresolveablePropertyException("unable to resolve variable '" + name + "'");
    }

    /** {@inheritDoc} */
    public boolean isTarget(String name) {
        Request request = requestHolder.getTilesRequestContext();
        if (name.endsWith("Scope")) {
            String scopeName = name.substring(0, name.length() - 5);
            for (String availableScope : request.getAvailableScopes()) {
                if (scopeName.equals(availableScope)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Resolves a single attribute stored in request, session or application scope.
     *
     * @version $Rev$ $Date$
     * @since 2.2.0
     */
    private class ScopeVariableResolver implements VariableResolver {

        /**
         * The name of the attribute.
         */
        private String name;

        /**
         * Constructor.
         *
         * @param name The name of the attribute.
         * @since 2.2.0
         */
        public ScopeVariableResolver(String name) {
            this.name = name;
        }

        /** {@inheritDoc} */
        public int getFlags() {
            return 0;
        }

        /** {@inheritDoc} */
        public String getName() {
            return name;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public Class getType() {
            return Map.class;
        }

        /** {@inheritDoc} */
        public Object getValue() {
            Request request = requestHolder.getTilesRequestContext();
            return request.getContext(name.substring(0, name.length() - 5));
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public void setStaticType(Class type) {
            // Does nothing for the moment.
        }

        /** {@inheritDoc} */
        public void setValue(Object value) {
            throw new UnsupportedOperationException("This resolver is read-only");
        }
    }
}
