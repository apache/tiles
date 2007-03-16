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

package org.apache.tiles.definition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.ComponentAttribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * @version $Rev$ $Date$
 */
public class ComponentDefinitionsImpl implements ComponentDefinitions {

    /**
     * Commons Logging instance.
     */
    private static Log log = LogFactory.getLog(ComponentDefinitionsImpl.class);

    /**
     * The base set of ComponentDefinition objects not discriminated by locale.
     */
    private Map<String, ComponentDefinition> baseDefinitions;
    /**
     * The locale-specific set of definitions objects.
     */
    private Map<Locale, Map<String, ComponentDefinition>> localeSpecificDefinitions;

    /**
     * Creates a new instance of ComponentDefinitionsImpl
     */
    public ComponentDefinitionsImpl() {
        baseDefinitions = new HashMap<String, ComponentDefinition>();
        localeSpecificDefinitions = new HashMap<Locale, Map<String, ComponentDefinition>>();
    }

    /**
     * Returns a ComponentDefinition object that matches the given name.
     *
     * @param name The name of the ComponentDefinition to return.
     * @return the ComponentDefinition matching the given name or null if none
     *         is found.
     */
    public ComponentDefinition getDefinition(String name) {
        return baseDefinitions.get(name);
    }

    /**
     * Adds new ComponentDefinition objects to the internal collection and
     * resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @throws NoSuchDefinitionException
     */
    public void addDefinitions(Map<String, ComponentDefinition> defsMap) throws NoSuchDefinitionException {
        this.baseDefinitions.putAll(defsMap);
        resolveAttributeDependencies(null);
        resolveInheritances();
    }

    /**
     * Adds new locale-specific ComponentDefinition objects to the internal
     * collection and resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @param locale  The locale to add the definitions to.
     * @throws NoSuchDefinitionException If something goes wrong during
     * inheritance resolution.
     */
    public void addDefinitions(Map<String, ComponentDefinition> defsMap, Locale locale) throws NoSuchDefinitionException {
        localeSpecificDefinitions.put(locale, defsMap);
        resolveAttributeDependencies(locale);
        resolveInheritances(locale);
    }

    /**
     * Returns a ComponentDefinition object that matches the given name and locale.
     *
     * @param name   The name of the ComponentDefinition to return.
     * @param locale The locale to use to resolve the definition.
     * @return the ComponentDefinition matching the given name or null if none
     *         is found.
     */
    public ComponentDefinition getDefinition(String name, Locale locale) {
        ComponentDefinition definition = null;

        if (locale != null) {
            Map<String, ComponentDefinition> localeSpecificMap = localeSpecificDefinitions
                    .get(locale);
            if (localeSpecificMap != null) {
                definition = localeSpecificMap.get(name);
            }
        }

        if (definition == null) {
            definition = getDefinition(name);
        }

        return definition;
    }

    /**
     * Resolve extended instances.
     *
     * @throws NoSuchDefinitionException If a parent definition is not found. 
     */
    public void resolveInheritances() throws NoSuchDefinitionException {
        for (ComponentDefinition definition : baseDefinitions.values()) {
            resolveInheritance(definition, null);
        }  // end loop
    }

    /**
     * Resolve locale-specific extended instances.
     *
     * @param locale The locale to use. 
     * @throws NoSuchDefinitionException If a parent definition is not found.
     */
    public void resolveInheritances(Locale locale) throws NoSuchDefinitionException {
        resolveInheritances();

        Map<String, ComponentDefinition> map = localeSpecificDefinitions.get(locale);
        if (map != null) {
            for (ComponentDefinition definition : map.values()) {
                resolveInheritance(definition, locale);
            }  // end loop
        }
    }

    /**
     * Clears definitions.
     */
    public void reset() {
        this.baseDefinitions = new HashMap<String, ComponentDefinition>();
        this.localeSpecificDefinitions = new HashMap<Locale, Map<String, ComponentDefinition>>();
    }

    /**
     * Returns base definitions collection;
     *
     * @return The base (i.e. not depending on any locale) definitions map. 
     */
    public Map<String, ComponentDefinition> getBaseDefinitions() {
        return baseDefinitions;
    }

    /**
     * Checks if an attribute is of type "definition".
     *
     * @param attr The attribute to check.
     * @return <code>true</code> if the attribute is a definition.
     */
    private boolean isDefinitionType(ComponentAttribute attr) {
        boolean explicit = (attr.getType() != null &&
            (attr.getType().equalsIgnoreCase("definition") ||
                attr.getType().equalsIgnoreCase("instance")));

        boolean implicit =
            attr.getType() == null &&
                attr.getValue() != null &&
                baseDefinitions.containsKey(attr.getValue().toString());

        return explicit || implicit;

    }

    /**
     * Resolves attribute dependencies for the given locale. If the locale is
     * <code>null</code>, it resolves base attribute dependencies.
     * 
     * @param locale The locale to use.
     */
    protected void resolveAttributeDependencies(Locale locale) {
        if (locale != null) {
            resolveAttributeDependencies(null); // FIXME Is it necessary?
        }
        Map<String, ComponentDefinition> defsMap = localeSpecificDefinitions.get(locale);
        if (defsMap == null) {
            return;
        }

        for (ComponentDefinition def : defsMap.values()) {
            Map<String, ComponentAttribute> attributes = def.getAttributes();
            for (ComponentAttribute attr : attributes.values()) {
                if (isDefinitionType(attr)) {
                    ComponentDefinition subDef =
                        getDefinitionByAttribute(attr, locale);
                    attr.setAttributes(subDef.getAttributes());
                    attr.setType("definition");
                }
            }
        }
    }

    /**
     * Searches for a definition specified as an attribute.
     *
     * @param attr   The attribute to use.
     * @param locale The locale to search into.
     * @return The required definition if found, otherwise it returns
     *         <code>null</code>.
     */
    private ComponentDefinition getDefinitionByAttribute(
        ComponentAttribute attr, Locale locale) {
        ComponentDefinition retValue = null;

        Object attrValue = attr.getValue();
        if (attrValue instanceof ComponentDefinition) {
            retValue = (ComponentDefinition) attrValue;
        } else if (attrValue instanceof String) {
            retValue = this.getDefinition((String) attr
                .getValue(), locale);
        }

        return retValue;
    }

    /**
     * Resolve locale-specific inheritance.
     * First, resolve parent's inheritance, then set template to the parent's
     * template.
     * Also copy attributes setted in parent, and not set in child
     * If instance doesn't extend anything, do nothing.
     * 
     * @param definition The definition to resolve
     * @param locale The locale to use.
     * @throws NoSuchDefinitionException If an inheritance can not be solved.
     */
    protected void resolveInheritance(ComponentDefinition definition,
                                      Locale locale) throws NoSuchDefinitionException {
        // Already done, or not needed ?
        if (definition.isIsVisited() || !definition.isExtending())
            return;

        if (log.isDebugEnabled())
            log.debug("Resolve definition for child name='"
                + definition.getName()
                + "' extends='" + definition.getExtends() + "'.");

        // Set as visited to avoid endless recurisvity.
        definition.setIsVisited(true);

        // Resolve parent before itself.
        ComponentDefinition parent = getDefinition(definition.getExtends(),
            locale);
        if (parent == null) { // error
            String msg = "Error while resolving definition inheritance: child '"
                + definition.getName()
                + "' can't find its ancestor '"
                + definition.getExtends()
                + "'. Please check your description file.";
            log.error(msg);
            // to do : find better exception
            throw new NoSuchDefinitionException(msg);
        }

        resolveInheritance(parent, locale);

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
        Iterator<String> parentAttributes = parent.getAttributes().keySet()
                .iterator();
        while (parentAttributes.hasNext()) {
            String name = parentAttributes.next();
            if (!child.getAttributes().containsKey(name))
                child.put(name, parent.getAttribute(name));
        }
        // Set template and role if not setted
        if (child.getTemplate() == null)
            child.setTemplate(parent.getTemplate());
        if (child.getRole() == null)
            child.setRole(parent.getRole());
        if (child.getPreparer() == null) {
            child.setPreparer(parent.getPreparer());
        }
    }
}
