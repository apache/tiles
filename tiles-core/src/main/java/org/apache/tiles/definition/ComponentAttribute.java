/*
 * $Id$
 *
 * Copyright 1999-2006 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.definition;

import java.io.Serializable;

/**
 * Common implementation of attribute definition.
 *
 * @version $Rev$ $Date$
 */
public class ComponentAttribute implements Serializable {

    /**
     * Role associated to this attribute.
     */
    protected String role = null;

    protected Object value = null;

    private String type = null;

    private String name = null;

    /**
     * Constructor.
     *
     */
    public ComponentAttribute() {
    }

    /**
     * Constructor.
     *
     * @param value Object to store.
     */
    public ComponentAttribute(Object value) {
        this.value = value;
    }

    /**
     * Constructor.
     *
     * @param value Object to store.
     */
    public ComponentAttribute(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Constructor.
     *
     * @param value Object to store.
     * @param role  Asociated role.
     */
    public ComponentAttribute(Object value, String role) {
        this.value = value;
        this.role = role;
    }

    /**
     * Constructor.
     *
     * @param value Object to store.
     * @param role  Asociated role.
     * @param type  Attribute type.
     */
    public ComponentAttribute(Object value, String role, String type) {
        this.value = value;
        this.role = role;
        this.type = type;
    }

    /**
     * Get role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Set role.
     *
     * @param role Associated role.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Get value.
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
     * Get String representation of this object.
     */
    public String toString() {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBody(String body) {
        if (body != null && body.length() != 0) {
            setValue(body);
        }
    }
}
