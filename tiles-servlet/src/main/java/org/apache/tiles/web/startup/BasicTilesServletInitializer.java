/*
 * $Id: TilesServlet.java 531864 2007-04-24 10:24:30Z apetrelli $
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

import org.apache.tiles.Initializable;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.context.AbstractTilesApplicationContextFactory;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;

/**
 * Default Tiles initialization delegate implementation under a servlet
 * environment. It uses init parameters to create the
 * {@link TilesApplicationContext} and the {@link TilesContainer}.
 *
 * @version $Rev: 531864 $ $Date: 2007-04-24 12:24:30 +0200 (mar, 24 apr 2007) $
 * @since 2.1.2
 */
public class BasicTilesServletInitializer implements TilesServletInitializer {

    /**
     * Init parameter to define the key under which the container will be
     * stored.
     *
     * @since 2.1.2
     */
    public static final String CONTAINER_KEY_INIT_PARAMETER =
        "org.apache.tiles.web.startup.TilesServletInitializer.CONTAINER_KEY";

    /** {@inheritDoc} */
    public void initialize(ServletContext servletContext) {
        TilesApplicationContext applicationContext = createTilesApplicationContext(servletContext);
        String key = getContainerKey(applicationContext);
        TilesContainer container = createContainer(applicationContext);
        TilesAccess.setContainer(servletContext, container, key);
    }

    /**
     * Creates the Tiles application context, to be used across all the
     * Tiles-based application.
     *
     * @param context The Servlet context to use.
     * @return The Tiles application context.
     * @since 2.1.2
     */
    protected TilesApplicationContext createTilesApplicationContext(
            ServletContext context) {
        TilesApplicationContext applicationContext = new ServletTilesApplicationContext(
                context);
        AbstractTilesApplicationContextFactory acFactory = AbstractTilesApplicationContextFactory
                .createFactory(applicationContext);
        if (acFactory instanceof Initializable) {
            ((Initializable) acFactory).init(applicationContext.getInitParams());
        }
        applicationContext = acFactory.createApplicationContext(context);
        return applicationContext;
    }

    /**
     * Returns the container key under which the container will be stored.
     *
     * @param applicationContext The Tiles application context to use.
     * @return The container key.
     * @since 2.1.2
     */
    protected String getContainerKey(TilesApplicationContext applicationContext) {
        String key = applicationContext.getInitParams().get(
                BasicTilesServletInitializer.CONTAINER_KEY_INIT_PARAMETER);
        return key;
    }

    /**
     * Creates a Tiles container.
     *
     * @param context The servlet context to use.
     * @return The created container.
     * @since 2.1.2
     */
    protected TilesContainer createContainer(TilesApplicationContext context) {
        AbstractTilesContainerFactory factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        return factory.createContainer(context);
    }
}
