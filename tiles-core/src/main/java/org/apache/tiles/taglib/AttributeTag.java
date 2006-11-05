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

import org.apache.tiles.context.jsp.JspUtil;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.TilesException;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.taglib.RenderTagSupport;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.Map;

/**
 * This is the tag handler for &lt;tiles:attribute&gt;, which defines an
 * attribute. If the attribute value is a template or a definition, its
 * attributes and its template can be overridden.
 *
 * @version $Rev$ $Date$
 */
public class AttributeTag extends RenderTagSupport {

    /**
     * Name to insert.
     */
    protected String name;

    protected String template;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    public void release() {
        super.release();
        this.name = null;
        this.template = null;
    }

    protected void render() throws JspException, TilesException, IOException {
        ComponentContext context = container.getComponentContext(pageContext);
        ComponentAttribute attr = context.getAttribute(name);
        if(attr == null && ignore) {
            return;
        }

        if(attr == null) {
            throw new TilesException("Attribute '"+name+"' not found.");
        }

        String type = calculateType(attr);
        if("string".equalsIgnoreCase(type)) {
            pageContext.getOut().print(attr.getValue());

        } else if(isDefinition(attr)) {
            if(template != null) {
                attr.setValue(template);
            }
            
            Map<String, ComponentAttribute> attrs = attr.getAttributes();
            if(attrs != null) {
                for(Map.Entry<String, ComponentAttribute> a : attrs.entrySet()) {
                    context.putAttribute(a.getKey(), a.getValue());
                }
            }
            container.render(pageContext, attr.getValue().toString());
            
        } else {
            JspUtil.doInclude(pageContext, attr.getValue().toString(), flush);
        }
    }

    private boolean isDefinition(ComponentAttribute attr) {
        return ComponentAttribute.DEFINITION.equals(attr.getType()) ||
            container.isValidDefinition(pageContext,
                attr.getValue().toString());
    }

    private String calculateType(ComponentAttribute attr) throws JspException {
       String type = attr.getType();
        if (type == null) {
            Object valueContent = attr.getValue();
            if (valueContent instanceof String) {
                String valueString = (String) valueContent;
                if (valueString.startsWith("/")) {
                    type = ComponentAttribute.TEMPLATE;
                } else {
                    if (container.isValidDefinition(pageContext, valueString)) {
                        type = ComponentAttribute.DEFINITION;
                    } else {
                        type = ComponentAttribute.STRING;
                    }
                }
            }
            if (type == null) {
                throw new JspException("Unrecognized type for attribute value "
                    + attr.getValue());
            }
        }
        return type;
    }
}
