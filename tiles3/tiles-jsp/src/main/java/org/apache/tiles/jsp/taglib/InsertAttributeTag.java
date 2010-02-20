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

package org.apache.tiles.jsp.taglib;

import java.io.IOException;

import org.apache.tiles.Attribute;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.InsertAttributeModel;
import org.apache.tiles.template.body.ModelBody;

/**
 * This is the tag handler for &lt;tiles:attribute&gt;, which defines an
 * attribute. If the attribute value is a template or a definition, its
 * attributes and its template can be overridden.
 *
 * @version $Rev$ $Date$
 */
public class InsertAttributeTag extends BodyTag {

    /**
     * The template model.
     */
    private InsertAttributeModel model = new InsertAttributeModel(
            new DefaultAttributeResolver());

    /**
     * Name to insert.
     */
    private String name;

    /**
     * The value of the attribute.
     */
    private Attribute value = null;

    /**
     * This value is evaluated only if <code>value</code> is null and the
     * attribute with the associated <code>name</code> is null.
     *
     * @since 2.1.2
     */
    private Object defaultValue;

    /**
     * The type of the {@link #defaultValue}, if it is a string.
     *
     * @since 2.1.2
     */
    private String defaultValueType;

    /**
     * The role to check for the default value. If the user is in the specified
     * role, the default value is taken into account; otherwise, it is ignored
     * (skipped).
     *
     * @since 2.1.2
     */
    private String defaultValueRole;

    /**
     * The role to check. If the user is in the specified role, the tag is taken
     * into account; otherwise, the tag is ignored (skipped).
     *
     * @since 2.1.1
     */
    private String role;

    /**
     * The view preparer to use before the rendering.
     *
     * @since 2.1.1
     */
    private String preparer;

    /**
     * This flag, if <code>true</code>, flushes the content before rendering.
     *
     * @since 2.1.1
     */
    private boolean flush;

    /**
     * This flag, if <code>true</code>, ignores exception thrown by preparers
     * and those caused by problems with definitions.
     *
     * @since 2.1.1
     */
    private boolean ignore;

    /**
     * Sets the name of the attribute.
     *
     * @param value
     *            The name of the attribute.
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Returns the name of the attribute.
     *
     * @return The name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value.
     *
     * @return The value.
     */
    public Attribute getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value
     *            The new value
     */
    public void setValue(Attribute value) {
        this.value = value;
    }

    /**
     * Returns the default value, that is evaluated only if <code>value</code>
     * is null and the attribute with the associated <code>name</code> is null.
     *
     * @return The default value.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value, that is evaluated only if <code>value</code> is
     * null and the attribute with the associated <code>name</code> is null.
     *
     * @param defaultValue
     *            The default value to set.
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the default value type. It will be used only if
     * {@link #getDefaultValue()} is a string.
     *
     * @return The default value type.
     */
    public String getDefaultValueType() {
        return defaultValueType;
    }

    /**
     * Sets the default value type. To be used in conjunction with
     * {@link #setDefaultValue(Object)} when passing a string.
     *
     * @param defaultValueType
     *            The default value type.
     */
    public void setDefaultValueType(String defaultValueType) {
        this.defaultValueType = defaultValueType;
    }

    /**
     * Returns the role to check for the default value. If the user is in the
     * specified role, the default value is taken into account; otherwise, it is
     * ignored (skipped).
     *
     * @return The default value role.
     */
    public String getDefaultValueRole() {
        return defaultValueRole;
    }

    /**
     * Sets the role to check for the default value. If the user is in the
     * specified role, the default value is taken into account; otherwise, it is
     * ignored (skipped).
     *
     * @param defaultValueRole
     *            The default value role.
     */
    public void setDefaultValueRole(String defaultValueRole) {
        this.defaultValueRole = defaultValueRole;
    }

    /**
     * Returns the role to check. If the user is in the specified role, the tag
     * is taken into account; otherwise, the tag is ignored (skipped).
     *
     * @return The role to check.
     * @since 2.1.1
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role to check. If the user is in the specified role, the tag is
     * taken into account; otherwise, the tag is ignored (skipped).
     *
     * @param role
     *            The role to check.
     * @since 2.1.1
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the preparer name.
     *
     * @return The preparer name.
     * @since 2.1.1
     */
    public String getPreparer() {
        return preparer;
    }

    /**
     * Sets the preparer name.
     *
     * @param preparer
     *            The preparer name.
     * @since 2.1.1
     */
    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    /**
     * Returns the flush flag. If <code>true</code>, current page out stream is
     * flushed before insertion.
     *
     * @return The flush flag.
     * @since 2.1.1
     */
    public boolean isFlush() {
        return flush;
    }

    /**
     * Sets the flush flag. If <code>true</code>, current page out stream is
     * flushed before insertion.
     *
     * @param flush
     *            The flush flag.
     * @since 2.1.1
     */
    public void setFlush(boolean flush) {
        this.flush = flush;
    }

    /**
     * Returns the ignore flag. If it is set to true, and the attribute
     * specified by the name does not exist, simply return without writing
     * anything. The default value is false, which will cause a runtime
     * exception to be thrown.
     *
     * @return The ignore flag.
     * @since 2.1.1
     */
    public boolean isIgnore() {
        return ignore;
    }

    /**
     * Sets the ignore flag. If this attribute is set to true, and the attribute
     * specified by the name does not exist, simply return without writing
     * anything. The default value is false, which will cause a runtime
     * exception to be thrown.
     *
     * @param ignore
     *            The ignore flag.
     * @since 2.1.1
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(Request request, ModelBody modelBody) throws IOException {
        model.execute(ignore, preparer, role, defaultValue, defaultValueRole,
                defaultValueType, name, value, request, modelBody);
    }
}
