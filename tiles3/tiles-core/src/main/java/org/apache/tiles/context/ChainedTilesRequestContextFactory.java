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

package org.apache.tiles.context;

import java.util.Iterator;
import java.util.List;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;

/**
 * Default implementation for TilesRequestContextFactory, that creates a chain
 * of sub-factories, trying each one until it returns a not-null value.
 *
 * @version $Rev$ $Date$
 * @since 2.1.1
 */
public class ChainedTilesRequestContextFactory implements TilesRequestContextFactory {

    /**
     * Factory class names initialization parameter to use.
     *
     * @since 2.1.1
     */
    public static final String FACTORY_CLASS_NAMES =
        "org.apache.tiles.context.ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES";

    /**
     * The default class names to instantiate that compose the chain..
     *
     * @since 2.1.1
     */
    public static final String[] DEFAULT_FACTORY_CLASS_NAMES = {
            "org.apache.tiles.servlet.context.ServletTilesRequestContextFactory",
            "org.apache.tiles.portlet.context.PortletTilesRequestContextFactory",
            "org.apache.tiles.jsp.context.JspTilesRequestContextFactory" };

    /**
     * The Tiles context factories composing the chain.
     */
    private List<TilesRequestContextFactory> factories;

    /**
     * Sets the factories to be used.
     *
     * @param factories The factories to be used.
     */
    public void setFactories(List<TilesRequestContextFactory> factories) {
        this.factories = factories;
        injectParentTilesContextFactory();
    }

    /** {@inheritDoc} */
    public TilesRequestContext createRequestContext(
            TilesApplicationContext context, Object... requestItems) {
        TilesRequestContext retValue = null;

        for (Iterator<TilesRequestContextFactory> factoryIt = factories
                .iterator(); factoryIt.hasNext() && retValue == null;) {
            retValue = factoryIt.next().createRequestContext(context,
                    requestItems);
        }

        if (retValue == null) {
            throw new IllegalArgumentException(
                    "Cannot find a factory to create the request context");
        }

        return retValue;
    }

    /**
     * Injects this context factory to all chained context factories.
     */
    protected void injectParentTilesContextFactory() {
        for (TilesRequestContextFactory factory : factories) {
            if (factory instanceof TilesRequestContextFactoryAware) {
                ((TilesRequestContextFactoryAware) factory)
                        .setRequestContextFactory(this);
            }
        }
    }
}
