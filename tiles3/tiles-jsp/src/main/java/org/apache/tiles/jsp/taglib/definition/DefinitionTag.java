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
package org.apache.tiles.jsp.taglib.definition;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.jsp.JspUtil;
import org.apache.tiles.jsp.context.JspTilesRequestContext;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.DefinitionModel;

/**
 * This is the tag handler for &lt;tiles:definition&gt;, which creates a custom
 * definition. If the configured container is a {@link MutableTilesContainer},
 * this newly created definition can be used in &lt;tiles:insertDefinition&gt.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionTag extends SimpleTagSupport {

    /**
     * The template model.
     */
    private DefinitionModel model = new DefinitionModel();

    /**
     * Name of the definition to configure.
     */
    private String name;

    /**
     * The template of the definition.
     */
    private String template;

    /**
     * The (optional) definition name that this definition extends.
     */
    private String extend;

    /**
     * The role to check when rendering this definition.
     */
    private String role;

    /**
     * The definition view preparer.
     */
    private String preparer;

    /**
     * Returns the name of the definition to configure.
     *
     * @return The definition name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the definition to configure.
     *
     * @param name The definition name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the template URI of the definition.
     *
     * @return The template URI.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Sets the template URI of the definition.
     *
     * @param template The template URI.
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Returns the (optional) definition name that this definition extends.
     *
     * @return The extending definition name.
     */
    public String getExtends() {
        return extend;
    }

    /**
     * Sets the (optional) definition name that this definition extends.
     *
     * @param extend The extending definition name.
     */
    public void setExtends(String extend) {
        this.extend = extend;
    }

    /**
     * Returns the role to check when rendering this definition.
     *
     * @return The role to check.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role to check when rendering this definition.
     *
     * @param role The role to check.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the definition view preparer.
     *
     * @return The view preparer name.
     */
    public String getPreparer() {
        return preparer;
    }

    /**
     * Sets the definition view preparer.
     *
     * @param preparer The view preparer name.
     */
    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    /** {@inheritDoc} */
    @Override
    public void doTag() throws JspException, IOException {
        JspContext jspContext = getJspContext();
        Request request = JspTilesRequestContext.createServletJspRequest(
                org.apache.tiles.request.jsp.JspUtil.getApplicationContext(jspContext),
                (PageContext) jspContext);
        model.start(name, template, role, extend, preparer, request);
        JspUtil.evaluateFragment(getJspBody());
        model.end(request);
    }
}
