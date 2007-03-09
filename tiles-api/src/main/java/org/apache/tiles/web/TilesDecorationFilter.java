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
import java.util.Map;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Decoration Filter.  Intercepts all requests and decorates them
 * with the configured definition.
 * <p/>
 * For example, given the following config:
 * <xmp>
 * <filter>
 * <filter-name>Tiles Decoration Filter</filter-name>
 * <filter-class>org.apache.tiles.web.TilesDecorationFilter</filter-class>
 * <init-param>
 * <param-name>definition</param-name>
 * <param-value>test.definition</param-value>
 * </init-param>
 * <init-param>
 * <param-name>attribute-name</param-name>
 * <param-value>body</param-value>
 * </init-param>
 * </filter>
 * <p/>
 * <filter-mapping>
 * <filter-name>Tiles Decoration Filter</filter-name>
 * <url-pattern>/testdecorationfilter.jsp</url-pattern>
 * <dispatcher>REQUEST</dispatcher>
 * </filter-mapping>
 * </xmp>
 * The filter will intercept all requests to the indicated url pattern
 * store the initial request path as the "body" component attribute
 * and then render the "test.definition" definition.
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

    private Map<String, String> alternateDefinitions;

    private ComponentContextMutator mutator = null;

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public ServletContext getServletContext() {
        return filterConfig.getServletContext();
    }

    public void init(FilterConfig config) throws ServletException {
        filterConfig = config;
        String temp = config.getInitParameter("attribute-name");
        if (temp != null) {
            componentAttributeName = temp;
        }

        temp = config.getInitParameter("definition");
        if (temp != null) {
            definitionName = temp;
        }

        alternateDefinitions = parseAlternateDefinitions();

        temp = config.getInitParameter("mutator");
        if(temp != null) {
            try {
                mutator = (ComponentContextMutator)Class.forName(temp).newInstance();
            } catch (Exception e) {
                throw new ServletException("Unable to instantiate specified context mutator.", e);
            }
        } else {
            mutator = new DefaultMutator();
        }
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> parseAlternateDefinitions() {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> e = filterConfig.getInitParameterNames();
        while(e.hasMoreElements()) {
            String parm = e.nextElement();
            if(parm.startsWith("definition(") && parm.endsWith("*)")) {
                String value = filterConfig.getInitParameter(parm);
                String mask = parm.substring("definition(".length());
                mask = mask.substring(0, mask.lastIndexOf("*)"));
                map.put(mask, value);
                LOG.info("Mapping all requests matching '"+mask+"*' to definition '"+value+"'");
            }
        }
        return map;
    }

    public void destroy() {
        filterConfig = null;
    }


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
        throws IOException, ServletException {
        TilesContainer container = TilesAccess.getContainer(getServletContext());
        mutator.mutate(container.getComponentContext(req, res), req);
        try {
            String definitionName = getDefinitionForRequest(req);
            container.render(definitionName, req, res);
        } catch (TilesException e) {
            throw new ServletException("Error wrapping jsp with tile definition. "+e.getMessage(), e);
        }
    }

    private String getDefinitionForRequest(ServletRequest request) {
        if(alternateDefinitions.size() < 1) {
            return definitionName;
        }
        String base = getRequestBase(request);
        for(Map.Entry<String, String> pair : alternateDefinitions.entrySet()) {
            if(base.startsWith(pair.getKey())) {
                return pair.getValue();
            }
        }
        return definitionName;
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

    class DefaultMutator implements ComponentContextMutator {
        public void mutate(ComponentContext ctx, ServletRequest req) {
            ComponentAttribute attr = new ComponentAttribute();
            attr.setType(ComponentAttribute.TEMPLATE);
            attr.setName(componentAttributeName);
            attr.setValue(getRequestBase(req));
            ctx.putAttribute(componentAttributeName, attr);
        }
    }

}
