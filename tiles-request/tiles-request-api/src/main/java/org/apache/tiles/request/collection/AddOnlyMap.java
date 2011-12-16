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
package org.apache.tiles.request.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.attribute.Addable;

/**
 * Exposes an {@link Addable} object as a put-only (no remove, no get) map.
 * This map will appear empty to anyone trying to fetch its content.
 *
 * @version $Rev$ $Date$
 * @since 3.0.0
 * @param <V> The type of the value of the attribute.
 */
public class AddOnlyMap<V> implements Map<String, V> {
    /** The request. */
    private Addable<V> request;

    /**
     * Constructor.
     *
     * @param request
     *            The request object to use.
     */
    public AddOnlyMap(Addable<V> request) {
        this.request = request;
    }

    /** {@inheritDoc} */
    public int size() {
        return 0;
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return true;
    }

    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return false;
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        return false;
    }

    /** {@inheritDoc} */
    public V get(Object key) {
        return null;
    }

    /** {@inheritDoc} */
    public V put(String key, V value) {
        request.setValue(key, value);
        return null;
    }

    /** {@inheritDoc} */
    public V remove(Object key) {
        return null;
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends V> map) {
        for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
            request.setValue(entry.getKey(), entry.getValue());
        }
    }

    /** {@inheritDoc} */
    public void clear() {
    }

    /** {@inheritDoc} */
    public Set<String> keySet() {
        return Collections.<String> emptySet();
    }

    /** {@inheritDoc} */
    public Collection<V> values() {
        return Collections.<V> emptySet();
    }

    /** {@inheritDoc} */
    public Set<java.util.Map.Entry<String, V>> entrySet() {
        return new AddOnlyEntrySet();
    }

    /**
     * Entry set implementation for {@link AddableParameterMap}.
     */
    private class AddOnlyEntrySet implements Set<Map.Entry<String, V>> {

        @Override
        public boolean add(java.util.Map.Entry<String, V> e) {
            request.setValue(e.getKey(), e.getValue());
            return true;
        }

        @Override
        public boolean addAll(
                Collection<? extends java.util.Map.Entry<String, V>> c) {
            for (Map.Entry<String, V> entry : c) {
                request.setValue(entry.getKey(), entry.getValue());
            }
            return true;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<java.util.Map.Entry<String, V>> iterator() {
            return Collections.<java.util.Map.Entry<String, V>>emptySet().iterator();
        }

        @Override
        public Object[] toArray() {
            return new java.util.Map.Entry[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return Collections.<java.util.Map.Entry<String, V>>emptySet().toArray(a);
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {
        }
    }
}
