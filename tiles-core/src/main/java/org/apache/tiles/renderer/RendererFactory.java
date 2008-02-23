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
package org.apache.tiles.renderer;

import java.util.Map;

import org.apache.tiles.TilesException;

/**
 * <p>
 * Factory interface used to create/retrieve instances of the
 * {@link AttributeRenderer} interface.
 * </p>
 *
 * <p>
 * This factory provides an extension point into the default tiles
 * implementation. Implementors wishing to provide per request initialization of
 * the AttributeRenderer (for instance) may provide a custom renderer.
 * </p>
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public interface RendererFactory {

    /**
     * Initialize the renderer factory.
     *
     * @param parameters The parameters for the initialization.
     * @throws TilesException If something goes wrong during initialization.
     * @since 2.1.0
     */
    void init(Map<String, String> parameters) throws TilesException;

    /**
     * Returns a renderer by its name.
     *
     * @param name The name of the renderer.
     * @return The renderer.
     * @since 2.1.0
     */
    AttributeRenderer getRenderer(String name);
}
