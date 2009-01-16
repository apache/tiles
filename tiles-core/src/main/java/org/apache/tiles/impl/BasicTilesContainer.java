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
package org.apache.tiles.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.BasicAttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.preparer.NoSuchPreparerException;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Basic implementation of the tiles container interface.
 * In most cases, this container will be customized by
 * injecting customized services, not necessarily by
 * override the container
 *
 * @since 2.0
 * @version $Rev$ $Date$
 */
public class BasicTilesContainer implements TilesContainer {

    /**
     * Constant representing the configuration parameter used to define the
     * tiles definition resources.
     *
     * @deprecated Use
     * {@link org.apache.tiles.definition.DefinitionsFactory#DEFINITIONS_CONFIG}.
     */
    public static final String DEFINITIONS_CONFIG = "org.apache.tiles.impl.BasicTilesContainer.DEFINITIONS_CONFIG";

    /**
     * Compatibility constant.
     *
     * @deprecated use {@link #DEFINITIONS_CONFIG} to avoid namespace collisions.
     */
    private static final String LEGACY_DEFINITIONS_CONFIG = "definitions-config";

    /**
     * Name used to store attribute context stack.
     */
    private static final String ATTRIBUTE_CONTEXT_STACK =
        "org.apache.tiles.AttributeContext.STACK";

    /**
     * Log instance for all BasicTilesContainer
     * instances.
     */
    private final Log log =
        LogFactory.getLog(BasicTilesContainer.class);

    /**
     * The Tiles application context object.
     */
    private TilesApplicationContext context;

    /**
     * The definitions factory.
     */
    private DefinitionsFactory definitionsFactory;

    /**
     * The preparer factory.
     */
    private PreparerFactory preparerFactory;

    /**
     * The renderer factory.
     */
    private RendererFactory rendererFactory;

    /**
     * The attribute evaluator.
     */
    private AttributeEvaluator evaluator;

    /**
     * The Tiles request context factory.
     */
    private TilesRequestContextFactory contextFactory;

    /**
     * Initialization flag. If set, this container cannot be changed.
     */
    private boolean initialized = false;

    /**
     * Initialize the Container with the given configuration.
     *
     * @param initParameters application context for this container
     * @throws IllegalStateException If the container has been already
     * initialized.
     * @throws DefinitionsFactoryException If something goes wrong during
     * initialization.
     */
    public void init(Map<String, String> initParameters) {
        checkInit();
        initialized = true;

        if (rendererFactory == null) {
            throw new IllegalStateException("RendererFactory not specified");
        }
        if (preparerFactory == null) {
            throw new IllegalStateException("PreparerFactory not specified");
        }
        if (definitionsFactory == null) {
            throw new IllegalStateException("DefinitionsFactory not specified");
        }
        if (evaluator == null) {
            throw new IllegalStateException("AttributeEvaluator not specified");
        }
        if (contextFactory == null) {
            throw new IllegalStateException("TilesContextFactory not specified");
        }
        if (context == null) {
            throw new IllegalStateException("TilesApplicationContext not specified");
        }
    }

    /** {@inheritDoc} */
    public AttributeContext startContext(Object... requestItems) {
        TilesRequestContext tilesContext = getRequestContext(requestItems);
        return startContext(tilesContext);
    }

    /** {@inheritDoc} */
    public void endContext(Object... requestItems) {
        TilesRequestContext tilesContext = getRequestContext(requestItems);
        endContext(tilesContext);
    }

    /** {@inheritDoc} */
    public void renderContext(Object... requestItems) {
        TilesRequestContext request = getRequestContext(requestItems);
        AttributeContext attributeContext = getAttributeContext(request);

        if (!isPermitted(request, attributeContext.getRoles())) {
            if (log.isDebugEnabled()) {
                log.debug("Access to current attribute context denied. "
                        + "User not in role '" + attributeContext.getRoles());
            }
            return;
        }

        render(request, attributeContext);
    }

    /**
     * Returns the Tiles application context used by this container.
     *
     * @return the application context for this container.
     */
    public TilesApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * Sets the Tiles application context to use.
     *
     * @param context The Tiles application context.
     */
    public void setApplicationContext(TilesApplicationContext context) {
        this.context = context;
    }

    /** {@inheritDoc} */
    public AttributeContext getAttributeContext(Object... requestItems) {
        TilesRequestContext tilesContext = getRequestContext(requestItems);
        return getAttributeContext(tilesContext);

    }

    /**
     * Returns the context factory.
     *
     * @return Always <code>null</code>.
     * @deprecated Do not use it, it returns <code>null</code>. Use
     * {@link #getRequestContextFactory()}.
     */
    @Deprecated
    public org.apache.tiles.context.TilesContextFactory getContextFactory() {
        return null;
    }

    /**
     * Returns the request context factory.
     *
     * @return The request context factory.
     * @since 2.1.1
     */
    protected TilesRequestContextFactory getRequestContextFactory() {
        return contextFactory;
    }

    /**
     * Sets the context factory.
     *
     * @param contextFactory The context factory.
     * @deprecated Use
     * {@link #setRequestContextFactory(TilesRequestContextFactory)}.
     */
    public void setContextFactory(org.apache.tiles.context.TilesContextFactory contextFactory) {
        // Does nothing
    }

    /**
     * Sets the request context factory.
     *
     * @param contextFactory The context factory.
     * @since 2.1.1
     */
    public void setRequestContextFactory(TilesRequestContextFactory contextFactory) {
        checkInit();
        this.contextFactory = contextFactory;
    }

    /**
     * Returns the definitions factory.
     *
     * @return The definitions factory used by this container.
     */
    public DefinitionsFactory getDefinitionsFactory() {
        return definitionsFactory;
    }

    /**
     * Set the definitions factory. This method first ensures
     * that the container has not yet been initialized.
     *
     * @param definitionsFactory the definitions factory for this instance.
     */
    public void setDefinitionsFactory(DefinitionsFactory definitionsFactory) {
        checkInit();
        this.definitionsFactory = definitionsFactory;
    }

    /**
     * Returns the preparer factory used by this container.
     *
     * @return return the preparerInstance factory used by this container.
     */
    public PreparerFactory getPreparerFactory() {
        return preparerFactory;
    }

    /**
     * Set the preparerInstance factory.  This method first ensures
     * that the container has not yet been initialized.
     *
     * @param preparerFactory the preparerInstance factory for this conainer.
     */
    public void setPreparerFactory(PreparerFactory preparerFactory) {
        this.preparerFactory = preparerFactory;
    }

    /**
     * Sets the renderer instance factory.
     *
     * @param rendererFactory the renderer instance factory for this container.
     * @since 2.1.0
     */
    public void setRendererFactory(RendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
    }

    /**
     * Sets the evaluator to use.
     *
     * @param evaluator The evaluator to use.
     * @since 2.1.0
     */
    public void setEvaluator(AttributeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    /** {@inheritDoc} */
    public void prepare(String preparer, Object... requestItems) {
        TilesRequestContext requestContext = getRequestContextFactory().createRequestContext(
            getApplicationContext(),
            requestItems
        );
        prepare(requestContext, preparer, false);
    }

    /** {@inheritDoc} */
    public void render(String definitionName, Object... requestItems) {
        TilesRequestContext requestContext = getRequestContextFactory().createRequestContext(
            getApplicationContext(),
            requestItems
        );
        render(requestContext, definitionName);
    }

    /** {@inheritDoc} */
    public void render(Attribute attr, Writer writer, Object... requestItems)
        throws IOException {
        if (attr == null) {
            throw new CannotRenderException("Cannot render a null attribute");
        }

        AttributeRenderer renderer = rendererFactory.getRenderer(attr
                .getRenderer());
        if (renderer == null) {
            throw new CannotRenderException(
                    "Cannot render an attribute with renderer name "
                            + attr.getRenderer());
        }
        renderer.render(attr, writer, requestItems);
    }

    /** {@inheritDoc} */
    public Object evaluate(Attribute attribute, Object... requestItems) {
        TilesRequestContext request = getRequestContextFactory()
                .createRequestContext(context, requestItems);
        return evaluator.evaluate(attribute, request);
    }

    /** {@inheritDoc} */
    public boolean isValidDefinition(String definitionName, Object... requestItems) {
        return isValidDefinition(getRequestContext(requestItems), definitionName);
    }

    /**
     * Returns a definition specifying its name.
     *
     * @param definitionName The name of the definition to find.
     * @param request The request context.
     * @return The definition, if found.
     * @throws DefinitionsFactoryException If the definitions factory throws an
     * exception.
     */
    protected Definition getDefinition(String definitionName,
            TilesRequestContext request) {
        Definition definition =
            definitionsFactory.getDefinition(definitionName, request);
        return definition;
    }

    /**
     * Derive the resource string from the initialization parameters.
     * If no parameter {@link #DEFINITIONS_CONFIG} is available, attempts
     * to retrieve {@link #LEGACY_DEFINITIONS_CONFIG}.  If niether are
     * available, returns "/WEB-INF/tiles.xml".
     *
     * @return resource string to be parsed.
     */
    protected String getResourceString() {
        return getResourceString(context.getInitParams());
    }

    /**
     * Derive the resource string from the initialization parameters.
     * If no parameter {@link #DEFINITIONS_CONFIG} is available, attempts
     * to retrieve {@link #LEGACY_DEFINITIONS_CONFIG}.  If niether are
     * available, returns "/WEB-INF/tiles.xml".
     *
     * @param parms The initialization parameters.
     * @return resource string to be parsed.
     */
    protected String getResourceString(Map<String, String> parms) {
        String resourceStr = parms.get(DEFINITIONS_CONFIG);
        if (resourceStr == null) {
            resourceStr = parms.get(LEGACY_DEFINITIONS_CONFIG);
        }
        if (resourceStr == null) {
            resourceStr = "/WEB-INF/tiles.xml";
        }
        return resourceStr;
    }

    /**
     * Parse the resourceString into a list of resource paths
     * which can be loaded by the application context.
     *
     * @param resourceString comma seperated resources
     * @return parsed resources
     */
    protected List<String> getResourceNames(String resourceString) {
        StringTokenizer tokenizer = new StringTokenizer(resourceString, ",");
        List<String> filenames = new ArrayList<String>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            filenames.add(tokenizer.nextToken().trim());
        }
        return filenames;
    }

    /**
     * Determine whether or not the container has been
     * initialized. Utility method used for methods which
     * can not be invoked after the container has been
     * started.
     *
     * @throws IllegalStateException if the container has already been initialized.
     */
    protected void checkInit() {
        if (initialized) {
            throw new IllegalStateException("Container allready initialized");
        }
    }

    /**
     * Initializes a definitions factory.
     *
     * @param definitionsFactory The factory to initialize.
     * @param resourceString The string containing a comma-separated-list of
     * resources.
     * @param initParameters A map containing the initialization parameters.
     * @throws DefinitionsFactoryException If something goes wrong.
     * @deprecated Do not use, the Definitions Factory should be initialized by
     * the Tiles Container Factory.
     */
    @Deprecated
    protected void initializeDefinitionsFactory(
            DefinitionsFactory definitionsFactory, String resourceString,
            Map<String, String> initParameters) {
        if (rendererFactory == null) {
            throw new IllegalStateException("No RendererFactory found");
        }

        definitionsFactory.init(initParameters);

        if (log.isInfoEnabled()) {
            log.info("Tiles2 container initialization complete.");
        }
    }

    /**
     * Returns the context stack.
     *
     * @param tilesContext The Tiles context object to use.
     * @return The needed stack of contexts.
     * @since 2.0.6
     */
    @SuppressWarnings("unchecked")
    protected Stack<AttributeContext> getContextStack(TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack =
            (Stack<AttributeContext>) tilesContext
                .getRequestScope().get(ATTRIBUTE_CONTEXT_STACK);
        if (contextStack == null) {
            contextStack = new Stack<AttributeContext>();
            tilesContext.getRequestScope().put(ATTRIBUTE_CONTEXT_STACK,
                    contextStack);
        }

        return contextStack;
    }

    /**
     * Pushes a context object in the stack.
     *
     * @param context The context to push.
     * @param tilesContext The Tiles context object to use.
     * @since 2.0.6
     */
    protected void pushContext(AttributeContext context,
            TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack = getContextStack(tilesContext);
        contextStack.push(context);
    }

    /**
     * Pops a context object out of the stack.
     *
     * @param tilesContext The Tiles context object to use.
     * @return The popped context object.
     * @since 2.0.6
     */
    protected AttributeContext popContext(TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack = getContextStack(tilesContext);
        return contextStack.pop();
    }

    /**
     * Get attribute context from request.
     *
     * @param tilesContext current Tiles application context.
     * @return BasicAttributeContext or null if context is not found.
     * @since 2.0.6
     */
    protected AttributeContext getContext(TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack = getContextStack(tilesContext);
        if (!contextStack.isEmpty()) {
            return contextStack.peek();
        } else {
            return null;
        }
    }

    /**
     * Returns the current attribute context.
     *
     * @param tilesContext The request context to use.
     * @return The current attribute context.
     */
    private AttributeContext getAttributeContext(TilesRequestContext tilesContext) {
        AttributeContext context = getContext(tilesContext);
        if (context == null) {
            context = new BasicAttributeContext();
            pushContext(context, tilesContext);
        }
        return context;
    }

    /**
     * Creates a Tiles request context from request items.
     *
     * @param requestItems The request items.
     * @return The created Tiles request context.
     */
    private TilesRequestContext getRequestContext(Object... requestItems) {
        return getRequestContextFactory().createRequestContext(
                getApplicationContext(), requestItems);
    }

    /**
     * Starts an attribute context inside the container.
     *
     * @param tilesContext The request context to use.
     * @return The newly created attribute context.
     */
    private AttributeContext startContext(TilesRequestContext tilesContext) {
        AttributeContext context = new BasicAttributeContext();
        Stack<AttributeContext>  stack = getContextStack(tilesContext);
        if (!stack.isEmpty()) {
            AttributeContext parent = stack.peek();
            context.inheritCascadedAttributes(parent);
        }
        stack.push(context);
        return context;
    }

    /**
     * Releases and removes a previously created attribute context.
     *
     * @param tilesContext The request context to use.
     */
    private void endContext(TilesRequestContext tilesContext) {
        popContext(tilesContext);
    }

    /**
     * Execute a preparer.
     *
     * @param context The request context.
     * @param preparerName The name of the preparer.
     * @param ignoreMissing If <code>true</code> if the preparer is not found,
     * it ignores the problem.
     * @throws NoSuchPreparerException If the preparer is not found (and
     * <code>ignoreMissing</code> is not set) or if the preparer itself threw an
     * exception.
     */
    private void prepare(TilesRequestContext context, String preparerName, boolean ignoreMissing) {

        if (log.isDebugEnabled()) {
            log.debug("Prepare request received for '" + preparerName);
        }

        ViewPreparer preparer = preparerFactory.getPreparer(preparerName, context);
        if (preparer == null && ignoreMissing) {
            return;
        }

        if (preparer == null) {
            throw new NoSuchPreparerException("Preparer '" + preparerName + " not found");
        }

        AttributeContext attributeContext = getContext(context);

        preparer.execute(context, attributeContext);
    }

    /**
     * Renders the specified definition.
     *
     * @param request The request context.
     * @param definitionName The name of the definition to render.
     * @throws NoSuchDefinitionException If the definition has not been found.
     * @throws DefinitionsFactoryException If something goes wrong when
     * obtaining the definition.
     */
    private void render(TilesRequestContext request, String definitionName) {

        if (log.isDebugEnabled()) {
            log.debug("Render request recieved for definition '" + definitionName + "'");
        }

        Definition definition = getDefinition(definitionName, request);

        if (definition == null) {
            if (log.isWarnEnabled()) {
                String message = "Unable to find the definition '" + definitionName + "'";
                log.warn(message);
            }
            throw new NoSuchDefinitionException(definitionName);
        }

        AttributeContext originalContext = getAttributeContext(request);
        BasicAttributeContext subContext = new BasicAttributeContext(originalContext);
        subContext.inherit(definition);

        if (!isPermitted(request, subContext.getRoles())) {
            if (log.isDebugEnabled()) {
                log.debug("Access to definition '" + definitionName
                        + "' denied.  User not in role '"
                        + definition.getRoles());
            }
            return;
        }

        pushContext(subContext, request);

        try {
            render(request, subContext);
        } finally {
            popContext(request);
        }
    }

    /**
     * Renders the specified attribute context.
     *
     * @param request The request context.
     * @param attributeContext The context to render.
     * @throws InvalidTemplateException If the template is not valid.
     * @throws CannotRenderException If something goes wrong during rendering.
     */
    private void render(TilesRequestContext request,
            AttributeContext attributeContext) {

        try {
            if (attributeContext.getPreparer() != null) {
                prepare(request, attributeContext.getPreparer(), true);
            }

            String dispatchPath = computeDispatchPath(request, attributeContext);

            if (log.isDebugEnabled()) {
                log.debug("Dispatching to definition path '"
                        + attributeContext.getTemplate() + " '");
            }
            request.dispatch(dispatchPath);
        } catch (IOException e) {
            throw new CannotRenderException(e.getMessage(), e);
        }
    }

    /**
     * Calculates the dispatch path, that will be the path it will be dispatched to.
     *
     * @param request The Tiles request.
     * @param attributeContext The Tiles attribute context.
     * @return The calculated dispatch path.
     * @throws InvalidTemplateException If the template is not valid.
     */
    private String computeDispatchPath(TilesRequestContext request,
            AttributeContext attributeContext) {
        String dispatchPath = attributeContext.getTemplate();

        if (dispatchPath != null) {
            return dispatchPath;
        }

        String expression = attributeContext.getTemplateExpression();
        if (expression != null) {
            Object value = evaluator.evaluate(expression, request);

            if (value != null) {
                if (value instanceof String) {
                    return (String) value;
                } else {
                    throw new InvalidTemplateException(
                            "Cannot render a template that is not an object: "
                                    + value.toString());
                }
            } else {
                throw new InvalidTemplateException(
                "Cannot render a null template");
            }
        } else {
            throw new InvalidTemplateException(
                    "No template or template expression has been specified");
        }
    }

    /**
     * Checks if the current user is in one of the comma-separated roles
     * specified in the <code>role</code> parameter.
     *
     * @param request The request context.
     * @param roles The list of roles.
     * @return <code>true</code> if the current user is in one of those roles.
     */
    private boolean isPermitted(TilesRequestContext request, Set<String> roles) {
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

    /**
     * Checks if a string is a valid definition name.
     *
     * @param context The request context.
     * @param definitionName The name of the definition to find.
     * @return <code>true</code> if <code>definitionName</code> is a valid
     * definition name.
     */
    private boolean isValidDefinition(TilesRequestContext context, String definitionName) {
        try {
            Definition definition = getDefinition(definitionName, context);
            return definition != null;
        } catch (NoSuchDefinitionException nsde) {
            return false;
        } catch (DefinitionsFactoryException e) {
            // TODO, is this the right thing to do?
            return false;
        }
    }
}
