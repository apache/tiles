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

import org.apache.tiles.context.BasicComponentContext;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.definition.ComponentDefinition;

import javax.servlet.jsp.JspException;
import java.util.HashMap;
import java.util.Map;

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
     *                      name.
     * @throws JspException - Throws by underlying nested call to
     *                      processDefinitionName()
     */
    public TagHandler processAttribute(String name) throws JspException {
        Object attrValue = null;
        BasicComponentContext context = getCurrentContext();

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
     *                      processDefinitionName()
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
            return new DirectStringHandler(value.getValue());
        } else if (type.equalsIgnoreCase("definition")) {
            Map<String, Object> attrs = new HashMap<String,Object>(value.getAttributes());
            return processDefinition((String) value.getValue(), attrs);
        } else {
            return new InsertHandler((String) value.getValue(), role,
                preparer);
        }
    }

    /**
     * Preprocess an attribute before using it. It guesses the type of the
     * attribute if it is missing, and gets the right definition if a definition
     * name has been specified.
     *
     * @param value The attribute to preprocess.
     * @throws JspException If something goes wrong during definition
     *                      resolution.
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
                    if (container.isValidDefinition(pageContext, valueString)) {
                        type = "definition";
                        value.setValue(valueString);
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
                if (!container.isValidDefinition(pageContext, (String) valueContent))
                    throw new JspException("Cannot find any definition named '"
                        + valueContent + "'");
            }
            value.setValue(valueContent);
        }
    }
}
