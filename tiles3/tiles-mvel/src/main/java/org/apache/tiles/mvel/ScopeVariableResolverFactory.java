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

import java.util.Map;

import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.request.Request;
import org.mvel2.integration.VariableResolver;

/**
 * Resolves beans stored in request, session and application scopes.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class ScopeVariableResolverFactory extends
        ReadOnlyVariableResolverFactory {

    /**
     * The length of the scope suffix: "Scope".
     */
    private static final int SCOPE_SUFFIX_LENGTH = 5;

    /**
     * Constructor.
     *
     * @param requestHolder The Tiles request holder.
     * @since 2.2.0
     */
    public ScopeVariableResolverFactory(TilesRequestContextHolder requestHolder) {
        super(requestHolder);
    }

    /** {@inheritDoc} */
    @Override
    public VariableResolver createVariableResolver(String name) {
        return new ScopeVariableResolver(name);
    }

    /** {@inheritDoc} */
    public boolean isTarget(String name) {
        Request request = requestHolder.getTilesRequestContext();
        if (name.endsWith("Scope")) {
            String scopeName = name.substring(0, name.length() - SCOPE_SUFFIX_LENGTH);
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
    private class ScopeVariableResolver extends ReadOnlyVariableResolver {

        /**
         * Constructor.
         *
         * @param name The name of the attribute.
         * @since 2.2.0
         */
        public ScopeVariableResolver(String name) {
            super(name);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("rawtypes")
        public Class getType() {
            return Map.class;
        }

        /** {@inheritDoc} */
        public Object getValue() {
            Request request = requestHolder.getTilesRequestContext();
            return request.getContext(name.substring(0, name.length() - SCOPE_SUFFIX_LENGTH));
        }
    }
}
