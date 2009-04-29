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

import org.apache.tiles.Initializable;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.awareness.TilesContainerAware;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorAware;
import org.apache.tiles.reflect.ClassUtil;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererException;
import org.apache.tiles.renderer.RendererFactory;

/**
 * Basic renderer factory implementation.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class BasicRendererFactory implements RendererFactory,
        TilesContainerAware, TilesRequestContextFactoryAware,
        TilesApplicationContextAware, AttributeEvaluatorAware {

    /**
     * The type renderers init parameter name.
     *
     * @since 2.1.0
     */
    public static final String TYPE_RENDERERS_INIT_PARAM =
        "org.apache.tiles.renderer.impl.BasicRendererFactory.TYPE_RENDERERS";

    /**
     * The default renderer init parameter.
     *
     * @since 2.1.0
     */
    public static final String DEFAULT_RENDERER_INIT_PARAM =
        "org.apache.tiles.rendere.impl.BasicRendererFactory.DEFAULT_RENDERER";

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
     * The Tiles container.
     *
     * @since 2.1.0
     */
    protected TilesContainer container;

    /**
     * The attribute evaluator.
     *
     * @since 2.1.0
     */
    protected AttributeEvaluator evaluator;

    /**
     * The renderer name/renderer map.
     *
     * @since 2.1.0
     */
    protected Map<String, AttributeRenderer> renderers;

    /**
     * Initialization parameters, as passed by the constructor.
     */
    private Map<String, String> initParameters;

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

    /**
     * Constructor.
     *
     * @since 2.1.0
     */
    public BasicRendererFactory() {
        renderers = new HashMap<String, AttributeRenderer>();
    }

    /** {@inheritDoc} */
    public void init(Map<String, String> parameters) {
        this.initParameters = parameters;
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
            String[] pairs = typeRenderersParam.split("\\s*;\\s*");
            for (int i = 0; i < pairs.length; i++) {
                String[] pair = pairs[i].split("\\s*,\\s*");
                if (pair == null || pair.length != 2) {
                    throw new RendererException("The string '" + pairs[i]
                            + "' is not a valid type-renderer pair");
                }
                completeParams.put(pair[0], pair[1]);
            }
        }

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
                retValue = (AttributeRenderer) ClassUtil.instantiate(name);
                initializeRenderer(retValue);
                renderers.put(name, retValue);
            }
        } else {
            retValue = defaultRenderer;
        }

        return retValue;
    }

    /**
     * Sets the default renderer.
     *
     * @param renderer The default renderer.
     * @since 2.1.0
     */
    public void setDefaultRenderer(AttributeRenderer renderer) {
        this.defaultRenderer = renderer;
    }

    /**
     * Registers a renderer.
     *
     * @param name The name of the renderer.
     * @param renderer The renderer to register.
     * @since 2.1.0
     */
    public void registerRenderer(String name, AttributeRenderer renderer) {
        renderers.put(name, renderer);
    }

    /** {@inheritDoc} */
    public void setContainer(TilesContainer container) {
        this.container = container;
    }

    /** {@inheritDoc} */
    public void setEvaluator(AttributeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    /** {@inheritDoc} */
    public void setRequestContextFactory(
            TilesRequestContextFactory contextFactory) {
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
        if (renderer instanceof TilesRequestContextFactoryAware) {
            ((TilesRequestContextFactoryAware) renderer)
                    .setRequestContextFactory(contextFactory);
        }
        if (renderer instanceof TilesApplicationContextAware) {
            ((TilesApplicationContextAware) renderer)
                    .setApplicationContext(applicationContext);
        }
        if (renderer instanceof TilesContainerAware) {
            ((TilesContainerAware) renderer).setContainer(container);
        }
        if (renderer instanceof AttributeEvaluatorAware) {
            ((AttributeEvaluatorAware) renderer).setEvaluator(evaluator);
        }
        if (renderer instanceof Initializable && initParameters != null) {
            ((Initializable) renderer).init(initParameters);
        }
    }
}
