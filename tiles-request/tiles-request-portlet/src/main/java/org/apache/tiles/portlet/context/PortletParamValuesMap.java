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

import javax.portlet.PortletRequest;

import org.apache.tiles.request.util.MapEntry;


/**
 * <p>Private implementation of <code>Map</code> for portlet parameter
 * name-values[].</p>
 *
 * @version $Rev$ $Date$
 */

final class PortletParamValuesMap implements Map<String, String[]> {


    /**
     * Constructor.
     *
     * @param request The portlet request to use.
     */
    public PortletParamValuesMap(PortletRequest request) {
        this.request = request;
    }


    /**
     * The portlet request to use.
     */
    private PortletRequest request = null;


    /** {@inheritDoc} */
    public void clear() {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return (request.getParameter(key(key)) != null);
    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        if (!(value instanceof String[])) {
            return (false);
        }
        String[] test = (String[]) value;
        Iterator<String[]> values = values().iterator();
        while (values.hasNext()) {
            String[] actual = values.next();
            if (test.length == actual.length) {
                boolean matched = true;
                for (int i = 0; i < test.length; i++) {
                    if (!test[i].equals(actual[i])) {
                        matched = false;
                        break;
                    }
                }
                if (matched) {
                    return (true);
                }
            }
        }
        return (false);
    }


    /** {@inheritDoc} */
    public Set<Map.Entry<String, String[]>> entrySet() {
        Set<Map.Entry<String, String[]>> set = new HashSet<Map.Entry<String, String[]>>();
        Enumeration<String> keys = request.getParameterNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            set.add(new MapEntry<String, String[]>(key, (request
                    .getParameterValues(key)), false));
        }
        return (set);
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        PortletRequest otherRequest = ((PortletParamValuesMap) o).request;
        boolean retValue = true;
        synchronized (request) {
            for (Enumeration<String> attribs = request.getParameterNames(); attribs
                    .hasMoreElements()
                    && retValue;) {
                String parameterName = attribs.nextElement();
                retValue = request.getParameterValues(parameterName).equals(
                        otherRequest.getParameterValues(parameterName));
            }
        }

        return retValue;
    }


    /** {@inheritDoc} */
    public String[] get(Object key) {
        return (request.getParameterValues(key(key)));
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return (request.hashCode());
    }


    /** {@inheritDoc} */
    public boolean isEmpty() {
        return (size() < 1);
    }


    /** {@inheritDoc} */
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }


    /** {@inheritDoc} */
    public String[] put(String key, String[] value) {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends String[]> map) {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public String[] remove(Object key) {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public int size() {
        int n = 0;
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }


    /** {@inheritDoc} */
    public Collection<String[]> values() {
        List<String[]> list = new ArrayList<String[]>();
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            list.add(request.getParameterValues(keys.nextElement()));
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
