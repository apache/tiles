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
package org.apache.tiles.request.portlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.tiles.request.util.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for HTTP session
 * attributes.</p>
 *
 * @version $Rev$ $Date$
 */

final class PortletSessionScopeMap implements Map<String, Object> {

    /**
     * The request object to use.
     */
    private PortletRequest request;

    private int scopeId;

    /**
     * Constructor.
     *
     * @param request The request object to use.
     */
    public PortletSessionScopeMap(PortletRequest request, int scopeId) {
        this.request = request;
        this.scopeId = scopeId;
    }

    /** {@inheritDoc} */
    public void clear() {
        PortletSession session = request.getPortletSession(false);
        if (session != null) {
            Iterator<String> keys = keySet().iterator();
            while (keys.hasNext()) {
                session.removeAttribute(keys.next(), scopeId);
            }
        }
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        PortletSession session = request.getPortletSession(false);
        if (session == null) {
            return false;
        }

        return (session.getAttribute(key(key), scopeId) != null);
    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        PortletSession session = request.getPortletSession(false);
        if (session == null || value == null) {
            return (false);
        }
        Enumeration<String> keys = session.getAttributeNames(scopeId);
        while (keys.hasMoreElements()) {
            Object next = session.getAttribute(keys.nextElement(), scopeId);
            if (next == value) {
                return (true);
            }
        }
        return (false);
    }


    /** {@inheritDoc} */
    public Set<Map.Entry<String, Object>> entrySet() {
        PortletSession session = request.getPortletSession(false);
        Set<Map.Entry<String, Object>> set = new HashSet<Map.Entry<String, Object>>();
        if (session != null) {
            Enumeration<String> keys = session.getAttributeNames(scopeId);
            String key;
            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                set.add(new MapEntry<String, Object>(key,
                        session.getAttribute(key, scopeId), true));
            }
        }
        return (set);
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        boolean retValue = true;

        PortletSession session = request.getPortletSession(false);
        synchronized (session) {
            PortletSession otherSession = ((PortletSessionScopeMap) o).request
                    .getPortletSession(false);
            if (session == null) {
                retValue = otherSession == null;
            } else {
                for (Enumeration<String> attribs = session.getAttributeNames(scopeId); attribs
                        .hasMoreElements()
                        && retValue;) {
                    String attributeName = attribs.nextElement();
                    retValue = session.getAttribute(attributeName, scopeId).equals(
                            otherSession.getAttribute(attributeName));
                }
            }
        }

        return retValue;
    }


    /** {@inheritDoc} */
    public Object get(Object key) {
        PortletSession session = request.getPortletSession(false);
        if (session == null) {
            return null;
        }

        return (session.getAttribute(key(key), scopeId));
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        PortletSession session = request.getPortletSession(false);
        if (session == null) {
            return 0;
        }

        return (session.hashCode() + scopeId);
    }


    /** {@inheritDoc} */
    public boolean isEmpty() {
        return (size() < 1);
    }


    /** {@inheritDoc} */
    public Set<String> keySet() {
        PortletSession session = request.getPortletSession(false);
        Set<String> set = new HashSet<String>();
        if (session != null) {
            Enumeration<String> keys = session.getAttributeNames(scopeId);
            while (keys.hasMoreElements()) {
                set.add(keys.nextElement());
            }
        }
        return (set);
    }


    /** {@inheritDoc} */
    public Object put(String key, Object value) {
        PortletSession session = request.getPortletSession();
        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = session.getAttribute(skey, scopeId);
        session.setAttribute(skey, value, scopeId);
        return (previous);
    }


    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends Object> map) {
        PortletSession session = request.getPortletSession();
        Iterator<? extends String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            session.setAttribute(key, map.get(key), scopeId);
        }
    }


    /** {@inheritDoc} */
    public Object remove(Object key) {
        PortletSession session = request.getPortletSession(false);
        if (session == null) {
            return null;
        }

        String skey = key(key);
        Object previous = session.getAttribute(skey, scopeId);
        session.removeAttribute(skey, scopeId);
        return (previous);
    }


    /** {@inheritDoc} */
    public int size() {
        PortletSession session = request.getPortletSession(false);
        int n = 0;
        if (session != null) {
            Enumeration<String> keys = session.getAttributeNames(scopeId);
            while (keys.hasMoreElements()) {
                keys.nextElement();
                n++;
            }
        }
        return (n);
    }

    /** {@inheritDoc} */
    public Collection<Object> values() {
        PortletSession session = request.getPortletSession(false);
        List<Object> list = new ArrayList<Object>();
        if (session != null) {
            Enumeration<String> keys = session.getAttributeNames(scopeId);
            while (keys.hasMoreElements()) {
                list.add(session.getAttribute(keys.nextElement(), scopeId));
            }
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
