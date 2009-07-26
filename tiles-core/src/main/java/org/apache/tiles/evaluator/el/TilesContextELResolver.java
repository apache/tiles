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
package org.apache.tiles.evaluator.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.BeanELResolver;
import javax.el.ELContext;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.util.CombinedBeanInfo;

/**
 * Resolves properties of {@link TilesRequestContext} and
 * {@link TilesApplicationContext}.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class TilesContextELResolver extends BeanELResolver {

    /**
     * The beaninfos about {@link TilesRequestContext} and {@link TilesApplicationContext}.
     */
    private CombinedBeanInfo requestBeanInfo = new CombinedBeanInfo(
            TilesRequestContext.class, TilesApplicationContext.class);

    /** {@inheritDoc} */
    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        // only resolve at the root of the context
        if (base != null) {
            return null;
        }

        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
            Object base) {
        // only resolve at the root of the context
        if (base != null) {
            return null;
        }

        return requestBeanInfo.getDescriptors().iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        // only resolve at the root of the context
        if (base != null) {
            return null;
        }

        Class<?> retValue = null;
        if (requestBeanInfo.getProperties(TilesRequestContext.class).contains(property)) {
            TilesRequestContext request = (TilesRequestContext) context
                    .getContext(TilesRequestContext.class);
            retValue = super.getType(context, request, property);
        } else if (requestBeanInfo.getProperties(TilesApplicationContext.class).contains(property)) {
            TilesApplicationContext applicationContext = (TilesApplicationContext) context
                    .getContext(TilesApplicationContext.class);
            retValue = super.getType(context, applicationContext, property);
        }

        if (retValue != null) {
            context.setPropertyResolved(true);
        }

        return retValue;
    }

    /** {@inheritDoc} */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        // only resolve at the root of the context
        if (base != null) {
            return null;
        }

        Object retValue = null;

        if (requestBeanInfo.getProperties(TilesRequestContext.class).contains(property)) {
            TilesRequestContext request = (TilesRequestContext) context
                    .getContext(TilesRequestContext.class);
            retValue = super.getValue(context, request, property);
        } else if (requestBeanInfo.getProperties(TilesApplicationContext.class)
                .contains(property)) {
            TilesApplicationContext applicationContext = (TilesApplicationContext) context
                    .getContext(TilesApplicationContext.class);
            retValue = super.getValue(context, applicationContext, property);
        }

        if (retValue != null) {
            context.setPropertyResolved(true);
        }

        return retValue;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(ELContext context, Object base, Object property,
            Object value) {
        // Does nothing for the moment.
    }
}
