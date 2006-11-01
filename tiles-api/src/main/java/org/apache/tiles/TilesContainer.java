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

import javax.servlet.jsp.PageContext;

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
     * @param context
     * @throws TilesException
     */
    void init(TilesApplicationContext context) throws TilesException;

    /**
     * Retrieve the containers context.
     * @return
     */
    TilesApplicationContext getApplicationContext();

    /**
     * 
     * @param request
     * @param response
     * @param definition
     */
    void prepare(Object request, Object response, String definition) throws TilesException;

    /**
     * 
     * @param pageContext
     * @param definition
     * @throws TilesException
     */
    void prepare(PageContext pageContext, String definition) throws TilesException;


    /**
     * Render the given tiles request
     *
     * @param request
     */
    void render(Object request, Object response, String definition) throws TilesException;

    /**
     *
     * @param pageContext
     * @param definition
     * @throws TilesException
     */
    void render(PageContext pageContext, String definition) throws TilesException;
}
