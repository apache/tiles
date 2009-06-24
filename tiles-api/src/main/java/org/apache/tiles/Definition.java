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
package org.apache.tiles;

import java.util.Map;

/**
 * A definition, i.e. a template with (completely or not) filled attributes.
 * Attributes of a template can be defined with the help of this class.<br>
 * It can be used as a data transfer object used for registering new
 * definitions with the Container.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public class Definition extends BasicAttributeContext {
    /**
     * Extends attribute value.
     */
    protected String inherit;
    /**
     * Definition name.
     */
    protected String name = null;

    /**
     * Constructor.
     */
    public Definition() {
        super();
    }

    /**
     * Copy Constructor.
     * Create a new definition initialized with parent definition.
     * Do a shallow copy : attributes are shared between copies, but not the Map
     * containing attributes.
     *
     * @param definition The definition to copy.
     */
    public Definition(Definition definition) {
        super(definition);
        this.name = definition.name;
        this.inherit = definition.inherit;
    }

    /**
     * Constructor.
     * @param name The name of the definition.
     * @param template The template of the definition.
     * @param attributes The attribute map of the definition.
     * @deprecated Use {@link #Definition(String, Attribute, Map)}.
     */
    public Definition(String name, String template,
                               Map<String, Attribute> attributes) {
        this(name, Attribute.createTemplateAttribute(template), attributes);
    }

    /**
     * Constructor.
     * @param name The name of the definition.
     * @param templateAttribute The template attribute of the definition.
     * @param attributes The attribute map of the definition.
     *
     * @since 2.1.2
     */
    public Definition(String name, Attribute templateAttribute,
                               Map<String, Attribute> attributes) {
        super(attributes);
        this.name = name;
        this.templateAttribute = templateAttribute;
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
     * @deprecated Use {@link #getTemplateAttribute()}.
     */
    @Deprecated
    public String getTemplate() {
        if (templateAttribute == null) {
            templateAttribute = Attribute.createTemplateAttribute(null);
        }
        return (String) templateAttribute.getValue();
    }

    /**
     * Sets the value of the template property.
     *
     * @param template the new value of the path property
     * @deprecated Use {@link #getTemplateAttribute()}.
     */
    @Deprecated
    public void setTemplate(String template) {
        if (templateAttribute == null) {
            templateAttribute = Attribute.createTemplateAttribute(template);
        } else {
            templateAttribute.setValue(template);
        }
    }

    /**
     * Access method for the role property.
     *
     * @return the current value of the role property
     * @deprecated Use {@link #getTemplateAttribute()}.
     */
    @Deprecated
    public String getRole() {
        if (templateAttribute == null) {
            templateAttribute = Attribute.createTemplateAttribute(null);
        }

        return templateAttribute.getRole();
    }

    /**
     * Sets the value of the role property.
     *
     * @param role the new value of the role property
     * @deprecated Use {@link #getTemplateAttribute()}.
     */
    @Deprecated
    public void setRole(String role) {
        if (templateAttribute == null) {
            templateAttribute = Attribute.createTemplateAttribute(null);
        }

        templateAttribute.setRole(role);
    }

    /**
     * Access method for the attributes property. If there is no attributes,
     * return an empty map.
     *
     * @return the current value of the attributes property
     * @deprecated Use {@link AttributeContext#getLocalAttributeNames()} and
     * {@link AttributeContext#getCascadedAttributeNames()}.
     */
    @Deprecated
    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Add an attribute to this definition.
     * <p/>
     * This method is used by Digester to load definitions.
     *
     * @param attribute Attribute to add.
     * @deprecated Use {@link Definition#putAttribute(String, Attribute)}.
     */
    @Deprecated
    public void addAttribute(Attribute attribute) {
        putAttribute(attribute.getName(), attribute);
    }

    /**
     * Checks whether the <code>key</code> attribute has been set.
     *
     * @param key The attribute key to check.
     * @return <code>true</code> if the attribute has a value.
     * @deprecated Check if the {@link AttributeContext#getAttribute(String)}
     * returns null.
     */
    @Deprecated
    public boolean hasAttributeValue(String key) {
        return getAttribute(key) != null;
    }

    /**
     * Put an attribute in template definition. Attribute can be used as content
     * for tag get.
     *
     * @param name Attribute name
     * @param content Attribute value
     * @deprecated Use {@link AttributeContext#putAttribute(String, Attribute)}
     * or {@link AttributeContext#putAttribute(String, Attribute, boolean)}.
     */
    @Deprecated
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
     * @deprecated Use {@link AttributeContext#putAttribute(String, Attribute)}
     * or {@link AttributeContext#putAttribute(String, Attribute, boolean)}.
     */
    @Deprecated
    public void put(String name, Object content, String role) {
        Attribute attribute = new Attribute(content, (Expression) null, role,
                (String) null);
        putAttribute(name, attribute);
    }

    /**
     * Put an attribute in template definition.
     * Attribute can be used as content for tag get.
     *
     * @param name    Attribute name
     * @param content Attribute value
     * @param type    attribute type: template, string, definition
     * @param role    Determine if content is used by get tag. If user is in role, content is used.
     * @deprecated Use {@link AttributeContext#putAttribute(String, Attribute)}
     * or {@link AttributeContext#putAttribute(String, Attribute, boolean)}.
     */
    @Deprecated
    public void put(String name, Object content,
            org.apache.tiles.Attribute.AttributeType type, String role) {
        // Is there a type set ?
        // First check direct attribute, and translate it to a valueType.
        // Then, evaluate valueType, and create requested typed attribute.
        Attribute attribute = new Attribute(content, role, type);
        putAttribute(name, attribute);
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

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
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
     * Returns a description of the attributes.
     *
     * @return A string representation of the content of this definition.
     */
    public String toString() {
        return "{name="
            + name
            + ", template="
            + templateAttribute.getValue()
            + ", role="
            + templateAttribute.getRoles()
            + ", preparerInstance="
            + preparer
            + ", attributes="
            + attributes
            + "}\n";
    }
}
