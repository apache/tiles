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
package org.apache.tiles;

import java.io.IOException;

import org.apache.tiles.request.ApplicationContext;

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
     * Retrieve the containers context.
     *
     * @return current application context
     */
    ApplicationContext getApplicationContext();

    /**
     * Retrive the attribute context of the current request.
     * @param requestItems the current request objects.
     * @return map of the attributes in the current attribute context.
     */
    AttributeContext getAttributeContext(Object... requestItems);

    /**
     * Starts a new context, where attribute values are stored independently
     * from others.<br>
     * When the use of the contexts is finished, call
     * {@link TilesContainer#endContext(Object...)}
     *
     * @param requestItems the current request objects.
     * @return The newly created context.
     */
    AttributeContext startContext(Object... requestItems);

    /**
     * Ends a context, where attribute values are stored independently
     * from others.<br>
     * It must be called after a
     * {@link TilesContainer#startContext(Object...)} call.
     *
     * @param requestItems the current request objects.
     */
    void endContext(Object... requestItems);

    /**
     * Renders the current context, as it is.
     *
     * @param requestItems the current request objects.
     * @since 2.1.0
     */
    void renderContext(Object... requestItems);

    /**
     * Executes a preparer.
     *
     * @param preparer The name of the preparer to execute.
     * @param requestItems the current request objects.
     */
    void prepare(String preparer, Object... requestItems);

    /**
     * Render the given tiles request.
     *
     * @param definition the current definition.
     * @param requestItems the current request objects.
     */
    void render(String definition, Object... requestItems);

    /**
     * Render the given Attribute.
     *
     * @param attribute The attribute to render.
     * @param requestItems the current request objects.
     * @throws IOException If something goes wrong during writing to the output.
     * @since 2.1.2
     */
    void render(Attribute attribute, Object... requestItems)
        throws IOException;

    /**
     * Evaluates the given attribute.
     *
     * @param attribute The attribute to evaluate.
     * @param requestItems the current request objects.
     * @return The evaluated object.
     * @since 2.1.0
     */
    Object evaluate(Attribute attribute, Object... requestItems);

    /**
     * Determine whether or not the definition exists.
     *
     * @param definition the name of the definition.
     * @param requestItems the current request objects.
     *
     * @return true if the definition is found.
     */
    boolean isValidDefinition(String definition, Object... requestItems);
}
