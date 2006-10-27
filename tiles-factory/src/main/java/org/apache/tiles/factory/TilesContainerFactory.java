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
package org.apache.tiles.factory;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesConfig;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;

/**
 * Factory provided for convenience.
 * This factory creates a default implementation of
 * the container, initializes, and puts it into service.
 *
 * @since 2.0
 * @version $Rev$
 *
 */
public class TilesContainerFactory {

    public static TilesContainer createContainer(Object context)
        throws ConfigurationNotSupportedException {

        TilesContainer container = new BasicTilesContainer();

        TilesApplicationContext tilesContext =
            TilesContextFactory.createApplicationContext(context);

        TilesConfig config = new TilesConfig();
        config.setApplicationContext(tilesContext);
        config.setInitParameter(
            TilesConfig.DEFINITIONS_FACTORY_CLASS_NAME_ATTR_KEY,
            UrlDefinitionsFactory.class.getName()
        );

        container.init(config);
        return container;
    }
}
