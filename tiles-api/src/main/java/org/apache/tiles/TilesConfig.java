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

import java.util.Map;
import java.util.HashMap;

/**
 * Encapsulation of configuration parameters for the
 * tiles system.
 *
 * @since 2.0
 * @version $Rev$
 */
public class TilesConfig {

    public static final String DEFINITIONS_FACTORY_CLASS_NAME_ATTR_KEY =
        "org.apache.tiles.DEFINITIONS_FACTORY";


    private TilesApplicationContext applicationContext;

    private Map initParameters = new HashMap();


    /**
     * The application context for this container.
     * Default instances are provided by the
     * tiles-impl module.
     *
     * @return context for the configured container.
     * @see TilesApplicationContext
     */
    public TilesApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Standard Setter.
     *
     * @param applicationContext context for the configured container.
     *
     */
    public void setApplicationContext(TilesApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    /**
     * Retrieve an imuutable map of the initialization
     * parameters.
     *
     * These parameters will be used to identify spi
     * implementations as well as to configure them.
     * Custom Parameters should be properly scoped in order
     * to avoid collisions with other spi implementation
     * attributes.
     *
     * @return map of all init parameters.
     */    public Map getInitParameters() {
        return initParameters;
    }

    /**
     * Retrieve an initialization parameter.
     *
     * @param name parameter name
     * @return named parameter value
     * @see #getInitParameters()
     */
    public String getInitParameter(String name) {
        return (String)initParameters.get(name);
    }

    /**
     * Standard Setter.
     *
     * @param name parameter key
     * @param value parameter value
     * @see #getInitParameters()
     */
    public void setInitParameter(String name, String value) {
        initParameters.put(name, value);
    }



}
