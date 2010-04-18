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

import org.apache.tiles.request.collection.extractor.HasKeys;
import org.apache.tiles.request.util.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet request
 * name-value.</p>
 *
 * @version $Rev$ $Date$
 */

public class ParameterMap extends AbstractEnumerationMap<String> {

    /**
     * Constructor.
     *
     * @param request The request object to use.
     * @param response The response object to use.
     * @since 2.2.0
     */
    public ParameterMap(HasKeys<String> request) {
        super(request);
    }

    /** {@inheritDoc} */
    public void clear() {
        throw new UnsupportedOperationException();
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return (request.getValue(key(key)) != null);
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
        return new HeaderEntrySet();
    }


    /** {@inheritDoc} */
    public String get(Object key) {
        return (request.getValue(key(key)));
    }


    /** {@inheritDoc} */
    public boolean isEmpty() {
        return !request.getKeys().hasMoreElements();
    }


    /** {@inheritDoc} */
    public Set<String> keySet() {
        return new KeySet(request);
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
        return enumerationSize(request.getKeys());
    }


    /** {@inheritDoc} */
    public Collection<String> values() {
        return new HeaderValuesCollection();
    }


    class HeaderEntrySet implements Set<Map.Entry<String, String>> {

        @Override
        public boolean add(java.util.Map.Entry<String, String> e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(
                Collection<? extends java.util.Map.Entry<String, String>> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean contains(Object o) {
            return containsEntry((java.util.Map.Entry<String, String>) o);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean containsAll(Collection<?> c) {
            Collection<Map.Entry<String, String>> realCollection =
                (Collection<Map.Entry<String, String>>) c;
            for (Map.Entry<String, String> entry : realCollection) {
                if (!containsEntry(entry)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean isEmpty() {
            return ParameterMap.this.isEmpty();
        }

        @Override
        public Iterator<java.util.Map.Entry<String, String>> iterator() {
            return new HeaderEntrySetIterator();
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
            return ParameterMap.this.size();
        }

        @Override
        public Object[] toArray() {
            return toList().toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return toList().toArray(a);
        }

        private boolean containsEntry(Map.Entry<String, String> entry) {
            String storedValue = request.getValue(key(entry.getKey()));
            return storedValue != null && storedValue.equals(entry.getValue());
        }

        private List<Map.Entry<String, String>> toList() {
            List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String,String>>();
            Enumeration<String> names = request.getKeys();
            while (names.hasMoreElements()) {
                entries.add(extractNextEntry(names));
            }
            return entries;
        }

        private MapEntry<String, String> extractNextEntry(
                Enumeration<String> names) {
            String name = names.nextElement();
            return new MapEntry<String, String>(name, request.getValue(name),
                    false);
        }

        private class HeaderEntrySetIterator implements Iterator<Map.Entry<String, String>> {

            private Enumeration<String> namesEnumeration = request.getKeys();

            @Override
            public boolean hasNext() {
                return namesEnumeration.hasMoreElements();
            }

            @Override
            public java.util.Map.Entry<String, String> next() {
                return extractNextEntry(namesEnumeration);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        }
    }

    private class HeaderValuesCollection implements Collection<String> {

        @Override
        public boolean add(String e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends String> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
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
            return ParameterMap.this.isEmpty();
        }

        @Override
        public Iterator<String> iterator() {
            return new HeaderValuesCollectionIterator();
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
            return ParameterMap.this.size();
        }

        @Override
        public Object[] toArray() {
            return toList().toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return toList().toArray(a);
        }

        private List<String> toList() {
            List<String> entries = new ArrayList<String>();
            Enumeration<String> names = request.getKeys();
            while (names.hasMoreElements()) {
                entries.add(request.getValue(names.nextElement()));
            }
            return entries;
        }


        private class HeaderValuesCollectionIterator implements Iterator<String> {

            private Enumeration<String> namesEnumeration = request.getKeys();

            @Override
            public boolean hasNext() {
                return namesEnumeration.hasMoreElements();
            }

            @Override
            public String next() {
                return request.getValue(namesEnumeration.nextElement());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
