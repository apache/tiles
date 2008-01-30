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
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.jsp.taglib.PutAttributeTag;
import org.apache.tiles.jsp.taglib.PutAttributeTagParent;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.Attribute.AttributeType;
import org.apache.tiles.access.TilesAccess;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;
import java.util.HashMap;

/**
 * This is the tag handler for &lt;tiles:definition&gt;, which creates a custom
 * definition. If the configured container is a {@link MutableTilesContainer},
 * this newly created definition can be used in &lt;tiles:insertDefinition&gt.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionTag extends TagSupport
    implements PutAttributeTagParent {


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
     * Maps attribute names with their attributes.
     */
    private Map<String, Attribute> attributes;


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
    public void release() {
        super.release();
        name = null;
        template = null;
        extend = null;
        role = null;
        preparer = null;
        attributes.clear();
    }

    /** {@inheritDoc} */
    public int doStartTag() throws JspException {
        attributes = new HashMap<String, Attribute>();

        TilesContainer c =
            TilesAccess.getContainer(pageContext.getServletContext());

        if (c == null) {
            throw new JspException("TilesContainer not initialized");
        }
        if (!(c instanceof MutableTilesContainer)) {
            throw new JspException(
                    "Unable to define definition for a "
                            + "container which does not implement MutableTilesContainer");
        }

        container = (MutableTilesContainer) c;
        return EVAL_BODY_INCLUDE;
    }

    /** {@inheritDoc} */
    public int doEndTag() throws JspException {
        Definition d = new Definition();
        d.setName(name);
        d.setTemplate(template);
        d.setExtends(extend);
        d.setRole(role);
        d.setPreparer(preparer);
        d.getAttributes().putAll(attributes);

        try {
            container.register(d, pageContext);
        } catch (TilesException e) {
            throw new JspException("Unable to add definition. " , e);
        }
        return EVAL_PAGE;
    }

    /**
     * Reset member values for reuse. This method calls super.release(),
     * which invokes TagSupport.release(), which typically does nothing.
     *
     * @param nestedTag The nested <code>PutAttributeTag</code>
     * @throws JspException Never thrown, it's here for API compatibility.
     */
    public void processNestedTag(PutAttributeTag nestedTag) throws JspException {
        Attribute attr = new Attribute(nestedTag.getValue(),
            nestedTag.getRole(), AttributeType.getType(nestedTag.getType()));
        attributes.put(nestedTag.getName(), attr);
    }
}
