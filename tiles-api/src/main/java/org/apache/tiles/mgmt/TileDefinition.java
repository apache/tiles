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
package org.apache.tiles.mgmt;

import org.apache.tiles.ComponentAttribute;

import java.util.Map;
import java.util.HashMap;

/**
 * Data transfer object used for registering new
 * definitions with the Container.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public class TileDefinition {
    /**
     * Extends attribute value.
     */
    protected String inherit;
    /**
     * TileDefinition name
     */
    protected String name = null;
    /**
     * Template path.
     */
    protected String template = null;
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


    public TileDefinition() {
        attributes = new HashMap<String, ComponentAttribute>();
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
     * Access method for the template property.
     *
     * @return the current value of the template property
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Sets the value of the template property.
     *
     * @param template the new value of the path property
     */
    public void setTemplate(String template) {
        this.template = template;
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
}
