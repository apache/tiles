package org.apache.tiles.template;

import java.util.Stack;

import org.apache.tiles.ListAttribute;

public class AddListAttributeModel {

    public void start(Stack<Object> composeStack, String role) {
        ListAttribute listAttribute = new ListAttribute();
        listAttribute.setRole(role);
        composeStack.push(listAttribute);
    }
    
    public ListAttribute end(Stack<Object> composeStack) {
        return (ListAttribute) composeStack.pop();
    }
}
