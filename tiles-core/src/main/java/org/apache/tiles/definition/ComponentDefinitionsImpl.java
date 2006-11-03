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
    private Map localeSpecificDefinitions;

    /**
     * Creates a new instance of ComponentDefinitionsImpl
     */
    public ComponentDefinitionsImpl() {
        baseDefinitions = new HashMap<String, ComponentDefinition>();
        localeSpecificDefinitions = new HashMap();
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
        resolveAttributeDependencies();
        resolveInheritances();
    }

    /**
     * Adds new locale-specific ComponentDefinition objects to the internal
     * collection and resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @param locale  The locale to add the definitions to.
     */
    public void addDefinitions(Map defsMap, Locale locale) throws NoSuchDefinitionException {
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
        Map localeSpecificMap = (Map) localeSpecificDefinitions.get(locale);
        if (localeSpecificMap != null) {
            definition = (ComponentDefinition) localeSpecificMap.get(name);
        }

        if (definition == null) {
            definition = getDefinition(name);
        }

        return definition;
    }

    /**
     * Resolve extended instances.
     */
    public void resolveInheritances() throws NoSuchDefinitionException {
        for (ComponentDefinition definition : baseDefinitions.values()) {
            resolveInheritance(definition);
        }  // end loop
    }

    /**
     * Resolve locale-specific extended instances.
     */
    public void resolveInheritances(Locale locale) throws NoSuchDefinitionException {
        resolveInheritances();

        Map map = (Map) localeSpecificDefinitions.get(locale);
        if (map != null) {
            Iterator i = map.values().iterator();
            while (i.hasNext()) {
                ComponentDefinition definition = (ComponentDefinition) i.next();
                resolveInheritance(definition, locale);
            }  // end loop
        }
    }

    /**
     * Clears definitions.
     */
    public void reset() {
        this.baseDefinitions = new HashMap<String, ComponentDefinition>();
        this.localeSpecificDefinitions = new HashMap();
    }

    /**
     * Returns base definitions collection;
     */
    public Map getBaseDefinitions() {
        return baseDefinitions;
    }

    public void resolveAttributeDependencies() {
        for (ComponentDefinition def: baseDefinitions.values()) {
            Map<String, ComponentAttribute> attributes = def.getAttributes();
            for (ComponentAttribute attr: attributes.values()) {
                if (isDefinitionType(attr)) {
                        ComponentDefinition subDef =
                            getDefinitionByAttribute(attr);
                        attr.setAttributes(subDef.getAttributes());
                }
            }
        }
    }

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

    public void resolveAttributeDependencies(Locale locale) {
        resolveAttributeDependencies(); // FIXME Is it necessary?
        Map defsMap = (Map) localeSpecificDefinitions.get(locale);
        if (defsMap == null) {
            return;
        }

        Iterator i = defsMap.values().iterator();

        while (i.hasNext()) {
            ComponentDefinition def = (ComponentDefinition) i.next();
            Map attributes = def.getAttributes();
            Iterator j = attributes.values().iterator();
            while (j.hasNext()) {
                ComponentAttribute attr = (ComponentAttribute) j.next();
                if (attr.getType() != null) {
                    if (attr.getType().equalsIgnoreCase("definition") ||
                        attr.getType().equalsIgnoreCase("instance")) {
                        ComponentDefinition subDef = getDefinitionByAttribute(
                            attr, locale);
                        attr.setValue(subDef);
                    }
                } else {
                    ComponentDefinition subDef = getDefinitionByAttribute(attr,
                        locale);
                    if (subDef != null) {
                        attr.setValue(subDef);
                    }
                }
            }
        }
    }

    /**
     * Searches for a definition specified as an attribute.
     *
     * @param attr The attribute to use.
     * @return The required definition if found, otherwise it returns
     *         <code>null</code>.
     */
    private ComponentDefinition getDefinitionByAttribute(
        ComponentAttribute attr) {
        ComponentDefinition retValue = null;

        Object attrValue = attr.getValue();
        if (attrValue instanceof ComponentDefinition) {
            retValue = (ComponentDefinition) attrValue;
        } else if (attrValue instanceof String) {
            retValue = this.getDefinition((String) attr
                .getValue());
        }

        return retValue;
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
     * Resolve inheritance.
     * First, resolve parent's inheritance, then set path to the parent's path.
     * Also copy attributes setted in parent, and not set in child
     * If instance doesn't extend anything, do nothing.
     *
     * @throws NoSuchDefinitionException If an inheritance can not be solved.
     */
    protected void resolveInheritance(ComponentDefinition definition)
        throws NoSuchDefinitionException {
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
        ComponentDefinition parent = getDefinition(definition.getExtends());
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

        resolveInheritance(parent);

        overload(parent, definition);
    }

    /**
     * Resolve locale-specific inheritance.
     * First, resolve parent's inheritance, then set path to the parent's path.
     * Also copy attributes setted in parent, and not set in child
     * If instance doesn't extend anything, do nothing.
     *
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

        resolveInheritance(definition, locale);

        overload(parent, definition);
    }

    /**
     * Overloads a child definition with a given parent.
     * All attributes present in child are kept. All missing attributes are
     * copied from the parent.
     * Special attribute 'path','role' and 'extends' are overloaded in child if
     * not defined
     *
     * @param parent The parent definition.
     * @param child  The child that will be overloaded.
     */
    protected void overload(ComponentDefinition parent,
                            ComponentDefinition child) {
        // Iterate on each parent's attribute and add it if not defined in child.
        Iterator parentAttributes = parent.getAttributes().keySet().iterator();
        while (parentAttributes.hasNext()) {
            String name = (String) parentAttributes.next();
            if (!child.getAttributes().containsKey(name))
                child.put(name, parent.getAttribute(name));
        }
        // Set path and role if not setted
        if (child.getPath() == null)
            child.setPath(parent.getPath());
        if (child.getRole() == null)
            child.setRole(parent.getRole());
        if (child.getPreparer() == null) {
            child.setPreparer(parent.getPreparer());
        }
    }
}
