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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Definition of a template / component attributes.
 * Attributes of a component can be defined with the help of this class.
 * An instance of this class can be used as a bean, and passed to 'insert' tag.
 */
public class ComponentDefinition implements Serializable {

    /**
     * Extends attribute value.
     */
    private String inherit;

    /**
     * Commons Logging instance.
     */
    protected static Log log = LogFactory.getLog(ComponentDefinition.class);

    /**
     * Definition name
     */
    protected String name = null;

    /**
     * Component / template path (URL).
     */
    protected String path = null;

    /**
     * Attributes defined for the component.
     */
    protected Map<String, ComponentAttribute> attributes = null;

    /**
     * Role associated to definition.
     */
    protected String role = null;

    /**
     * Associated ViewPreparer URL or classname, if defined
     */
    protected String preparer = null;

    /**
     * Used for resolving inheritance.
     */
    private boolean isVisited = false;

    /**
     * Constructor.
     */
    public ComponentDefinition() {
        attributes = new HashMap();
    }

    /**
     * Copy Constructor.
     * Create a new definition initialized with parent definition.
     * Do a shallow copy : attributes are shared between copies, but not the Map
     * containing attributes.
     */
    public ComponentDefinition(ComponentDefinition definition) {
        attributes = new HashMap(definition.getAttributes());
        this.name = definition.getName();
        this.path = definition.getPath();
        this.role = definition.getRole();
        this.preparer = definition.getPreparer();
    }

    /**
     * Constructor.
     */
    public ComponentDefinition(String name, String path, Map attributes) {
        this.name = name;
        this.path = path;
        this.attributes = attributes;
    }

    /**
     * Access method for the name property.
     *
     * @return the current value of the name property
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param aName the new value of the name property
     */
    public void setName(String aName) {
        name = aName;
    }

    /**
     * Access method for the path property.
     *
     * @return The current value of the path property.
     */
    public String getPage() {
        return path;
    }

    /**
     * Sets the value of the path property.
     *
     * @param page the new value of the path property
     */
    public void setPage(String page) {
        path = page;
    }

    /**
     * Access method for the path property.
     *
     * @return the current value of the path property
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     *
     * @param aPath the new value of the path property
     */
    public void setPath(String aPath) {
        path = aPath;
    }

    /**
     * Access method for the template property.
     * Same as getPath()
     *
     * @return the current value of the template property
     */
    public String getTemplate() {
        return path;
    }

    /**
     * Sets the value of the template property.
     * Same as setPath()
     *
     * @param template the new value of the path property
     */
    public void setTemplate(String template) {
        path = template;
    }

    /**
     * Access method for the role property.
     *
     * @return the current value of the role property
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     *
     * @param role the new value of the path property
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Access method for the attributes property.
     * If there is no attributes, return an empty map.
     *
     * @return the current value of the attributes property
     */
    public Map<String, ComponentAttribute> getAttributes() {
        return attributes;
    }

    /**
     * Returns the value of the named attribute as an Object, or null if no
     * attribute of the given name exists.
     *
     * @param key name of the attribute
     * @return requested attribute or null if not found
     */
    public Object getAttribute(String key) {
        ComponentAttribute attribute = attributes.get(key);
        if (attribute != null) {
            return attribute.getValue();
        } else {
            return null;
        }
    }

    /**
     * Put a new attribute in this component
     *
     * @param key   String key for attribute
     * @param value Attibute value.
     */
    public void putAttribute(String key, ComponentAttribute value) {
        attributes.put(key, value);
    }

    /**
     * Put an attribute in component / template definition.
     * Attribute can be used as content for tag get.
     *
     * @param name    Attribute name
     * @param content Attribute value
     */
    public void put(String name, Object content) {
        put(name, content, false, null);
    }

    /**
     * Put an attribute in template definition.
     * Attribute can be used as content for tag get.
     *
     * @param name    Attribute name
     * @param content Attribute value �
     * @param direct  Determines how content is handled by get tag: true means content is printed directly; false, the default, means content is included
     */
    public void put(String name, Object content, boolean direct) {
        put(name, content, direct, null);
    }

    /**
     * Put an attribute in template definition.
     * Attribute can be used as content for tag get.
     *
     * @param name    Attribute name
     * @param content Attribute value
     * @param direct  Determines how content is handled by get tag: true means content is printed directly; false, the default, means content is included
     * @param role    Determine if content is used by get tag. If user is in role, content is used.
     */
    public void put(String name, Object content, boolean direct, String role) {
        if (direct) { // direct String
            put(name, content, "string", role);
        } else {
            put(name, content, "template", role);
        }

    }

    /**
     * Put an attribute in template definition.
     * Attribute can be used as content for tag get.
     *
     * @param name    Attribute name
     * @param content Attribute value
     * @param type    attribute type: template, string, definition
     * @param role    Determine if content is used by get tag. If user is in role, content is used.
     */
    public void put(String name, Object content, String type, String role) {
        // Is there a type set ?
        // First check direct attribute, and translate it to a valueType.
        // Then, evaluate valueType, and create requested typed attribute.
        ComponentAttribute attribute = new ComponentAttribute(content, role, type);
        putAttribute(name, attribute);
    }

    /**
     * Returns a description of the attributes.
     */
    public String toString() {
        return "{name="
            + name
            + ", path="
            + path
            + ", role="
            + role
            + ", preparerInstance="
            + preparer
            + ", attributes="
            + attributes
            + "}\n";
    }

    /**
     * Get associated preparerInstance
     */
    public String getPreparer() {
        return preparer;
    }

    /**
     * Set associated preparerInstance URL.
     *
     * @param url Url called locally
     */
    public void setPreparer(String url) {
        this.preparer = url;
    }

    /**
     * Add an attribute to this component.
     * <p/>
     * This method is used by Digester to load definitions.
     *
     * @param attribute Attribute to add.
     */
    public void addAttribute(ComponentAttribute attribute) {
        putAttribute(attribute.getName(), attribute);
    }

    /**
     * Set extends.
     *
     * @param name Name of the extended definition.
     */
    public void setExtends(String name) {
        inherit = name;
    }

    /**
     * Get extends.
     *
     * @return Name of the extended definition.
     */
    public String getExtends() {
        return inherit;
    }

    /**
     * Get extends flag.
     */
    public boolean isExtending() {
        return inherit != null;
    }

    /**
     * Sets the visit flag, used during inheritance resolution.
     *
     * @param isVisited <code>true</code> is the definition has been visited.
     */
    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    /**
     * Returns the visit flag, used during inheritance resolution.
     *
     * @return isVisited <code>true</code> is the definition has been visited.
     */
    public boolean isIsVisited() {
        return isVisited;
    }


    /**
     * Overload this definition with passed child.
     * All attributes from child are copied to this definition. Previous attributes with
     * same name are disguarded.
     * Special attribute 'path','role' and 'extends' are overloaded if defined in child.
     *
     * @param child Child used to overload this definition.
     */
    public void overload(ComponentDefinition child) {
        if (child.getPath() != null) {
            path = child.getPath();
        }
        if (child.getExtends() != null) {
            inherit = child.getExtends();
        }
        if (child.getRole() != null) {
            role = child.getRole();
        }
        if (child.getPreparer() != null) {
            preparer = child.getPreparer();
        }
        // put all child attributes in parent.
        attributes.putAll(child.getAttributes());
    }
}
