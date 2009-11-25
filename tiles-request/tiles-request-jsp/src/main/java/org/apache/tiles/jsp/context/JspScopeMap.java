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
package org.apache.tiles.jsp.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.util.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet request
 * attributes.</p>
 *
 * @version $Rev$ $Date$
 */

final class JspScopeMap implements Map<String, Object> {

    /**
     * The request object to use.
     */
    private PageContext request = null;

    private int scopeId;

    /**
     * Constructor.
     *
     * @param request The request object to use.
     */
    public JspScopeMap(PageContext pageContext, int scopeId) {
        this.request = pageContext;
        this.scopeId = scopeId;
    }


    /** {@inheritDoc} */
    public void clear() {
        Iterator<String> keys = keySet().iterator();
        while (keys.hasNext()) {
            request.removeAttribute(keys.next(), scopeId);
        }
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return (request.getAttribute(key(key), scopeId) != null);
    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
        while (keys.hasMoreElements()) {
            Object next = request.getAttribute(keys.nextElement(), scopeId);
            if (next == value) {
                return (true);
            }
        }
        return (false);
    }


    /** {@inheritDoc} */
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> set = new HashSet<Map.Entry<String, Object>>();
        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            set.add(new MapEntry<String, Object>(key,
                    request.getAttribute(key, scopeId), true));
        }
        return (set);
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        PageContext otherRequest = ((JspScopeMap) o).request;
        boolean retValue = true;
        synchronized (request) {
            for (Enumeration<String> attribs = request
                    .getAttributeNamesInScope(scopeId); attribs
                    .hasMoreElements()
                    && retValue;) {
                String attributeName = attribs.nextElement();
                retValue = request.getAttribute(attributeName, scopeId).equals(
                        otherRequest.getAttribute(attributeName));
            }
        }

        return retValue;
    }


    /** {@inheritDoc} */
    public Object get(Object key) {
        return (request.getAttribute(key(key), scopeId));
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return (request.hashCode()) + scopeId;
    }


    /** {@inheritDoc} */
    public boolean isEmpty() {
        return (size() < 1);
    }


    /** {@inheritDoc} */
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
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
        Object previous = request.getAttribute(skey, scopeId);
        request.setAttribute(skey, value, scopeId);
        return (previous);
    }


    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends Object> map) {
        Iterator<? extends String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            request.setAttribute(key, map.get(key), scopeId);
        }
    }


    /** {@inheritDoc} */
    public Object remove(Object key) {
        String skey = key(key);
        Object previous = request.getAttribute(skey, scopeId);
        request.removeAttribute(skey, scopeId);
        return (previous);
    }


    /** {@inheritDoc} */
    public int size() {
        int n = 0;
        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }


    /** {@inheritDoc} */
    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
        while (keys.hasMoreElements()) {
            list.add(request.getAttribute(keys.nextElement()));
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
