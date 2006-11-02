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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Put an attribute in enclosing attribute container tag.
 * Enclosing attribute container tag can be : &lt;insert&gt; or &lt;definition&gt;.
 * Exception is thrown if no appropriate tag can be found.
 * Put tag can have following atributes :
 * <li>
 * <ul>name : Name of the attribute</ul>
 * <ul>value | content : value to put as attribute</ul>
 * <ul>type : value type. Only valid if value is a String and is set by
 * value="something" or by a bean.
 * Possible type are : string (value is used as direct string),
 * template (value is used as a page url to insert),
 * definition (value is used as a definition name to insert)</ul>
 * <ul>direct : Specify if value is to be used as a direct string or as a
 * page url to insert. This is another way to specify the type. It only apply
 * if value is set as a string, and type is not present.</ul>
 * <ul>beanName : Name of a bean used for setting value. Only valid if value is not set.
 * If property is specified, value come from bean's property. Otherwise, bean
 * itself is used for value.</ul>
 * <ul>beanProperty : Name of the property used for retrieving value.</ul>
 * <ul>beanScope : Scope containing bean. </ul>
 * <ul>role : Role to check when 'insert' will be called. If enclosing tag is
 * &lt;insert&gt;, role is checked immediately. If enclosing tag is
 * &lt;definition&gt;, role will be checked when this definition will be
 * inserted.</ul>
 * </li>
 * Value can also come from tag body. Tag body is taken into account only if
 * value is not set by one of the tag attributes. In this case Attribute type is
 * "string", unless tag body define another type.
 */
public class PutTag extends BodyTagSupport implements ComponentConstants {

    /* JSP Tag attributes */

    /**
     * Name of attribute to put in component context.
     */
    protected String attributeName = null;

    /**
     * Associated attribute value.
     */
    private Object value = null;

    /**
     * Requested type for the value.
     */
    private String valueType = null;

    /**
     * Role attribute.
     */
    private String role = null;

    /* Internal properties */

    /**
     * Cached real value computed from tag attributes.
     */
    protected Object realValue = null;

    /**
     * The body content of this tag.
     */
    protected String body = null;

    /**
     * Default constructor.
     */
    public PutTag() {
        super();
    }

    /**
     * Release all allocated resources.
     */
    public void release() {

        super.release();

        attributeName = null;
        valueType = null;
        value = null;
        role = null;
        body = null;
    }

    /**
     * Release internal properties.
     */
    protected void releaseInternal() {
        realValue = null;
    }

    /**
     * Set name.
     */
    public void setName(String value) {
        this.attributeName = value;
    }

    /**
     * Get name.
     */
    public String getName() {
        return attributeName;
    }

    /**
     * Set value.
     * Method added to satisfy Tomcat (bug ?).
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get value.
     * Method added to satisfy Tomcat (bug ?).
     */
    public String getValue() {
        return (String) this.value;
    }

    /**
     * Set value.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Set property value as an object.
     * Added because some web containers react badly to value as <code>Object</code>.
     */
    public void setObjectValue(Object value) {
        this.value = value;
    }

    /**
     * Set content.
     */
    public void setContent(Object value) {
        this.value = value;
    }

    /**
     * Set type.
     */
    public void setType(String value) {
        this.valueType = value;
    }

    /**
     * Get type.
     */
    public String getType() {
        return this.valueType;
    }

    /**
     * Set role attribute.
     *
     * @param role The role the user must be in to store content.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Get role attribute
     *
     * @return The role defined in the tag or <code>null</code>.
     */
    public String getRole() {
        return role;
    }

    /**
     * Get real value according to tag attribute.
     * Real value is the value computed after attribute processing.
     *
     * @return Real value.
     * @throws JspException If something goes wrong while getting value from bean.
     */
    public Object getRealValue() throws JspException {
        if (realValue == null) {
            computeRealValue();
        }

        return realValue;
    }

    /**
     * Compute real value according to tag attributes.
     *
     * @throws JspException If something goes wrong while getting value from bean.
     */
    protected void computeRealValue() throws JspException {
        // Compute real value from attributes set.
        realValue = value;

        // If realValue is not set, value must come from body
        if (value == null) {
            // Test body content in case of empty body.
            if (body != null) {
                realValue = body;
                valueType = "string";
            } else {
                realValue = "";
            }
        }
    }

    /**
     * Extract real value from specified bean.
     * @throws JspException If something goes wrong while getting value from bean.
     */
    /* - GDR - Commenting out in case someone wants to add feature back.
    protected void getRealValueFromBean() throws JspException {
        try {
            Object bean = TagUtils.retrieveBean(beanName, beanScope, pageContext);
            if (bean != null && beanProperty != null) {
                realValue = PropertyUtils.getProperty(bean, beanProperty);
            } else {
                realValue = bean; // value can be null
            }
            
        } catch (NoSuchMethodException ex) {
            throw new JspException(
                "Error - component.PutAttributeTag : Error while retrieving value from bean '"
                    + beanName
                    + "' with property '"
                    + beanProperty
                    + "' in scope '"
                    + beanScope
                    + "'. (exception : "
                    + ex.getMessage(), ex);

        } catch (InvocationTargetException ex) {
            throw new JspException(
                "Error - component.PutAttributeTag : Error while retrieving value from bean '"
                    + beanName
                    + "' with property '"
                    + beanProperty
                    + "' in scope '"
                    + beanScope
                    + "'. (exception : "
                    + ex.getMessage(), ex);

        } catch (IllegalAccessException ex) {
            throw new JspException(
                "Error - component.PutAttributeTag : Error while retrieving value from bean '"
                    + beanName
                    + "' with property '"
                    + beanProperty
                    + "' in scope '"
                    + beanScope
                    + "'. (exception : "
                    + ex.getMessage(), ex);
        }
    }
     */
    /**
     * Do start tag.
     */
    public int doStartTag() throws JspException {

        // clear body content
        body = null;

        // Do we need to evaluate body ?
        if (value == null) {
            return EVAL_BODY_BUFFERED;
        } else {
            // Value is set, don't evaluate body.
            return SKIP_BODY;
        }
    }

    /**
     * Save the body content of this tag (if any)
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            body = bodyContent.getString();
        }
        return (SKIP_BODY);

    }

    /**
     * Do end tag.
     */
    public int doEndTag() throws JspException {
        // Call parent tag which in turn does what it want
        callParent();

        // clean up tag handler for reuse.
        releaseInternal();

        return EVAL_PAGE;
    }

    /**
     * Find parent tag which must implement AttributeContainer.
     *
     * @throws JspException If we can't find an appropriate enclosing tag.
     */
    protected void callParent() throws JspException {
        // Get enclosing parent
        PutTagParent enclosingParent = findEnclosingPutTagParent();
        enclosingParent.processNestedTag(this);
    }

    /**
     * Find parent tag which must implement AttributeContainer.
     *
     * @throws JspException If we can't find an appropriate enclosing tag.
     */
    protected PutTagParent findEnclosingPutTagParent() throws JspException {
        try {
            PutTagParent parent =
                (PutTagParent) findAncestorWithClass(this, PutTagParent.class);

            if (parent == null) {
                throw new JspException("Error - tag put : enclosing tag doesn't accept 'put' tag.");
            }

            return parent;

        } catch (ClassCastException ex) {
            throw new JspException("Error - tag put : enclosing tag doesn't accept 'put' tag.", ex);
        }
    }

}
