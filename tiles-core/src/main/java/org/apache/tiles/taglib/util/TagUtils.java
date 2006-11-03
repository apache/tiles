/*
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
package org.apache.tiles.taglib.util;

import org.apache.tiles.ComponentContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.taglib.ComponentConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection of utilities.
 * This class also serves as an interface between Components and Struts. If
 * you want to rip away Struts, simply reimplement some methods in this class.
 * You can copy them from Struts.
 */
public class TagUtils {

    /**
     * Debug flag
     */
    public static final boolean debug = true;

    /**
     * Maps lowercase JSP scope names to their PageContext integer constant
     * values.
     */
    private static final Map<String, Integer> scopes = new HashMap<String, Integer>();

    /**
     * Initialize the scope names map and the encode variable with the
     * Java 1.4 method if available.
     */
    static {
        scopes.put("page", PageContext.PAGE_SCOPE);
        scopes.put("request", PageContext.REQUEST_SCOPE);
        scopes.put("session", PageContext.SESSION_SCOPE);
        scopes.put("application", PageContext.APPLICATION_SCOPE);
    }

    /**
     * Get scope value from string value
     *
     * @param scopeName    Scope as a String.
     * @param defaultValue Returned default value, if not found.
     * @return Scope as an <code>int</code>, or <code>defaultValue</code> if scope is <code>null</code>.
     * @throws JspException Scope name is not recognized as a valid scope.
     */
    public static int getScope(String scopeName, int defaultValue) throws JspException {
        if (scopeName == null) {
            return defaultValue;
        }

        if (scopeName.equalsIgnoreCase("component")) {
            return ComponentConstants.COMPONENT_SCOPE;

        } else if (scopeName.equalsIgnoreCase("template")) {
            return ComponentConstants.COMPONENT_SCOPE;

        } else if (scopeName.equalsIgnoreCase("tile")) {
            return ComponentConstants.COMPONENT_SCOPE;

        } else {
            return getScope(scopeName);
        }
    }

    /**
     * Converts the scope name into its corresponding PageContext constant value.
     *
     * @param scopeName Can be "page", "request", "session", or "application" in any
     *                  case.
     * @return The constant representing the scope (ie. PageContext.REQUEST_SCOPE).
     * @throws JspException if the scopeName is not a valid name.
     */
    public static int getScope(String scopeName) throws JspException {
        Integer scope = scopes.get(scopeName.toLowerCase());

        if (scope == null) {
            //throw new JspException(messages.getMessage("lookup.scope", scope));
            throw new JspException("Unable to retrieve the scope " + scopeName);
        }

        return scope;
    }

    /**
     * Get object from requested context. Return <code>null</code> if not found.
     * Context can be "component" or normal JSP contexts.
     *
     * @param beanName    Name of bean to retrieve.
     * @param scope       Scope from which bean must be retrieved.
     * @param pageContext Current pageContext.
     * @return Requested bean or <code>null</code> if not found.
     */
    public static Object getAttribute(String beanName, int scope, PageContext pageContext) {

        if (scope == ComponentConstants.COMPONENT_SCOPE) {
            TilesContainer container = TilesAccess.getContainer(pageContext);
            ComponentContext compContext = container.getComponentContext(pageContext);

            return compContext.getAttribute(beanName);
        }
        return pageContext.getAttribute(beanName, scope);
    }

    /**
     * Store bean in requested context.
     * If scope is <code>null</code>, save it in REQUEST_SCOPE context.
     *
     * @param pageContext Current pageContext.
     * @param name        Name of the bean.
     * @param scope       Scope under which bean is saved (page, request, session, application)
     *                    or <code>null</code> to store in <code>request()</code> instead.
     * @param value       Bean value to store.
     * @throws JspException Scope name is not recognized as a valid scope
     */
    public static void setAttribute(PageContext pageContext, String name,
        ComponentDefinition value, String scope)
        throws JspException {

        if (scope == null)
            pageContext.setAttribute(name, value, PageContext.REQUEST_SCOPE);
        else if (scope.equalsIgnoreCase("page"))
            pageContext.setAttribute(name, value, PageContext.PAGE_SCOPE);
        else if (scope.equalsIgnoreCase("request"))
            pageContext.setAttribute(name, value, PageContext.REQUEST_SCOPE);
        else if (scope.equalsIgnoreCase("session"))
            pageContext.setAttribute(name, value, PageContext.SESSION_SCOPE);
        else if (scope.equalsIgnoreCase("application"))
            pageContext.setAttribute(name, value, PageContext.APPLICATION_SCOPE);
        else {
            throw new JspException("Error - bad scope name '" + scope + "'");
        }
    }

    /**
     * Store bean in REQUEST_SCOPE context.
     *
     * @param pageContext Current pageContext.
     * @param name        Name of the bean.
     * @param beanValue   Bean value to store.
     * @throws JspException Scope name is not recognized as a valid scope
     */
    public static void setAttribute(PageContext pageContext, String name, Object beanValue)
        throws JspException {
        pageContext.setAttribute(name, beanValue, PageContext.REQUEST_SCOPE);
    }


}
