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

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.TilesRuntimeException;
import org.apache.tiles.awareness.TilesContainerAware;
import org.apache.tiles.awareness.TilesContextFactoryAware;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererException;
import org.apache.tiles.renderer.RendererFactory;
import org.apache.tiles.util.ClassUtil;

/**
 * Basic renderer factory implementation.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class BasicRendererFactory implements RendererFactory,
        TilesContainerAware, TilesContextFactoryAware {

    /**
     * The type renderers init parameter name.
     *
     * @since 2.1.0
     */
    public static final String TYPE_RENDERERS_INIT_PARAM =
        "org.apache.tiles.renderer.impl.BasicRendereFactory.TYPE_RENDERERS";

    /**
     * The default renderer init parameter.
     *
     * @since 2.1.0
     */
    public static final String DEFAULT_RENDERER_INIT_PARAM =
        "org.apache.tiles.rendere.impl.BasicRendereFactory.DEFAULT_RENDERER";

    /**
     * The default renderer class name.
     *
     * @since 2.1.0
     */
    public static final String DEFAULT_RENDERER_CLASS_NAME =
        UntypedAttributeRenderer.class.getName();

    /**
     * The default renderer name/renderer class map.
     *
     * @since 2.1.0
     */
    protected static final Map<String, String> DEFAULT_TYPE_2_RENDERER;

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
     * The Tiles container.
     *
     * @since 2.1.0
     */
    protected TilesContainer container;

    /**
     * The renderer name/renderer map.
     *
     * @since 2.1.0
     */
    protected Map<String, AttributeRenderer> renderers;

    /**
     * The default renderer.
     *
     * @since 2.1.0
     */
    protected AttributeRenderer defaultRenderer;

    static {
        DEFAULT_TYPE_2_RENDERER = new HashMap<String, String>();
        DEFAULT_TYPE_2_RENDERER.put("string", StringAttributeRenderer.class
                .getName());
        DEFAULT_TYPE_2_RENDERER.put("definition",
                DefinitionAttributeRenderer.class.getName());
        DEFAULT_TYPE_2_RENDERER.put("template", TemplateAttributeRenderer.class
                .getName());
    }

    /** {@inheritDoc} */
    public void init(Map<String, String> parameters) throws TilesException {
        String defaultRendererParam = parameters.get(DEFAULT_RENDERER_INIT_PARAM);
        if (defaultRendererParam == null) {
            defaultRendererParam = DEFAULT_RENDERER_CLASS_NAME;
        }
        defaultRenderer = (AttributeRenderer) ClassUtil
                .instantiate(defaultRendererParam);
        initializeRenderer(defaultRenderer);
        String typeRenderersParam = parameters.get(TYPE_RENDERERS_INIT_PARAM);
        Map<String, String> completeParams = new HashMap<String, String>(
                DEFAULT_TYPE_2_RENDERER);
        if (typeRenderersParam != null) {
            String[] pairs = typeRenderersParam.split("\\s*;\\s");
            for (int i = 0; i < pairs.length; i++) {
                String[] pair = pairs[i].split("\\s*,\\s*");
                if (pair == null || pair.length != 2) {
                    throw new RendererException("The string '" + pairs[i]
                            + "' is not a valid type-renderer pair");
                }
                completeParams.put(pair[0], pair[1]);
            }
        }

        renderers = new HashMap<String, AttributeRenderer>();
        for (Map.Entry<String, String> entry : completeParams.entrySet()) {
            AttributeRenderer renderer = (AttributeRenderer) ClassUtil
                    .instantiate(entry.getValue());
            initializeRenderer(renderer);
            renderers.put(entry.getKey(), renderer);
        }
    }

    /** {@inheritDoc} */
    public AttributeRenderer getRenderer(String name) {
        AttributeRenderer retValue;
        if (name != null) {
            retValue = renderers.get(name);
            if (retValue == null) {
                try {
                    retValue = (AttributeRenderer) ClassUtil.instantiate(name);
                    initializeRenderer(retValue);
                } catch (TilesException e) {
                    throw new TilesRuntimeException(
                            "Cannot instantiate renderer " + name, e);
                }
                renderers.put(name, retValue);
            }
        } else {
            retValue = defaultRenderer;
        }

        return retValue;
    }

    /** {@inheritDoc} */
    public void setContainer(TilesContainer container) {
        this.container = container;
    }

    /** {@inheritDoc} */
    public void setContextFactory(TilesContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    /** {@inheritDoc} */
    public void setApplicationContext(TilesApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Initialize a renderer, by injecting dependencies.
     *
     * @param renderer The renderer to initialize.
     * @since 2.1.0
     */
    protected void initializeRenderer(AttributeRenderer renderer) {
        if (renderer instanceof TilesContextFactoryAware) {
            TilesContextFactoryAware cfaRenderer = (TilesContextFactoryAware) renderer;
            cfaRenderer.setApplicationContext(applicationContext);
            cfaRenderer.setContextFactory(contextFactory);
        }
        if (renderer instanceof TilesContainerAware) {
            ((TilesContainerAware) renderer).setContainer(container);
        }
    }
}
