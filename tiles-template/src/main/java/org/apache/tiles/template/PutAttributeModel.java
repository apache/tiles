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
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;

/**
 * <p>
 * <strong>Put an attribute in enclosing attribute container tag.</strong>
 * </p>
 * <p>
 * Enclosing attribute container tag can be :
 * <ul>
 * <li>&lt;initContainer&gt;</li>
 * <li>&lt;definition&gt;</li>
 * <li>&lt;insertAttribute&gt;</li>
 * <li>&lt;insertDefinition&gt;</li>
 * <li>&lt;putListAttribute&gt;</li>
 * </ul>
 * (or any other tag which implements the <code>PutAttributeTagParent</code>
 * interface. Exception is thrown if no appropriate tag can be found.
 * </p>
 * <p>
 * Put tag can have following atributes :
 * <ul>
 * <li>name : Name of the attribute</li>
 * <li>value : value to put as attribute</li>
 * <li>type : value type. Possible type are : string (value is used as direct
 * string), template (value is used as a page url to insert), definition (value
 * is used as a definition name to insert), object (value is used as it is)</li>
 * <li>role : Role to check when 'insertAttribute' will be called.</li>
 * </ul>
 * </p>
 * <p>
 * Value can also come from tag body. Tag body is taken into account only if
 * value is not set by one of the tag attributes. In this case Attribute type is
 * "string", unless tag body define another type.
 * </p>
 * 
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class PutAttributeModel {

    /**
     * Starts the operation.
     * 
     * @param composeStack The compose stack.
     * @since 2.2.0
     */
    public void start(Stack<Object> composeStack) {
        Attribute attribute = new Attribute();
        composeStack.push(attribute);
    }

    /**
     * Ends the operation.
     *
     * @param container The Tiles container to use.
     * @param composeStack The composing stack.
     * @param name The name of the attribute to put.
     * @param value The value of the attribute. Use this parameter, or
     * expression, or body.
     * @param expression The expression to calculate the value from. Use this
     * parameter, or value, or body.
     * @param body The body of the tag. Use this parameter, or value, or
     * expression.
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param type The type (renderer) of the attribute.
     * @param cascade If <code>true</code> the attribute will be cascaded to all nested attributes.
     * @param requestItems The request objects.
     * @since 2.2.0
     */
    public void end(TilesContainer container, Stack<Object> composeStack,
            String name, Object value, String expression, String body,
            String role, String type, boolean cascade, Object... requestItems) {
        Attribute attribute = (Attribute) composeStack.pop();
        putAttributeInParent(attribute, container, composeStack, name, value,
                expression, body, role, type, cascade, requestItems);
    }

    /**
     * Executes the operation.
     *
     * @param container The Tiles container to use.
     * @param composeStack The composing stack.
     * @param name The name of the attribute to put.
     * @param value The value of the attribute. Use this parameter, or
     * expression, or body.
     * @param expression The expression to calculate the value from. Use this
     * parameter, or value, or body.
     * @param body The body of the tag. Use this parameter, or value, or
     * expression.
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param type The type (renderer) of the attribute.
     * @param cascade If <code>true</code> the attribute will be cascaded to all nested attributes.
     * @param requestItems The request objects.
     * @since 2.2.0
     */
    public void execute(TilesContainer container, Stack<Object> composeStack,
            String name, Object value, String expression, String body,
            String role, String type, boolean cascade, Object... requestItems) {
        putAttributeInParent(new Attribute(), container, composeStack, name,
                value, expression, body, role, type, cascade, requestItems);
    }

    /**
     * Determines the parent and puts the attribute inside it.
     * 
     * @param attribute The attribute to put;
     * @param container The Tiles container to use.
     * @param composeStack The composing stack.
     * @param name The name of the attribute to put.
     * @param value The value of the attribute. Use this parameter, or
     * expression, or body.
     * @param expression The expression to calculate the value from. Use this
     * parameter, or value, or body.
     * @param body The body of the tag. Use this parameter, or value, or
     * expression.
     * @param role A comma-separated list of roles. If present, the attribute
     * will be rendered only if the current user belongs to one of the roles.
     * @param type The type (renderer) of the attribute.
     * @param cascade If <code>true</code> the attribute will be cascaded to all nested attributes.
     * @param requestItems The request objects.
     */
    private void putAttributeInParent(Attribute attribute,
            TilesContainer container, Stack<Object> composeStack, String name,
            Object value, String expression, String body, String role,
            String type, boolean cascade, Object... requestItems) {
        AttributeContext attributeContext = null;
        if (!composeStack.isEmpty()) {
            Object obj = composeStack.peek();
            if (obj instanceof AttributeContext) {
                attributeContext = (AttributeContext) obj;
            }
        }
        if (attributeContext == null) {
            attributeContext = container.getAttributeContext(requestItems);
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

        attributeContext.putAttribute(name, attribute, cascade);
    }
}
