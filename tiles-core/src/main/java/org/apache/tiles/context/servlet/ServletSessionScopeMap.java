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
 *
 */
package org.apache.tiles.context.servlet;


import org.apache.tiles.context.MapEntry;

import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * <p>Private implementation of <code>Map</code> for HTTP session
 * attributes.</p>
 *
 * @version $Rev$ $Date$
 */

final class ServletSessionScopeMap implements Map<String, Object> {


    public ServletSessionScopeMap(HttpSession session) {
        this.session = session;
    }


    private HttpSession session = null;


    public void clear() {
        Iterator<String> keys = keySet().iterator();
        while (keys.hasNext()) {
            session.removeAttribute(keys.next());
        }
    }


    public boolean containsKey(Object key) {
        return (session.getAttribute(key(key)) != null);
    }


    @SuppressWarnings("unchecked")
    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Enumeration<String> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = session.getAttribute(keys.nextElement());
            if (next == value) {
                return (true);
            }
        }
        return (false);
    }


    @SuppressWarnings("unchecked")
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> set = new HashSet<Map.Entry<String, Object>>();
        Enumeration<String> keys = session.getAttributeNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            set.add(new MapEntry<String, Object>(key,
                    session.getAttribute(key), true));
        }
        return (set);
    }


    public boolean equals(Object o) {
        return (session.equals(o));
    }


    public Object get(Object key) {
        return (session.getAttribute(key(key)));
    }


    public int hashCode() {
        return (session.hashCode());
    }


    public boolean isEmpty() {
        return (size() < 1);
    }


    @SuppressWarnings("unchecked")
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        Enumeration<String> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }


    public Object put(String key, Object value) {
        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = session.getAttribute(skey);
        session.setAttribute(skey, value);
        return (previous);
    }


    public void putAll(Map<? extends String, ? extends Object> map) {
        Iterator<? extends String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            session.setAttribute(key, map.get(key));
        }
    }


    public Object remove(Object key) {
        String skey = key(key);
        Object previous = session.getAttribute(skey);
        session.removeAttribute(skey);
        return (previous);
    }


    @SuppressWarnings("unchecked")
    public int size() {
        int n = 0;
        Enumeration<String> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }


    @SuppressWarnings("unchecked")
    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        Enumeration<String> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(session.getAttribute(keys.nextElement()));
        }
        return (list);
    }


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