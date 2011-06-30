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
package org.apache.tiles.factory;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.ApplicationContext;

/**
 * Abstract Factory that creates instances of {@link TilesContainerFactory}.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public abstract class AbstractTilesContainerFactory {

    /**
     * Initialization parameter that represents the container factory class
     * name.
     *
     * @since 2.1.0
     */
    public static final String CONTAINER_FACTORY_INIT_PARAM =
        "org.apache.tiles.factory.AbstractTilesContainerFactory";

    /**
     * Creates a Tiles container.
     *
     * @param applicationContext The Tiles application context object.
     * @return The created container.
     * @throws TilesContainerFactoryException If something goes wrong during
     * instantiation.
     * @since 2.1.1
     */
    public abstract TilesContainer createContainer(ApplicationContext applicationContext);
}
