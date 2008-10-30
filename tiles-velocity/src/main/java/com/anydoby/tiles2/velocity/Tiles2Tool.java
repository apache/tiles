package com.anydoby.tiles2.velocity;

import java.io.IOException;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.velocity.tools.view.context.ChainedContext;

/**
 * 
 * @author SergeyZ
 * 
 */
public class Tiles2Tool {

    private final TilesContainer container;
    private final VelocityTiles2RequestContext context;

    public Tiles2Tool(TilesContainer container, VelocityTiles2RequestContext context) {
        this.container = container;
        this.context = context;
    }

    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * 
     * @param attributeName
     */
    public String getAsString(String attributeName) {
        Attribute attribute = getAttribute(attributeName);
        String value = null;
        if (attribute != null) {
            Object value2 = attribute.getValue();
            if (value2 != null) {
                value = value2.toString();
            }
        }
        return value;
    }

    public Attribute getAttribute(String key) {
        AttributeContext attributeContext = container.getAttributeContext(context);
        Attribute attribute = attributeContext.getAttribute(key);
        return attribute;
    }

    /**
     * Imports attribute to current velocity context using the optional toName
     * as the destination name in context
     * 
     * @param attributeName
     * @param toName
     */
    public void importAttribute(String attributeName, String toName) {
        Object attribute = getAttribute(attributeName);
        if (toName == null) {
            toName = attributeName;
        }
        context.put(toName, attribute);
    }

    /**
     * Invokes {@link #insertAttribute(String, true)}
     * 
     * @param attributeName
     * @throws TilesException
     * @throws IOException
     */
    public void insertAttribute(String attributeName) throws TilesException, IOException {
        insertAttribute(attributeName, true);
    }

    /**
     * <p>
     * <strong>Inserts the value of an attribute into the page.</strong>
     * </p>
     * <p>
     * This tag can be flexibly used to insert the value of an attribute into a
     * page. As in other usages in Tiles, every attribute can be determined to
     * have a "type", either set explicitly when it was defined, or "computed".
     * If the type is not explicit, then if the attribute value is a valid
     * definition, it will be inserted as such. Otherwise, if it begins with a
     * "/" character, it will be treated as a "template". Finally, if it has not
     * otherwise been assigned a type, it will be treated as a String and
     * included without any special handling.
     * </p>
     * 
     * @param attributeName
     * @throws TilesException
     * @throws IOException
     */
    public void insertAttribute(String attributeName, boolean ownContext) throws TilesException, IOException {
        Attribute attribute = getAttribute(attributeName);
        if (attribute == null) {
            throw new TilesException("Attribute '" + attributeName + "' is null");
        }
        if (ownContext) {
            ChainedContext chainedContext = new ChainedContext(context.getContext(), context.getEngine(), context
                    .getRequest(), context.getResponse(), context.getServletContext());
            container.startContext(chainedContext);
            try {
                render(attribute);
            } finally {
                container.endContext(chainedContext);
            }
        } else {
            render(attribute);
        }
    }

    /**
     * Invokes {@link #insertDefinition(String, true)}.
     * 
     * @param definitionName
     * @throws TilesException
     */
    public void insertDefinition(String definitionName) throws TilesException {
        insertDefinition(definitionName, true);
    }

    /**
     * Inserts a named definition from the tiles definitions set.
     * 
     * @param definitionName
     * @param ownContext
     *            if <code>true</code> a separate request context will be
     *            created for the definition rendering. Can be used to avoid
     *            name conflicts if the definition being included contains the
     *            same attribute names as the invoking tile
     * @throws TilesException
     */
    public void insertDefinition(String definitionName, boolean ownContext) throws TilesException {
        if (ownContext) {
            ChainedContext chainedContext = new ChainedContext(context.getContext(), context.getEngine(), context
                    .getRequest(), context.getResponse(), context.getServletContext());
            container.startContext(chainedContext);
            try {
                container.render(definitionName, chainedContext);
            } finally {
                container.endContext(chainedContext);
            }
        } else {
            container.render(definitionName, context);
        }
    }

    /**
     * Includes the specified page.
     * 
     * @param template
     * @throws IOException
     */
    public void insertTemplate(String template) throws IOException {
        context.include(template);
    }

    private void render(Attribute attribute) throws TilesException, IOException {
        container.render(attribute, context.getResponse().getWriter(), context);
    }

}
