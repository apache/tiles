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

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer.KeyExtractor;
import org.apache.tiles.impl.mgmt.CachingKeyedDefinitionsFactoryTilesContainer;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.util.ClassUtil;

/**
 * Factory that creates instances of container that will extend the
 * {@link KeyedDefinitionsFactoryTilesContainer} class.
 *
 * @version $Rev$ $Date$
 */
public class KeyedDefinitionsFactoryTilesContainerFactory extends
        TilesContainerFactory {

    /**
     * The name of the initialization parameter that will contain a
     * comma-separated list of keys to use.
     */
    public static final String CONTAINER_KEYS_INIT_PARAM =
        "org.apache.tiles.factory.KeyedDefinitionsFactoryTilesContainerFactory.KEYS";

    /**
     * Init parameter name that contains the class name for the key extractor.
     */
    public static final String KEY_EXTRACTOR_CLASS_INIT_PARAM =
        "org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer.KeyExtractor";

    /** {@inheritDoc} */
    @Override
    public MutableTilesContainer createMutableTilesContainer(Object context) throws TilesException {
        CachingKeyedDefinitionsFactoryTilesContainer container =
            new CachingKeyedDefinitionsFactoryTilesContainer();
        initializeContainer(context, container);
        return container;
    }

    /** {@inheritDoc} */
    @Override
    public TilesContainer createTilesContainer(Object context) throws TilesException {
        KeyedDefinitionsFactoryTilesContainer container =
            new KeyedDefinitionsFactoryTilesContainer();
        initializeContainer(context, container);
        return container;
    }

    // FIXME Probably we should create some sort of "FactoryUtils" to create
    // factories dynamically depending on a configuration.
    // I think this method does not belong here.
    /**
     * Creates a definitions factory.
     * @param context The context object to use.
     * @return The newly created definitions factory.
     * @throws TilesException If something goes wrong.
     */
    public DefinitionsFactory createDefinitionsFactory(Object context)
            throws TilesException {
        DefinitionsFactory retValue;
        Map<String, String> config = new HashMap<String, String>(defaultConfiguration);
        config.putAll(getInitParameterMap(context));
        retValue = (DefinitionsFactory) createFactory(config,
                    DEFINITIONS_FACTORY_INIT_PARAM);

        return retValue;
    }

    /** {@inheritDoc} */
    @Override
    protected void storeContainerDependencies(Object context,
            Map<String, String> initParameters,
            Map<String, String> configuration, BasicTilesContainer container)
            throws TilesException {
        super.storeContainerDependencies(context, initParameters, configuration, container);

        String keyExtractorClassName = configuration.get(
                KEY_EXTRACTOR_CLASS_INIT_PARAM);
        if (keyExtractorClassName != null
                && container instanceof KeyedDefinitionsFactoryTilesContainer) {
            ((KeyedDefinitionsFactoryTilesContainer) container).setKeyExtractor(
                    (KeyExtractor) ClassUtil.instantiate(keyExtractorClassName));
        }

        String keysString = initParameters.get(CONTAINER_KEYS_INIT_PARAM);
        if (keysString != null
                && container instanceof KeyedDefinitionsFactoryTilesContainer) {
            String[] keys = keysString.split(",");
            Map<String, String> config = new HashMap<String, String>(defaultConfiguration);
            config.putAll(getInitParameterMap(context));
            for (int i = 0; i < keys.length; i++) {
                Map<String, String> initParams = new HashMap<String, String>();
                String param = initParameters.get(
                        KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX + keys[i]);
                if (param != null) {
                    initParams.put(BasicTilesContainer.DEFINITIONS_CONFIG,
                            param);
                }

                DefinitionsFactory defsFactory =
                    (DefinitionsFactory) createFactory(config,
                            DEFINITIONS_FACTORY_INIT_PARAM);
                ((KeyedDefinitionsFactoryTilesContainer) container)
                        .setDefinitionsFactory(keys[i], defsFactory,
                                initParams);
            }
        }
    }
}
