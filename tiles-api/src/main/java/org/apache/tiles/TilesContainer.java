/*
 * $Id$
 *
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tiles;

/**
 * An encapsulation of the tiles framework.  This interface is
 * used to expose tiles features to frameworks which leverage
 * it as a plugin.  It can alternately be used by web applications
 * which would like a programmatic interface.
 * 
 * @since 2.0
 * @version $Rev$
 */
public interface TilesContainer {

    /**
     * Initialize the container with the given
     * configuration.
     * 
     * @param config
     */
    void init(TilesConfig config);

    /**
     * Retrieve the containers context.
     */
    TilesApplicationContext getApplicationContext();

    /**
     * Render the given tiles request
     *
     * @param request
     */
    void render(TilesRequestContext request);

}
