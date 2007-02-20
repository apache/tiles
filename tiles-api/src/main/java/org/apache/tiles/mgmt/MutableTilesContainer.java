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
 *
 */
package org.apache.tiles.mgmt;

import javax.servlet.jsp.PageContext;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;

/**
 * Defines a mutable version of the TilesContainer.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public interface MutableTilesContainer extends TilesContainer {

    /**
     * Register a new definition with the container.
     *
     * @param definition
     * @param context The PageContext to use
     */
    void register(TileDefinition definition, PageContext context)
            throws TilesException;

    /**
     * Register a new definition with the container.
     *
     * @param definition
     * @param request The request object to use.
     * @param response The response object to use.
     */
    void register(TileDefinition definition, Object request, Object response)
            throws TilesException;
}
