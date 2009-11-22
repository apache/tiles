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

package org.apache.tiles.jsp;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.tiles.jsp.taglib.TilesJspException;

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
