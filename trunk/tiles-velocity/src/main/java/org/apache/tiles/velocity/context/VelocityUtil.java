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

package org.apache.tiles.velocity.context;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.ArrayStack;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.Renderable;

/**
 * Utilities for Velocity usage in Tiles.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public final class VelocityUtil {

    /**
     * A renderable object that does not render anything.
     *
     * @since 2.2.0
     */
    public static final Renderable EMPTY_RENDERABLE;

    static {
        EMPTY_RENDERABLE = new Renderable() {

            @Override
            public String toString() {
                return "";
            }

            public boolean render(InternalContextAdapter context, Writer writer)
                    throws IOException {
                // Does nothing, really!
                return true;
            }
        };
    }

    /**
     * The attribute key that will be used to store the parameter map, to use across Velocity tool calls.
     *
     * @since 2.2.0
     */
    private static final String PARAMETER_MAP_STACK_KEY = "org.apache.tiles.velocity.PARAMETER_MAP_STACK";

    /**
     * Private constructor to avoid instantiation.
     */
    private VelocityUtil() {
    }

    /**
     * Null-safe conversion from Boolean to boolean.
     *
     * @param obj The Boolean object.
     * @param defaultValue This value will be returned if <code>obj</code> is null.
     * @return The boolean value of <code>obj</code> or, if null, <code>defaultValue</code>.
     * @since 2.2.0
     */
    public static boolean toSimpleBoolean(Boolean obj, boolean defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    /**
     * Returns or creates the parameter stack to use. It is useful to store parameters across tool calls.
     *
     * @param context The Velocity context.
     * @return The parameter stack.
     * @since 2.2.0
     */
    @SuppressWarnings("unchecked")
    public static ArrayStack<Map<String, Object>> getParameterStack(Context context) {
        ArrayStack<Map<String, Object>> stack = (ArrayStack<Map<String, Object>>) context
                .get(PARAMETER_MAP_STACK_KEY);
        if (stack == null) {
            stack = new ArrayStack<Map<String, Object>>();
            context.put(PARAMETER_MAP_STACK_KEY, stack);
        }
        return stack;
    }

    /**
     * Sets an attribute in the desired scope.
     *
     * @param velocityContext The Velocity context.
     * @param request The HTTP request.
     * @param servletContext The servlet context.
     * @param name The name of the attribute.
     * @param obj The value of the attribute.
     * @param scope The scope. It can be <code>page</code>, <code>request</code>
     * , <code>session</code>, <code>application</code>.
     * @since 2.2.0
     */
    public static void setAttribute(Context velocityContext,
            HttpServletRequest request, ServletContext servletContext,
            String name, Object obj, String scope) {
        if (scope == null) {
            scope = "page";
        }
        if ("page".equals(scope)) {
            velocityContext.put(name, obj);
        } else if ("request".equals(scope)) {
            request.setAttribute(name, obj);
        } else if ("session".equals(scope)) {
            request.getSession().setAttribute(name, obj);
        } else if ("application".equals(scope)) {
            servletContext.setAttribute(name, obj);
        }
    }
}
