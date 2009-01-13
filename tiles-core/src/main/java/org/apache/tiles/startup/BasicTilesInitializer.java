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
package org.apache.tiles.startup;

import org.apache.tiles.Initializable;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.context.AbstractTilesApplicationContextFactory;
import org.apache.tiles.factory.AbstractTilesContainerFactory;

/**
 * Default Tiles initialization delegate implementation under a servlet
 * environment. It uses init parameters to create the
 * {@link TilesApplicationContext} and the {@link TilesContainer}.
 *
 * @version $Rev: 531864 $ $Date: 2007-04-24 12:24:30 +0200 (mar, 24 apr 2007) $
 * @since 2.1.2
 */
public class BasicTilesInitializer implements TilesInitializer {

    /**
     * Init parameter to define the key under which the container will be
     * stored.
     *
     * @since 2.1.2
     */
    public static final String CONTAINER_KEY_INIT_PARAMETER =
        "org.apache.tiles.startup.BasicTilesInitializer.CONTAINER_KEY";

    /** {@inheritDoc} */
    public void initialize(TilesApplicationContext applicationContext) {
        applicationContext = createTilesApplicationContext(applicationContext);
        String key = getContainerKey(applicationContext);
        TilesContainer container = createContainer(applicationContext);
        TilesAccess.setContainer(applicationContext, container, key);
    }

    /**
     * Creates the Tiles application context, to be used across all the
     * Tiles-based application. If you override this class, please override this
     * method or
     * {@link #createAndInitializeTilesApplicationContextFactory(TilesApplicationContext)}.
     *
     * @param preliminaryContext The preliminary application context to use.
     * @return The Tiles application context.
     * @since 2.1.2
     */
    protected TilesApplicationContext createTilesApplicationContext(
            TilesApplicationContext preliminaryContext) {
        AbstractTilesApplicationContextFactory acFactory =
            createAndInitializeTilesApplicationContextFactory(preliminaryContext);
        return acFactory.createApplicationContext(preliminaryContext.getContext());
    }

    /**
     * Creates and initializes the Tiles application context factory, to create
     * a {@link TilesApplicationContext} to be used across all the Tiles-based
     * application. If you override this class, please override this method or
     * {@link #createTilesApplicationContext(TilesApplicationContext)}.
     *
     * @param preliminaryContext The preliminary application context to use.
     * @return The Tiles application context factory.
     * @since 2.1.2
     */
    protected AbstractTilesApplicationContextFactory createAndInitializeTilesApplicationContextFactory(
            TilesApplicationContext preliminaryContext) {
        AbstractTilesApplicationContextFactory acFactory = AbstractTilesApplicationContextFactory
                .createFactory(preliminaryContext);
        if (acFactory instanceof Initializable) {
            ((Initializable) acFactory).init(preliminaryContext.getInitParams());
        }
        return acFactory;
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
                CONTAINER_KEY_INIT_PARAMETER);
        return key;
    }

    /**
     * Creates a Tiles container. If you override this class, please override
     * this method or {@link #createContainerFactory(TilesApplicationContext)}.
     *
     * @param context The servlet context to use.
     * @return The created container.
     * @since 2.1.2
     */
    protected TilesContainer createContainer(TilesApplicationContext context) {
        AbstractTilesContainerFactory factory = createContainerFactory(context);
        return factory.createContainer(context);
    }

    /**
     * Creates a Tiles container factory. If you override this class, please
     * override this method or {@link #createContainer(TilesApplicationContext)}.
     *
     * @param context The servlet context to use.
     * @return The created container factory.
     * @since 2.1.2
     */
    protected AbstractTilesContainerFactory createContainerFactory(
            TilesApplicationContext context) {
        AbstractTilesContainerFactory factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        return factory;
    }
}
