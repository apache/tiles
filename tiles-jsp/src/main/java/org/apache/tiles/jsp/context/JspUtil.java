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

package org.apache.tiles.jsp.context;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.NoSuchContainerException;
import org.apache.tiles.jsp.taglib.TilesJspException;
import org.apache.tiles.servlet.context.ServletUtil;

/**
 * Utility class for working within a Jsp environment.
 *
 * @version $Rev$ $Date$
 */
public final class JspUtil {

    /**
     * The name of the attribute that will contain the compose stack.
     */
    public static final String COMPOSE_STACK_ATTRIBUTE_NAME = "org.apache.tiles.template.COMPOSE_STACK";

    /**
     * Maps scope names to their constants.
     */
    private static final Map<String, Integer> SCOPES =
        new HashMap<String, Integer>();

    static {
        JspUtil.SCOPES.put("page", PageContext.PAGE_SCOPE);
        JspUtil.SCOPES.put("request", PageContext.REQUEST_SCOPE);
        JspUtil.SCOPES.put("session", PageContext.SESSION_SCOPE);
        JspUtil.SCOPES.put("application", PageContext.APPLICATION_SCOPE);
    }

    /**
     * Constructor, private to avoid instantiation.
     */
    private JspUtil() {
    }

    /**
     * Returns true if forced include of the result is needed.
     *
     * @param context The page context.
     * @return If <code>true</code> the include operation must be forced.
     * @since 2.0.6
     */
    public static boolean isForceInclude(JspContext context) {
        Boolean retValue = (Boolean) context.getAttribute(
                ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
                PageContext.REQUEST_SCOPE);
        return retValue != null && retValue.booleanValue();
    }

    /**
     * Sets the option that enables the forced include of the response.
     *
     * @param context The page context.
     * @param forceInclude If <code>true</code> the include operation must be
     * forced.
     * @since 2.0.6
     */
    public static void setForceInclude(JspContext context, boolean forceInclude) {
        Boolean retValue = Boolean.valueOf(forceInclude);
        context.setAttribute(
                ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
                retValue, PageContext.REQUEST_SCOPE);
    }

    /**
     * Returns the default Tiles container.
     *
     * @param context The page context to use.
     * @return The default Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(JspContext context) {
        return getContainer(context, TilesAccess.CONTAINER_ATTRIBUTE);
    }

    /**
     * Returns a specific Tiles container.
     *
     * @param context The page context to use.
     * @param key The key under which the container is stored. If null, the
     * default container will be returned.
     * @return The requested Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(JspContext context, String key) {
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }
        return (TilesContainer) context.getAttribute(key,
                PageContext.APPLICATION_SCOPE);
    }

    /**
     * Configures the default container to be used in the application.
     *
     * @param context The page context object to use.
     * @param container The container object to set.
     * @since 2.1.2
     */
    public static void setContainer(JspContext context,
            TilesContainer container) {
        setContainer(context, container, TilesAccess.CONTAINER_ATTRIBUTE);
    }

    /**
     * Configures the container to be used in the application.
     *
     * @param context The page context object to use.
     * @param container The container object to set.
     * @param key The key under which the container will be stored.
     * @since 2.1.2
     */
    public static void setContainer(JspContext context,
            TilesContainer container, String key) {
        Log log = LogFactory.getLog(ServletUtil.class);
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }

        if (container == null) {
            if (log.isInfoEnabled()) {
                log.info("Removing TilesContext for context: " + context.getClass().getName());
            }
            context.removeAttribute(key, PageContext.APPLICATION_SCOPE);
        }
        if (container != null && log.isInfoEnabled()) {
            log.info("Publishing TilesContext for context: " + context.getClass().getName());
        }
        context.setAttribute(key, container, PageContext.APPLICATION_SCOPE);
    }

    /**
     * Sets the current container to use in web pages.
     *
     * @param context The page context to use.
     * @param key The key under which the container is stored.
     * @since 2.1.0
     */
    public static void setCurrentContainer(JspContext context, String key) {
        TilesContainer container = getContainer(context, key);
        if (container != null) {
            context.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                    container, PageContext.REQUEST_SCOPE);
        } else {
            throw new NoSuchContainerException("The container with the key '"
                    + key + "' cannot be found");
        }
    }

    /**
     * Sets the current container to use in web pages.
     *
     * @param context The page context to use.
     * @param container The container to use as the current container.
     * @since 2.1.0
     */
    public static void setCurrentContainer(JspContext context,
            TilesContainer container) {
        if (container != null) {
            context.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                    container, PageContext.REQUEST_SCOPE);
        } else {
            throw new NoSuchContainerException("The container cannot be null");
        }
    }

    /**
     * Returns the current container that has been set, or the default one.
     *
     * @param context The page context to use.
     * @return The current Tiles container to use in web pages.
     * @since 2.1.0
     */
    public static TilesContainer getCurrentContainer(JspContext context) {
        TilesContainer container = (TilesContainer) context.getAttribute(
                ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                PageContext.REQUEST_SCOPE);
        if (container == null) {
            container = getContainer(context);
            context.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                    container, PageContext.REQUEST_SCOPE);
        }

        return container;
    }

    /**
     * Returns the compose stack, that is used by the tags to compose
     * definitions, attributes, etc.
     *
     * @param context The page context.
     * @return The compose stack.
     * @since 2.2.0
     */
    @SuppressWarnings("unchecked")
    public static Stack<Object> getComposeStack(JspContext context) {
        Stack<Object> composeStack = (Stack<Object>) context.getAttribute(
                COMPOSE_STACK_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE);
        if (composeStack == null) {
            composeStack = new Stack<Object>();
            context.setAttribute(COMPOSE_STACK_ATTRIBUTE_NAME, composeStack,
                    PageContext.REQUEST_SCOPE);
        }
        return composeStack;
    }

    /**
     * Converts the scope name into its corresponding PageContext constant value.
     *
     * @param scopeName Can be "page", "request", "session", or "application" in any
     *                  case.
     * @return The constant representing the scope (ie. PageContext.REQUEST_SCOPE).
     * @throws TilesJspException if the scopeName is not a valid name.
     * @since 2.2.0
     */
    public static int getScope(String scopeName) throws TilesJspException {
        if (scopeName == null) {
            return PageContext.PAGE_SCOPE;
        }

        Integer scope = JspUtil.SCOPES.get(scopeName.toLowerCase());

        if (scope == null) {
            throw new TilesJspException("Unable to retrieve the scope "
                    + scopeName);
        }

        return scope;
    }

    /**
     * Evaluates the fragment (invokes it with a null Writer) if not null.
     *
     * @param fragment The fragment to evaluate.
     * @throws JspException If the fragment invocation fails.
     * @throws IOException If the fragment invocation fails.
     * @since 2.2.0
     */
    public static void evaluateFragment(JspFragment fragment) throws JspException, IOException {
        if (fragment != null) {
            fragment.invoke(null);
        }
    }

    /**
     * Evaluates the fragment and returns its content as a string.
     *
     * @param fragment The fragment to evaluate.
     * @return The fragment evaluated as a string.
     * @throws JspException If the fragment invocation fails.
     * @throws IOException If the fragment invocation fails with an I/O error.
     * @since 2.2.0
     */
    public static String evaluateFragmentAsString(JspFragment fragment) throws JspException, IOException {
        String body = null;
        if (fragment != null) {
            StringWriter writer = new StringWriter();
            try {
                fragment.invoke(writer);
            } finally {
                writer.close();
            }
            body = writer.toString();
        }
        return body;
    }
}
