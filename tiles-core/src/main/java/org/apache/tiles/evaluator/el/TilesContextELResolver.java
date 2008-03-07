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

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.el.BeanELResolver;
import javax.el.ELContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;

/**
 * Resolves properties of {@link TilesRequestContext} and
 * {@link TilesApplicationContext}.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class TilesContextELResolver extends BeanELResolver {
    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory
            .getLog(TilesContextBeanELResolver.class);

    /**
     * The descriptors of {@link TilesRequestContext} and
     * {@link TilesApplicationContext}.
     *
     * @since 2.1.0
     */
    private List<FeatureDescriptor> descriptors;

    /**
     * Constructor.
     */
    public TilesContextELResolver() {
        List<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();
        collectBeanInfo(TilesRequestContext.class, list);
        collectBeanInfo(TilesApplicationContext.class, list);
    }

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

        return descriptors.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        // only resolve at the root of the context
        if (base != null) {
            return null;
        }

        TilesRequestContext request = (TilesRequestContext) context
                .getContext(TilesRequestContext.class);

        Class<?> retValue = super.getType(context, request, property);
        if (retValue == null) {
            TilesApplicationContext applicationContext = (TilesApplicationContext) context
                    .getContext(TilesApplicationContext.class);
            retValue = super.getType(context, applicationContext, property);
        }
        context.setPropertyResolved(true);
        return retValue;
    }

    /** {@inheritDoc} */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        // only resolve at the root of the context
        if (base != null) {
            return null;
        }

        TilesRequestContext request = (TilesRequestContext) context
                .getContext(TilesRequestContext.class);

        Object retValue = super.getValue(context, request, property);
        if (retValue == null) {
            TilesApplicationContext applicationContext = (TilesApplicationContext) context
                    .getContext(TilesApplicationContext.class);
            retValue = super.getValue(context, applicationContext, property);
        }
        context.setPropertyResolved(true);
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

    /**
     * Collects bean infos from a class and filling a list.
     *
     * @param clazz The class to be inspected.
     * @param list The list to fill.
     */
    protected void collectBeanInfo(Class<?> clazz, List<FeatureDescriptor> list) {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(clazz);
        } catch (Exception ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cannot inspect class " + clazz, ex);
            }
        }
        if (info == null) {
            return;
        }
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            pd.setValue("type", pd.getPropertyType());
            pd.setValue("resolvableAtDesignTime", Boolean.TRUE);
            list.add(pd);
        }
    }
}
