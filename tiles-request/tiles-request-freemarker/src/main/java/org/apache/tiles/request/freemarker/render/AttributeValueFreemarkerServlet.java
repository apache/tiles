package org.apache.tiles.request.freemarker.render;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.request.freemarker.servlet.TilesFreemarkerServlet;

/**
 * Extends {@link TilesFreemarkerServlet} to use the attribute value as the template name.
 */
public class AttributeValueFreemarkerServlet extends TilesFreemarkerServlet {

    private static final long serialVersionUID = 5412169082301451211L;

    /**
     * Holds the value that should be used as the template name.
     */
    private ThreadLocal<String> valueHolder = new ThreadLocal<String>();

    /**
     * Sets the value to use as the template name.
     *
     * @param value The template name.
     * @since 2.2.0
     */
    public void setValue(String value) {
        valueHolder.set(value);
    }

    /** {@inheritDoc} */
    @Override
    protected String requestUrlToTemplatePath(HttpServletRequest request) {
        return valueHolder.get();
    }
}