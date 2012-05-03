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

import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.reflect.CannotAccessMethodException;
import org.apache.tiles.util.CombinedBeanInfo;
import org.mvel2.integration.VariableResolver;

/**
 * Resolves {@link org.apache.tiles.request.Request} and
 * {@link org.apache.tiles.request.ApplicationContext} properties as variables.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class TilesContextVariableResolverFactory extends
        ReadOnlyVariableResolverFactory {

    /**
     * Beaninfo about {@link org.apache.tiles.request.Request} and
     * {@link org.apache.tiles.request.ApplicationContext}.
     */
    private CombinedBeanInfo requestBeanInfo = new CombinedBeanInfo(
            Request.class, ApplicationContext.class);

    /**
     * Constructor.
     *
     * @param requestHolder The Tiles request holder.
     * @since 2.2.0
     */
    public TilesContextVariableResolverFactory(TilesRequestContextHolder requestHolder) {
        super(requestHolder);
    }

    /** {@inheritDoc} */
    public boolean isTarget(String name) {
        return requestBeanInfo.getMappedDescriptors(Request.class).containsKey(
                name)
                || requestBeanInfo.getMappedDescriptors(
                        ApplicationContext.class).containsKey(name);
    }

    /** {@inheritDoc} */
    @Override
    public VariableResolver createVariableResolver(String name) {
        VariableResolver resolver = null;
        PropertyDescriptor descriptor = requestBeanInfo.getMappedDescriptors(Request.class).get(name);
        if (descriptor != null) {
            resolver = new RequestVariableResolver(name, descriptor);
        } else {
            descriptor = requestBeanInfo.getMappedDescriptors(ApplicationContext.class).get(name);
            if (descriptor != null) {
                resolver = new ApplicationVariableResolver(name, descriptor);
            }
        }
        return resolver;
    }

    /**
     * Resolves a {@link org.apache.tiles.request.Request} property as a variable.
     *
     * @version $Rev$ $Date$
     * @since 2.2.0
     */
    private class RequestVariableResolver extends ReadOnlyVariableResolver {

        /**
         * The property descriptor.
         */
        private PropertyDescriptor descriptor;

        /**
         * Constructor.
         *
         * @param name The name of the property.
         * @param descriptor The property descriptor.
         * @since 2.2.0
         */
        public RequestVariableResolver(String name, PropertyDescriptor descriptor) {
            super(name);
            this.descriptor = descriptor;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("rawtypes")
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
    }

    /**
     * Resolves a {@link org.apache.tiles.request.ApplicationContext} property as a
     * variable.
     *
     * @version $Rev$ $Date$
     * @since 2.2.0
     */
    private class ApplicationVariableResolver extends ReadOnlyVariableResolver {

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
         * @param descriptor The property descriptor.
         * @since 2.2.0
         */
        public ApplicationVariableResolver(String name, PropertyDescriptor descriptor) {
            super(name);
            this.descriptor = descriptor;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("rawtypes")
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
    }
}
