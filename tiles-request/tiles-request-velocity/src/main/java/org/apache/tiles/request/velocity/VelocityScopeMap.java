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
package org.apache.tiles.request.velocity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.util.MapEntry;
import org.apache.velocity.context.Context;

/**
 * <p>Private implementation of <code>Map</code> for servlet request
 * attributes.</p>
 *
 * @version $Rev$ $Date$
 */

final class VelocityScopeMap implements Map<String, Object> {


    /**
     * The request object to use.
     */
    private Context request = null;


    /**
     * Constructor.
     *
     * @param request The request object to use.
     */
    public VelocityScopeMap(Context request) {
        this.request = request;
    }


    /** {@inheritDoc} */
    public void clear() {
        Object[] keys = request.getKeys();
        for (Object key: keys) {
            request.remove(key);
        }
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return request.containsKey(key);
    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Object[] keys = request.getKeys();
        for (Object key : keys) {
            Object next = request.get((String) key);
            if (next == value) {
                return (true);
            }
        }
        return (false);
    }


    /** {@inheritDoc} */
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> set = new HashSet<Map.Entry<String, Object>>();
        Object[] keys = request.getKeys();
        for (Object key : keys) {
            set.add(new MapEntry<String, Object>((String) key,
                    request.get((String) key), true));
        }
        return (set);
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        Context otherRequest = ((VelocityScopeMap) o).request;
        boolean retValue = true;
        synchronized (request) {
            for (Object key : request.getKeys()) {
                String attributeName = (String) key;
                retValue = request.get(attributeName).equals(
                        otherRequest.get(attributeName));
            }
        }

        return retValue;
    }


    /** {@inheritDoc} */
    public Object get(Object key) {
        return (request.get(key(key)));
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
        for (Object key : request.getKeys()) {
            set.add((String) key);
        }
        return (set);
    }


    /** {@inheritDoc} */
    public Object put(String key, Object value) {
        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = request.get(skey);
        request.put(skey, value);
        return (previous);
    }


    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends Object> map) {
        Iterator<? extends String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            request.put(key, map.get(key));
        }
    }


    /** {@inheritDoc} */
    public Object remove(Object key) {
        String skey = key(key);
        Object previous = request.get(skey);
        request.remove(skey);
        return (previous);
    }


    /** {@inheritDoc} */
    public int size() {
        return request.getKeys().length;
    }


    /** {@inheritDoc} */
    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        for (Object key : request.getKeys()) {
            list.add(request.get((String) key));
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
