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

import static org.apache.tiles.CompareUtil.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Common implementation of attribute definition.
 *
 * @version $Rev$ $Date$
 */
public class Attribute implements Serializable {

    /**
     * The name of the template renderer.
     */
    private static final String TEMPLATE_RENDERER = "template";

    /**
     * Attribute types.
     *
     * @deprecated Use {@link Attribute#setRenderer(String)} and
     * {@link Attribute#getRenderer()}.
     */
    public static enum AttributeType {
        /**
         * Attribute of type string.
         */
        STRING("string"),

        /**
         * Attribute of type definition.
         */
        DEFINITION("definition"),

        /**
         * Attribute of type template.
         */
        TEMPLATE("template"),

        /**
         * Attribute of type object.
         */
        OBJECT("object");

        /**
         * The string representation of the enum element.
         */
        private String stringRepresentation;

        /**
         * Maps the string representation to the attribute type.
         */
        private static Map<String, AttributeType> representation2type;

        static {
            representation2type = new HashMap<String, AttributeType>();
            representation2type.put("string", AttributeType.STRING);
            representation2type.put("definition", AttributeType.DEFINITION);
            representation2type.put("template", AttributeType.TEMPLATE);
            representation2type.put("object", AttributeType.OBJECT);
        }

        /**
         * Returns the type for the given string representation.
         *
         * @param stringRepresentation The string representation of the needed
         * type.
         * @return The corresponding attribute type, if found, or
         * <code>null</code> if not.
         */
        public static AttributeType getType(String stringRepresentation) {
            return representation2type.get(stringRepresentation);
        }

        /**
         * Constructor.
         *
         * @param stringRepresentation The string representation of the enum
         * element.
         */
        private AttributeType(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        /**
         * Returns the string representation of the element.
         *
         * @return The string representation.
         */
        @Override
        public String toString() {
            return stringRepresentation;
        }
    };

    /**
     * The roles that can render this attribute.
     * @since 2.0.6
     */
    protected Set<String> roles = null;

    /**
     * The value of the attribute.
     */
    protected Object value = null;

    /**
     * The expression to evaluate. Ignored if {@link #value} is not
     * <code>null</code>.
     *
     * @since 2.1.2
     */
    protected String expression = null;

    /**
     * The renderer name of the attribute. Default names are <code>string</code>,
     * <code>template</code>, <code>definition</code>, <code>object</code>.
     */
    private String renderer = null;

    /**
     * The name of the attribute. If it is <code>null</code>, it should be used
     * as an element of a list attribute.
     * @deprecated It is not used.
     */
    @Deprecated
    private String name = null;

    /**
     * Constructor.
     *
     */
    public Attribute() {
    }

    /**
     * Constructor.
     *
     * @param value Object to store.
     */
    public Attribute(Object value) {
        this.value = value;
    }

    /**
     * Copy constructor.
     *
     * @param attribute The attribute to copy from.
     */
    public Attribute(Attribute attribute) {
        this.name = attribute.name;
        this.roles = attribute.roles;
        this.value = attribute.getValue();
        this.expression = attribute.expression;
        this.renderer = attribute.renderer;
    }

    /**
     * Constructor.
     *
     * @param name name of the attribute
     * @param value Object to store.
     */
    public Attribute(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Constructor.
     *
     * @param value Object to store.
     * @param role  Asociated role.
     */
    public Attribute(Object value, String role) {
        this.value = value;
        setRole(role);
    }

    /**
     * Constructor.
     *
     * @param value Object to store.
     * @param role Associated role.
     * @param type Attribute type.
     * @deprecated Use {@link Attribute#Attribute(Object, String, String, String)}.
     */
    @Deprecated
    public Attribute(Object value, String role, AttributeType type) {
        this.value = value;
        setType(type);
        setRole(role);
    }

    /**
     * Constructor.
     *
     * @param value Object to store. If specified, the <code>expression</code>
     * parameter will be ignored.
     * @param expression The expression to be evaluated. Ignored if the
     * <code>value</code> is not null.
     * @param role Associated role.
     * @param rendererName The renderer name.
     * @since 2.1.2
     */
    public Attribute(Object value, String expression, String role, String rendererName) {
        this.value = value;
        this.expression = expression;
        this.renderer = rendererName;
        setRole(role);
    }

    /**
     * Constructor.
     *
     * @param name name of the attribute
     * @param value Object to store.
     * @param role Asociated role.
     * @param type Attribute type.
     * @deprecated Use
     * {@link Attribute#Attribute(Object, String, String, String)}.
     */
    public Attribute(String name, Object value, String role,
            AttributeType type) {
        this.name = name;
        this.value = value;
        setType(type);
        setRole(role);
    }

    /**
     * Creates a template attribute, starting from the name of the template.
     *
     * @param template The template that will be rendered.
     * @return The template attribute.
     * @since 2.1.2
     */
    public static Attribute createTemplateAttribute(String template) {
        Attribute attribute = new Attribute();
        attribute.setValue(template);
        attribute.setRenderer(TEMPLATE_RENDERER);
        return attribute;
    }

    /**
     * Creates a template attribute, starting from the expression to evaluate to
     * obtain the template.
     *
     * @param templateExpression The expression to evaluate.
     * @return The template attribute.
     * @since 2.1.2
     */
    public static Attribute createTemplateAttributeWithExpression(
            String templateExpression) {
        Attribute attribute = new Attribute();
        attribute.setExpression(templateExpression);
        attribute.setRenderer(TEMPLATE_RENDERER);
        return attribute;
    }

    /**
     * Get role.
     * @return the name of the required role(s)
     */
    public String getRole() {
        String retValue = null;

        if (roles != null && !roles.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            Iterator<String> roleIt = roles.iterator();
            if (roleIt.hasNext()) {
                builder.append(roleIt.next());
                while (roleIt.hasNext()) {
                    builder.append(",");
                    builder.append(roleIt.next());
                }
                retValue = builder.toString();
            }
        }

        return retValue;
    }

    /**
     * Returns the roles that can render this attribute.
     *
     * @return The enabled roles.
     * @since 2.0.6
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * Set role.
     *
     * @param role Associated role.
     */
    public void setRole(String role) {
        if (role != null && role.trim().length() > 0) {
            String[] rolesStrings = role.split("\\s*,\\s*");
            roles = new HashSet<String>();
            for (int i = 0; i < rolesStrings.length; i++) {
                roles.add(rolesStrings[i]);
            }
        } else {
            roles = null;
        }
    }

    /**
     * Sets the roles that can render this attribute.
     *
     * @param roles The enabled roles.
     * @since 2.0.6
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    /**
     * Get value.
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set value.
     *
     * @param value New value.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Returns The expression to evaluate. Ignored if {@link #value} is not
     * <code>null</code>.
     *
     * @return The expression to be evaluated.
     * @since 2.1.2
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets The expression to evaluate. Ignored if {@link #value} is not
     * <code>null</code>.
     *
     * @param expression The expression to be evaluated.
     * @since 2.1.2
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /** {@inheritDoc} */
    public String toString() {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    /**
     * Returns the type of this attribute.
     *
     * @return The attribute type. It can be <code>string</code>,
     * <code>template</code>, <code>definition</code>, <code>object</code>.
     * @deprecated Use {@link Attribute#getRenderer()}.
     */
    public AttributeType getType() {
        return AttributeType.getType(renderer);
    }

    /**
     * Sets the type of this attribute.
     *
     * @param type The attribute type.
     * @deprecated Use {@link Attribute#setRenderer(String)}.
     */
    public void setType(AttributeType type) {
        this.renderer = type.toString();
    }

    /**
     * Returns the renderer name to use.
     *
     * @return The renderer name.
     * @since 2.1.0
     */
    public String getRenderer() {
        return renderer;
    }

    /**
     * Sets the renderer name to use.
     *
     * @param rendererName The renderer.
     * @since 2.1.0
     */
    public void setRenderer(String rendererName) {
        this.renderer = rendererName;
    }

    /**
     * Returns the name of the attribute.
     *
     * @return The name of the attribute. It can be <code>null</code>, but in
     * this case it should be used as an element of <code>ListAttribute</code>
     * @deprecated Use the <code>getName</code> methods in object that contain
     * attributes.
     */
    @Deprecated
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the attribute.
     *
     * @param name The name of the attribute. It can be <code>null</code>,
     * but in this case it should be used as an element of
     * <code>ListAttribute</code>
     * @deprecated Use the <code>setName</code> methods in object that contain
     * attributes.
     */
    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the body of this attribute.
     *
     * @param body The attribute body.
     */
    // FIXME Is it necessary?
    public void setBody(String body) {
        if (body != null && body.length() != 0) {
            setValue(body);
        }
    }

    /**
     * Inherits an attribute, i.e. overwrites null properties with the ones
     * provided by the attribute.
     *
     * @param attribute The attribute to inherit.
     * @since 2.1.2
     */
    public void inherit(Attribute attribute) {
        if (value == null) {
            value = attribute.getValue();
        }
        if (expression == null) {
            expression = attribute.getExpression();
        }
        if (roles == null || roles.isEmpty()) {
            roles = attribute.getRoles();
        }
        if (renderer == null) {
            renderer = attribute.getRenderer();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        Attribute attribute = (Attribute) obj;
        return nullSafeEquals(value, attribute.value)
                && nullSafeEquals(renderer, attribute.renderer)
                && nullSafeEquals(roles, attribute.roles)
                && nullSafeEquals(expression, attribute.expression);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return nullSafeHashCode(value) + nullSafeHashCode(renderer)
                + nullSafeHashCode(roles) + nullSafeHashCode(expression);
    }
}
