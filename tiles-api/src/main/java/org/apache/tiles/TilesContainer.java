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
package org.apache.tiles;

import javax.servlet.jsp.PageContext;
import java.util.Map;
import java.io.IOException;

/**
 * An encapsulation of the tiles framework.  This interface is
 * used to expose tiles features to frameworks which leverage
 * it as a plugin.  It can alternately be used by web applications
 * which would like a programmatic interface.
 *
 * @since 2.0
 * @version $Rev$ $Date$
 */
public interface TilesContainer {

    /**
     * Initialize the container with the given
     * configuration.
     *
     * @param initParameters application context
     * @throws TilesException when an initialization error occurs
     */
    void init(Map<String, String> initParameters) throws TilesException;

    /**
     * Retrieve the containers context.
     *
     * @return current application context
     */
    TilesApplicationContext getApplicationContext();

    /**
     * Retrive the component context of the current request.
     * @param request the current request.
     * @param response the current reponse.
     * @return map of the attributes in the current component context.
     */
    ComponentContext getComponentContext(Object request, Object response);

    /**
     * Retrieve the component context of the current request
     * @param context the current request.
     * @return map of the attributes in the current component context.
     */
    ComponentContext getComponentContext(PageContext context);

    /**
     * Creates a new component context from the current request
     * @return map of the attributes in the current component context.
     */
    ComponentContext createComponentContext();

    /**
     * @param request the current request
     * @param response the current response
     * @param definition the requested definition
     * @throws TilesException is processing fails.
     */
    void prepare(Object request, Object response, String definition) throws TilesException;

    /**
     * @param pageContext the current pageContext
     * @param definition the current definition
     * @throws TilesException is processing fails.
     */
    void prepare(PageContext pageContext, String definition) throws TilesException;


    /**
     * @param request the current request
     * @param response the current response
     * @param componentContext the current component context
     * @param definition the requested definition
     * @throws TilesException is processing fails.
     */
    void prepare(Object request, Object response, ComponentContext componentContext,
    		String definition) throws TilesException;

    /**
     * @param pageContext the current pageContext
     * @param definition the current definition
     * @param componentContext the current component context
     * @throws TilesException is processing fails.
     */
    void prepare(PageContext pageContext, ComponentContext componentContext,
    		String definition) throws TilesException;

    /**
     * Render the given tiles request
     *
     * @param request the current request
     * @param response the current response
     * @param definition the current definition
     * @throws TilesException is processing fails.
     */
    void render(Object request, Object response, String definition) throws TilesException;

    /**
     * @param pageContext the current pageContext.
     * @param definition the requested definition.
     * @throws TilesException is processing fails.
     */
    void render(PageContext pageContext, String definition) throws TilesException;

    /**
     * Render the given ComponentAttribute.
     * @param pageContext
     * @param attribute
     * @throws TilesException
     */
    void render(PageContext pageContext, ComponentAttribute attribute)
        throws TilesException, IOException;

    /**
     * Render the given tiles request
     *
     * @param request the current request
     * @param response the current response
     * @param definition the current definition
     * @throws TilesException is processing fails.
     */
    void render(Object request, Object response, ComponentContext componentContext,
    		String definition) throws TilesException;

    /**
     * @param pageContext the current pageContext.
     * @param definition the requested definition.
     * @throws TilesException is processing fails.
     */
    void render(PageContext pageContext, ComponentContext componentContext,
    		String definition) throws TilesException;

    /**
     * Render the given ComponentAttribute.
     * @param pageContext
     * @param attribute
     * @throws TilesException
     */
    void render(PageContext pageContext, ComponentContext componentContext,
    		ComponentAttribute attribute) throws TilesException, IOException;

    /**
     * Determine whether or not the definition exists.
     * 
     * @param pageContext the current page context
     * @param definition the name of the definition.
     * @return true if the definition is found.
     */
    boolean isValidDefinition(PageContext pageContext, String definition);

    /**
     * Determine whether or not the definition exists.
     *
     * @param request the current request
     * @param response the current response
     * @param definition the name of the definition.
     * @return true if the definition is found.
     */
    boolean isValidDefinition(Object request, Object response, String definition);
}
