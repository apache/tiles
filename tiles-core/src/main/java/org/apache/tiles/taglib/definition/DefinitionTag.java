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
package org.apache.tiles.taglib.definition;

import org.apache.tiles.taglib.PutTagParent;
import org.apache.tiles.taglib.PutTag;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;

import javax.servlet.jsp.JspException;
import java.util.Map;
import java.util.HashMap;

/**
 * This is the tag handler for &lt;tiles:definition&gt;, which defines
 * a tiles (or template / component). Definition is put in requested context and can be
 * used in &lt;tiles:insert&gt.
 *
 * @version $Rev$ $Date$
 */
public class DefinitionTag extends DefinitionTagSupport
    implements PutTagParent {

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

    private TilesContainer container;

    private Map<String, ComponentAttribute> attributes;


    public DefinitionTag() {
        attributes = new HashMap<String, ComponentAttribute>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getExtendsDefinition() {
        return extendsDefinition;
    }

    public void setExtendsDefinition(String extendsDefinition) {
        this.extendsDefinition = extendsDefinition;
    }

    public int doStartTag() {
        container = TilesAccess.getContainer(pageContext.getServletContext());
        return EVAL_BODY_INCLUDE;
    }

    /**
     * TODO: Implement mutable container features.
     *
     * @return
     */
    public int doEndTag() {
        for(Map.Entry<String, ComponentAttribute> entry : attributes.entrySet()) {
//            container.addDefinition(entry.getKey(), entry.getValue());
        }
        return EVAL_PAGE;
    }

    /**
     * Reset member values for reuse. This method calls super.release(),
     * which invokes TagSupport.release(), which typically does nothing.
     */
    public void release() {
        super.release();
        name = null;
        scope = null;
        extendsDefinition = null;
        attributes.clear();
    }

    public void processNestedTag(PutTag nestedTag) throws JspException {
        ComponentAttribute attr = new ComponentAttribute(nestedTag.getValue(),
            nestedTag.getRole(), nestedTag.getType());
        attributes.put(nestedTag.getName(), attr);
    }
}
