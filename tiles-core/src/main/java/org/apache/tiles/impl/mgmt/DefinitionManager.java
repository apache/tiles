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
package org.apache.tiles.impl.mgmt;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages custom and configured definitions, so they can be used by the
 * container, instead of using a simple {@link DefinitionsFactory}.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionManager {

    /**
     * The logging object.
     */
    private static final Log LOG =
        LogFactory.getLog(DefinitionManager.class);

    /**
     * The default name of the attribute in which storing custom definitions.
     */
    private static final String DEFAULT_DEFINITIONS_ATTRIBUTE_NAME =
        "org.apache.tiles.impl.mgmt.DefinitionManager.DEFINITIONS";

    /**
     * The definitions factory to use to get main definitions.
     */
    private DefinitionsFactory factory;

    /**
     * The name of the attribute in which storing custom definitions.
     */
    private String definitionsAttributeName;

    /**
     * Constructor.
     */
    public DefinitionManager() {
        definitionsAttributeName = DEFAULT_DEFINITIONS_ATTRIBUTE_NAME;
    }

    /**
     * Constructor.
     *
     * @param definitionsAttributeName The name of the attribute in which
     * storing custom definitions.
     */
    public DefinitionManager(String definitionsAttributeName) {
        this.definitionsAttributeName = definitionsAttributeName;
        if (this.definitionsAttributeName == null) {
            this.definitionsAttributeName = DEFAULT_DEFINITIONS_ATTRIBUTE_NAME;
        }
    }

    /**
     * Returns the used definitions factory.
     *
     * @return The used definitions factory.
     */
    public DefinitionsFactory getFactory() {
        return factory;
    }

    /**
     * Sets the definitions factory to use.
     *
     * @param factory The definitions factory.
     */
    public void setFactory(DefinitionsFactory factory) {
        this.factory = factory;
    }

    /**
     * Returns a definition by name.
     *
     * @param definition The name of the definition.
     * @param request The current request.
     * @return The requested definition, either main or custom.
     * @throws DefinitionsFactoryException If something goes wrong when
     * obtaining a main definition.
     */
    public Definition getDefinition(String definition, TilesRequestContext request)
        throws DefinitionsFactoryException {
        Map<String, Definition> definitions =
            getDefinitions(request);
        if (definitions != null && definitions.containsKey(definition)) {
            return definitions.get(definition);
        }
        return getFactory().getDefinition(definition, request);
    }

    /**
     * Adds a definition to the set of custom ones.
     *
     * @param definition The definition to add.
     * @param request The current request.
     * @throws DefinitionsFactoryException If something goes wrong during the
     * addition.
     */
    public void addDefinition(Definition definition,
            TilesRequestContext request)
        throws DefinitionsFactoryException {
        validate(definition);

        if (definition.isExtending()) {
            this.resolveInheritance(definition, request);
        }

        getOrCreateDefinitions(request).put(definition.getName(), definition);
    }

    /**
     * Validates a custom definition.
     *
     * @param definition The definition to validate.
     */
    private void validate(Definition definition) {
        Map<String, Attribute> attrs = definition.getAttributes();
        for (Attribute attribute : attrs.values()) {
            if (attribute.getValue() == null) {
                throw new IllegalArgumentException("Attribute value not defined");
            }
        }
    }

    /**
     * Resolve inheritance.
     * First, resolve parent's inheritance, then set template to the parent's
     * template.
     * Also copy attributes setted in parent, and not set in child
     * If instance doesn't extend anything, do nothing.
     *
     * @param definition The definition that needs to have its inheritances
     * resolved.
     * @param request The current request.
     * @throws DefinitionsFactoryException If an inheritance can not be solved.
     */
    protected void resolveInheritance(Definition definition,
            TilesRequestContext request)
        throws DefinitionsFactoryException  {
        // Already done, or not needed ?
        if (!definition.isExtending()) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolve definition for child name='"
                + definition.getName()
                + "' extends='" + definition.getExtends() + "'.");
        }

        // TODO Factories our factory implementations will be context agnostic,
        //  however, this may cause errors for other implementations.
        //  we should probably make all factories agnostic and allow the manager to
        //  utilize the correct factory based on the context.
        Definition parent = getDefinition(definition.getExtends(), request);

        if (parent == null) { // error
            String msg = "Error while resolving definition inheritance: child '"
                + definition.getName()
                + "' can't find its ancestor '"
                + definition.getExtends()
                + "'. Please check your description file.";
            LOG.error(msg);
            // to do : find better exception
            throw new NoSuchDefinitionException(msg);
        }

        // Resolve parent before itself.
        resolveInheritance(parent, request);
        overload(parent, definition);
    }

    /**
     * Overloads a child definition with a given parent.
     * All attributes present in child are kept. All missing attributes are
     * copied from the parent.
     * Special attribute 'template','role' and 'extends' are overloaded in child
     * if not defined
     *
     * @param parent The parent definition.
     * @param child  The child that will be overloaded.
     */
    // FIXME This is the same as DefinitionsImpl.overload.
    protected void overload(Definition parent, Definition child) {
        // Iterate on each parent's attribute and add it if not defined in child.
        for (Map.Entry<String, Attribute> entry : parent.getAttributes().entrySet()) {
            if (!child.hasAttributeValue(entry.getKey())) {
                child.putAttribute(entry.getKey(), new Attribute(entry.getValue()));
            }
        }

        if (child.getTemplate() == null) {
            child.setTemplate(parent.getTemplate());
        }

        if (child.getRoles() == null) {
            child.setRoles(parent.getRoles());
        }

        if (child.getPreparer() == null) {
            child.setPreparer(parent.getPreparer());
        }
    }

    /**
     * Returns the map with custom definitions for the current request.
     *
     * @param request The current request.
     * @return A map that connects a definition name to a definition.
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Definition> getDefinitions(
            TilesRequestContext request) {
        return (Map<String, Definition>) request.getRequestScope()
                .get(definitionsAttributeName);
    }

    /**
     * Returns a map of type "definition name -> definition" and, if it has not
     * been defined before, creates one.
     *
     * @param request The current request.
     * @return A map that connects a definition name to a definition.
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Definition> getOrCreateDefinitions(
            TilesRequestContext request) {
        Map<String, Definition> definitions =
            (Map<String, Definition>) request
                .getRequestScope().get(definitionsAttributeName);
        if (definitions == null) {
            definitions = new HashMap<String, Definition>();
            request.getRequestScope()
                    .put(definitionsAttributeName, definitions);
        }

        return definitions;
    }
}
