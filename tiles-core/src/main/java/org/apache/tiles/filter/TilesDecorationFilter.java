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
 *
 */
package org.apache.tiles.filter;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.TilesException;
import org.apache.tiles.access.TilesAccess;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Processes Reloadable Tiles Definitions.
 *
 * @version $Rev$ $Date$
 */

public class TilesDecorationFilter implements Filter {

    private static final Log LOG = LogFactory.getLog(TilesDecorationFilter.class);

    /**
     * The filter configuration object we are associated with.  If
     * this value is null, this filter instance is not currently
     * configured.
     */
    private FilterConfig filterConfig = null;

    private ServletContext servletContext = null;

    private String targetAttributeName = "body";

    private String definition = "layout";


    /**
     * Checks whether Tiles Definitions need to be reloaded.
     *
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain    The filter chain we are processing
     * @throws java.io.IOException      if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException {

        TilesContainer container = TilesAccess.getContainer(servletContext);
        ComponentContext ctx = container.getComponentContext(request, response);

        ComponentAttribute attr = new ComponentAttribute();
        attr.setType(ComponentAttribute.TEMPLATE);
        attr.setName(targetAttributeName);
        attr.setValue(getTargetResource(request));
        ctx.putAttribute(targetAttributeName, attr);

        try {
            container.render(definition, request, response);
        } catch (TilesException e) {
            throw new ServletException("Error wrapping jsp with tile definition.", e);
        }
    }

    private String getTargetResource(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest)request;
        String includePath = (String)req.getAttribute("");
        return includePath == null ? req.getServletPath() : includePath;

    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String name = filterConfig.getInitParameter("target-attribute-name");
        if(name != null) {
            targetAttributeName = name;
        }
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
        this.filterConfig = null;
        this.servletContext = null;
        this.targetAttributeName = "body";
    }


}
