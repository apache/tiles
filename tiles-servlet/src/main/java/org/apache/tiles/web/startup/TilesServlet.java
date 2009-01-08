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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesException;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.web.util.ServletContextAdapter;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;

/**
 * Initialization Servlet.
 *
 * @see org.apache.tiles.web.startup.TilesListener
 * @version $Rev$ $Date$
 */
public class TilesServlet extends HttpServlet {

    /**
     * Log instance.
     */
    protected static final Log LOG =
        LogFactory.getLog(TilesServlet.class);

    /**
     * The private listener instance, that is used to initialize Tiles
     * container.
     */
    private TilesServletInitializer initializer;

    /**
     * Constructor.
     *
     * @since 2.1.2
     */
    public TilesServlet() {
        initializer = createTilesServletInitializer();
    }

    /**
     * Constructor with injected initializer.
     *
     * @param initializer The initializer to use.
     */
    public TilesServlet(TilesServletInitializer initializer) {
        this.initializer = initializer;
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        try {
            TilesAccess.setContainer(getServletContext(), null);
        } catch (TilesException e) {
            LOG.warn("Unable to remove tiles container from service.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init() throws ServletException {
        initializer.initialize(new ServletContextAdapter(getServletConfig()));
    }

    /**
     * Creates a new instance of {@link BasicTilesServletInitializer}. Override it to use a different initializer.
     *
     * @return The Tiles servlet-based initializer.
     * @since 2.1.2
     */
    protected TilesServletInitializer createTilesServletInitializer() {
        return new BasicTilesServletInitializer();
    }
}
