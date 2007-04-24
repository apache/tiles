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

package org.apache.tiles.web.startup;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.definition.util.DefinitionsFactoryUtil;

/**
 * Processes Reloadable Tiles Definitions.
 *
 * @version $Rev$ $Date$
 */

public class TilesFilter extends TilesServlet implements Filter {

    /**
     * The logging object.
     */
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(TilesFilter.class);

    /**
     * The filter configuration object we are associated with.  If
     * this value is null, this filter instance is not currently
     * configured.
     */
    private FilterConfig filterConfig = null;


    /**
     * Checks whether Tiles Definitions need to be reloaded.
     *
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain    The filter chain we are processing
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException {

        try {
            DefinitionsFactoryUtil.reloadDefinitionsFactory(
                    getServletContext());
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new ServletException("Error processing request.", e);
        }
    }

    /**
     * Returns the filter configuration object for this filter.
     *
     * @return The filter configuration.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig
        (FilterConfig
            filterConfig) {

        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter.
     */
    public void destroy
        () {
        super.destroy();
    }

    /** {@inheritDoc} */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        super.init(createServletConfig());

        if (DEBUG) {
            log("TilesDecorationFilter:Initializing filter");
        }
    }

    /** {@inheritDoc} */
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    /**
     * A DEBUG flag.
     */
    // FIXME Is it really used?
    private static final boolean DEBUG = true;

    /**
     * Creates a servlet configuration object from the filter configuration
     * object.
     *
     * @return The servlet configuration object.
     */
    private ServletConfig createServletConfig() {
        return new ServletConfigAdapter(filterConfig);
    }


    /**
     * Adapts a filter configuration object to become a servlet configuration
     * object.
     */
    class ServletConfigAdapter implements ServletConfig {

        /**
         * The filter configuration object to use.
         */
        private FilterConfig config;


        /**
         * Constructor.
         *
         * @param config The filter configuration object to use.
         */
        public ServletConfigAdapter(FilterConfig config) {
            this.config = config;
        }

        /** {@inheritDoc} */
        public String getServletName() {
            return config.getFilterName();
        }

        /** {@inheritDoc} */
        public ServletContext getServletContext() {
            return config.getServletContext();
        }

        /** {@inheritDoc} */
        public String getInitParameter(String string) {
            return config.getInitParameter(string);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public Enumeration getInitParameterNames() {
            return config.getInitParameterNames();
        }
    }

}
