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
import org.apache.tiles.Attribute;
import org.apache.tiles.mgmt.TileDefinition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A definition, i.e. a template with (completely or not) filled attributes.
 * Attributes of a template can be defined with the help of this class.
 * An instance of this class can be used as a bean, and passed to 'insert' tag.
 *
 * @version $Rev$ $Date$
 */
public class Definition extends TileDefinition implements Serializable {

    /**
     * Commons Logging instance.
     */
    protected static Log log = LogFactory.getLog(Definition.class);

    /**
     * Used for resolving inheritance.
     */
    private boolean isVisited = false;

    /**
     * Constructor.
     */
    public Definition() {
        attributes = new HashMap<String, Attribute>();
    }

    /**
     * Copy Constructor.
     * Create a new definition initialized with parent definition.
     * Do a shallow copy : attributes are shared between copies, but not the Map
     * containing attributes.
     *
     * @param definition The definition to copy.
     */
    public Definition(TileDefinition definition) {
        attributes = new HashMap<String, Attribute>(
            definition.getAttributes());
        this.name = definition.getName();
        this.template = definition.getTemplate();
        this.role = definition.getRole();
        this.preparer = definition.getPreparer();
        this.inherit = definition.getExtends();
    }

    /**
     * Constructor.
     * @param name The name of the definition.
     * @param template The template of the definition.
     * @param attributes The attribute map of the definition.
     */
    public Definition(String name, String template,
                               Map<String, Attribute> attributes) {
        this.name = name;
        this.template = template;
        this.attributes = attributes;
    }
    
    /**
     * Checks whether the <code>key</code> attribute has been set.
     * 
     * @param key The attribute key to check.
     * @return <code>true</code> if the attribute has a value.
     */
    public boolean hasAttributeValue(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Put an attribute in template definition.
     * Attribute can be used as content for tag get.
     *
     * @param name    Attribute name
     * @param content Attribute value
     */
    public void put(String name, Object content) {
        put(name, content, null);
    }

    /**
     * Put an attribute in template definition.
     * Attribute can be used as content for tag get.
     *
     * @param name    Attribute name
     * @param content Attribute value
     * @param role    Determine if content is used by get tag. If user is in role, content is used.
     */
    public void put(String name, Object content, String role) {
        put(name, content, null, role);
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
        Attribute attribute = new Attribute(content, role, type);
        putAttribute(name, attribute);
    }

    /**
     * Returns a description of the attributes.
     *
     * @return A string representation of the content of this definition.
     */
    public String toString() {
        return "{name="
            + name
            + ", template="
            + template
            + ", role="
            + role
            + ", preparerInstance="
            + preparer
            + ", attributes="
            + attributes
            + "}\n";
    }

    /**
     * Get extends flag.
     *
     * @return <code>true</code> if this definition extends another.
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
}
