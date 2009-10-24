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
package org.apache.tiles.factory;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.Initializable;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.awareness.TilesContainerAware;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.context.AbstractTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.AttributeEvaluatorFactoryAware;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.preparer.BasicPreparerFactory;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.reflect.ClassUtil;
import org.apache.tiles.renderer.RendererFactory;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory provided for convenience. This factory creates a default
 * implementation of the container, initializes, and puts it into service. Uses
 * initialization parameters to configure all the objects created in this phase.
 *
 * @version $Rev$ $Date$
 * @since 2.0
 * @deprecated Please extend {@link AbstractTilesContainerFactory} or use an
 * already extended class. Parameter-based initialization is no longer
 * supported.
 */
@Deprecated
public class TilesContainerFactory extends AbstractTilesContainerFactory {

    /**
     * Initialization parameter that represents the container factory class
     * name.
     *
     * @deprecated Use {@link AbstractTilesContainerFactory#CONTAINER_FACTORY_INIT_PARAM}.
     */
    @Deprecated
	public static final String CONTAINER_FACTORY_INIT_PARAM =
        "org.apache.tiles.factory.TilesContainerFactory";

    /**
     * Initialization parameter that indicates if the container factory is
     * mutable.
     */
    public static final String CONTAINER_FACTORY_MUTABLE_INIT_PARAM =
        "org.apache.tiles.factory.TilesContainerFactory.MUTABLE";

    /**
     * Initialization parameter that represents the context factory class name.
     *
     * @deprecated Use {@link AbstractTilesApplicationContextFactory#APPLICATION_CONTEXT_FACTORY_INIT_PARAM} or
     * {@link #REQUEST_CONTEXT_FACTORY_INIT_PARAM}.
     */
    @Deprecated
	public static final String CONTEXT_FACTORY_INIT_PARAM =
        "org.apache.tiles.context.TilesContextFactory";

    /**
     * Initialization parameter that represents the context factory class name.
     *
     * @since 2.1.1
     */
    public static final String REQUEST_CONTEXT_FACTORY_INIT_PARAM =
        "org.apache.tiles.context.TilesRequestContextFactory";

    /**
     * Initialization parameter that represents the definitions factory class
     * name.
     */
    public static final String DEFINITIONS_FACTORY_INIT_PARAM =
        "org.apache.tiles.definition.DefinitionsFactory";

    /**
     * Initialization parameter that represents the preparer factory class name.
     */
    public static final String PREPARER_FACTORY_INIT_PARAM =
        "org.apache.tiles.preparer.PreparerFactory";

    /**
     * Initialization parameter that represents the renderer factory class name.
     * @since 2.1.0
     */
    public static final String RENDERER_FACTORY_INIT_PARAM =
        "org.apache.tiles.renderer.RendererFactory";

    /**
     * Initialization parameter that represents the attribute evaluator class
     * name.
     *
     * @since 2.1.0
     */
    public static final String ATTRIBUTE_EVALUATOR_INIT_PARAM =
        "org.apache.tiles.evaluator.AttributeEvaluator";

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(TilesContainerFactory.class);

    /**
     * Default configuration parameters.
     */
    private static final Map<String, String> DEFAULTS =
        new HashMap<String, String>();

    static {
        DEFAULTS.put(AbstractTilesApplicationContextFactory.APPLICATION_CONTEXT_FACTORY_INIT_PARAM,
                ChainedTilesApplicationContextFactory.class.getName());
        DEFAULTS.put(REQUEST_CONTEXT_FACTORY_INIT_PARAM,
                ChainedTilesRequestContextFactory.class.getName());
        DEFAULTS.put(DEFINITIONS_FACTORY_INIT_PARAM, UrlDefinitionsFactory.class.getName());
        DEFAULTS.put(PREPARER_FACTORY_INIT_PARAM, BasicPreparerFactory.class.getName());
        DEFAULTS.put(RENDERER_FACTORY_INIT_PARAM, BasicRendererFactory.class.getName());
        DEFAULTS.put(ATTRIBUTE_EVALUATOR_INIT_PARAM, DirectAttributeEvaluator.class.getName());
    }

    /**
     * The default configuration to be used by the factory.
     */
    protected Map<String, String> defaultConfiguration =
        new HashMap<String, String>(DEFAULTS);

    /**
     * Retrieve a factory instance as configured through the specified context.
     * <p/> The context will be queried and if a init parameter named
     * 'org.apache.tiles.factory.TilesContainerFactory' is discovered this class
     * will be instantiated and returned. Otherwise, the factory will attempt to
     * utilize one of it's internal factories.
     *
     * @param context the executing applications context. Typically a
     * ServletContext or PortletContext
     * @return a tiles container
     * @throws TilesContainerFactoryException if an error occurs creating the
     * factory.
     * @since 2.1.0
     * @deprecated Use
     * {@link AbstractTilesContainerFactory#getTilesContainerFactory(TilesApplicationContext)}.
     */
    @Deprecated
    public static TilesContainerFactory getFactory(Object context) {
        return getFactory(context, DEFAULTS);
    }

    /**
     * Retrieve a factory instance as configured through the specified context.
     * <p/> The context will be queried and if a init parameter named
     * 'org.apache.tiles.factory.TilesContainerFactory' is discovered this class
     * will be instantiated and returned. Otherwise, the factory will attempt to
     * utilize one of it's internal factories.
     *
     * @param context the executing applications context. Typically a
     * ServletContext or PortletContext
     * @param defaults Default configuration parameters values, used if the
     * context object has not the corresponding parameters.
     * @return a tiles container
     * @throws TilesContainerFactoryException if an error occurs creating the
     * factory.
     * @deprecated Use
     * {@link AbstractTilesContainerFactory#getTilesContainerFactory(TilesApplicationContext)}
     * and then {@link #setDefaultConfiguration(Map)}.
     */
    @Deprecated
	public static TilesContainerFactory getFactory(Object context,
            Map<String, String> defaults) {
        Map<String, String> configuration = new HashMap<String, String>(defaults);
        configuration.putAll(TilesContainerFactory.getInitParameterMap(context));
        TilesContainerFactory factory =
            (TilesContainerFactory) TilesContainerFactory.createFactory(configuration,
                CONTAINER_FACTORY_INIT_PARAM);
        factory.setDefaultConfiguration(defaults);
        return factory;
    }

    /**
     * Creates a Tiles container.
     *
     * @param context The (application) context object.
     * @return The created container.
     * @throws TilesContainerFactoryException If something goes wrong during
     * instantiation.
     * @deprecated Use {@link #createContainer(TilesApplicationContext)}.
     */
    @Deprecated
    public TilesContainer createContainer(Object context) {
        if (context instanceof TilesApplicationContext) {
            return createContainer((TilesApplicationContext) context);
        }

        throw new UnsupportedOperationException("Class "
                + context.getClass().getName()
                + " not recognized a TilesApplicationContext");
    }

    /** {@inheritDoc} */
    @Override
	public TilesContainer createContainer(TilesApplicationContext context) {
        String value = context.getInitParams().get(
                CONTAINER_FACTORY_MUTABLE_INIT_PARAM);
        if (Boolean.parseBoolean(value)) {
            return createMutableTilesContainer(context);
        } else {
            return createTilesContainer(context);
        }
    }

    /**
     * Sets the default configuration parameters.
     *
     * @param defaultConfiguration The default configuration parameters.
     */
    public void setDefaultConfiguration(Map<String, String> defaultConfiguration) {
        if (defaultConfiguration != null) {
            this.defaultConfiguration.putAll(defaultConfiguration);
        }
    }

    /**
     * Sets one default configuration parameter value.
     *
     * @param key The key of the configuration parameter.
     * @param value The value of the configuration parameter.
     */
    public void setDefaultValue(String key, String value) {
        this.defaultConfiguration.put(key, value);
    }

    /**
     * Creates an immutable Tiles container.
     *
     * @param context The (application) context object.
     * @return The created Tiles container.
     * @throws TilesContainerFactoryException If something goes wrong during
     * initialization.
     * @deprecated Use {@link #createTilesContainer(TilesApplicationContext)}.
     */
    @Deprecated
    public TilesContainer createTilesContainer(Object context) {
        if (context instanceof TilesApplicationContext) {
            return createTilesContainer((TilesApplicationContext) context);
        }

        throw new UnsupportedOperationException("Class "
                + context.getClass().getName()
                + " not recognized a TilesApplicationContext");
    }

    /**
     * Creates an immutable Tiles container.
     *
     * @param context The Tiles application context object.
     * @return The created Tiles container.
     * @throws TilesContainerFactoryException If something goes wrong during
     * initialization.
     * @since 2.1.1
     */
    public TilesContainer createTilesContainer(TilesApplicationContext context) {
        BasicTilesContainer container = new BasicTilesContainer();
        initializeContainer(context, container);
        return container;
    }

    /**
     * Creates an immutable Tiles container.
     *
     * @param context The (application) context object.
     * @return The created Tiles container.
     * @throws TilesContainerFactoryException If something goes wrong during
     * initialization.
     * @deprecated Use
     * {@link #createMutableTilesContainer(TilesApplicationContext)}.
     */
    @Deprecated
    public TilesContainer createMutableTilesContainer(Object context) {
        if (context instanceof TilesApplicationContext) {
            return createMutableTilesContainer((TilesApplicationContext) context);
        }

        throw new UnsupportedOperationException("Class "
                + context.getClass().getName()
                + " not recognized a TilesApplicationContext");
    }

    /**
     * Creates a mutable Tiles container.
     *
     * @param context The Tiles application context object.
     * @return The created Tiles container.
     * @throws TilesContainerFactoryException If something goes wrong during
     * initialization.
     */
    public MutableTilesContainer createMutableTilesContainer(
            TilesApplicationContext context) {
        CachingTilesContainer container = new CachingTilesContainer();
        initializeContainer(context, container);
        return container;
    }

    /**
     * Initializes a container.
     *
     * @param context The (application) context object to use.
     * @param container The container to be initialized.
     * @throws TilesContainerFactoryException If something goes wrong during
     * initialization.
     * @deprecated Use
     * {@link #initializeContainer(TilesApplicationContext, BasicTilesContainer)}.
     */
    @Deprecated
    protected void initializeContainer(Object context,
            BasicTilesContainer container) {
        if (context instanceof TilesApplicationContext) {
            initializeContainer((TilesApplicationContext) context, container);
        }

        throw new UnsupportedOperationException("Class "
                + context.getClass().getName()
                + " not recognized a TilesApplicationContext");
    }

    /**
     * Initializes a container.
     *
     * @param context The Tiles application context object to use.
     * @param container The container to be initialized.
     * @throws TilesContainerFactoryException If something goes wrong during
     * initialization.
     */
    protected void initializeContainer(TilesApplicationContext context,
            BasicTilesContainer container) {
        log.warn("DEPRECATION WARNING! You are using parameter-based initialization, "
                + "that is no longer supported! Please see docs: "
                + "http://tiles.apache.org/framework/tutorial/configuration.html");

        Map <String, String> initParameterMap;

        if (log.isInfoEnabled()) {
            log.info("Initializing Tiles2 container. . .");
        }

        initParameterMap = context.getInitParams();
        Map<String, String> configuration = new HashMap<String, String>(defaultConfiguration);
        configuration.putAll(initParameterMap);
        storeContainerDependencies(context, initParameterMap, configuration, container);
        container.init(initParameterMap);

        if (log.isInfoEnabled()) {
            log.info("Tiles2 container initialized");
        }
    }

    /**
     * Stores container dependencies, that is called before
     * {@link TilesContainer#init(Map)}.
     *
     * @param context The (application) context object to use.
     * @param initParameters The initialization parameters.
     * @param configuration The merged configuration parameters (both defaults
     * and context ones).
     * @param container The container to use.
     * @throws TilesContainerFactoryException If something goes wrong during
     * initialization.
     * @deprecated Use
     * {@link #storeContainerDependencies(TilesApplicationContext, Map, Map, BasicTilesContainer)}
     * .
     */
    @Deprecated
	protected void storeContainerDependencies(Object context,
            Map<String, String> initParameters,
            Map<String, String> configuration, BasicTilesContainer container) {
        if (context instanceof TilesApplicationContext) {
            storeContainerDependencies((TilesApplicationContext) context,
                    initParameters, configuration, container);
        }

        throw new UnsupportedOperationException("Class "
                + context.getClass().getName()
                + " not recognized a TilesApplicationContext");
    }

    /**
     * Stores container dependencies, that is called before
     * {@link TilesContainer#init(Map)}.
     *
     * @param context The (application) context object to use.
     * @param initParameters The initialization parameters.
     * @param configuration The merged configuration parameters (both defaults
     * and context ones).
     * @param container The container to use.
     * @throws TilesContainerFactoryException If something goes wrong during
     * initialization.
     */
    protected void storeContainerDependencies(TilesApplicationContext context,
            Map<String, String> initParameters,
            Map<String, String> configuration, BasicTilesContainer container) {
        AbstractTilesApplicationContextFactory contextFactory =
            (AbstractTilesApplicationContextFactory) createFactory(configuration,
                AbstractTilesApplicationContextFactory.APPLICATION_CONTEXT_FACTORY_INIT_PARAM);
        if (contextFactory instanceof Initializable) {
            ((Initializable) contextFactory).init(configuration);
        }

        TilesRequestContextFactory requestContextFactory =
            (TilesRequestContextFactory) createFactory(configuration,
                REQUEST_CONTEXT_FACTORY_INIT_PARAM);
        requestContextFactory.init(configuration);

        RendererFactory rendererFactory =
            (RendererFactory) createFactory(configuration,
                RENDERER_FACTORY_INIT_PARAM);

        AttributeEvaluator evaluator = (AttributeEvaluator) createFactory(
                configuration, ATTRIBUTE_EVALUATOR_INIT_PARAM);
        AttributeEvaluatorFactory attributeEvaluatorFactory = new BasicAttributeEvaluatorFactory(
                evaluator);

        if (evaluator instanceof TilesApplicationContextAware) {
            ((TilesApplicationContextAware) evaluator)
                    .setApplicationContext(context);
        }

        if (evaluator instanceof TilesContainerAware) {
            ((TilesContainerAware) evaluator).setContainer(container);
        }

        evaluator.init(configuration);

        if (rendererFactory instanceof TilesRequestContextFactoryAware) {
            ((TilesRequestContextFactoryAware) rendererFactory)
                    .setRequestContextFactory(requestContextFactory);
        }

        if (rendererFactory instanceof TilesApplicationContextAware) {
            ((TilesApplicationContextAware) rendererFactory)
                    .setApplicationContext(context);
        }

        if (rendererFactory instanceof TilesContainerAware) {
            ((TilesContainerAware) rendererFactory).setContainer(container);
        }

        if (rendererFactory instanceof AttributeEvaluatorFactoryAware) {
            ((AttributeEvaluatorFactoryAware) rendererFactory)
                    .setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        }
        rendererFactory.init(initParameters);

        PreparerFactory prepFactory =
            (PreparerFactory) createFactory(configuration,
                PREPARER_FACTORY_INIT_PARAM);

        postCreationOperations(requestContextFactory, context, rendererFactory,
                evaluator, initParameters, configuration, container);

        container.setRequestContextFactory(requestContextFactory);
        container.setPreparerFactory(prepFactory);
        container.setApplicationContext(context);
        container.setRendererFactory(rendererFactory);
        container.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
    }

    /**
     * After the creation of the elements, it is possible to do other operations that
     * will be done after the creation and before the assignment to the container.
     *
     * @param contextFactory The Tiles context factory.
     * @param tilesContext The Tiles application context.
     * @param rendererFactory The renderer factory.
     * @param evaluator The attribute evaluator.
     * @param initParameters The initialization parameters.
     * @param configuration The merged configuration parameters (both defaults
     * and context ones).
     * @param container The container to use.
     * @since 2.1.1
     */
    protected void postCreationOperations(TilesRequestContextFactory contextFactory,
            TilesApplicationContext tilesContext,
            RendererFactory rendererFactory, AttributeEvaluator evaluator,
            Map<String, String> initParameters,
            Map<String, String> configuration, BasicTilesContainer container) {
        DefinitionsFactory defsFactory =
            (DefinitionsFactory) createFactory(configuration,
                DEFINITIONS_FACTORY_INIT_PARAM);
        if (defsFactory instanceof TilesApplicationContextAware) {
            ((TilesApplicationContextAware) defsFactory)
                    .setApplicationContext(tilesContext);
        }

        defsFactory.init(configuration);

        container.setDefinitionsFactory(defsFactory);
    }

    /**
     * Creates a factory instance.
     *
     * @param configuration The merged configuration parameters (both defaults
     * and context ones).
     * @param initParameterName The initialization parameter name from which the
     * class name is got.
     * @return The created factory.
     * @throws TilesContainerFactoryException If something goes wrong during
     * creation.
     */
    protected static Object createFactory(Map<String, String> configuration,
            String initParameterName) {
        String factoryName = resolveFactoryName(configuration, initParameterName);
        return ClassUtil.instantiate(factoryName);
    }

    /**
     * Resolves a factory class name.
     *
     * @param configuration The merged configuration parameters (both defaults
     * and context ones).
     * @param parameterName The name of the initialization parameter to use.
     * @return The factory class name.
     * @throws TilesContainerFactoryException If something goes wrong during
     * resolution.
     */
    protected static String resolveFactoryName(
            Map<String, String> configuration, String parameterName) {
        Object factoryName = configuration.get(parameterName);
        return factoryName == null
            ? DEFAULTS.get(parameterName)
            : factoryName.toString();
    }
    /**
     * Returns the value of an initialization parameter.
     *
     * @param context The (application) context object to use.
     * @param parameterName The parameter name to retrieve.
     * @return The parameter value.
     * @throws TilesContainerFactoryException If the context has not been
     * recognized.
     * @deprecated Do not use.
     */
    @Deprecated
    protected static String getInitParameter(Object context,
            String parameterName) {
        Object value;
        Class<?> contextClass = context.getClass();
        Method getInitParameterMethod = ClassUtil
                .getForcedAccessibleMethod(contextClass,
                        "getInitParameter", String.class);
        value = ClassUtil.invokeMethod(context, getInitParameterMethod,
                parameterName);

        return value == null ? null : value.toString();
    }


    /**
     * Returns a map containing parameters name-value entries.
     *
     * @param context The (application) context object to use.
     * @return The initialization parameters map.
     * @throws TilesContainerFactoryException If the context object has not been
     * recognized.
     * @deprecated Do not use.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    protected static Map<String, String> getInitParameterMap(Object context) {
        Map<String, String> initParameters = new HashMap<String, String>();
        Class<?> contextClass = context.getClass();
        Method method = ClassUtil.getForcedAccessibleMethod(contextClass,
                "getInitParameterNames");
        Enumeration<String> e = (Enumeration<String>) ClassUtil
                .invokeMethod(context, method);

        method = ClassUtil.getForcedAccessibleMethod(contextClass,
                "getInitParameter", String.class);
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            initParameters.put(key, (String) ClassUtil.invokeMethod(
                    context, method, key));
        }

        return initParameters;
    }
}
