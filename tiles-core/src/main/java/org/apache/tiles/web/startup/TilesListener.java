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
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.factory.AbstractTilesContainerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener for the initialization of the Tiles container.
 *
 * @version $Rev$ $Date$
 */
public class TilesListener
    implements ServletContextListener {

    /**
     * Init parameter to define the key under which the container will be
     * stored.
     *
     * @since 2.1.0
     */
    public static final String CONTAINER_KEY_INIT_PARAMETER =
        "org.apache.tiles.web.startup.TilesListener.CONTAINER_KEY";

    /**
     * Log instance.
     */
    protected static final Log LOG =
        LogFactory.getLog(TilesListener.class);

    /**
     * Initialize the TilesContainer and place it
     * into service.
     *
     * @param event The intercepted event.
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        String key = servletContext.getInitParameter(
                CONTAINER_KEY_INIT_PARAMETER);
        TilesContainer container = createContainer(servletContext);
        TilesAccess.setContainer(servletContext, container, key);
    }

    /**
     * Remove the tiles container from service.
     *
     * @param event The intercepted event.
     */
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        try {
            TilesAccess.setContainer(servletContext, null);
        } catch (TilesException e) {
            LOG.warn("Unable to remove tiles container from service.", e);
        }
    }

    /**
     * Creates a Tiles container.
     *
     * @param context The servlet context to use.
     * @return The created container
     */
    protected TilesContainer createContainer(ServletContext context) {
        AbstractTilesContainerFactory factory =
            AbstractTilesContainerFactory.getTilesContainerFactory(context);
        return factory.createContainer(context);
    }

}
