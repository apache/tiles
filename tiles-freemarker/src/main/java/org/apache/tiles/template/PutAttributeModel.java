package org.apache.tiles.template;

import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;

public class PutAttributeModel {

    public void start(Stack<Object> composeStack) {
        Attribute attribute = new Attribute();
        composeStack.push(attribute);
    }
    
    public void end(TilesContainer container, Stack<Object> composeStack,
            String name, Object value, String expression, String body,
            String role, String type, boolean cascade, Object... requestItems) {
        AttributeContext attributeContext = container
                .getAttributeContext(requestItems);
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

        attributeContext.putAttribute(name, attribute, cascade);
    }
}
