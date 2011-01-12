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
package org.apache.tiles.web.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.reflect.CannotInstantiateObjectException;
import org.apache.tiles.request.reflect.ClassUtil;
import org.apache.tiles.request.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decoration Filter.  Intercepts all requests and decorates them
 * with the configured definition.
 * <p/>
 * For example, given the following config:
 * &lt;xmp&gt;
 * &lt;filter&gt;
 * &lt;filter-name&gt;Tiles Decoration Filter&lt;/filter-name&gt;
 * &lt;filter-class&gt;org.apache.tiles.web.TilesDecorationFilter&lt;/filter-class&gt;
 * &lt;init-param&gt;
 * &lt;param-name&gt;definition&lt;/param-name&gt;
 * &lt;param-value&gt;test.definition&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * &lt;param-name&gt;attribute-name&lt;/param-name&gt;
 * &lt;param-value&gt;body&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * &lt;param-name&gt;prevent-token&lt;/param-name&gt;
 * &lt;param-value&gt;layout&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;/filter&gt;
 * <p/>
 * &lt;filter-mapping&gt;
 * &lt;filter-name&gt;Tiles Decoration Filter&lt;/filter-name&gt;
 * &lt;url-pattern&gt;/testdecorationfilter.jsp&lt;/url-pattern&gt;
 * &lt;dispatcher&gt;REQUEST&lt;/dispatcher&gt;
 * &lt;/filter-mapping&gt;
 * &lt;/xmp&gt;
 * The filter will intercept all requests to the indicated url pattern
 * store the initial request path as the "body"  attribute and then render the
 * "test.definition" definition.  The filter will only redecorate those requests
 * which do not contain the request attribute associated with the prevent token
 * "layout".
 */
public class TilesDecorationFilter implements Filter {

    /**
     * Init parameter to define the key of the container to use.
     *
     * @since 2.1.2
     */
    public static final String CONTAINER_KEY_INIT_PARAMETER =
        "org.apache.tiles.web.util.TilesDecorationFilter.CONTAINER_KEY";

    /**
     * The logging object.
     */
    private Logger log = LoggerFactory.getLogger(TilesDecorationFilter.class);

    /**
     * Filter configuration.
     */
    private FilterConfig filterConfig;

    /**
     * The key under which the container is stored.
     */
    private String containerKey;

    /**
     * The name of the definition attribute used to
     * pass on the request.
     */
    private String definitionAttributeName = "content";

    /**
     * The definition name to use.
     */
    private String definitionName = "layout";

    /**
     * Token used to prevent re-decoration of requests.
     * This token is used to prevent infinate loops on
     * filters configured to match wildcards.
     */
    private String preventDecorationToken;

    /**
     * Stores a map of the type "mask -> definition": when a definition name
     * mask is identified, it is substituted with the configured definition.
     */
    private Map<String, String> alternateDefinitions;

    /**
     * The object that will mutate the attribute context so that it uses
     * different attributes.
     */
    private AttributeContextMutator mutator = null;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /** {@inheritDoc} */
    public void init(FilterConfig config) throws ServletException {
        filterConfig = config;
        servletContext = filterConfig.getServletContext();

        containerKey = filterConfig
                .getInitParameter(CONTAINER_KEY_INIT_PARAMETER);

        String temp = config.getInitParameter("attribute-name");
        if (temp != null) {
            definitionAttributeName = temp;
        }

        temp = config.getInitParameter("definition");
        if (temp != null) {
            definitionName = temp;
        }

        temp = config.getInitParameter("prevent-token");
        preventDecorationToken = "org.apache.tiles.decoration.PREVENT:"
                + (temp == null ? definitionName : temp);

        alternateDefinitions = parseAlternateDefinitions();

        temp = config.getInitParameter("mutator");
        if (temp != null) {
            try {
                mutator = (AttributeContextMutator) ClassUtil.instantiate(temp);
            } catch (CannotInstantiateObjectException e) {
                throw new ServletException("Unable to instantiate specified context mutator.", e);
            }
        } else {
            mutator = new DefaultMutator();
        }
    }

    /**
     * Creates the alternate definitions map, to map a mask of definition names
     * to a configured definition.
     *
     * @return The alternate definitions map.
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> parseAlternateDefinitions() {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> e = filterConfig.getInitParameterNames();
        while (e.hasMoreElements()) {
            String parm = e.nextElement();
            if (parm.startsWith("definition(") && parm.endsWith("*)")) {
                String value = filterConfig.getInitParameter(parm);
                String mask = parm.substring("definition(".length());
                mask = mask.substring(0, mask.lastIndexOf("*)"));
                map.put(mask, value);
                log.info("Mapping all requests matching '" + mask
                        + "*' to definition '" + value + "'");
            }
        }
        return map;
    }

    /** {@inheritDoc} */
    public void destroy() {
        filterConfig = null;
    }


    /**
     * {@inheritDoc}
     */
    public void doFilter(javax.servlet.ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        // If the request contains the prevent token, we must not reapply the definition.
        // This is used to ensure that filters mapped to wild cards do not infinately
        // loop.
        if (!isPreventTokenPresent(req)) {
            ApplicationContext applicationContext = org.apache.tiles.request.servlet.ServletUtil
                    .getApplicationContext(servletContext);
            Request request = new ServletRequest(applicationContext,
                    (HttpServletRequest) req, (HttpServletResponse) res);
            TilesContainer container = TilesAccess.getContainer(applicationContext,
                    containerKey);
            mutator.mutate(container.getAttributeContext(request), req);
            if (preventDecorationToken != null) {
                req.setAttribute(preventDecorationToken, Boolean.TRUE);
            }
            String definitionName = getDefinitionForRequest(req);
            container.render(definitionName, request);
        }
        filterChain.doFilter(req, res);
    }

    /**
     * Returns the final definition to render for the given request.
     *
     * @param request The request object.
     * @return The final definition name.
     */
    private String getDefinitionForRequest(javax.servlet.ServletRequest request) {
        if (alternateDefinitions.size() < 1) {
            return definitionName;
        }
        String base = getRequestBase(request);
        for (Map.Entry<String, String> pair : alternateDefinitions.entrySet()) {
            if (base.startsWith(pair.getKey())) {
                return pair.getValue();
            }
        }
        return definitionName;
    }

    /**
     * Returns the request base, i.e. the the URL to calculate all the relative
     * paths.
     *
     * @param request The request object to use.
     * @return The request base.
     */
    private String getRequestBase(javax.servlet.ServletRequest request) {
        // Included Path
        String include = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (include != null) {
            return include;
        }

        // As opposed to includes, if a forward occurs, it will update the servletPath property
        // and include the original as the request attribute.
        return ((HttpServletRequest) request).getServletPath();
    }

    /**
     * The default attribute context mutator to use.
     */
    class DefaultMutator implements AttributeContextMutator {

        /** {@inheritDoc} */
        public void mutate(AttributeContext ctx, javax.servlet.ServletRequest req) {
            Attribute attr = new Attribute();
            attr.setRenderer("template");
            attr.setValue(getRequestBase(req));
            ctx.putAttribute(definitionAttributeName, attr);
        }
    }

    /**
     * Checks if the prevent evaluation token is present.
     *
     * @param request The HTTP request object.
     * @return <code>true</code> if the token is present.
     */
    private boolean isPreventTokenPresent(javax.servlet.ServletRequest request) {
        return preventDecorationToken != null && request.getAttribute(preventDecorationToken) != null;
    }
}
