/*
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
package org.apache.tiles.impl.mgmt;

import org.apache.tiles.TilesException;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.mgmt.TileDefinition;
import org.apache.tiles.mgmt.MutableTilesContainer;

/**
 * Mutable container which caches (in memory) the definitions
 * registered to it.  If a definition is not found in cache, it
 * will revert back to it's definitions factory.
 *
 * @since Tiles 2.0
 * @version $Rev$
 */
public class CachingTilesContainer extends BasicTilesContainer
    implements MutableTilesContainer {

    private DefinitionManager mgr = new DefinitionManager();

    public void register(TileDefinition definition)
        throws TilesException {
        ComponentDefinition def = new ComponentDefinition(definition);
        mgr.addDefinition(def);
    }

}
