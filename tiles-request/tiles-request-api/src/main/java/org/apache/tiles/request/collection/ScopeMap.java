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

import static org.apache.tiles.request.util.RequestUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.collection.extractor.AttributeExtractor;
import org.apache.tiles.request.util.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet context
 * attributes.</p>
 *
 * @version $Rev$ $Date$
 */

public class ScopeMap extends AbstractEnumerationMap<Object> {

    private AttributeExtractor context;

    /**
     * Constructor.
     *
     * @param context The servlet context to use.
     */
    public ScopeMap(AttributeExtractor context) {
        super(context);
        this.context = context;
    }


    /** {@inheritDoc} */
    public void clear() {
        Enumeration<String> keys = context.getKeys();
        while (keys.hasMoreElements()) {
            context.removeValue(keys.nextElement());
        }
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return context.getValue(key(key)) != null;
    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Enumeration<String> keys = context.getKeys();
        while (keys.hasMoreElements()) {
            Object next = context.getValue(keys.nextElement());
            if (next == value) {
                return true;
            }
        }
        return false;
    }


    /** {@inheritDoc} */
    public Set<Map.Entry<String, Object>> entrySet() {
        return new ServletApplicationScopeEntrySet();
    }



    /** {@inheritDoc} */
    public Object get(Object key) {
        return context.getValue(key(key));
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return !context.getKeys().hasMoreElements();
    }


    /** {@inheritDoc} */
    public Set<String> keySet() {
        return new RemovableKeySet(context);
    }


    /** {@inheritDoc} */
    public Object put(String key, Object value) {
        if (value == null) {
            return remove(key);
        }
        String skey = key(key);
        Object previous = context.getValue(skey);
        context.setValue(skey, value);
        return previous;
    }


    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends Object> map) {
        Iterator<? extends String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            context.setValue(key, map.get(key));
        }
    }


    /** {@inheritDoc} */
    public Object remove(Object key) {
        String skey = key(key);
        Object previous = context.getValue(skey);
        context.removeValue(skey);
        return (previous);
    }


    /** {@inheritDoc} */
    public int size() {
        return enumerationSize(context.getKeys());
    }


    /** {@inheritDoc} */
    public Collection<Object> values() {
        return new ServletApplicationScopeValuesCollection();
    }

    private class ServletApplicationScopeEntrySet implements Set<Map.Entry<String, Object>> {

        @Override
        public boolean add(java.util.Map.Entry<String, Object> e) {
            Object oldValue = get(e.getKey());
            put(e.getKey(), e.getValue());
            return oldValue == null || !oldValue.equals(e.getValue());
        }

        @Override
        public boolean addAll(
                Collection<? extends java.util.Map.Entry<String, Object>> c) {
            boolean retValue = false;
            for (Map.Entry<String, Object> entry : c) {
                retValue |= add(entry);
            }
            return retValue;
        }

        @Override
        public void clear() {
            ScopeMap.this.clear();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean contains(Object o) {
            return containsEntry((java.util.Map.Entry<String, Object>) o);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean containsAll(Collection<?> c) {
            Collection<Map.Entry<String, Object>> realCollection =
                (Collection<Map.Entry<String, Object>>) c;
            for (Map.Entry<String, Object> entry : realCollection) {
                if (!containsEntry(entry)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean isEmpty() {
            return ScopeMap.this.isEmpty();
        }

        @Override
        public Iterator<java.util.Map.Entry<String, Object>> iterator() {
            return new ServletApplicationScopeEntrySetIterator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean remove(Object o) {
            Map.Entry<String, Object> entry = (java.util.Map.Entry<String, Object>) o;
            Object currentValue = get(entry.getKey());
            if (currentValue != null && currentValue.equals(entry.getValue())) {
                context.removeValue(entry.getKey());
                return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean removeAll(Collection<?> c) {
            Collection<Map.Entry<String, Object>> realCollection = (Collection<java.util.Map.Entry<String, Object>>) c;
            boolean retValue = false;
            for (Map.Entry<String, Object> entry : realCollection) {
                retValue |= remove(entry);
            }
            return retValue;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean retainAll(Collection<?> c) {
            Collection<Map.Entry<String, Object>> realCollection = (Collection<java.util.Map.Entry<String, Object>>) c;
            boolean retValue = false;
            for (Map.Entry<String, Object> entry : realCollection) {
                if (containsEntry(entry)) {
                    retValue |= remove(entry);
                }
            }
            return retValue;
        }

        @Override
        public int size() {
            return ScopeMap.this.size();
        }

        @Override
        public Object[] toArray() {
            return toList().toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return toList().toArray(a);
        }

        private boolean containsEntry(Map.Entry<String, Object> entry) {
            Object storedValue = context.getValue(key(entry.getKey()));
            return storedValue != null && storedValue.equals(entry.getValue());
        }

        private List<Map.Entry<String, Object>> toList() {
            List<Map.Entry<String, Object>> entries = new ArrayList<Map.Entry<String,Object>>();
            Enumeration<String> names = context.getKeys();
            while (names.hasMoreElements()) {
                entries.add(extractNextEntry(names));
            }
            return entries;
        }

        private MapEntry<String, Object> extractNextEntry(
                Enumeration<String> names) {
            String name = names.nextElement();
            return new MapEntry<String, Object>(name, context.getValue(name),
                    false);
        }

        private class ServletApplicationScopeEntrySetIterator implements Iterator<Map.Entry<String, Object>> {

            private Enumeration<String> namesEnumeration = context.getKeys();

            @Override
            public boolean hasNext() {
                return namesEnumeration.hasMoreElements();
            }

            @Override
            public java.util.Map.Entry<String, Object> next() {
                return extractNextEntry(namesEnumeration);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        }
    }

    private class ServletApplicationScopeValuesCollection implements Collection<Object> {

        @Override
        public boolean add(Object e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends Object> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            ScopeMap.this.clear();
        }

        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean containsAll(Collection<?> c) {
            Collection<String> realCollection = (Collection<String>) c;
            for (String value : realCollection) {
                if (!containsValue(value)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean isEmpty() {
            return ScopeMap.this.isEmpty();
        }

        @Override
        public Iterator<Object> iterator() {
            return new ServletApplicationScopeValuesCollectionIterator();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return ScopeMap.this.size();
        }

        @Override
        public Object[] toArray() {
            return toList().toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return toList().toArray(a);
        }

        private List<Object> toList() {
            List<Object> entries = new ArrayList<Object>();
            Enumeration<String> names = context.getKeys();
            while (names.hasMoreElements()) {
                entries.add(context.getValue(names.nextElement()));
            }
            return entries;
        }

        private class ServletApplicationScopeValuesCollectionIterator implements Iterator<Object> {

            private Enumeration<String> namesEnumeration = context.getKeys();

            @Override
            public boolean hasNext() {
                return namesEnumeration.hasMoreElements();
            }

            @Override
            public Object next() {
                return context.getValue(namesEnumeration.nextElement());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
