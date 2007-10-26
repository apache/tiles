/*
 * $Id:SecurityWrappingFilter.java 586909 2007-10-21 15:13:47Z apetrelli $
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
package org.apache.tiles.test.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Filter that wraps an HttpServletRequest to override "isUserInRole".
 *
 * @version $Rev:586909 $ $Date:2007-10-21 17:13:47 +0200 (dom, 21 ott 2007) $
 */
public class SecurityWrappingFilter implements Filter {

    /**
     * The role that the current user is supposed to use.
     */
    public static final String GOOD_ROLE = "goodrole";

    /** {@inheritDoc} */
    public void init(FilterConfig filterConfig) throws ServletException {
        // No operation
    }

    /** {@inheritDoc} */
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest wrappedRequest = new SecurityWrapperHttpServletRequest(
                (HttpServletRequest) servletRequest);
        filterChain.doFilter(wrappedRequest, servletResponse);
    }

    /** {@inheritDoc} */
    public void destroy() {
        // No operation
    }

    /**
     * Request wrapper that overrides "isUserInRole" method.
     *
     * @version $Rev:586909 $ $Date:2007-10-21 17:13:47 +0200 (dom, 21 ott 2007) $
     */
    private static class SecurityWrapperHttpServletRequest extends
            HttpServletRequestWrapper {
        public SecurityWrapperHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        public boolean isUserInRole(String role) {
            return GOOD_ROLE.equals(role);
        }
    }
}
