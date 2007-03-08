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
 *
 */
package org.apache.tiles.jsp.taglib.definition;

import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.jsp.taglib.PutAttributeTag;
import org.apache.tiles.jsp.taglib.PutAttributeTagParent;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.mgmt.TileDefinition;
import org.apache.tiles.access.TilesAccess;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;
import java.util.HashMap;

/**
 * This is the tag handler for &lt;tiles:definition&gt;, which defines
 * a tiles (or template / component). TileDefinition is put in requested context and can be
 * used in &lt;tiles:insert&gt.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionTag extends TagSupport
    implements PutAttributeTagParent {


    private String name;
    private String template;
    private String extend;
    private String role;
    private String preparer;


    private MutableTilesContainer container;
    private Map<String, ComponentAttribute> attributes;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getExtends() {
        return extend;
    }

    public void setExtends(String extend) {
        this.extend = extend;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPreparer() {
        return preparer;
    }

    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    public void release() {
        super.release();
        name = null;
        template = null;
        extend = null;
        role = null;
        preparer = null;
        attributes.clear();
    }

    public int doStartTag() throws JspException {
        attributes = new HashMap<String, ComponentAttribute>();

        TilesContainer c =
            TilesAccess.getContainer(pageContext.getServletContext());

        if (c == null) {
            throw new JspException("TilesContainer not initialized");
        }
        if(!(c instanceof MutableTilesContainer)) {
            throw new JspException("Unable to define definition for a " +
                "container which does not implement MutableTilesContainer");
        }

        container = (MutableTilesContainer)c;
        return EVAL_BODY_INCLUDE;
    }

    /**
     *
     *
     * @return
     */
    public int doEndTag() throws JspException {
        TileDefinition d = new TileDefinition();
        d.setName(name);
        d.setTemplate(template);
        d.setExtends(extend);
        d.setRole(role);
        d.setPreparer(preparer);
        d.getAttributes().putAll(attributes);
        
        try {
            container.register(d, pageContext);
        } catch (TilesException e) {
            throw new JspException("Unable to add definition. " ,e);
        }
        return EVAL_PAGE;
    }

    /**
     * Reset member values for reuse. This method calls super.release(),
     * which invokes TagSupport.release(), which typically does nothing.
     */

    public void processNestedTag(PutAttributeTag nestedTag) throws JspException {
        ComponentAttribute attr = new ComponentAttribute(nestedTag.getValue(),
            nestedTag.getRole(), nestedTag.getType());
        attr.setName(nestedTag.getName());
        attributes.put(nestedTag.getName(), attr);
    }
}
