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
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Attribute;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesException;
import org.apache.tiles.awareness.TilesContextFactoryAware;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorAware;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererException;

/**
 * Base abstract class that manages authorization to display the attribute.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public abstract class AbstractBaseAttributeRenderer implements
        AttributeRenderer, TilesContextFactoryAware, AttributeEvaluatorAware {

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory
            .getLog(AbstractBaseAttributeRenderer.class);

    /**
     * The Tiles context factory.
     *
     * @since 2.1.0
     */
    protected TilesContextFactory contextFactory;

    /**
     * The Tiles application context.
     *
     * @since 2.1.0
     */
    protected TilesApplicationContext applicationContext;

    /**
     * The attribute evaluator.
     *
     * @since 2.1.0
     */
    protected AttributeEvaluator evaluator;

    /** {@inheritDoc} */
    public void setContextFactory(TilesContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    /** {@inheritDoc} */
    public void setApplicationContext(TilesApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /** {@inheritDoc} */
    public void setEvaluator(AttributeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    /** {@inheritDoc} */
    public void render(Attribute attribute, Writer writer,
            Object... requestItems) throws IOException, TilesException {
        TilesRequestContext request = getRequestContext(requestItems);

        if (attribute == null) {
            throw new RendererException("Cannot render a null attribute");
        }

        if (!isPermitted(request, attribute.getRoles())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Access to attribute denied.  User not in role '"
                        + attribute.getRoles() + "'");
            }
            return;
        }

        Object value = attribute.getValue();

        if (attribute.getValue() instanceof String) {
            value = evaluator.evaluate((String) value, request);
        }

        write(value, attribute, writer, request, requestItems);
    }

    /**
     * Implement this method knowing that the attribute won't be null and it
     * will be authorized.
     * @param value The value of the attribute to be rendered.
     * @param attribute The attribute to render.
     * @param writer The writer to use.
     * @param request The Tiles request object.
     * @param requestItems The original request items.
     *
     * @throws IOException If something goes wrong during rendition.
     * @throws TilesException If something goes wrong in Tiles engine.
     * @since 2.1.0
     */
    public abstract void write(Object value, Attribute attribute,
            Writer writer, TilesRequestContext request, Object... requestItems)
            throws IOException, TilesException;

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
