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

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * <p>Private implementation of <code>Map</code> for servlet request
 * name-values[].</p>
 *
 * @version $Rev$ $Date$
 */

final class ServletHeaderValuesMap implements Map<String, String[]> {


    public ServletHeaderValuesMap(HttpServletRequest request) {
        this.request = request;
    }


    private HttpServletRequest request = null;


    public void clear() {
        throw new UnsupportedOperationException();
    }


    public boolean containsKey(Object key) {
        return (request.getHeader(key(key)) != null);
    }


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


    @SuppressWarnings("unchecked")
    public Set<Map.Entry<String, String[]>> entrySet() {
        Set<Map.Entry<String, String[]>> set = new HashSet<Map.Entry<String, String[]>>();
        Enumeration<String> keys = request.getHeaderNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            Enumeration<String> headerEnum = request.getHeaders(key);
            set.add(new MapEntry<String, String[]>(key,
                    enumeration2array(headerEnum), false));
        }
        return (set);
    }


    public boolean equals(Object o) {
        return (request.equals(o));
    }


    @SuppressWarnings("unchecked")
	public String[] get(Object key) {
        List<String> list = new ArrayList<String>();
        Enumeration<String> values = request.getHeaders(key(key));
        while (values.hasMoreElements()) {
            list.add(values.nextElement());
        }
        return ((list.toArray(new String[list.size()])));
    }


    public int hashCode() {
        return (request.hashCode());
    }


    public boolean isEmpty() {
        return (size() < 1);
    }


    @SuppressWarnings("unchecked")
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }


    public String[] put(String key, String[] value) {
        throw new UnsupportedOperationException();
    }


    public void putAll(Map<? extends String, ? extends String[]> map) {
        throw new UnsupportedOperationException();
    }


    public String[] remove(Object key) {
        throw new UnsupportedOperationException();
    }



    @SuppressWarnings("unchecked")
    public int size() {
        int n = 0;
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }


    @SuppressWarnings("unchecked")
	public Collection<String[]> values() {
        List<String[]> list = new ArrayList<String[]>();
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Enumeration<String> values = request.getHeaders(key);
            list.add(enumeration2array(values));
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

    private String[] enumeration2array(Enumeration<String> enumeration) {
        List<String> list1 = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            list1.add(enumeration.nextElement());
        }
        
        return list1.toArray(new String[list1.size()]);
    }
}