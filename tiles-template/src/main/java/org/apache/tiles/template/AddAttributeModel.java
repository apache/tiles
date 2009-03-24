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

import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.ListAttribute;

/**
 * <p>
 * <strong>Add an element to the surrounding list. Equivalent to 'putAttribute',
 * but for list element.</strong>
 * </p>
 * 
 * <p>
 * Add an element to the surrounding list. This tag can only be used inside
 * 'putListAttribute' or 'addListAttribute' tags. Value can come from a direct
 * assignment (value="aValue")
 * </p>
 * 
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class AddAttributeModel {

    /**
     * Starts the operation..
     * 
     * @param composeStack The composing stack.
     * @since 2.2.0
     */
    public void start(Stack<Object> composeStack) {
        Attribute attribute = new Attribute();
        composeStack.push(attribute);
    }

    /**
     * Ends the operation.
     * 
     * @param composeStack The composing stack.
     * @param value The value of the attribute. Use this parameter, or
     * expression, or body.
     * @param expression The expression to calculate the value from. Use this
     * parameter, or value, or body.
     * @param body The body of the tag. Use this parameter, or value, or
     * expression.
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param type The type (renderer) of the attribute.
     * @since 2.2.0
     */
    public void end(Stack<Object> composeStack, Object value,
            String expression, String body, String role, String type) {
        Attribute attribute = (Attribute) composeStack.pop();
        addAttributeToList(attribute, composeStack, value, expression, body,
                role, type);
    }

    /**
     * Executes the operation.
     * 
     * @param composeStack The composing stack.
     * @param value The value of the attribute. Use this parameter, or
     * expression, or body.
     * @param expression The expression to calculate the value from. Use this
     * parameter, or value, or body.
     * @param body The body of the tag. Use this parameter, or value, or
     * expression.
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param type The type (renderer) of the attribute.
     * @since 2.2.0
     */
    public void execute(Stack<Object> composeStack, Object value,
            String expression, String body, String role, String type) {
        addAttributeToList(new Attribute(), composeStack, value, expression,
                body, role, type);
    }

    /**
     * Adds the attribute to the containing list attribute.
     * 
     * @param attribute The attribute to add to the list attribute.
     * @param composeStack The composing stack.
     * @param value The value of the attribute. Use this parameter, or
     * expression, or body.
     * @param expression The expression to calculate the value from. Use this
     * parameter, or value, or body.
     * @param body The body of the tag. Use this parameter, or value, or
     * expression.
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param type The type (renderer) of the attribute.
     * @since 2.2.0
     */
    private void addAttributeToList(Attribute attribute,
            Stack<Object> composeStack, Object value, String expression,
            String body, String role, String type) {
        ListAttribute listAttribute = (ListAttribute) ComposeStackUtil
                .findAncestorWithClass(composeStack, ListAttribute.class);
        
        if (listAttribute == null) {
            throw new NullPointerException("There is no ListAttribute in the stack");
        }
        if (value != null) {
            attribute.setValue(value);
        } else if (attribute.getValue() == null && body != null) {
            attribute.setValue(body);
        }
        if (expression != null) {
            attribute.setExpression(expression);
        }
        if (role != null) {
            attribute.setRole(role);
        }
        if (type != null) {
            attribute.setRenderer(type);
        }
        listAttribute.add(attribute);
    }
}
