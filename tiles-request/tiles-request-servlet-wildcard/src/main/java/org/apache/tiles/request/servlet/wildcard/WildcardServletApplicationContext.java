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

package org.apache.tiles.request.servlet.wildcard;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.tiles.request.servlet.ServletApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

/**
 * Servlet-based implementation of the TilesApplicationContext interface that
 * can resolve resources even using wildcards.
 *
 * @version $Rev$ $Date$
 * @since 2.2.1
 */
public class WildcardServletApplicationContext extends
        ServletApplicationContext {

    /**
     * The pattern resolver.
     *
     * @since 2.2.1
     */
    protected ResourcePatternResolver resolver;

    /**
     * Constructor.
     *
     * @param servletContext The servlet context.
     * @since 2.2.1
     */
    public WildcardServletApplicationContext(ServletContext servletContext) {
        super(servletContext);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(ServletContext context) {
        super.initialize(context);

        resolver = new ServletContextResourcePatternResolver(context);
    }

    /** {@inheritDoc} */
    @Override
    public URL getResource(String path) throws IOException {
        URL retValue = null;
        Set<URL> urlSet = getResources(path);
        if (urlSet != null && !urlSet.isEmpty()) {
            retValue = urlSet.iterator().next();
        }
        return retValue;
    }

    /** {@inheritDoc} */
    @Override
    public Set<URL> getResources(String path) throws IOException {
        Set<URL> urlSet = null;
        Resource[] resources = resolver.getResources(path);
        if (resources != null && resources.length > 0) {
            urlSet = new HashSet<URL>();
            for (int i = 0; i < resources.length; i++) {
                urlSet.add(resources[i].getURL());
            }
        }
        return urlSet;
    }
}
