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

import org.apache.tiles.web.util.ServletContextAdapter;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

/**
 * Initialization Servlet. Provided for backwards compatibility.
 * The prefered method of initialization is to use the TilesListener.
 *
 * @see org.apache.tiles.web.startup.TilesListener
 * @version $Rev$ $Date$
 */
public class TilesServlet extends HttpServlet {

    /**
     * The private listener instance, that is used to initialize Tiles
     * container.
     */
    private TilesListener listener = new TilesListener();

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        listener.contextDestroyed(createEvent());
    }

    /** {@inheritDoc} */
    @Override
    public void init() throws ServletException {
        listener.contextInitialized(createEvent());
    }

    /**
     * Creates an servlet context event starting for the servlet configuration
     * object.
     *
     * @return The servlet context event.
     */
    private ServletContextEvent createEvent() {
        return new ServletContextEvent(
            new ServletContextAdapter(getServletConfig())
        );
    }

}
