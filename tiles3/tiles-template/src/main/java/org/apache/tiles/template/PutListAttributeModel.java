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

package org.apache.tiles.template;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.Request;

/**
 * <p>
 * <strong>Declare a list that will be pass as attribute to tile. </strong>
 * </p>
 * <p>
 * Declare a list that will be pass as attribute to tile. List elements are
 * added using the tags 'addAttribute' or 'addListAttribute'. This tag can only
 * be used inside 'insertTemplate', 'insertDefinition', 'definition' tags.
 * </p>
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class PutListAttributeModel {

    /**
     * Starts the operation.
     *
     * @param composeStack The composing stack.
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param inherit If <code>true</code> the list contained in the in the same
     * attribute of the parent definition will be extended.
     * @since 2.2.0
     */
    public void start(ArrayStack<Object> composeStack, String role, boolean inherit) {
        ListAttribute listAttribute = new ListAttribute();
        listAttribute.setRole(role);
        listAttribute.setInherit(inherit);
        composeStack.push(listAttribute);
    }

    /**
     * Ends the operation.
     *
     * @param container The Tiles container to use.
     * @param composeStack The composing stack.
     * @param name The name of the attribute to put.
     * @param cascade If <code>true</code> the attribute will be cascaded to all nested attributes.
     * @param request TODO
     * @since 2.2.0
     */
    public void end(TilesContainer container, ArrayStack<Object> composeStack,
            String name, boolean cascade, Request request) {
        ListAttribute listAttribute = (ListAttribute) composeStack.pop();
        AttributeContext attributeContext = null;
        if (!composeStack.isEmpty()) {
            Object obj = composeStack.peek();
            if (obj instanceof Definition) {
                attributeContext = (AttributeContext) obj;
            }
        }
        if (attributeContext == null) {
            attributeContext = container.getAttributeContext(request);
        }
        attributeContext.putAttribute(name, listAttribute, cascade);
    }
}
