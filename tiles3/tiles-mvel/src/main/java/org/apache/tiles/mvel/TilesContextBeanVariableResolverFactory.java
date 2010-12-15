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

import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.request.Request;
import org.mvel2.integration.VariableResolver;

/**
 * Resolves beans stored in request, session and application scopes.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class TilesContextBeanVariableResolverFactory extends
        ReadOnlyVariableResolverFactory {

    /**
     * Constructor.
     *
     * @param requestHolder The Tiles request holder.
     * @since 2.2.0
     */
    public TilesContextBeanVariableResolverFactory(TilesRequestContextHolder requestHolder) {
        super(requestHolder);
    }

    /** {@inheritDoc} */
    @Override
    public VariableResolver createVariableResolver(String name) {
        return new TilesContextBeanVariableResolver(name);
    }

    /** {@inheritDoc} */
    public boolean isTarget(String name) {
        Request request = requestHolder.getTilesRequestContext();
        for (String scope : request.getAvailableScopes()) {
            if (request.getContext(scope).containsKey(name)) {
                return true;
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
    private class TilesContextBeanVariableResolver extends ReadOnlyVariableResolver {

        /**
         * Constructor.
         *
         * @param name The name of the attribute.
         * @since 2.2.0
         */
        public TilesContextBeanVariableResolver(String name) {
            super(name);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("rawtypes")
        public Class getType() {
            Object value = getValue();
            if (value != null) {
                return value.getClass();
            }
            return Object.class;
        }

        /** {@inheritDoc} */
        public Object getValue() {
            Request request = requestHolder.getTilesRequestContext();
            for (String scope : request.getAvailableScopes()) {
                Object value = request.getContext(scope).get(name);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }
    }
}
