package org.apache.tiles.template;

import java.util.Stack;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;

public class PutListAttributeModel {

    public void start(Stack<Object> composeStack, String role, boolean inherit) {
        ListAttribute listAttribute = new ListAttribute();
        listAttribute.setRole(role);
        listAttribute.setInherit(inherit);
        composeStack.push(listAttribute);
    }
    
    public void end(TilesContainer container,
            Stack<Object> composeStack, String name, boolean cascade, Object... requestItems) {
        ListAttribute listAttribute = (ListAttribute) composeStack.pop();
        AttributeContext attributeContext = null;
        if (!composeStack.isEmpty()) {
            Object obj = composeStack.peek();
            if (obj instanceof Definition) {
                attributeContext = (AttributeContext) obj;
            }
        }
        if (attributeContext == null) {
            attributeContext = container.getAttributeContext(requestItems);
        }
        attributeContext.putAttribute(name, listAttribute, cascade);
    }
}
