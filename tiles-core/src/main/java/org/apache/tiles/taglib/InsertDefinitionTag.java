/*
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
 *
 */

package org.apache.tiles.taglib;

import org.apache.tiles.TilesRequestContext;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.taglib.util.TagUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * This is the tag handler for &lt;tiles:insertDefinition&gt;, which includes a
 * definition, eventually overriding or filling attributes of its template.
 *
 * @version $Rev$ $Date$
 */
public class InsertDefinitionTag extends BaseInsertTag {

    /**
     * Name to insert.
     */
    protected String name = null;

    /**
     * Set name.
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Get name.
     */
    public String getName() {
        return name;
    }

    /**
     * Processes tag attributes and create corresponding tag handler.<br>
     * This implementation processes the definition name to create an
     * {@link InsertHandler} with its template page.
     */
    public TagHandler createTagHandler() throws JspException {
        return processDefinitionName(name);
    }

    /**
     * Reset member values for reuse. This method calls super.release(), which
     * invokes TagSupport.release(), which typically does nothing.
     */
    public void release() {

        super.release();

        flush = true;
        name = null;
        template = null;
        role = null;
        isErrorIgnored = false;

        releaseInternal();
    }

    /**
     * Process tag attribute "definition". First, search definition in the
     * factory, then create handler from this definition.
     *
     * @param name Name of the definition.
     * @return Appropriate TagHandler.
     * @throws JspException- NoSuchDefinitionException No Definition found for
     *                       name.
     * @throws JspException- FactoryNotFoundException Can't find Definitions
     *                       factory.
     * @throws JspException- DefinedComponentFactoryException General error in
     *                       factory.
     * @throws JspException  InstantiationException Can't create requested
     *                       preparer
     */
    protected TagHandler processDefinitionName(String name) throws JspException {

        try {
            TilesRequestContext tilesContext = TagUtils.getTilesRequestContext(pageContext);
            ComponentDefinition definition = null;
            definition = TagUtils.getComponentDefinition(name, pageContext,
                tilesContext);

            if (definition == null) { // is it possible ?
                throw new NoSuchDefinitionException(
                    "Error -  Tag Insert : Can't get definition '"
                        + name
                        + "'. Check if this name exists in definitions factory.");
            }

            return processDefinition(definition);

        } catch (NoSuchDefinitionException ex) {
            // Save exception to be able to show it later
            pageContext.setAttribute(ComponentConstants.EXCEPTION_KEY, ex,
                PageContext.REQUEST_SCOPE);
            throw new JspException(ex);
        }
    }
}
