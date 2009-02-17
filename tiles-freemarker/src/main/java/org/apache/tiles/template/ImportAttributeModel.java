package org.apache.tiles.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;

public class ImportAttributeModel {

    private Log log = LogFactory.getLog(getClass());
    
    public Map<String, Object> getImportedAttributes(TilesContainer container,
            String name, String toName, boolean ignore, Object... requestItems) {
        Map<String, Object> retValue = new HashMap<String, Object>();
        AttributeContext attributeContext = container
                .getAttributeContext(requestItems);
        // Some tags allow for unspecified attributes.  This
        // implies that the tag should use all of the attributes.
        if (name != null) {
            importSingleAttribute(container, attributeContext, name, toName,
                    ignore, retValue, requestItems);
        } else {
            importAttributes(attributeContext.getCascadedAttributeNames(),
                    container, attributeContext, retValue, ignore, requestItems);
            importAttributes(attributeContext.getLocalAttributeNames(),
                    container, attributeContext, retValue, ignore, requestItems);
        }
        return retValue;
    }

    private void importSingleAttribute(TilesContainer container,
            AttributeContext attributeContext, String name, String toName,
            boolean ignore, Map<String, Object> attributes,
            Object... requestItems) {
        Attribute attribute = attributeContext.getAttribute(name);
        if ((attribute == null || attribute.getValue() == null) && ignore) {
            return;
        }

        if (attribute == null) {
            throw new NoSuchAttributeException("Attribute with name '" + name
                    + "' not found");
        }

        Object attributeValue = null;
        
        try {
            attributeValue = container.evaluate(attribute, requestItems);
        } catch (RuntimeException e) {
            if (!ignore) {
                throw e;
            } else if (log.isDebugEnabled()) {
                log.debug("Ignoring Tiles Exception", e);
            }
        }

        if (attributeValue == null && !ignore) {
            throw new NoSuchAttributeException("Attribute with name '"
                    + name + "' has a null value.");
        }
        
        if (toName != null) {
            attributes.put(toName, attributeValue);
        } else {
            attributes.put(name, attributeValue);
        }
    }

    /**
     * Imports an attribute set.
     *
     * @param names The names of the attributes to be imported.
     * @throws TilesJspException If something goes wrong during the import.
     */
    private void importAttributes(Collection<String> names,
            TilesContainer container, AttributeContext attributeContext,
            Map<String, Object> attributes, boolean ignore,
            Object... requestItems) {
        if (names == null || names.isEmpty()) {
            return;
        }

        for (String name : names) {
            if (name == null && !ignore) {
                throw new NoSuchAttributeException(
                        "Error importing attributes. "
                                + "Attribute with null key found.");
            } else if (name == null) {
                continue;
            }

            Attribute attr = attributeContext.getAttribute(name);

            if (attr != null) {
                try {
                    Object attributeValue = container.evaluate(attr, requestItems);
                    if (attributeValue == null && !ignore) {
                        throw new NoSuchAttributeException(
                                "Error importing attributes. " + "Attribute '"
                                        + name + "' has a null value ");
                    }
                    attributes.put(name, attributeValue);
                } catch (RuntimeException e) {
                    if (!ignore) {
                        throw e;
                    } else if (log.isDebugEnabled()) {
                        log.debug("Ignoring Tiles Exception", e);
                    }
                }
            } else if (!ignore) {
                throw new NoSuchAttributeException(
                        "Error importing attributes. " + "Attribute '" + name
                                + "' is null");
            }
        }
    }
}
