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

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.jsp.JspUtil;
import org.apache.tiles.jsp.context.JspTilesRequestContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.PutListAttributeModel;

/**
 * PutList tag implementation.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class PutListAttributeTag extends SimpleTagSupport {

    /**
     * The template model.
     */
    private PutListAttributeModel model = new PutListAttributeModel();

    /**
     * Name of attribute to put in attribute context.
     */
    private String name = null;

    /**
     * If <code>true</code>, the attribute will be cascaded to all nested
     * definitions.
     */
    private boolean cascade = false;

    /**
     * The role to check. If the user is in the specified role, the tag is taken
     * into account; otherwise, the tag is ignored (skipped).
     */
    private String role;

    /**
     * Requested type for the value.
     */
    private String type = null;

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'.
     */
    private boolean inherit = false;

    /**
     * Returns the name of the attribute.
     *
     * @return The name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the attribute.
     *
     * @param name
     *            The name of the attribute.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if the attribute should be cascaded to nested definitions.
     *
     * @return <code>true</code> if the attribute will be cascaded.
     * @since 2.1.0
     */
    public boolean isCascade() {
        return cascade;
    }

    /**
     * Sets the property that tells if the attribute should be cascaded to
     * nested definitions.
     *
     * @param cascade
     *            <code>true</code> if the attribute will be cascaded.
     * @since 2.1.0
     */
    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

    /**
     * Returns the role to check. If the user is in the specified role, the tag
     * is taken into account; otherwise, the tag is ignored (skipped).
     *
     * @return The role to check.
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
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * <p>
     * Returns content type: string, template or definition.
     * </p>
     * <ul>
     * <li>String : Content is printed directly.</li>
     * <li>template : Content is included from specified URL. Value is used as
     * an URL.</li>
     * <li>definition : Value denote a definition defined in factory (xml file).
     * Definition will be searched in the inserted tile, in a
     * <code>&lt;insert attribute="attributeName"&gt;</code> tag, where
     * 'attributeName' is the name used for this tag.</li>
     * </ul>
     *
     * @return The attribute type.
     */
    public String getType() {
        return type;
    }

    /**
     * <p>
     * Sets content type: string, template or definition.
     * </p>
     * <ul>
     * <li>String : Content is printed directly.</li>
     * <li>template : Content is included from specified URL. Value is used as
     * an URL.</li>
     * <li>definition : Value denote a definition defined in factory (xml file).
     * Definition will be searched in the inserted tile, in a
     * <code>&lt;insert attribute="attributeName"&gt;</code> tag, where
     * 'attributeName' is the name used for this tag.</li>
     * </ul>
     *
     * @param type
     *            The attribute type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'
     *
     * @param inherit
     *            The "inherit" value.
     * @since 2.1.0
     */
    public void setInherit(boolean inherit) {
        this.inherit = inherit;
    }

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'
     *
     * @return The "inherit" value.
     * @since 2.1.0
     */
    public boolean getInherit() {
        return inherit;
    }

    /** {@inheritDoc} */
    @Override
    public void doTag() throws JspException, IOException {
        JspContext jspContext = getJspContext();
        Request request = JspTilesRequestContext.createServletJspRequest(
                org.apache.tiles.request.jsp.JspUtil
                        .getApplicationContext(jspContext),
                (PageContext) jspContext);
        model.start(role, inherit, request);
        JspUtil.evaluateFragment(getJspBody());
        model.end(name, cascade, request);
    }
}
