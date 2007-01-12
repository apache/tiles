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

package org.apache.tiles.taglib;

import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.taglib.PutTagParent;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

/**
 * PutList tag implementation.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class PutListTag extends PutAttributeTag
    implements PutTagParent {


    public PutListTag() {
    }

    /**
     * Get list defined in tag.
     */
    public List<ComponentAttribute> getValue() {
        return (List<ComponentAttribute>) super.getValue();
    }

    public void setValue(Object object) {
        throw new IllegalStateException("The value of the PutListTag must be the originally defined list.");
    }

    public int doStartTag() {
        super.setValue(new ArrayList<ComponentAttribute>());
    	return super.doStartTag();
    }
    /**
     * PutListTag may not have any body, except for PutAttribute tags.
     *
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
     * Process nested &lg;put&gt; tag.
     * <p/>
     * Places the value of the nested tag within the
     * {@link org.apache.tiles.ComponentContext}.It is the responsibility
     * of the descendent to check security.  Tags extending
     * the {@link org.apache.tiles.taglib.ContainerTagSupport} will automatically provide
     * the appropriate security.
     *
     * @param nestedTag the put tag desciendent.
     */
    public void processNestedTag(PutAttributeTag nestedTag) {
        ComponentAttribute attribute = new ComponentAttribute(
            nestedTag.getValue(), nestedTag.getRole(),
            nestedTag.getType());

        this.addValue(attribute);	
    }

	private void addValue( ComponentAttribute attribute ) {
		this.getValue().add(attribute);
	}
}
