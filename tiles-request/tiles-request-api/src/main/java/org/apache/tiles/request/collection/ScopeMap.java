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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
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

public class ScopeMap extends ReadOnlyEnumerationMap<Object> {

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
    public Set<Map.Entry<String, Object>> entrySet() {
        return new ScopeEntrySet();
    }

    /** {@inheritDoc} */
    public Set<String> keySet() {
        return new RemovableKeySet(context);
    }

    /** {@inheritDoc} */
    public Object put(String key, Object value) {
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

    private class ScopeEntrySet extends ReadOnlyEnumerationMap<Object>.ReadOnlyEnumerationMapEntrySet {

        @Override
        public boolean add(java.util.Map.Entry<String, Object> e) {
            String key = e.getKey();
            Object value = e.getValue();
            Object oldValue = get(key);
            if (oldValue == null || !oldValue.equals(value)) {
                context.setValue(key, value);
                return true;
            }
            return false;
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
        public boolean remove(Object o) {
            Map.Entry<String, Object> entry = (java.util.Map.Entry<String, Object>) o;
            String key = entry.getKey();
            Object currentValue = context.getValue(key);
            if (currentValue != null && currentValue.equals(entry.getValue())) {
                context.removeValue(key);
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
            for (Enumeration<String> keys = context.getKeys(); keys.hasMoreElements(); ) {
                String key = keys.nextElement();
                Object value = context.getValue(key);
                Map.Entry<String, Object> entry = new MapEntry<String, Object>(key, value, false);
                if (!realCollection.contains(entry)) {
                    retValue = true;
                    context.removeValue(key);
                }
            }
            return retValue;
        }
    }
}
