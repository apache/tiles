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
import org.apache.tiles.ListAttribute;
import org.apache.tiles.request.Request;

/**
 * <p>
 * <strong>Declare a list that will be pass as an attribute. </strong>
 * </p>
 * <p>
 * Declare a list that will be pass as an attribute . List elements are added
 * using the tag 'addAttribute' or 'addListAttribute'. This tag can only be used
 * inside 'insertTemplate', 'insertDefinition' or 'definition' tag.
 * </p>
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class AddListAttributeModel {

    /**
     * Starts the operation.
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param request TODO
     *
     * @since 2.2.0
     */
    public void start(String role, Request request) {
        ArrayStack<Object> composeStack = ComposeStackUtil.getComposeStack(request);
        ListAttribute listAttribute = new ListAttribute();
        listAttribute.setRole(role);
        composeStack.push(listAttribute);
    }

    /**
     * Ends the operation.
     * @param request TODO
     *
     * @since 2.2.0
     */
    public void end(Request request) {
        ArrayStack<Object> composeStack = ComposeStackUtil.getComposeStack(request);
        ListAttribute listAttribute = (ListAttribute) composeStack.pop();
        ListAttribute parent = (ListAttribute) composeStack.peek();
        parent.add(listAttribute);
    }
}
