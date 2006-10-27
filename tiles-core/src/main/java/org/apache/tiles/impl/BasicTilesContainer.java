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
package org.apache.tiles.impl;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesConfig;
import org.apache.tiles.TilesRequestContext;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.definition.UrlDefinitionsFactory;

import java.util.Map;

/**
 * TODO Flush out the basic container implementation.
 */
public class BasicTilesContainer implements TilesContainer {

    private TilesConfig config;

    public void init(TilesConfig config) {
        this.config = config;
        initializeDefinitionsFactory();

    }

    public TilesApplicationContext getApplicationContext() {
        return config.getApplicationContext();
    }

    public void render(TilesRequestContext request) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    void initializeDefinitionsFactory() {
        Map init = config.getInitParameters();
        String className = (String)init.get(TilesConfig.DEFINITIONS_FACTORY_CLASS_NAME_ATTR_KEY);
        if(className == null) {
            className = UrlDefinitionsFactory.class.getName();
        }
        //. . .
    }
}
