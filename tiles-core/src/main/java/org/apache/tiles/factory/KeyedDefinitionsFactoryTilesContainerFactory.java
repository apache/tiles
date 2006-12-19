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
package org.apache.tiles.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer;
import org.apache.tiles.impl.mgmt.CachingKeyedDefinitionsFactoryTilesContainerFactory;
import org.apache.tiles.mgmt.MutableTilesContainer;

public class KeyedDefinitionsFactoryTilesContainerFactory extends
		TilesContainerFactory {
    
    public static final String CONTAINER_KEYS_INIT_PARAM =
        "org.apache.tiles.CONTAINER_KEYS";

    @Override
    public MutableTilesContainer createMutableTilesContainer(Object context) throws TilesException {
        CachingKeyedDefinitionsFactoryTilesContainerFactory container =
            new CachingKeyedDefinitionsFactoryTilesContainerFactory();
        return container;
    }

    @Override
    public TilesContainer createTilesContainer(Object context) throws TilesException {
        KeyedDefinitionsFactoryTilesContainer container =
            new KeyedDefinitionsFactoryTilesContainer();
        return container;
    }
    
    @Override
    protected void storeContainerDependencies(Object context,
            BasicTilesContainer container) throws TilesException {
        super.storeContainerDependencies(context, container);
        String keysString = getInitParameter(context,
                CONTAINER_KEYS_INIT_PARAM);
        if (keysString != null
                && container instanceof KeyedDefinitionsFactoryTilesContainer) {
            String[] keys = keysString.split(",");
            Map<String, String> initParams = new HashMap<String, String>();
            for (int i=0; i < keys.length; i++) {
                String param = getInitParameter(context,
                        BasicTilesContainer.DEFINITIONS_CONFIG + "@" + keys[i]);
                if (param != null) {
                    initParams.put(BasicTilesContainer.DEFINITIONS_CONFIG,
                            param);
                }

                DefinitionsFactory defsFactory =
                    (DefinitionsFactory) createFactory(context,
                            DEFINITIONS_FACTORY_INIT_PARAM, defaults);
                ((KeyedDefinitionsFactoryTilesContainer) container)
                        .setDefinitionsFactory(keys[i], defsFactory,
                                initParams);
            }
        }
    }
}
