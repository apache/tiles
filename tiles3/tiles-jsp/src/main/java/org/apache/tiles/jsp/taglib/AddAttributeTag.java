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
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.jsp.JspModelBody;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.jsp.JspRequest;
import org.apache.tiles.template.AddAttributeModel;
import org.apache.tiles.template.body.ModelBody;

/**
 * <p>
 * <strong>Adds an attribute in enclosing attribute container tag.</strong>
 * </p>
 * <p>
 * Enclosing attribute container tag can be :
 * <ul>
 * <li>&lt;putListAttribute&gt;</li>
 * <li>&lt;putAttribute&gt;</li>
 * </ul>
 * Exception is thrown if no appropriate tag can be found.
 * </p>
 * <p>
 * Put tag can have following atributes :
 * <ul>
 * <li>name : Name of the attribute</li>
 * <li>value : value to put as attribute</li>
 * <li>type : value type. Only valid if value is a String and is set by
 * value="something" or by a bean. Possible type are : string (value is used as
 * direct string), template (value is used as a page url to insert), definition
 * (value is used as a definition name to insert)</li>
 * <li>role : Role to check when 'insert' will be called. If enclosing tag is
 * &lt;insert&gt;, role is checked immediately. If enclosing tag is
 * &lt;definition&gt;, role will be checked when this definition will be
 * inserted.</li>
 * </ul>
 * </p>
 * <p>
 * Value can also come from tag body. Tag body is taken into account only if
 * value is not set by one of the tag attributes. In this case Attribute type is
 * "string", unless tag body define another type.
 * </p>
 *
 * @version $Rev$ $Date$
 */
public class AddAttributeTag extends SimpleTagSupport {

    /**
     * The template model.
     */
    private AddAttributeModel model = new AddAttributeModel();

    /**
     * The role to check. If the user is in the specified role, the tag is taken
     * into account; otherwise, the tag is ignored (skipped).
     */
    private String role;

    /**
     * Associated attribute value.
     */
    private Object value = null;

    /**
     * The expression to calculate the value from. Use this parameter, or value,
     * or body.
     */
    private String expression = null;

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
     * Returns the attribute value.
     *
     * @return Attribute value. Can be a String or Object.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the attribute value.
     *
     * @param value Attribute value. Can be a String or Object.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Returns the expression to calculate the value from. Use this parameter,
     * or value, or body.
     *
     * @return The expression
     * @since 2.2.0
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets the expression to calculate the value from. Use this parameter, or
     * value, or body.
     *
     * @param expression The expression
     * @since 2.2.0
     */
    public void setExpression(String expression) {
        this.expression = expression;
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
    public void doTag() throws IOException {
        JspContext pageContext = getJspContext();
        Request request = JspRequest.createServletJspRequest(
                org.apache.tiles.request.jsp.JspUtil
                        .getApplicationContext(pageContext),
                (PageContext) pageContext);
        ModelBody modelBody = new JspModelBody(getJspBody(), pageContext);
        model.execute(value, expression, role, type, request, modelBody);
    }
}
