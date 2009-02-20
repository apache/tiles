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

import org.apache.tiles.jsp.context.JspUtil;
import org.apache.tiles.template.InsertDefinitionModel;

/**
 * This is the tag handler for &lt;tiles:insertDefinition&gt;, which includes a
 * name, eventually overriding or filling attributes of its template.
 *
 * @version $Rev$ $Date$
 */
public class InsertDefinitionTag extends TilesBodyTag {

    /**
     * The template model.
     */
    private InsertDefinitionModel model = new InsertDefinitionModel();

    /**
     * The definition name.
     */
    private String name;

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
     * A string representing the URI of a template (for example, a JSP page).
     *
     * @since 2.1.0
     */
    private String template;

    /**
     * Returns the name of the definition to insert.
     *
     * @return The name of the definition.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the definition to insert.
     *
     * @param name The name of the definition.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the role to check. If the user is in the specified role, the tag is
     * taken into account; otherwise, the tag is ignored (skipped).
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
     * @param role The role to check.
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
     * @param preparer The preparer name.
     * @since 2.1.1
     */
    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    /**
     * Returns the flush flag. If <code>true</code>, current page out stream
     * is flushed before insertion.
     *
     * @return The flush flag.
     * @since 2.1.1
     */
    public boolean isFlush() {
        return flush;
    }

    /**
     * Sets the flush flag. If <code>true</code>, current page out stream
     * is flushed before insertion.
     *
     * @param flush The flush flag.
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
     * @param ignore The ignore flag.
     * @since 2.1.1
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

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
    public void release() {
        super.release();
        name = null;
        preparer = null;
        flush = false;
        ignore = false;
        role = null;
        template = null;
    }

    /** {@inheritDoc} */
    @Override
    public int doStartTag() throws TilesJspException {
        model.start(JspUtil.getCurrentContainer(pageContext), pageContext);
        return EVAL_BODY_INCLUDE;
    }

    /** {@inheritDoc} */
    @Override
    public int doEndTag() throws TilesJspException {
        model.end(JspUtil.getCurrentContainer(pageContext), name, template,
                role, preparer, pageContext);
        return EVAL_PAGE;
    }
}
