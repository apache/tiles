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

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import java.io.IOException;

/**
 * This is the tag handler for &lt;tiles:attribute&gt;, which defines an
 * attribute. If the attribute value is a template or a definition, its
 * attributes and its template can be overridden.
 *
 * @version $Rev$ $Date$
 */
public class InsertAttributeTag extends RenderTag {

    /**
     * Name to insert.
     */
    protected String name;

    /**
     * The value of the attribute.
     */
    protected Object value = null;

    /**
     * This value is evaluated only if <code>value</code> is null and the
     * attribute with the associated <code>name</code> is null.
     *
     * @since 2.1.2
     */
    protected Object defaultValue;

    /**
     * The type of the {@link #defaultValue}, if it is a string.
     *
     * @since 2.1.2
     */
    protected String defaultValueType;

    /**
     * The role to check for the default value. If the user is in the specified
     * role, the default value is taken into account; otherwise, it is ignored
     * (skipped).
     *
     * @since 2.1.2
     */
    protected String defaultValueRole;

    /**
     * The evaluated attribute.
     *
     * @since 2.1.0
     */
    protected Attribute attribute;

    /**
     * Sets the name of the attribute.
     *
     * @param value The name of the attribute.
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Returns  the name of the attribute.
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
    public Object getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value The new value
     */
    public void setValue(Object value) {
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
     * @param defaultValue The default value to set.
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
     * @param defaultValueType The default value type.
     */
    public void setDefaultValueType(String defaultValueType) {
        this.defaultValueType = defaultValueType;
    }

    /**
     * Returns the role to check for the default value. If the user is in the specified
     * role, the default value is taken into account; otherwise, it is ignored
     * (skipped).
     *
     * @return The default value role.
     */
    public String getDefaultValueRole() {
        return defaultValueRole;
    }

    /**
     * Sets the role to check for the default value. If the user is in the specified
     * role, the default value is taken into account; otherwise, it is ignored
     * (skipped).
     *
     * @param defaultValueRole The default value role.
     */
    public void setDefaultValueRole(String defaultValueRole) {
        this.defaultValueRole = defaultValueRole;
    }

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.name = null;
        this.value = null;
        this.defaultValue = null;
        this.defaultValueType = null;
        this.defaultValueType = null;
        this.attribute = null;
    }

    /** {@inheritDoc} */
    @Override
    public int doStartTag() throws TilesJspException {
        if (value == null && name == null) {
            throw new TilesJspException(
                    "No attribute name or value has been provided.");
        }
        return super.doStartTag();
    }

    /** {@inheritDoc} */
    protected void render() throws TilesJspException, IOException {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

        // Checks if the attribute can be rendered with the current user.
        if ((role != null && !req.isUserInRole(role))
                || (attribute == null && ignore)) {
            return;
        }
        render(attribute);
    }

    /** {@inheritDoc} */
    @Override
    protected void startContext(PageContext context) {
        if (preparer != null) {
            container.prepare(preparer, context);
        }

        attribute = computeAttribute(context);

        super.startContext(context);
    }

    /**
     * Renders an attribute for real.
     *
     * @param attr The attribute to render.
     * @throws IOException If something goes wrong during the reading of
     * definition files.
     */
    protected void render(Attribute attr) throws IOException {
        container.render(attr, pageContext);
    }

    /**
     * Computes the attribute to render, evaluating the various tag attributes.
     *
     * @param context The page context.
     * @return The computed attribute.
     */
    private Attribute computeAttribute(PageContext context) {
        Attribute attribute = (Attribute) value;

        if (attribute == null) {
            AttributeContext evaluatingContext = container
                    .getAttributeContext(context);
            attribute = evaluatingContext.getAttribute(name);
            if (attribute == null) {
                attribute = computeDefaultAttribute();
                if (attribute == null && !ignore) {
                    throw new NoSuchAttributeException("Attribute '" + name
                            + "' not found.");
                }
            }
        }
        return attribute;
    }

    /**
     * Computes the default attribute.
     *
     * @return The default attribute.
     */
    private Attribute computeDefaultAttribute() {
        Attribute attribute = null;
        if (defaultValue != null) {
            if (defaultValue instanceof Attribute) {
                attribute = (Attribute) defaultValue;
            } else if (defaultValue instanceof String) {
                attribute = new Attribute(defaultValue,
                        null, defaultValueRole, defaultValueType);
            }
        }
        return attribute;
    }
}
