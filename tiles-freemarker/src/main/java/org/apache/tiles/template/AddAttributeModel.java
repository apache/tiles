package org.apache.tiles.template;

import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.ListAttribute;

public class AddAttributeModel {

    public void start(Stack<Object> composeStack) {
        Attribute attribute = new Attribute();
        composeStack.push(attribute);
    }
    
    public void end(Stack<Object> composeStack, Object value,
            String expression, String body, String role, String type) {
        ListAttribute listAttribute = (ListAttribute) ComposeStackUtil
                .findAncestorWithClass(composeStack, ListAttribute.class);
        Attribute attribute = (Attribute) composeStack.pop();
        if(value != null) {
            attribute.setValue(value);
        } else if (body != null) {
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
