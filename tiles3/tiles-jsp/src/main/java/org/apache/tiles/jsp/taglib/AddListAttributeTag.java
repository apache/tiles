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
import org.apache.tiles.request.Request;
import org.apache.tiles.request.jsp.JspRequest;
import org.apache.tiles.template.AddListAttributeModel;

/**
 * AddListAttribute tag implementation.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class AddListAttributeTag extends SimpleTagSupport {

    /**
     * The template model.
     */
    private AddListAttributeModel model = new AddListAttributeModel();

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
     * @param role The role to check.
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
     * @param type The attribute type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /** {@inheritDoc} */
    @Override
    public void doTag() throws JspException, IOException {
        JspContext jspContext = getJspContext();
        Request request = JspRequest.createServletJspRequest(
                org.apache.tiles.request.jsp.JspUtil
                        .getApplicationContext(jspContext),
                (PageContext) jspContext);
        model.start(role, request);
        JspUtil.evaluateFragment(getJspBody());
        model.end(request);
    }
}
