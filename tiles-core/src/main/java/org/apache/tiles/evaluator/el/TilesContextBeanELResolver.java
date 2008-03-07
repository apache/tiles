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
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;

/**
 * Resolves beans in request, session and application scope.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class TilesContextBeanELResolver extends ELResolver {

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory
            .getLog(TilesContextBeanELResolver.class);

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
        List<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();

        TilesRequestContext request = (TilesRequestContext) context
                .getContext(TilesRequestContext.class);
        collectBeanInfo(request.getRequestScope(), list);
        collectBeanInfo(request.getSessionScope(), list);

        TilesApplicationContext applicationContext = (TilesApplicationContext) context
                .getContext(TilesApplicationContext.class);
        collectBeanInfo(applicationContext.getApplicationScope(), list);
        return list.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (base != null) {
            return null;
        }

        Object obj = findObjectByProperty(context, property);
        if (obj != null) {
            return obj.getClass();
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (base != null) {
            return null;
        }

        return findObjectByProperty(context, property);
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
     * Collects bean infos from a map's values and filling a list.
     *
     * @param map The map containing the bean to be inspected.
     * @param list The list to fill.
     */
    protected void collectBeanInfo(Map<String, ? extends Object> map,
            List<FeatureDescriptor> list) {
        if (map == null || map.isEmpty()) {
            return;
        }

        for (Object bean : map.values()) {
            BeanInfo info = null;
            try {
                info = Introspector.getBeanInfo(bean.getClass());
            } catch (Exception ex) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Cannot inspect bean " + bean.getClass(), ex);
                }
            }
            if (info == null) {
                return;
            }
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                pd.setValue("type", pd.getPropertyType());
                pd.setValue("resolvableAtDesignTime", Boolean.FALSE);
                list.add(pd);
            }
        }
    }

    /**
     * Finds an object in request, session or application scope, in this order.
     *
     * @param context The context to use.
     * @param property The property used as an attribute name.
     * @return The found bean, if it exists, or <code>null</code> otherwise.
     */
    protected Object findObjectByProperty(ELContext context, Object property) {
        Object retValue = null;

        TilesRequestContext request = (TilesRequestContext) context
                .getContext(TilesRequestContext.class);

        String prop = property.toString();

        retValue = getObject(request.getRequestScope(), prop);
        if (retValue == null) {
            retValue = getObject(request.getSessionScope(), prop);
            if (retValue == null) {
                TilesApplicationContext applicationContext = (TilesApplicationContext) context
                        .getContext(TilesApplicationContext.class);
                retValue = getObject(applicationContext.getApplicationScope(),
                        prop);
            }
        }

        return retValue;
    }

    /**
     * Returns an object from a map in a null-safe manner.
     *
     * @param map The map to use.
     * @param property The property to use as a key.
     * @return The object, if present, or <code>null</code> otherwise.
     */
    protected Object getObject(Map<String, ? extends Object> map,
            String property) {
        Object retValue = null;
        if (map != null) {
            retValue = map.get(property);
        }
        return retValue;
    }
}
