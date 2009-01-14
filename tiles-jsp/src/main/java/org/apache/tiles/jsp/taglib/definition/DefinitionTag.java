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

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.jsp.context.JspUtil;
import org.apache.tiles.jsp.taglib.PutAttributeTag;
import org.apache.tiles.jsp.taglib.PutAttributeTagParent;
import org.apache.tiles.jsp.taglib.PutListAttributeTag;
import org.apache.tiles.jsp.taglib.PutListAttributeTagParent;
import org.apache.tiles.jsp.taglib.TilesJspException;
import org.apache.tiles.jsp.taglib.TilesTag;
import org.apache.tiles.mgmt.MutableTilesContainer;

/**
 * This is the tag handler for &lt;tiles:definition&gt;, which creates a custom
 * definition. If the configured container is a {@link MutableTilesContainer},
 * this newly created definition can be used in &lt;tiles:insertDefinition&gt.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionTag extends TilesTag implements PutAttributeTagParent,
        PutListAttributeTagParent {


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
     * The mutable Tiles container to use.
     */
    private MutableTilesContainer container;

    /**
     * The definition currently being built.
     */
    private Definition definition;

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
    protected void reset() {
        super.reset();
        name = null;
        template = null;
        extend = null;
        role = null;
        preparer = null;
        definition = null;
    }

    /** {@inheritDoc} */
    public int doStartTag() throws TilesJspException {
        definition = new Definition();
        definition.setName(name);
        definition.setTemplate(template);
        definition.setExtends(extend);
        definition.setRole(role);
        definition.setPreparer(preparer);

        TilesContainer c = JspUtil.getCurrentContainer(pageContext);

        if (c == null) {
            throw new TilesJspException("TilesContainer not initialized");
        }
        if (!(c instanceof MutableTilesContainer)) {
            throw new TilesJspException(
                    "Unable to define definition for a "
                            + "container which does not implement MutableTilesContainer");
        }

        container = (MutableTilesContainer) c;
        return EVAL_BODY_INCLUDE;
    }

    /** {@inheritDoc} */
    public int doEndTag() throws TilesJspException {
        container.register(definition, pageContext);
        callParent();
        return EVAL_PAGE;
    }

    /**
     * Reset member values for reuse. This method calls super.release(),
     * which invokes TagSupport.release(), which typically does nothing.
     *
     * @param nestedTag The nested <code>PutAttributeTag</code>
     * @throws TilesJspException Never thrown, it's here for API compatibility.
     */
    public void processNestedTag(PutAttributeTag nestedTag) throws TilesJspException {
        Attribute attr = new Attribute(nestedTag.getValue(),
            null, nestedTag.getRole(), nestedTag.getType());
        definition.putAttribute(nestedTag.getName(), attr, nestedTag
                .isCascade());
    }

    /** {@inheritDoc} */
    public void processNestedTag(PutListAttributeTag nestedTag) {
        ListAttribute attribute = new ListAttribute(nestedTag.getAttributes());
        attribute.setRole(nestedTag.getRole());
        attribute.setInherit(nestedTag.getInherit());
        definition.putAttribute(nestedTag.getName(), attribute, nestedTag
                .isCascade());
    }

    /**
     * Find parent tag which must implement {@link DefinitionTagParent}.
     * @throws TilesJspException If we can't find an appropriate enclosing tag.
     * @since 2.1.0
     */
    protected void callParent() throws TilesJspException {
        // Get enclosing parent
        DefinitionTagParent enclosingParent =
                findEnclosingDefinitionTagParent();
        if (enclosingParent != null) {
            enclosingParent.processNestedDefinitionName(definition.getName());
        }
    }

    /**
     * Find parent tag which must implement AttributeContainer.
     * @throws TilesJspException If we can't find an appropriate enclosing tag.
     * @return The parent tag.
     * @since 2.1.0
     */
    protected DefinitionTagParent findEnclosingDefinitionTagParent()
            throws TilesJspException {
        try {
            DefinitionTagParent parent =
                    (DefinitionTagParent) findAncestorWithClass(this,
                            DefinitionTagParent.class);

            if (parent == null && name == null) {
                throw new TilesJspException(
                        "Error - tag definition : enclosing tag doesn't accept 'definition'"
                                + " tag and a name was not specified.");
            }

            return parent;

        } catch (ClassCastException ex) { // Is it possibile?
            throw new TilesJspException(
                    "Error - tag definition : enclosing tag doesn't accept 'definition' tag.", ex);
        }
    }
}
