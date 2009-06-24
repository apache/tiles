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

package org.apache.tiles.evaluator.mvel;

import java.util.HashMap;

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextHolder;
import org.mvel2.UnresolveablePropertyException;
import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.impl.BaseVariableResolverFactory;

/**
 * Resolves beans stored in request, session and application scopes.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class TilesContextBeanVariableResolverFactory extends
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
    public TilesContextBeanVariableResolverFactory(TilesRequestContextHolder requestHolder) {
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
    public VariableResolver getVariableResolver(String name) {
        if (isResolveable(name)) {
            if (variableResolvers != null && variableResolvers.containsKey(name)) {
                return variableResolvers.get(name);
            } else if (isTarget(name)) {
                VariableResolver variableResolver = new TilesContextBeanVariableResolver(name);
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
        TilesRequestContext request = requestHolder.getTilesRequestContext();
        return request.getRequestScope().containsKey(name)
                || request.getSessionScope().containsKey(name)
                || request.getApplicationContext().getApplicationScope()
                        .containsKey(name);
    }

    /**
     * Resolves a single attribute stored in request, session or application scope.
     *
     * @version $Rev$ $Date$
     * @since 2.2.0
     */
    private class TilesContextBeanVariableResolver implements VariableResolver {

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
        public TilesContextBeanVariableResolver(String name) {
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
            Object value = getValue();
            if (value != null) {
                return value.getClass();
            }
            return Object.class;
        }

        /** {@inheritDoc} */
        public Object getValue() {
            TilesRequestContext request = requestHolder.getTilesRequestContext();
            Object value = request.getRequestScope().get(name);
            if (value == null) {
                value = request.getSessionScope().get(name);
                if (value == null) {
                    value = request.getApplicationContext()
                            .getApplicationScope().get(name);
                }
            }
            return value;
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
