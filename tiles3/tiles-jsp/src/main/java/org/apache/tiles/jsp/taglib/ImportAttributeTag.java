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

import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.jsp.context.JspUtil;
import org.apache.tiles.template.ImportAttributeModel;


/**
 * Import attribute(s) into the specified scope.
 * If not explicitly named, all attributes are imported.
 * If the scope is not specified, page scope is assumed.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class ImportAttributeTag extends SimpleTagSupport {

    /**
     * The template model.
     */
    private ImportAttributeModel model = new ImportAttributeModel();

    /**
     * The scope name.
     */
    private String scopeName = null;

    /**
     * The name of the attribute.
     */
    private String name = null;

    /**
     * Flag that, if <code>true</code>, ignores exceptions.
     */
    private boolean ignore = false;

    /**
     * The destination attribute name.
     */
    private String toName;

    /**
     * Set the scope.
     *
     * @param scope Scope.
     */
    public void setScope(String scope) {
        this.scopeName = scope;
    }

    /**
     * Get scope.
     *
     * @return Scope.
     */
    public String getScope() {
        return scopeName;
    }

    /**
     * Get the name.
     *
     * @return Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set ignore flag.
     *
     * @param ignore default: <code>false</code>: Exception is thrown when attribute is not found, set to <code>
     *               true</code> to ignore missing attributes silently
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    /**
     * Get ignore flag.
     *
     * @return default: <code>false</code>: Exception is thrown when attribute is not found, set to <code>
     *         true</code> to ignore missing attributes silently
     */
    public boolean isIgnore() {
        return ignore;
    }

    /**
     * <p>
     * Returns the name of the destination attribute. If not specified, the name
     * will be the same as specified in <code>name</code> attribute
     * </p>
     *
     * @return The destination attribute name.
     */
    public String getToName() {
        return toName;
    }

    /**
     * <p>
     * Sets the name of the destination attribute. If not specified, the name
     * will be the same as specified in <code>name</code> attribute
     * </p>
     *
     * @param toName The destination attribute name.
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /** {@inheritDoc} */
    @Override
    public void doTag() throws JspException {
        JspContext jspContext = getJspContext();
        Map<String, Object> attributes = model.getImportedAttributes(JspUtil
                .getCurrentContainer(jspContext), name, toName, ignore,
                jspContext);
        int scopeId = JspUtil.getScope(scopeName);
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            jspContext.setAttribute(entry.getKey(), entry.getValue(), scopeId);
        }
    }
}
