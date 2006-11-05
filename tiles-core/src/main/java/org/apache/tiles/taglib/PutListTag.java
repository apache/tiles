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

import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.taglib.PutTagParent;

import java.util.ArrayList;
import java.util.List;

/**
 * PutList tag implementation.
 *
 * @version $Rev$
 * @since Tiles 1.0
 */
public class PutListTag extends PutTag
    implements PutTagParent {


    public PutListTag() {
        super.setValue(new ArrayList());
    }

    /**
     * Get list defined in tag.
     */
    public List getValue() {
        return (List) super.getValue();
    }

    public void setValue(Object object) {
        throw new IllegalStateException("The value of the PutListTag must be the originally defined list.");
    }

    /**
     * Release the state of this put list by
     * clearing the contents of the list.
     */
    public void release() {
        super.release();
        getValue().clear();
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
    public void processNestedTag(PutTag nestedTag) {
        ComponentAttribute attribute = new ComponentAttribute(
            nestedTag.getValue(), nestedTag.getRole(),
            nestedTag.getType());

        componentContext.putAttribute(
            nestedTag.getName(),
            attribute
        );
    }
}
