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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.tiles.request.servlet.ServletApplicationContext;
import org.apache.tiles.startup.TilesInitializer;
import org.apache.tiles.web.util.ServletContextAdapter;

/**
 * Abstract Initialization Servlet. Uses a {@link TilesInitializer}, created by
 * {@link #createTilesInitializer()} to initialize Tiles.
 *
 * @deprecated use {@link AbstractTilesListener} instead.
 * @see org.apache.tiles.web.startup.TilesListener
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
@Deprecated
public abstract class AbstractTilesInitializerServlet extends HttpServlet {

    /**
     * The private listener instance, that is used to initialize Tiles
     * container.
     */
    private TilesInitializer initializer;

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        initializer.destroy();
    }

    /** {@inheritDoc} */
    @Override
    public void init() {
        initializer = createTilesInitializer();
        ServletContext adaptedContext = new ServletContextAdapter(
                getServletConfig());
        ServletApplicationContext preliminaryContext = new ServletApplicationContext(
                adaptedContext);
        initializer.initialize(preliminaryContext);
    }

    /**
     * Creates a new instance of {@link TilesInitializer}. Implement it to use
     * your custom initializer.
     *
     * @return The Tiles servlet-based initializer.
     * @since 2.2.0
     */
    protected abstract TilesInitializer createTilesInitializer();
}
