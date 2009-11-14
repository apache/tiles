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
 * init parameters.</p>
 *
 * @version $Rev$ $Date$
 */

final class PortletInitParamMap implements Map<String, String> {


    /**
     * Constructor.
     *
     * @param context The portlet context to use.
     */
    public PortletInitParamMap(PortletContext context) {
        this.context = context;
    }


    /**
     * The portlet context to use.
     */
    private PortletContext context = null;


    /** {@inheritDoc} */
    public void clear() {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return (context.getInitParameter(key(key)) != null);
    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        Iterator<String> values = values().iterator();
        while (values.hasNext()) {
            if (value.equals(values.next())) {
                return (true);
            }
        }
        return (false);
    }


    /** {@inheritDoc} */
    public Set<Map.Entry<String, String>> entrySet() {
        Set<Map.Entry<String, String>> set = new HashSet<Map.Entry<String, String>>();
        Enumeration<String> keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            set.add(new MapEntry<String, String>(key, context
                    .getInitParameter(key), false));
        }
        return (set);
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        PortletContext otherContext = ((PortletInitParamMap) o).context;
        boolean retValue = true;
        synchronized (context) {
            for (Enumeration<String> attribs = context.getInitParameterNames(); attribs
                    .hasMoreElements()
                    && retValue;) {
                String parameterName = attribs.nextElement();
                retValue = context.getInitParameter(parameterName).equals(
                        otherContext.getInitParameter(parameterName));
            }
        }

        return retValue;
    }


    /** {@inheritDoc} */
    public String get(Object key) {
        return (context.getInitParameter(key(key)));
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
        Enumeration<String> keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }


    /** {@inheritDoc} */
    public String put(String key, String value) {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends String> map) {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public String remove(Object key) {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public int size() {
        int n = 0;
        Enumeration<String> keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }


    /** {@inheritDoc} */
    public Collection<String> values() {
        List<String> list = new ArrayList<String>();
        Enumeration<String> keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            list.add(context.getInitParameter(keys.nextElement()));
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
