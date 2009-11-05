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
package org.apache.tiles.access;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provides static access to the tiles container.
 *
 * @version $Rev$ $Date$
 */
public final class TilesAccess {

    /**
     * Constructor, private to avoid instantiation.
     */
    private TilesAccess() {
    }

    /**
     * The name of the attribute to use when getting and setting the container
     * object in a context.
     */
    public static final String CONTAINER_ATTRIBUTE =
        "org.apache.tiles.CONTAINER";

    /**
     * Configures the default container to be used in the application.
     *
     * @param context The Tiles application context object to use.
     * @param container The container object to set.
     * @since 2.1.2
     */
    public static void setContainer(TilesApplicationContext context,
            TilesContainer container) {
        setContainer(context, container, CONTAINER_ATTRIBUTE);
    }

    /**
     * Configures the container to be used in the application.
     *
     * @param context The Tiles application context object to use.
     * @param container The container object to set.
     * @param key The key under which the container will be stored.
     * @since 2.1.2
     */
    public static void setContainer(TilesApplicationContext context,
            TilesContainer container, String key) {
        Logger log = LoggerFactory.getLogger(TilesAccess.class);
        if (key == null) {
            key = CONTAINER_ATTRIBUTE;
        }

        if (container == null) {
            if (log.isInfoEnabled()) {
                log.info("Removing TilesContext for context: " + context.getClass().getName());
            }
            context.getApplicationScope().remove(key);
        } else {
            if (log.isInfoEnabled()) {
                log.info("Publishing TilesContext for context: " + context.getClass().getName());
            }
            context.getApplicationScope().put(key, container);
        }
    }

    /**
     * Returns default the container to be used in the application.
     *
     * @param context The Tiles application context object to use.
     * @return The default container object.
     * @since 3.0.0
     */
    public static TilesContainer getContainer(TilesApplicationContext context) {
        return getContainer(context, CONTAINER_ATTRIBUTE);
    }

    /**
     * Returns the container to be used in the application registered under a specific key.
     *
     * @param context The Tiles application context object to use.
     * @param key The key under which the container will be stored.
     * @return The container object.
     * @since 3.0.0
     */
    public static TilesContainer getContainer(TilesApplicationContext context,
            String key) {
        if (key == null) {
            key = CONTAINER_ATTRIBUTE;
        }

        return (TilesContainer) context.getApplicationScope().get(key);
    }
}
