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

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.reflect.CannotAccessMethodException;
import org.apache.tiles.util.CombinedBeanInfo;
import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.impl.BaseVariableResolverFactory;

/**
 * Resolves {@link org.apache.tiles.context.TilesRequestContext} and
 * {@link org.apache.tiles.TilesApplicationContext} properties as variables.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class TilesContextVariableResolverFactory extends
        BaseVariableResolverFactory {

    /**
     * The Tiles request holder.
     */
    private TilesRequestContextHolder requestHolder;

    /**
     * Beaninfo about {@link org.apache.tiles.context.TilesRequestContext} and
     * {@link org.apache.tiles.TilesApplicationContext}.
     */
    private CombinedBeanInfo requestBeanInfo = new CombinedBeanInfo(
            TilesRequestContext.class, TilesApplicationContext.class);

    /**
     * Constructor.
     *
     * @param requestHolder The Tiles request holder.
     * @since 2.2.0
     */
    public TilesContextVariableResolverFactory(TilesRequestContextHolder requestHolder) {
        this.requestHolder = requestHolder;
        variableResolvers = new HashMap<String, VariableResolver>();
        for (PropertyDescriptor descriptor : requestBeanInfo
                .getMappedDescriptors(TilesRequestContext.class).values()) {
            String descriptorName = descriptor.getName();
            variableResolvers.put(descriptorName, new RequestVariableResolver(descriptorName));
        }
        for (PropertyDescriptor descriptor : requestBeanInfo
                .getMappedDescriptors(TilesApplicationContext.class).values()) {
            String descriptorName = descriptor.getName();
            variableResolvers.put(descriptorName, new ApplicationVariableResolver(descriptorName));
        }
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
        if (nextFactory != null) {
            return nextFactory.createVariable(name, value, type);
        }
        throw new UnsupportedOperationException("This variable resolver factory is read only");
    }

    /** {@inheritDoc} */
    public boolean isResolveable(String name) {
        if (variableResolvers.containsKey(name)) {
            return true;
        }
        return isNextResolveable(name);
    }

    /** {@inheritDoc} */
    public boolean isTarget(String name) {
        return variableResolvers.containsKey(name);
    }

    /**
     * Resolves a {@link org.apache.tiles.context.TilesRequestContext} property as a variable.
     *
     * @version $Rev$ $Date$
     * @since 2.2.0
     */
    private class RequestVariableResolver implements VariableResolver {

        /**
         * The name of the property.
         */
        private String name;

        /**
         * The property descriptor.
         */
        private PropertyDescriptor descriptor;

        /**
         * Constructor.
         *
         * @param name The name of the property.
         * @since 2.2.0
         */
        public RequestVariableResolver(String name) {
            this.name = name;
            descriptor = requestBeanInfo.getMappedDescriptors(
                    TilesRequestContext.class).get(name);
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
            return descriptor.getPropertyType();
        }

        /** {@inheritDoc} */
        public Object getValue() {
            Method method = descriptor.getReadMethod();
            try {
                return method.invoke(requestHolder.getTilesRequestContext());
            } catch (IllegalArgumentException e) {
                throw new CannotAccessMethodException(
                        "Arguments are wrong for property '"
                                + descriptor.getName() + "'", e);
            } catch (IllegalAccessException e) {
                throw new CannotAccessMethodException(
                        "Cannot access getter method for property '"
                                + descriptor.getName() + "'", e);
            } catch (InvocationTargetException e) {
                throw new CannotAccessMethodException(
                        "The getter method for property '"
                                + descriptor.getName() + "' threw an exception",
                        e);
            }
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

    /**
     * Resolves a {@link org.apache.tiles.TilesApplicationContext} property as a
     * variable.
     *
     * @version $Rev$ $Date$
     * @since 2.2.0
     */
    private class ApplicationVariableResolver implements VariableResolver {

        /**
         * The name of the property.
         *
         * @since 2.2.0
         */
        private String name;

        /**
         * The property descriptor.
         *
         * @since 2.2.0
         */
        private PropertyDescriptor descriptor;

        /**
         * Constructor.
         *
         * @param name The name of the property.
         * @since 2.2.0
         */
        public ApplicationVariableResolver(String name) {
            this.name = name;
            descriptor = requestBeanInfo.getMappedDescriptors(
                    TilesApplicationContext.class).get(name);
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
            return descriptor.getPropertyType();
        }

        /** {@inheritDoc} */
        public Object getValue() {
            Method method = descriptor.getReadMethod();
            try {
                return method.invoke(requestHolder.getTilesRequestContext()
                        .getApplicationContext());
            } catch (IllegalArgumentException e) {
                throw new CannotAccessMethodException(
                        "Arguments are wrong for property '"
                                + descriptor.getName() + "'", e);
            } catch (IllegalAccessException e) {
                throw new CannotAccessMethodException(
                        "Cannot access getter method for property '"
                                + descriptor.getName() + "'", e);
            } catch (InvocationTargetException e) {
                throw new CannotAccessMethodException(
                        "The getter method for property '"
                                + descriptor.getName() + "' threw an exception",
                        e);
            }
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
