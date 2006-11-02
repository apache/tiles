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

import org.apache.tiles.definition.ComponentAttribute;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.taglib.util.TagUtils;

import javax.servlet.jsp.JspException;

/**
 * This is the tag handler for &lt;tiles:definition&gt;, which defines
 * a tiles (or template / component). Definition is put in requested context and can be
 * used in &lt;tiles:insert&gt.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionTag
    extends DefinitionTagSupport
    implements PutTagParent, PutListTagParent {

    /* JSP Tag attributes */
    /**
     * Definition identifier.
     */
    private String name = null;

    /**
     * Scope into which definition will be saved.
     */
    private String scope = null;

    /**
     * Extends attribute value.
     */
    private String extendsDefinition = null;

    /* Internal properties */
    /**
     * Template definition
     */
    private ComponentDefinition definition = null;

    /**
     * Reset member values for reuse. This method calls super.release(),
     * which invokes TagSupport.release(), which typically does nothing.
     */
    public void release() {
        super.release();
        name = null;
        template = null;
        scope = null;
        role = null;
        extendsDefinition = null;
    }

    /**
     * Release internal references.
     */
    protected void releaseInternal() {
        definition = null;
    }

    /**
     * This method is a convenience for other tags for
     * putting content into the tile definition.
     * Content is already typed by caller.
     */
    public void putAttribute(String name, Object content) {
        if (content instanceof ComponentAttribute) {
            definition.putAttribute(name, (ComponentAttribute) content);
        } else {
            definition.putAttribute(name, new ComponentAttribute(content));
        }
    }

    /**
     * Process nested &lg;put&gt; tag.
     * Method is called from nested &lg;put&gt; tags.
     * Nested list is added to current list.
     * If role is defined, nested attribute is wrapped into an untyped definition
     * containing attribute value and role.
     */
    public void processNestedTag(PutTag nestedTag) throws JspException {
        // Get real value and check role
        // If role is set, add it in attribute definition if any.
        // If no attribute definition, create untyped one and set role.
        Object attributeValue = nestedTag.getRealValue();
        ComponentAttribute def = null;

        if (attributeValue != null
            && attributeValue instanceof ComponentAttribute) {
            def = ((ComponentAttribute) attributeValue);
            if (nestedTag.getRole() != null) {
                def.setRole(nestedTag.getRole());
            }
        } else {
            def = new ComponentAttribute(attributeValue, nestedTag.getRole(),
                nestedTag.getType());
        }

        // now add attribute to enclosing parent (i.e. : this object)
        putAttribute(nestedTag.getName(), def);
    }

    /**
     * Process nested &lg;putList&gt; tag.
     * Method is called from nested &lg;putList&gt; tags.
     * Nested list is added to current list.
     * If role is defined, nested attribute is wrapped into an untyped definition
     * containing attribute value and role.
     */
    public void processNestedTag(PutListTag nestedTag) throws JspException {
        // Get real value and check role
        // If role is set, add it in attribute definition if any.
        // If no attribute definition, create untyped one and set role.
        Object attributeValue = nestedTag.getList();

        if (nestedTag.getRole() != null) {
            ComponentAttribute def = new ComponentAttribute(attributeValue);
            def.setRole(nestedTag.getRole());
            attributeValue = def;
        }

        // Check if a name is defined
        if (nestedTag.getName() == null) {
            throw new JspException("Error - PutList : attribute name is not defined. It is mandatory as the list is added to a 'definition'.");
        }

        // now add attribute to enclosing parent (i.e. : this object).
        putAttribute(nestedTag.getName(), new ComponentAttribute(attributeValue));
    }

    /**
     * Get the ID.
     *
     * @return ID
     */
    public String getName() {
        return name;
    }

    /**
     * Set the ID.
     *
     * @param name New name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the scope.
     *
     * @return Scope.
     */
    public String getScope() {
        return scope;
    }

    /**
     * Set the scope.
     *
     * @param aScope Scope.
     */
    public void setScope(String aScope) {
        scope = aScope;
    }

    /**
     * Set <code>extends</code> (parent) definition name.
     *
     * @param definitionName Name of parent definition.
     */
    public void setExtends(String definitionName) {
        this.extendsDefinition = definitionName;
    }

    /**
     * Get <code>extends</code> (parent) definition name.
     *
     * @return Name of parent definition.
     */
    public String getExtends() {
        return extendsDefinition;
    }

    /**
     * Process the start tag by creating a new definition.
     *
     * @throws JspException On errors processing tag.
     */
    public int doStartTag() throws JspException {
        // Do we extend a definition ?
        if (extendsDefinition != null && !extendsDefinition.equals("")) {
            ComponentDefinition parentDef =
                TagUtils.getComponentDefinition(extendsDefinition, pageContext);

            definition = new ComponentDefinition(parentDef);

        } else {
            definition = new ComponentDefinition();
        }

        // Set definitions attributes
        if (template != null) {
            definition.setTemplate(template);
        }

        if (role != null) {
            definition.setRole(role);
        }

        return EVAL_BODY_INCLUDE;
    }

    /**
     * Process the end tag by putting the definition in appropriate context.
     *
     * @throws JspException On errors processing tag.
     */
    public int doEndTag() throws JspException {
        TagUtils.setAttribute(pageContext, name, definition, scope);

        releaseInternal();
        return EVAL_PAGE;
    }

}
