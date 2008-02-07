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
     * Attribute types.
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
     * The type of the attribute. It can be <code>string</code>,
     * <code>template</code>, <code>definition</code>.
     */
    private AttributeType type = null;

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
        this.type = attribute.type;
        this.value = attribute.getValue();
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
     * @param role  Asociated role.
     * @param type  Attribute type.
     */
    public Attribute(Object value, String role, AttributeType type) {
        this.value = value;
        this.type = type;
        setRole(role);
    }

    /**
     * Constructor.
     *
     * @param name name of the attribute
     * @param value Object to store.
     * @param role  Asociated role.
     * @param type  Attribute type.
     */
    public Attribute(String name, Object value, String role,
            AttributeType type) {
        this.name = name;
        this.value = value;
        this.type = type;
        setRole(role);
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
     */
    public AttributeType getType() {
        return type;
    }

    /**
     * Sets the type of this attribute.
     *
     * @param type The attribute type.
     */
    public void setType(AttributeType type) {
        this.type = type;
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
}
