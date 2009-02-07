package org.apache.tiles.template;

import java.io.IOException;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;

public class InsertAttributeModel {

    private Log log = LogFactory.getLog(getClass());
    
    private AttributeResolver attributeResolver;
    
    public InsertAttributeModel(AttributeResolver attributeResolver) {
        this.attributeResolver = attributeResolver;
    }

    public void start(TilesContainer container, String preparer, Object... requestItems) {
        if (preparer != null) {
            container.prepare(preparer, requestItems);
        }
        container.startContext(requestItems);
    }
    
    public void end(Stack<Object> composeStack, TilesContainer container,
            boolean flush, boolean ignore, String preparer, String role,
            Object defaultValue, String defaultValueRole, String defaultValueType,
            String name, Attribute value, Object... requestItems) throws IOException {
        Attribute attribute = attributeResolver.computeAttribute(container,
                value, name, ignore, defaultValue, defaultValueRole,
                defaultValueType, requestItems);
        if (attribute == null && ignore) {
            return;
        }
        try {
            container.render(attribute, requestItems);
        } catch (IOException e) {
            if (!ignore) {
                throw e;
            } else if (log.isDebugEnabled()) {
                log.debug("Ignoring exception", e);
            }
        } catch (RuntimeException e) {
            if (!ignore) {
                throw e;
            } else if (log.isDebugEnabled()) {
                log.debug("Ignoring exception", e);
            }
        } finally {
            container.endContext(requestItems);
        }
    }
}
