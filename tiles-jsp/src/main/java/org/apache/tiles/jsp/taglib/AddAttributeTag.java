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

package org.apache.tiles.jsp.taglib;

import org.apache.tiles.ComponentConstants;
import org.apache.tiles.jsp.taglib.ContainerTagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p><strong>Adds an attribute in enclosing attribute container tag.</strong></p>
 * <p>Enclosing attribute container tag can be : 
 * <ul>
 * <li>&lt;putListAttribute&gt;</li> 
 * <li>&lt;putAttribute&gt;</li> 
 * </ul>
 * (or any other tag which implements the <code>{@link AddAttributeTagParent}</code> interface.
 * Exception is thrown if no appropriate tag can be found.</p>
 * <p>Put tag can have following atributes :
 * <ul>
 * <li>name : Name of the attribute</li>
 * <li>value : value to put as attribute</li>
 * <li>type : value type. Only valid if value is a String and is set by
 * value="something" or by a bean.
 * Possible type are : string (value is used as direct string),
 * template (value is used as a page url to insert),
 * definition (value is used as a definition name to insert)</li>
 * <li>role : Role to check when 'insert' will be called. If enclosing tag is
 * &lt;insert&gt;, role is checked immediately. If enclosing tag is
 * &lt;definition&gt;, role will be checked when this definition will be
 * inserted.</li>
 * </ul></p>
 * <p>Value can also come from tag body. Tag body is taken into account only if
 * value is not set by one of the tag attributes. In this case Attribute type is
 * "string", unless tag body define another type.</p>
 *
 * @version $Rev$ $Date$
 */
public class AddAttributeTag extends ContainerTagSupport implements ComponentConstants {

    private static final Log LOG = LogFactory.getLog(AddAttributeTag.class);

    /**
     * Associated attribute value.
     */
    private Object value = null;

    /**
     * Requested type for the value.
     */
    private String type = null;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        value = null;
        type = null;
    }

    /**
     * Save the body content of this tag (if any)
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {
        if (bodyContent != null) {
            value = bodyContent.getString();
            type = "string";
        }
        return (SKIP_BODY);
    }

    @Override
    protected void startContext(PageContext context) {
        if (container != null) {
            componentContext = container.getComponentContext(context);
        }
    }

    @Override
    protected void endContext(PageContext context) {
        // Do nothing
    }

    protected void execute() throws JspException {
        AddAttributeTagParent parent = (AddAttributeTagParent)
            TagSupport.findAncestorWithClass(this, AddAttributeTagParent.class);


        if (parent == null) {
            String message = "Error: enclosing tag '"
                +getParent().getClass().getName()+" doesn't accept 'put' tag.";
            LOG.error(message);
            throw new JspException(message);
        }

        parent.processNestedTag(this);
    }
}
