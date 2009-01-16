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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
     * The logging object.
     */
    private final Log log = LogFactory
            .getLog(ChainedTilesRequestContextFactory.class);

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
    @SuppressWarnings("unchecked")
    public void init(Map<String, String> configParameters) {
        String[] classNames = null;
        String classNamesString = configParameters.get(FACTORY_CLASS_NAMES);
        if (classNamesString != null) {
            classNames = classNamesString.split("\\s*,\\s*");
        }
        if (classNames == null || classNames.length <= 0) {
            classNames = DEFAULT_FACTORY_CLASS_NAMES;
        }

        factories = new ArrayList<TilesRequestContextFactory>();
        for (int i = 0; i < classNames.length; i++) {
            try {
                Class<TilesRequestContextFactory> clazz = (Class<TilesRequestContextFactory>) Class
                        .forName(classNames[i]);
                if (TilesRequestContextFactory.class.isAssignableFrom(clazz)) {
                    TilesRequestContextFactory factory = clazz.newInstance();
                    factories.add(factory);
                } else {
                    throw new IllegalArgumentException("The class "
                            + classNames[i]
                            + " does not implement TilesRequestContextFactory");
                }
            } catch (ClassNotFoundException e) {
                // We log it, because it could be a default configuration class that
                // is simply not present.
                log.warn("Cannot find TilesRequestContextFactory class "
                        + classNames[i]);
                if (log.isDebugEnabled()) {
                    log.debug("Cannot find TilesRequestContextFactory class "
                            + classNames[i], e);
                }
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(
                        "Cannot instantiate TilesRequestContextFactory class " + classNames[i],
                        e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(
                        "Cannot access TilesRequestContextFactory class " + classNames[i]
                                + " default constructor", e);
            }
        }
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
