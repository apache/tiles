package org.apache.tiles.template;

import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.mgmt.MutableTilesContainer;

public class DefinitionModel {

    public void start(Stack<Object> composeStack, String name, String template,
            String role, String extend, String preparer) {
        Definition definition = new Definition();
        definition.setName(name);
        Attribute templateAttribute = Attribute
                .createTemplateAttribute(template);
        templateAttribute.setRole(role);
        definition.setTemplateAttribute(templateAttribute);
        definition.setExtends(extend);
        definition.setPreparer(preparer);
        composeStack.push(definition);
    }
    
    public void end(MutableTilesContainer container,
            Stack<Object> composeStack, String name, Object... requestItems) {
        Definition definition = (Definition) composeStack.pop();
        container.register(definition, requestItems);
        Object obj = composeStack.peek();
        if (obj instanceof Attribute) {
            Attribute attribute = (Attribute) obj;
            attribute.setValue(definition.getName());
            if (attribute.getRenderer() == null) {
                attribute.setRenderer("definition");
            }
        }
    }
}
