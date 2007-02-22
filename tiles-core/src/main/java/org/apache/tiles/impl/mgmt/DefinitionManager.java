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
package org.apache.tiles.impl.mgmt;

import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.mgmt.TileDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Rev$ $Date$
 */
public class DefinitionManager {

    private static final Log LOG =
        LogFactory.getLog(DefinitionManager.class);

    private static final String DEFAULT_DEFINITIONS_ATTRIBUTE_NAME =
        "org.apache.tiles.impl.mgmt.DefinitionManager.DEFINITIONS";

    private DefinitionsFactory factory;
    
    private String definitionsAttributeName;

    public DefinitionManager() {
        definitionsAttributeName = DEFAULT_DEFINITIONS_ATTRIBUTE_NAME;
    }

    public DefinitionManager(String definitionsAttributeName) {
        this.definitionsAttributeName = definitionsAttributeName;
        if (this.definitionsAttributeName == null) {
            this.definitionsAttributeName = DEFAULT_DEFINITIONS_ATTRIBUTE_NAME;
        }
    }

    public DefinitionsFactory getFactory() {
        return factory;
    }

    public void setFactory(DefinitionsFactory factory) {
        this.factory = factory;
    }

    public ComponentDefinition getDefinition(String definition, TilesRequestContext request)
        throws DefinitionsFactoryException {
        Map<String, ComponentDefinition> definitions =
            getDefinitions(request);
        if (definitions != null && definitions.containsKey(definition)) {
            return definitions.get(definition);
        }
        return getFactory().getDefinition(definition, request);
    }

    public void addDefinition(ComponentDefinition definition,
            TilesRequestContext request)
        throws DefinitionsFactoryException {
        validate(definition);

        if(definition.isExtending()) {
            this.resolveInheritance(definition, request);
        }

        for(ComponentAttribute attr : definition.getAttributes().values()) {
            if(isDefinition(attr, request)) {
                ComponentDefinition d = getDefinition(attr.getValue().toString(), request);
                attr.setAttributes(d.getAttributes());
            }
        }

        getOrCreateDefinitions(request).put(definition.getName(), definition);
    }

    private boolean isDefinition(ComponentAttribute attribute,
            TilesRequestContext request) throws DefinitionsFactoryException {
        boolean explicit =  ComponentAttribute.DEFINITION.equals(attribute.getType());
        boolean implicit =  attribute.getType() == null  &&
                            (getDefinition((String)attribute.getValue(), request) != null);
        return explicit || implicit;
    }

    private void validate(TileDefinition definition) {
        Map<String, ComponentAttribute> attrs = definition.getAttributes();
        for (ComponentAttribute attribute : attrs.values()) {
            if (attribute.getName() == null) {
                throw new IllegalArgumentException("Attribute name not defined");
            }

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
     * @throws NoSuchDefinitionException If an inheritance can not be solved.
     * @param definition def
     */
    protected void resolveInheritance(ComponentDefinition definition,
            TilesRequestContext request)
        throws DefinitionsFactoryException  {
        // Already done, or not needed ?
        if (definition.isIsVisited() || !definition.isExtending())
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Resolve definition for child name='"
                + definition.getName()
                + "' extends='" + definition.getExtends() + "'.");

        // Set as visited to avoid endless recurisvity.
        definition.setIsVisited(true);

        // TODO Factories our factory implementations will be context agnostic,
        //  however, this may cause errors for other implementations.
        //  we should probably make all factories agnostic and allow the manager to
        //  utilize the correct factory based on the context.
        ComponentDefinition parent = getDefinition(definition.getExtends(), request);


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
    protected void overload(ComponentDefinition parent,
                            ComponentDefinition child) {
        // Iterate on each parent's attribute and add it if not defined in child.
        for(Map.Entry<String, ComponentAttribute> entry : parent.getAttributes().entrySet()) {
            if (!child.hasAttributeValue(entry.getKey())) {
                child.putAttribute(entry.getKey(), new ComponentAttribute(entry.getValue()));
            }
        }

        if (child.getTemplate() == null)
            child.setTemplate(parent.getTemplate());

        if (child.getRole() == null)
            child.setRole(parent.getRole());

        if (child.getPreparer() == null) {
            child.setPreparer(parent.getPreparer());
        }
    }
    
    @SuppressWarnings("unchecked")
    protected Map<String, ComponentDefinition> getDefinitions(
            TilesRequestContext request) {
        return (Map<String, ComponentDefinition>) request.getRequestScope()
                .get(definitionsAttributeName);
    }
    
    @SuppressWarnings("unchecked")
    protected Map<String, ComponentDefinition> getOrCreateDefinitions(
            TilesRequestContext request) {
        Map<String, ComponentDefinition> definitions =
            (Map<String, ComponentDefinition>) request
                .getRequestScope().get(definitionsAttributeName);
        if (definitions == null) {
            definitions = new HashMap<String, ComponentDefinition>();
            request.getRequestScope()
                    .put(definitionsAttributeName, definitions);
        }

        return definitions;
    }
}
