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

import org.apache.tiles.servlet.context.ServletUtil;

import javax.servlet.jsp.PageContext;

/**
 * Utility class for working within a Jsp environment.
 *
 * @version $Rev$ $Date$
 */
public final class JspUtil {

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
    public static boolean isForceInclude(PageContext context) {
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
    public static void setForceInclude(PageContext context, boolean forceInclude) {
        Boolean retValue = Boolean.valueOf(forceInclude);
        context.setAttribute(
                ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME,
                retValue, PageContext.REQUEST_SCOPE);
    }
}
