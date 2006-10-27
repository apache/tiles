/*
 * $Id$ 
 *
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.taglib;

import javax.servlet.jsp.JspException;

import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.ComponentDefinition;
import org.apache.tiles.taglib.util.TagUtils;

/**
 * This is the tag handler for &lt;tiles:attribute&gt;, which defines an
 * attribute. If the attribute value is a template or a definition, its
 * attributes and its template can be overridden.
 * 
 * @version $Rev$ $Date$
 */
public class AttributeTag extends BaseInsertTag {
    
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
     * This implementation processes the attribute name to create an
     * {@link InsertHandler} (if the attribute type is "definition" or
     * "template") or a {@link DirectStringHandler} (if the type is "string").
     */
    public TagHandler createTagHandler() throws JspException {
        return processAttribute(name);
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
     * Process tag attribute "attribute". Get value from component attribute.
     * Found value is process by processObjectValue().
     * 
     * @param name Name of the attribute.
     * @return Appropriate TagHandler.
     * @throws JspException - NoSuchDefinitionException No Definition found for
     * name.
     * @throws JspException - Throws by underlying nested call to
     * processDefinitionName()
     */
    public TagHandler processAttribute(String name) throws JspException {
        Object attrValue = null;
        ComponentContext context = getCurrentContext();
        
        if (context != null) {
            attrValue = context.getAttribute(name);
        }
        
        if (attrValue == null) {
            throw new JspException(
                    "Error - Tag Insert : No value found for attribute '"
                            + name + "'.");
        } else if (attrValue instanceof ComponentAttribute) {
            return processTypedAttribute((ComponentAttribute) attrValue);
        } else {
            throw new JspException("Invalid attribute type: "
                    + attrValue.getClass().getName());
        }
    }
    
    /**
     * Process typed attribute explicitly according to its type.
     * 
     * @param value Typed attribute to process.
     * @return appropriate TagHandler.
     * @throws JspException - Throws by underlying nested call to
     * processDefinitionName()
     */
    public TagHandler processTypedAttribute(ComponentAttribute value)
            throws JspException {
        
        if (value == null) {
            // FIXME.
            return null;
        }
        
        // FIXME Currently this call executes with every attribute, even
        // those that do not need to be preprocessed, like attributes from
        // Tiles definitions files. Fix it to improve performances.
        preprocessAttribute(value);
        String type = value.getType();
        
        if (type == null) {
            throw new JspException("Unrecognized type for attribute value "
                    + value.getValue());
        }
        
        if (type.equalsIgnoreCase("string")) {
            return new DirectStringHandler((String) value.getValue());
        } else if (type.equalsIgnoreCase("definition")) {
            return processDefinition((ComponentDefinition) value.getValue());
        } else {
            return new InsertHandler((String) value.getValue(), role,
                    getPreparer());
        }
    }
    
    /**
     * Preprocess an attribute before using it. It guesses the type of the
     * attribute if it is missing, and gets the right definition if a definition
     * name has been specified.
     * 
     * @param value The attribute to preprocess.
     * @throws JspException If something goes wrong during definition
     * resolution.
     */
    protected void preprocessAttribute(ComponentAttribute value)
            throws JspException {
        String type = value.getType();
        if (type == null) {
            Object valueContent = value.getValue();
            if (valueContent instanceof String) {
                String valueString = (String) valueContent;
                if (valueString.startsWith("/")) {
                    type = "template";
                } else {
                    ComponentDefinition definition = TagUtils
                            .getComponentDefinition(valueString, pageContext);
                    if (definition != null) {
                        type = "definition";
                        value.setValue(definition);
                    } else {
                        type = "string";
                    }
                }
            } else if (valueContent instanceof ComponentDefinition) {
                type = "definition";
            }
            if (type == null) {
                throw new JspException("Unrecognized type for attribute value "
                        + value.getValue());
            }
            value.setType(type);
        } else if (type.equalsIgnoreCase("definition")) {
            Object valueContent = value.getValue();
            if (valueContent instanceof String) {
                ComponentDefinition definition = TagUtils
                        .getComponentDefinition((String) valueContent,
                                pageContext);
                if (definition == null) {
                    throw new JspException("Cannot find any definition named '"
                            + valueContent + "'");
                }
                value.setValue(definition);
            }
        }
    }
}
