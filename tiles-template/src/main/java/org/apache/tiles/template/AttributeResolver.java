package org.apache.tiles.template;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;

public interface AttributeResolver {
    Attribute computeAttribute(TilesContainer container, Attribute attribute,
            String name, String role, boolean ignore,
            Object defaultValue, String defaultValueRole, String defaultValueType, Object... requestItems);
}
