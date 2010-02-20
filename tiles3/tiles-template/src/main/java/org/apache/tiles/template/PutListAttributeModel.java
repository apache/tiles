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

import java.io.IOException;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.body.ModelBody;

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
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param inherit If <code>true</code> the list contained in the in the same
     * attribute of the parent definition will be extended.
     * @param request TODO
     * @param composeStack The composing stack.
     *
     * @since 2.2.0
     */
    public void start(String role, boolean inherit, Request request) {
        ArrayStack<Object> composeStack = ComposeStackUtil.getComposeStack(request);
        ListAttribute listAttribute = new ListAttribute();
        listAttribute.setRole(role);
        listAttribute.setInherit(inherit);
        composeStack.push(listAttribute);
    }

    /**
     * Ends the operation.
     * @param name The name of the attribute to put.
     * @param cascade If <code>true</code> the attribute will be cascaded to all nested attributes.
     * @param request TODO
     * @param container The Tiles container to use.
     * @param composeStack The composing stack.
     *
     * @since 2.2.0
     */
    public void end(String name, boolean cascade, Request request) {
        TilesContainer container = TilesAccess.getCurrentContainer(request);
        ArrayStack<Object> composeStack = ComposeStackUtil.getComposeStack(request);
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

    public void execute(String name, String role, boolean inherit,
            boolean cascade, Request request, ModelBody modelBody) throws IOException {
        ArrayStack<Object> composeStack = ComposeStackUtil.getComposeStack(request);
        ListAttribute listAttribute = new ListAttribute();
        listAttribute.setRole(role);
        listAttribute.setInherit(inherit);
        composeStack.push(listAttribute);
        modelBody.evaluateWithoutWriting();
        TilesContainer container = TilesAccess.getCurrentContainer(request);
        listAttribute = (ListAttribute) composeStack.pop();
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
