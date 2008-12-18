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

import org.apache.tiles.jsp.context.JspUtil;

/**
 * This is the tag handler for &lt;tiles:insertTemplate&gt;, which includes a
 * template ready to be filled.
 *
 * @version $Rev$ $Date$
 */
public class InsertTemplateTag extends RenderTagSupport {

    /**
     * A string representing the URI of a template (for example, a JSP page).
     *
     * @since 2.1.0
     */
    protected String template;

    /**
     * Returns a string representing the URI of a template (for example, a JSP
     * page).
     *
     * @return The template URI.
     * @since 2.1.0
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Sets a string representing the URI of a template (for example, a JSP
     * page).
     *
     * @param template The template URI.
     * @since 2.1.0
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.template = null;
    }

    /** {@inheritDoc} */
    @Override
    protected void render() throws IOException {
        attributeContext.setTemplate(template);
        attributeContext.setPreparer(preparer);
        attributeContext.setRole(role);
        renderContext();
    }

    /**
     * Renders the current context.
     *
     * @throws IOException if an io exception occurs.
     */
    protected void renderContext() throws IOException {
        JspUtil.setForceInclude(pageContext, true);
        container.renderContext(pageContext);
    }
}
