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
package org.apache.tiles.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.access.TilesAccess;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Decoration Filter.  Intercepts all requests and decorates them
 * with the configured definition.
 *
 * For example, given the following config:
 * <xmp>
    <filter>
        <filter-name>Tiles Decoration Filter</filter-name>
        <filter-class>org.apache.tiles.web.TilesDecorationFilter</filter-class>
        <init-param>
            <param-name>definition</param-name>
            <param-value>test.definition</param-value>
        </init-param>
        <init-param>
            <param-name>attribute-name</param-name>
            <param-value>body</param-value>
        </init-param>
    </filter>

    <filter-mapping>
        <filter-name>Tiles Decoration Filter</filter-name>
        <url-pattern>/testdecorationfilter.jsp</url-pattern>
        <dispatcher>REQUEST</dispatcher>
    </filter-mapping>
 * </xmp>
 * The filter will intercept all requests to the indicated url pattern
 * store the initial request path as the "body" component attribute
 * and then render the "test.definition" definition.
 *
 */
public class TilesDecorationFilter implements Filter {

    private static final Log LOG =
        LogFactory.getLog(TilesDecorationFilter.class);

    /**
     * Filter configuration.
     */
    private FilterConfig filterConfig;

    /**
     * The name of the component attribute used to
     * pass on the request.
     */
    private String componentAttributeName = "content";

    private String definitionName = "layout";


    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public ServletContext getServletContext() {
        return filterConfig.getServletContext();
    }

    public void init(FilterConfig config) throws ServletException {
        filterConfig = config;
        String temp = config.getInitParameter("attribute-name");
        if(temp != null) {
            componentAttributeName = temp;
        }

        temp = config.getInitParameter("definition");
        if(temp != null) {
            definitionName = temp;
        }
    }

    public void destroy() {
        filterConfig = null;
    }


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
        throws IOException, ServletException {
        TilesContainer container = TilesAccess.getContainer(getServletContext());
        prepareComponentAttributes(req, container.getComponentContext(req, res));
        try {
            container.render(req, res, definitionName);
        } catch (TilesException e) {
            throw new ServletException("Error wrapping jsp with tile definition.", e);
        }
    }

    protected void prepareComponentAttributes(ServletRequest req, ComponentContext ctx) {
        ComponentAttribute attr = new ComponentAttribute();
        attr.setType(ComponentAttribute.TEMPLATE);
        attr.setName(componentAttributeName);
        attr.setValue(getRequestBase(req));
        ctx.putAttribute(componentAttributeName, attr);
    }


    private String getRequestBase(ServletRequest request) {
        // Included Path
        String include = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (include != null) {
            return include;
        }

        // As opposed to includes, if a forward occurs, it will update the servletPath property
        // and include the original as the request attribute.
        return ((HttpServletRequest) request).getServletPath();
    }

}
