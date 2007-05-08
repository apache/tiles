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

package org.apache.tiles.jsp.taglib;

import org.apache.tiles.Attribute;
import org.apache.tiles.Attribute.AttributeType;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

/**
 * AddListAttribute tag implementation.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class AddListAttributeTag extends AddAttributeTag
    implements AddAttributeTagParent {

    /**
     * Get list defined in tag.
     *
     * @return The list of attributes.
     */
    @SuppressWarnings("unchecked")
    public List<Attribute> getValue() {
        return (List<Attribute>) super.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(Object object) {
        throw new IllegalStateException("The value of the PutListAttributeTag must be the originally defined list.");
    }

    /** {@inheritDoc} */
    public int doStartTag() throws JspException {
        super.setValue(new ArrayList<Attribute>());
        return super.doStartTag();
    }

    /**
     * PutListAttributeTag may not have any body, except for PutAttribute tags.
     *
     * @return <code>SKIP_BODY</code>.
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {
        return (SKIP_BODY);
    }

    /**
     * Release the state of this put list by
     * clearing the contents of the list.
     */
    public void release() {
        super.setValue(null);
        super.release();
    }

    /**
     * Process nested &lg;addAttribute&gt; tag.
     * <p/>
     * Places the value of the nested tag within the
     * {@link org.apache.tiles.AttributeContext}.It is the responsibility
     * of the descendent to check security.  Tags extending
     * the {@link org.apache.tiles.jsp.taglib.ContainerTagSupport} will automatically provide
     * the appropriate security.
     *
     * @param nestedTag the put tag desciendent.
     */
    public void processNestedTag(AddAttributeTag nestedTag) {
        Attribute attribute = new Attribute(
            nestedTag.getValue(), nestedTag.getRole(),
            AttributeType.getType(nestedTag.getType()));

        this.addValue(attribute);
    }

    /**
     * Adds a value in this list.
     *
     * @param attribute The attribute to add.
     */
    private void addValue(Attribute attribute) {
        this.getValue().add(attribute);
    }
}
