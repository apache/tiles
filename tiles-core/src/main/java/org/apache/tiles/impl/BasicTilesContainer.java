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
package org.apache.tiles.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.BasicComponentContext;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.preparer.NoSuchPreparerException;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.preparer.ViewPreparer;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     * Constant representing the configuration parameter
     * used to define the tiles definition resources.
     */
    public static final String DEFINITIONS_CONFIG = "org.apache.tiles.DEFINITIONS_CONFIG";

    /**
     * Compatibility constant.
     *
     * @deprecated use {@link #DEFINITIONS_CONFIG} to avoid namespace collisions.
     */
    private static final String LEGACY_DEFINITIONS_CONFIG = "definitions-config";

    /**
     * Log instance for all BasicTilesContainer
     * instances.
     */
    private static final Log LOG =
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
     * The Tiles context factory.
     */
    private TilesContextFactory contextFactory;

    /**
     * Initialization flag. If set, this container cannot be changed.
     */
    private boolean initialized = false;

    /**
     * Initialize the Container with the given configuration.
     *
     * @param initParameters application context for this container
     * @throws TilesException
     */
    public void init(Map<String, String> initParameters) throws TilesException {
        checkInit();
        initialized = true;
        if (LOG.isInfoEnabled()) {
            LOG.info("Initializing Tiles2 container. . .");
        }

        //Everything is now initialized.  We will populate
        // our definitions
        initializeDefinitionsFactory(definitionsFactory, getResourceString(),
        		initParameters);
    }

    /** {@inheritDoc} */
	public ComponentContext startContext(Object... requestItems) {
        TilesRequestContext tilesContext = getRequestContext(requestItems);
		return startContext(tilesContext);
	}

    /** {@inheritDoc} */
    public void endContext(Object... requestItems) {
        TilesRequestContext tilesContext = getRequestContext(requestItems);
        endContext(tilesContext);
    }
    
    /**
     * Starts a component context inside the container.
     *
     * @param tilesContext The request context to use.
     * @return The newly created component context.
     */
    private ComponentContext startContext(TilesRequestContext tilesContext) {
        ComponentContext context = new BasicComponentContext();
        BasicComponentContext.pushContext(context, tilesContext);
        return context;
    }

    /**
     * Releases and removes a previously created component context.
     *
     * @param tilesContext The request context to use.
     */
    private void endContext(TilesRequestContext tilesContext) {
        BasicComponentContext.popContext(tilesContext);
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
     * @param definitionsFactory The factory to initializes. 
     * @param resourceString The string containing a comma-separated-list of
     * resources.
     * @param initParameters A map containing the initialization parameters.
     * @throws TilesException If something goes wrong.
     */
    protected void initializeDefinitionsFactory(
    		DefinitionsFactory definitionsFactory, String resourceString,
    		Map<String, String> initParameters) throws TilesException {
        List<String> resources = getResourceNames(resourceString);
        definitionsFactory.init(initParameters);
        
        try {
            for (String resource : resources) {
                URL resourceUrl = context.getResource(resource);
                if (resourceUrl != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding resource '" + resourceUrl + "' to definitions factory.");
                    }
                    definitionsFactory.addSource(resourceUrl);
                } else {
                    LOG.warn("Unable to find configured definition '" + resource + "'");
                }
            }
        } catch (IOException e) {
            throw new DefinitionsFactoryException("Unable to parse definitions from "
                + resourceString, e);
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("Tiles2 container initialization complete.");
        }
    }

    /**
     * Standard Getter
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
    public ComponentContext getComponentContext(Object... requestItems) {
        TilesRequestContext tilesContext = getRequestContext(requestItems);
        return getComponentContext(tilesContext);

    }

    /**
     * Returns the current component context.
     *
     * @param tilesContext The request context to use.
     * @return The current component context.
     */
    private ComponentContext getComponentContext(TilesRequestContext tilesContext) {
        ComponentContext context = BasicComponentContext.getContext(tilesContext);
        if (context == null) {
            context = new BasicComponentContext();
            BasicComponentContext.pushContext(context, tilesContext);
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
        return getContextFactory().createRequestContext(
            getApplicationContext(),
            requestItems
        );
    }

    /**
     * Returns the context factory.
     *
     * @return The context factory.
     */
    public TilesContextFactory getContextFactory() {
        return contextFactory;
    }

    /**
     * Sets the context factory.
     *
     * @param contextFactory The context factory.
     */
    public void setContextFactory(TilesContextFactory contextFactory) {
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
     * Standard Getter
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

    /** {@inheritDoc} */
    public void prepare(String preparer, Object... requestItems)
        throws TilesException {
        TilesRequestContext requestContext = getContextFactory().createRequestContext(
            getApplicationContext(),
            requestItems
        );
        prepare(requestContext, preparer, false);
    }

    /**
     * Execute a preparer.
     *
     * @param context The request context.
     * @param preparerName The name of the preparer.
     * @param ignoreMissing If <code>true</code> if the preparer is not found,
     * it ignores the problem.
     * @throws TilesException If the preparer is not found (and
     * <code>ignoreMissing</code> is not set) or if the preparer itself threw an
     * exception.
     */
    private void prepare(TilesRequestContext context, String preparerName, boolean ignoreMissing)
        throws TilesException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Prepare request received for '" + preparerName);
        }

        ViewPreparer preparer = preparerFactory.getPreparer(preparerName, context);
        if (preparer == null && ignoreMissing) {
            return;
        }

        if (preparer == null) {
            throw new NoSuchPreparerException("Preparer '" + preparerName + " not found");
        }

        ComponentContext componentContext = BasicComponentContext.getContext(context);

        // TODO: Temporary while preparerInstance gets refactored to throw a more specific exception.
        try {
            preparer.execute(context, componentContext);
        } catch (Exception e) {
            throw new PreparerException(e.getMessage(), e);
        }
    }

    /** {@inheritDoc} */
    public void render(String definitionName, Object... requestItems)
        throws TilesException {
        TilesRequestContext requestContext = getContextFactory().createRequestContext(
            getApplicationContext(),
            requestItems
        );
        render(requestContext, definitionName);
    }

    /**
     * Renders the specified definition.
     *
     * @param request The request context. 
     * @param definitionName The name of the definition to render.
     * @throws TilesException If something goes wrong during rendering.
     */
    private void render(TilesRequestContext request, String definitionName)
        throws TilesException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Render request recieved for definition '" + definitionName + "'");
        }

        ComponentDefinition definition = getDefinition(definitionName, request);

        if (definition == null) {
            if (LOG.isWarnEnabled()) {
                String message = "Unable to find the definition '" + definitionName + "'";
                LOG.warn(message);
            }
            throw new NoSuchDefinitionException(definitionName);
        }

        if (!isPermitted(request, definition.getRole())) {
            LOG.info("Access to definition '" + definitionName +
                "' denied.  User not in role '" + definition.getRole());
            return;
        }

        ComponentContext originalContext = getComponentContext(request);
        BasicComponentContext subContext = new BasicComponentContext(originalContext);
        subContext.addMissing(definition.getAttributes());
        BasicComponentContext.pushContext(subContext, request);

        try {
            if (definition.getPreparer() != null) {
                prepare(request, definition.getPreparer(), true);
            }

            String dispatchPath = definition.getTemplate();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Dispatching to definition path '" +
                    definition.getTemplate() + " '");
            }
            request.dispatch(dispatchPath);

            // tiles exception so that it doesn't need to be rethrown.
        } catch (TilesException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Error rendering tile", e);
            // TODO it would be nice to make the preparerInstance throw a more specific
            throw new TilesException(e.getMessage(), e);
        } finally {
            BasicComponentContext.popContext(request);
        }
    }

    /** {@inheritDoc} */
    public void render(ComponentAttribute attr, Writer writer, Object... requestItems)
        throws TilesException, IOException {
        ComponentContext context = getComponentContext(requestItems);
        TilesRequestContext request = getRequestContext(requestItems);

        String type = calculateType(attr, requestItems);
        if ("string".equalsIgnoreCase(type)) {
            writer.write(attr.getValue().toString());
            return;

        }

        Map<String, ComponentAttribute> attrs = attr.getAttributes();
        if (attrs != null) {
            for (Map.Entry<String, ComponentAttribute> a : attrs.entrySet()) {
                context.putAttribute(a.getKey(), a.getValue());
            }
        }

        if (isDefinition(attr, requestItems)) {
            render(request, attr.getValue().toString());
        } else {
            request.dispatch(attr.getValue().toString());
        }
    }

    /**
     * Checks if an attribute contains a definition.
     *
     * @param attr The attribute to check.
     * @param requestItems The request items.
     * @return <code>true</code> if the attribute is a definition.
     */
    private boolean isDefinition(ComponentAttribute attr, Object... requestItems) {
        return ComponentAttribute.DEFINITION.equals(attr.getType()) ||
            isValidDefinition(attr.getValue().toString(), requestItems);
    }

    /**
     * Calculates the type of an attribute.
     *
     * @param attr The attribute to check.
     * @param requestItems The request items.
     * @return The calculated attribute type.
     * @throws TilesException If the type is not recognized.
     */
    private String calculateType(ComponentAttribute attr,
            Object... requestItems) throws TilesException {
        String type = attr.getType();
        if (type == null) {
            Object valueContent = attr.getValue();
            if (valueContent instanceof String) {
                String valueString = (String) valueContent;
                if (isValidDefinition(valueString, requestItems)) {
                    type = ComponentAttribute.DEFINITION;
                } else if (valueString.startsWith("/")) {
                    type = ComponentAttribute.TEMPLATE;
                } else {
                    type = ComponentAttribute.STRING;
                }
            }
            if (type == null) {
                throw new TilesException("Unrecognized type for attribute value "
                    + attr.getValue());
            }
        }
        return type;
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
    protected ComponentDefinition getDefinition(String definitionName, TilesRequestContext request) throws DefinitionsFactoryException {
        ComponentDefinition definition =
            definitionsFactory.getDefinition(definitionName, request);
        return definition;
    }

    /**
     * Checks if the current user is in one of the comma-separated roles
     * specified in the <code>role</code> parameter.
     *
     * @param request The request context.
     * @param role The comma-separated list of roles.
     * @return <code>true</code> if the current user is in one of those roles.
     */
    private boolean isPermitted(TilesRequestContext request, String role) {
        if (role == null) {
            return true;
        }

        StringTokenizer st = new StringTokenizer(role, ",");
        while (st.hasMoreTokens()) {
            if (request.isUserInRole(st.nextToken())) {
                return true;
            }
        }
        return false;
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

    /** {@inheritDoc} */
    public boolean isValidDefinition(String definitionName, Object... requestItems) {
        return isValidDefinition(getRequestContext(requestItems), definitionName);
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
            ComponentDefinition definition = getDefinition(definitionName, context);
            return definition != null;
        }
        catch (NoSuchDefinitionException nsde) {
            return false;
        } catch (DefinitionsFactoryException e) {
            // TODO, is this the right thing to do?
            return false;
        }
    }
}
