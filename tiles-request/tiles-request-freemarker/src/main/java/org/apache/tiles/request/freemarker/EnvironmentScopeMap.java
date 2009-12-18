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
package org.apache.tiles.request.freemarker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.util.MapEntry;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * <p>
 * Private implementation of <code>Map</code> for servlet request attributes.
 * </p>
 *
 * @version $Rev$ $Date$
 */

final class EnvironmentScopeMap implements Map<String, Object> {

    /**
     * The request object to use.
     */
    private Environment request = null;

    /**
     * Constructor.
     *
     * @param request The request object to use.
     */
    public EnvironmentScopeMap(Environment request) {
        this.request = request;
    }

    /** {@inheritDoc} */
    public void clear() {
        Iterator<String> keys = keySet().iterator();
        while (keys.hasNext()) {
            request.setVariable(keys.next(), null);
        }
    }

    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return getAttribute(key(key)) != null;
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Set<String> keys;
        keys = getAttributeNames();
        for (String name : keys) {
            Object next = getAttribute(name);
            if (next == value) {
                return (true);
            }
        }
        return (false);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> set = new HashSet<Map.Entry<String, Object>>();
        Set<String> keys;
        keys = getAttributeNames();
        for (String name : keys) {
            Object next = getAttribute(name);
            set.add(new MapEntry(name, next, false));
        }
        return (set);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        Environment otherRequest = ((EnvironmentScopeMap) o).request;
        boolean retValue = true;
        Set<String> keys;
        try {
            keys = getAttributeNames();
            for (String name : keys) {
                Object next = getAttribute(name);
                TemplateModel otherVariable = otherRequest.getVariable(name);
                if (otherVariable != null) {
                    Object otherObj = DeepUnwrap.unwrap(otherVariable);
                    retValue = (next == null && otherObj == null)
                            || (next != null && next.equals(otherObj));
                }
            }
        } catch (TemplateModelException e) {
            throw new FreemarkerRequestException("Cannot get the entry set", e);
        }

        return retValue;
    }

    /** {@inheritDoc} */
    public Object get(Object key) {
        return getAttribute(key(key));
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
        return getAttributeNames();
    }

    /** {@inheritDoc} */
    public Object put(String key, Object value) {
        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = getAttribute(skey);
        setAttribute(skey, value);
        return (previous);
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends Object> map) {
        Iterator<? extends String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            setAttribute(key, map.get(key));
        }
    }

    /** {@inheritDoc} */
    public Object remove(Object key) {
        String skey = key(key);
        Object previous = getAttribute(skey);
        setAttribute(skey, null);
        return (previous);
    }

    /** {@inheritDoc} */
    public int size() {
        return getAttributeNames().size();
    }

    /** {@inheritDoc} */
    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        for(String name : getAttributeNames()) {
            list.add(getAttribute(name));
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

    private Object getAttribute(String name) {
        try {
            TemplateModel variable = request.getVariable(name);
            if (variable != null) {
                return DeepUnwrap.unwrap(variable);
            }
            return null;
        } catch (TemplateModelException e) {
            throw new FreemarkerRequestException(
                    "Cannot get attribute with name '" + name + "'", e);
        }
    }

    private void setAttribute(String name, Object value) {
        try {
            TemplateModel model = request.getObjectWrapper().wrap(value);
            request.setVariable(name, model);
        } catch (TemplateModelException e) {
            throw new FreemarkerRequestException(
                    "Error when wrapping an object setting the '" + name
                            + "' attribute", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> getAttributeNames() {
        try {
            return request.getKnownVariableNames();
        } catch (TemplateModelException e) {
            throw new FreemarkerRequestException(
                    "Cannot get variable names from request", e);
        }
    }
}
