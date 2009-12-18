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
package org.apache.tiles.request.jsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.util.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet request
 * attributes.</p>
 *
 * @version $Rev$ $Date$
 */

final class JspSessionScopeMap implements Map<String, Object> {

    /**
     * The request object to use.
     */
    private PageContext request = null;

    private static final int scopeId = PageContext.SESSION_SCOPE;

    /**
     * Constructor.
     *
     * @param request The request object to use.
     */
    public JspSessionScopeMap(PageContext pageContext) {
        this.request = pageContext;
    }


    /** {@inheritDoc} */
    public void clear() {
    	if (request.getSession() == null) {
    		return;
    	}
        Iterator<String> keys = keySet().iterator();
        while (keys.hasNext()) {
            request.removeAttribute(keys.next(), scopeId);
        }
    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
    	if (request.getSession() == null) {
    		return false;
    	}
        return (request.getAttribute(key(key), scopeId) != null);
    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
    	if (request.getSession() == null) {
    		return false;
    	}
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
    	if (request.getSession() != null) {
	        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
	        String key;
	        while (keys.hasMoreElements()) {
	            key = keys.nextElement();
	            set.add(new MapEntry<String, Object>(key,
	                    request.getAttribute(key, scopeId), true));
	        }
    	}
        return (set);
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        PageContext otherRequest = ((JspSessionScopeMap) o).request;
        if (otherRequest.getSession() == null) {
        	return request.getSession() == null;
        } else if (request.getSession() == null) {
        	return false;
        }
        boolean retValue = true;
        for (Enumeration<String> attribs = request
                .getAttributeNamesInScope(scopeId); attribs
                .hasMoreElements()
                && retValue;) {
            String attributeName = attribs.nextElement();
            retValue = request.getAttribute(attributeName, scopeId).equals(
                    otherRequest.getAttribute(attributeName));
        }

        return retValue;
    }


    /** {@inheritDoc} */
    public Object get(Object key) {
    	if (request.getSession() == null) {
    		return null;
    	}
        return (request.getAttribute(key(key), scopeId));
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
    	if (request.getSession() == null) {
    		return 0;
    	}
        return (request.hashCode()) + scopeId;
    }


    /** {@inheritDoc} */
    public boolean isEmpty() {
    	if (request.getSession() == null) {
    		return true;
    	}
        return (size() < 1);
    }


    /** {@inheritDoc} */
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
    	if (request.getSession() != null) {
	        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
	        while (keys.hasMoreElements()) {
	            set.add(keys.nextElement());
	        }
    	}
        return (set);
    }


    /** {@inheritDoc} */
    public Object put(String key, Object value) {
    	forceSessionCreation();
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
    	forceSessionCreation();
        Iterator<? extends String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            request.setAttribute(key, map.get(key), scopeId);
        }
    }


    /** {@inheritDoc} */
    public Object remove(Object key) {
    	if (request.getSession() == null) {
    		return null;
    	}
        String skey = key(key);
        Object previous = request.getAttribute(skey, scopeId);
        request.removeAttribute(skey, scopeId);
        return (previous);
    }


    /** {@inheritDoc} */
    public int size() {
        int n = 0;
    	if (request.getSession() != null) {
	        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
	        while (keys.hasMoreElements()) {
	            keys.nextElement();
	            n++;
	        }
    	}
        return (n);
    }


    /** {@inheritDoc} */
    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        if (request.getSession() != null) {
	        Enumeration<String> keys = request.getAttributeNamesInScope(scopeId);
	        while (keys.hasMoreElements()) {
	            list.add(request.getAttribute(keys.nextElement()));
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


	private void forceSessionCreation() {
		ServletRequest servletRequest = request.getRequest();
    	if (servletRequest instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
			httpRequest.getSession();
		}
	}
}
