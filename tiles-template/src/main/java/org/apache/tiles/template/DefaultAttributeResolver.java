package org.apache.tiles.template;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;

public class DefaultAttributeResolver implements AttributeResolver {

    /**
     * Computes the attribute to render, evaluating the various tag attributes.
     * @param context The page context.
     *
     * @return The computed attribute.
     */
    public Attribute computeAttribute(TilesContainer container, Attribute attribute,
            String name, String role, boolean ignore,
            Object defaultValue, String defaultValueRole, String defaultValueType, Object... requestItems) {
        if (attribute == null) {
            AttributeContext evaluatingContext = container
                    .getAttributeContext(requestItems);
            attribute = evaluatingContext.getAttribute(name);
            if (attribute == null) {
                attribute = computeDefaultAttribute(defaultValue,
                        defaultValueRole, defaultValueType);
                if (attribute == null && !ignore) {
                    throw new NoSuchAttributeException("Attribute '" + name
                            + "' not found.");
                }
            }
        }
        if (attribute != null && role != null && !"".equals(role.trim())) {
            attribute = new Attribute(attribute);
            attribute.setRole(role);
        }
        return attribute;
    }

    /**
     * Computes the default attribute.
     *
     * @return The default attribute.
     */
    private Attribute computeDefaultAttribute(Object defaultValue,
            String defaultValueRole, String defaultValueType) {
        Attribute attribute = null;
        if (defaultValue != null) {
            if (defaultValue instanceof Attribute) {
                attribute = (Attribute) defaultValue;
            } else if (defaultValue instanceof String) {
                attribute = new Attribute(defaultValue,
                        null, defaultValueRole, defaultValueType);
            }
        }
        return attribute;
    }
}
