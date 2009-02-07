package org.apache.tiles.template;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;

public class InsertDefinitionModel {
    
    public void start(TilesContainer container, Object... requestItems) {
        container.startContext(requestItems);
    }

    public void end(TilesContainer container, String definitionName,
            String template, String role, String preparer,
            Object... requestItems) {
        try {
            AttributeContext attributeContext = container
                    .getAttributeContext(requestItems);
            Attribute templateAttribute = Attribute
                    .createTemplateAttribute(template);
            templateAttribute.setRole(role);
            attributeContext.setPreparer(preparer);
            attributeContext.setTemplateAttribute(templateAttribute);
            container.renderContext(definitionName, requestItems);
        } finally {
            container.endContext(requestItems);
        }
    }
}
