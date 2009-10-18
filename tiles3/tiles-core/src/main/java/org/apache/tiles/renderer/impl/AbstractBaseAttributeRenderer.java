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
package org.apache.tiles.renderer.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.AttributeEvaluatorFactoryAware;
import org.apache.tiles.renderer.AttributeRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base abstract class that manages authorization to display the attribute.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public abstract class AbstractBaseAttributeRenderer implements
        AttributeRenderer, TilesRequestContextFactoryAware,
        TilesApplicationContextAware, AttributeEvaluatorFactoryAware {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(AbstractBaseAttributeRenderer.class);

    /**
     * The Tiles request context factory.
     *
     * @since 2.1.1
     */
    protected TilesRequestContextFactory contextFactory;

    /**
     * The Tiles application context.
     *
     * @since 2.1.0
     */
    protected TilesApplicationContext applicationContext;

    /**
     * The attribute evaluator factory.
     *
     * @since 2.2.0
     */
    protected AttributeEvaluatorFactory attributeEvaluatorFactory;

    /** {@inheritDoc} */
    public void setRequestContextFactory(TilesRequestContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    /** {@inheritDoc} */
    public void setApplicationContext(TilesApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /** {@inheritDoc} */
    public void setAttributeEvaluatorFactory(AttributeEvaluatorFactory attributeEvaluatorFactory) {
        this.attributeEvaluatorFactory = attributeEvaluatorFactory;
    }

    /** {@inheritDoc} */
    public void render(Attribute attribute, TilesRequestContext request) throws IOException {
        if (!isPermitted(request, attribute.getRoles())) {
            if (log.isDebugEnabled()) {
                log.debug("Access to attribute denied.  User not in role '"
                        + attribute.getRoles() + "'");
            }
            return;
        }

        AttributeEvaluator evaluator = attributeEvaluatorFactory
                .getAttributeEvaluator(attribute);
        Object value = evaluator.evaluate(attribute, request);

        write(value, attribute, request);
    }

    /**
     * Implement this method knowing that the attribute won't be null and it
     * will be authorized.
     * @param value The value of the attribute to be rendered.
     * @param attribute The attribute to render.
     * @param request The Tiles request object.
     * @throws IOException If something goes wrong during rendition.
     * @since 2.1.2
     */
    public abstract void write(Object value, Attribute attribute,
            TilesRequestContext request)
            throws IOException;

    /**
     * Creates a Tiles request context from request items.
     *
     * @param requestItems The request items.
     * @return The created Tiles request context.
     * @since 2.1.0
     */
    protected TilesRequestContext getRequestContext(Object... requestItems) {
        return contextFactory.createRequestContext(applicationContext,
                requestItems);
    }

    /**
     * Checks if the current user is in one of the comma-separated roles
     * specified in the <code>role</code> parameter.
     *
     * @param request The request context.
     * @param roles The list of roles.
     * @return <code>true</code> if the current user is in one of those roles.
     * @since 2.1.0
     */
    protected boolean isPermitted(TilesRequestContext request, Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return true;
        }

        boolean retValue = false;

        for (Iterator<String> roleIt = roles.iterator(); roleIt.hasNext()
                && !retValue;) {
            retValue = request.isUserInRole(roleIt.next());
        }

        return retValue;
    }
}
