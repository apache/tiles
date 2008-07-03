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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * PutList tag implementation.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class PutListAttributeTag extends PutAttributeTag
    implements AddAttributeTagParent {

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'.
     */
    private boolean inherit = false;

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'
     *
     * @param inherit The "inherit" value.
     * @since 2.1.0
     */
    public void setInherit(boolean inherit) {
        this.inherit = inherit;
    }

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'
     *
     * @return The "inherit" value.
     * @since 2.1.0
     */
    public boolean getInherit() {
        return inherit;
    }

    /**
     * Get list defined in tag.
     *
     * @return The value of this list attribute.
     */
    @SuppressWarnings("unchecked")
    public List<Attribute> getAttributes() {
        return (List<Attribute>) super.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(Object object) {
        throw new IllegalStateException("The value of the PutListAttributeTag must be the originally defined list.");
    }

    /** {@inheritDoc} */
    @Override
    public int doStartTag() {
        super.setValue(new ArrayList<Attribute>());
        return EVAL_BODY_BUFFERED;
    }

    /**
     * PutListAttributeTag may not have any body, except for PutAttribute tags.
     *
     * @return <code>SKIP_BODY</code>.
     */
    public int doAfterBody() {
        return (SKIP_BODY);
    }

    /**
     * Release the state of this put list by
     * clearing the contents of the list.
     */
    public void release() {
        inherit = false;
        super.setValue(null);
        super.release();
    }

    /**
     * Process nested &lg;putAttribute&gt; tag.
     * <p/>
     * Places the value of the nested tag within the
     * {@link org.apache.tiles.AttributeContext}.It is the responsibility
     * of the descendent to check security.  Security will be managed by called
     * tags.
     *
     * @param nestedTag the put tag desciendent.
     */
    public void processNestedTag(AddAttributeTag nestedTag) {
        Attribute attribute = new Attribute(nestedTag.getValue(), nestedTag
                .getRole(), nestedTag.getType());

        this.addValue(attribute);
    }

    /**
     * Adds an attribute value to the list.
     *
     * @param attribute The attribute to add.
     */
    private void addValue(Attribute attribute) {
        this.getAttributes().add(attribute);
    }

    /** {@inheritDoc} */
    @Override
    protected void execute() throws TilesJspException {
        PutListAttributeTagParent parent = (PutListAttributeTagParent) TagSupport
                .findAncestorWithClass(this, PutListAttributeTagParent.class);

        if (parent == null) {
            // Try with the old method.
            super.execute();
        }

        parent.processNestedTag(this);
    }
}
