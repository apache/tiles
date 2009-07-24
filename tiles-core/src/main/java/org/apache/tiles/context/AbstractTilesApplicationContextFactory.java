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
package org.apache.tiles.context;

import java.util.Map;

import org.apache.tiles.Initializable;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.reflect.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract factory to create factories for {@link TilesApplicationContext}.
 *
 * @version $Rev$ $Date$
 * @since 2.1.1
 * @deprecated Create an instance of {@link TilesApplicationContext} yourself,
 * by implementing {@link org.apache.tiles.startup.TilesInitializer} or
 * extending {@link org.apache.tiles.startup.AbstractTilesInitializer} and
 * overriding <code>createTilesApplicationContext</code> method.
 */
public abstract class AbstractTilesApplicationContextFactory  {

    /**
     * Initialization parameter that represents the context factory class name.
     *
     * @since 2.1.1
     */
    public static final String APPLICATION_CONTEXT_FACTORY_INIT_PARAM =
        "org.apache.tiles.context.AbstractTilesApplicationContextFactory";

    /**
     * Create a TilesApplicationContext for the given context.
     *
     * @param context The (application) context to use.
     * @return TilesApplicationContext The Tiles application context.
     * @since 2.1.1
     */
    public abstract TilesApplicationContext createApplicationContext(
            Object context);

    /**
     * Creates the Tiles application context factory.
     *
     * @param preliminaryContext The preliminary {@link TilesApplicationContext}
     * that allows access to the initialization parameters.
     * @return The factory.
     * @since 2.1.1
     */
    public static AbstractTilesApplicationContextFactory createFactory(
            TilesApplicationContext preliminaryContext) {
        Logger log = LoggerFactory
                .getLogger(AbstractTilesApplicationContextFactory.class);
        AbstractTilesApplicationContextFactory retValue;

        if (log.isInfoEnabled()) {
            log.info("Initializing Tiles2 application context. . .");
        }

        Map<String, String> params = preliminaryContext.getInitParams();

        String className = params.get(APPLICATION_CONTEXT_FACTORY_INIT_PARAM);

        if (className != null) {
            retValue = (AbstractTilesApplicationContextFactory) ClassUtil
                    .instantiate(className);
        } else {
            retValue = new ChainedTilesApplicationContextFactory();
        }

        if (retValue instanceof Initializable) {
            ((Initializable) retValue).init(params);
        }

        if (log.isInfoEnabled()) {
            log.info("Finished initializing Tiles2 application context.");
        }

        return retValue;
    }
}
