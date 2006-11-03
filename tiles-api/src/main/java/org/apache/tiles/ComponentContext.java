package org.apache.tiles;

import javax.servlet.jsp.PageContext;
import java.util.Map;
import java.util.Iterator;

/**
 * Encapsulation of the current state of execution.
 *
 * @since Tiles 2.0
 * @version $Rev$
 *
 */
public interface ComponentContext {

    /**
     * Add all attributes to the context.
     *
     * @param newAttributes the attributes to be added.
     */
    void addAll(Map<String, ComponentAttribute> newAttributes);

    /**
     * Add all attributes to the context
     *
     * @param defaultAttributes attributes which should be present.
     */
    void addMissing(Map<String, ComponentAttribute> defaultAttributes);

    /**
     * Retrieve the named attribute.
     *
     * @param name key name for the attribute.
     * @return
     */
    ComponentAttribute getAttribute(String name);

    /**
     * Iterator of all attribute names.
     *
     * @return
     */
    Iterator<String> getAttributeNames();

    /**
     * Add the specified attribute.
     *
     * @param name
     * @param value
     */
    void putAttribute(String name, ComponentAttribute value);

    /**
     * Find the attribute
     *
     * @param beanName
     * @param pageContext
     * @return
     */
    ComponentAttribute findAttribute(String beanName, PageContext pageContext);

    /**
     * Find the named attribute.
     *
     * @param beanName
     * @param scope
     * @param pageContext
     * @return
     */
    ComponentAttribute getAttribute(
        String beanName,
        int scope,
        PageContext pageContext);
}
