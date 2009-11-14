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
package org.apache.tiles.portlet.context;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletContext;

import org.apache.tiles.request.util.MapEntry;


/**
 * <p>Private implementation of <code>Map</code> for portlet context
 * attributes.</p>
 *
 * @version $Rev$ $Date$
 */

final class PortletApplicationScopeMap implements Map<String, Object> {


    /**
     * Constructor.
     *
     * @param context The portlet context to use.
     */
    public PortletApplicationScopeMap(PortletContext context) {
        this.context = context;
    }


    /**
     * The portlet context to use.
     */
    private PortletContext context = null;


    /** {@inheritDoc} */
    public void clear() {
        Iterator<String> keys = keySet().iterator();
        while (keys.hasNext()) {
            context.removeAttribute(keys.next());
        }
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return (context.getAttribute(key(key)) != null);
    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Enumeration<String> keys = context.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = context.getAttribute(keys.nextElement());
            if (next == value) {
                return (true);
            }
        }
        return (false);
    }


    /** {@inheritDoc} */
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> set = new HashSet<Map.Entry<String, Object>>();
        Enumeration<String> keys = context.getAttributeNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            set.add(new MapEntry<String, Object>(key, context.getAttribute(key), true));
        }
        return (set);
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        PortletContext otherContext = ((PortletApplicationScopeMap) o).context;
        boolean retValue = true;
        synchronized (context) {
            for (Enumeration<String> attribs = context.getAttributeNames(); attribs
                    .hasMoreElements()
                    && retValue;) {
                String parameterName = attribs.nextElement();
                retValue = context.getAttribute(parameterName).equals(
                        otherContext.getAttribute(parameterName));
            }
        }

        return retValue;
    }


    /** {@inheritDoc} */
    public Object get(Object key) {
        return (context.getAttribute(key(key)));
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return (context.hashCode());
    }


    /** {@inheritDoc} */
    public boolean isEmpty() {
        return (size() < 1);
    }


    /** {@inheritDoc} */
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        Enumeration<String> keys = context.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }


    /** {@inheritDoc} */
    public Object put(String key, Object value) {
        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = context.getAttribute(skey);
        context.setAttribute(skey, value);
        return (previous);
    }


    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends Object> map) {
        Iterator<? extends String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            context.setAttribute(key, map.get(key));
        }
    }


    /** {@inheritDoc} */
    public Object remove(Object key) {
        String skey = key(key);
        Object previous = context.getAttribute(skey);
        context.removeAttribute(skey);
        return (previous);
    }


    /** {@inheritDoc} */
    public int size() {
        int n = 0;
        Enumeration<String> keys = context.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }


    /** {@inheritDoc} */
    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        Enumeration<String> keys = context.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(context.getAttribute(keys.nextElement()));
        }
        return (list);
    }


    /**
     * Returns the string representation of the key.
     *
     * @param key The key.
     * @return The string representation of the key.
     * @throws IllegalArgumentException If the key is <code>null</code>.
     */
    private String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }
    }


}
