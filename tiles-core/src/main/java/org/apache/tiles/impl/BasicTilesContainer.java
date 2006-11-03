/*
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
import org.apache.tiles.*;
import org.apache.tiles.context.BasicComponentContext;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.preparer.NoSuchPreparerException;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.preparer.ViewPreparer;

import javax.servlet.jsp.PageContext;
import java.io.IOException;
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
 * @version $Rev$
 * @since 2.0
 */
public class BasicTilesContainer implements TilesContainer {

    /**
     * Constant representing the configuration parameter
     * used to define the tiles definition resources.
     */
    public static final String DEFINITIONS_CONFIG = "org.apache.tiles.DEFINITION_CONFIG";

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

    private TilesApplicationContext context;
    private DefinitionsFactory definitionsFactory;
    private PreparerFactory preparerFactory;

    private TilesContextFactory contextFactory;

    /**
     * Initialize the Container with the given configuration.
     *
     * @param context application context for this container
     * @throws TilesException
     */
    public void init(TilesApplicationContext context) throws TilesException {
        checkInit();
        if (LOG.isInfoEnabled()) {
            LOG.info("Initializing Tiles2 container. . .");
        }
        this.context = context;
        contextFactory.init(context.getInitParams());
        definitionsFactory.init(context.getInitParams());

        //Everything is now initialized.  We will populate
        // our definitions
        String resourceString = getResourceString();
        List<String> resources = getResourceNames(resourceString);
        try {
            for (String resource : resources) {
                URL resourceUrl = context.getResource(resource);
                if (resourceUrl != null) {
                    if(LOG.isDebugEnabled()) {
                        LOG.debug("Adding resource '"+resourceUrl+"' to definitions factory.");
                    }
                    definitionsFactory.addSource(resourceUrl);
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
     * Determine whether or not the container has been
     * initialized. Utility method used for methods which
     * can not be invoked after the container has been
     * started.
     *
     * @throws IllegalStateException if the container has already been initialized.
     */
    private void checkInit() {
        if (context != null) {
            throw new IllegalStateException("Container allready initialized");
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

    public ComponentContext getComponentContext(Object request, Object response) {
        TilesRequestContext tilesContext = getRequestContext(request, response);
        return getComponentContext(tilesContext);

    }

    public ComponentContext getComponentContext(PageContext pageContext) {
        TilesRequestContext tilesContext = getRequestContext(pageContext);
        return getComponentContext(tilesContext);
    }

    private ComponentContext getComponentContext(TilesRequestContext tilesContext) {
        ComponentContext context = BasicComponentContext.getContext(tilesContext);
        if (context == null) {
            context = new BasicComponentContext();
            BasicComponentContext.setContext(context, tilesContext);
        }
        return context;
    }

    private TilesRequestContext getRequestContext(Object request, Object response) {
        return getContextFactory().createRequestContext(
            getApplicationContext(),
            request,
            response
        );
    }

    private TilesRequestContext getRequestContext(PageContext context) {
        return getContextFactory().createRequestContext(
            getApplicationContext(),
            context
        );
    }


    public TilesContextFactory getContextFactory() {
        return contextFactory;
    }

    public void setContextFactory(TilesContextFactory contextFactory) {
        checkInit();
        this.contextFactory = contextFactory;
    }

    /**
     * Standard Getter
     *
     * @return the definitions factory used by this container.
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

    public void prepare(Object request, Object response, String preparer)
        throws TilesException {
        TilesRequestContext requestContext = getContextFactory().createRequestContext(
            getApplicationContext(),
            request,
            response
        );
        prepare(requestContext, preparer, false);
    }

    public void prepare(PageContext context, String preparer)
        throws TilesException {
        TilesRequestContext requestContext = getContextFactory().createRequestContext(
            getApplicationContext(), context
        );
        prepare(requestContext, preparer, false);
    }

    private void prepare(TilesRequestContext context, String preparerName, boolean ignoreMissing)
        throws TilesException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Prepare request recieved for '" + preparerName);
        }

        ViewPreparer preparer = preparerFactory.getPreparer(preparerName, null);
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

    /**
     * Render the specified definition.
     *
     * @param request the TilesRequestContext
     * @throws TilesException
     */
    public void render(Object request, Object response, String definitionName)
        throws TilesException {
        TilesRequestContext requestContext = getContextFactory().createRequestContext(
            getApplicationContext(),
            request,
            response
        );
        render(requestContext, definitionName);
    }

    public void render(PageContext context, String definitionName)
        throws TilesException {
        TilesRequestContext requestContext = getContextFactory().createRequestContext(
            getApplicationContext(), context
        );
        render(requestContext, definitionName);
    }

    private void render(TilesRequestContext request, String definitionName)
        throws TilesException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Render request recieved for definition '" + definitionName + "'");
        }

        ComponentDefinition definition =
            definitionsFactory.getDefinition(definitionName, request);

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
        BasicComponentContext.setContext(subContext, request);

        try {
            if(definition.getPreparer() != null) {
                prepare(request, definition.getPreparer(), true);
            }

            String dispatchPath = definition.getPath();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Dispatching to definition path '" +
                    definition.getPath() + " '");
            }
            request.dispatch(dispatchPath);

        } catch (TilesException e) {
            throw e;
        } catch (Exception e) {
            // TODO it would be nice to make the preparerInstance throw a more specific
            // tiles exception so that it doesn't need to be rethrown.
            throw new TilesException(e.getMessage(), e);
        } finally {
            BasicComponentContext.setContext(originalContext, request);
        }
    }

    private boolean isPermitted(TilesRequestContext request, String role) {
        if(role == null) {
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
        Map<String, String> parms = context.getInitParams();
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

    public boolean isValidDefinition(Object request, Object response, String definitionName) {
        return isValidDefinition(getRequestContext(request, response), definitionName);
    }

    public boolean isValidDefinition(PageContext pageContext, String definitionName) {
        return isValidDefinition(getRequestContext(pageContext), definitionName);
    }

    private boolean isValidDefinition(TilesRequestContext context, String definitionName) {
        try {
            ComponentDefinition definition =
                definitionsFactory.getDefinition(definitionName, context);
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
