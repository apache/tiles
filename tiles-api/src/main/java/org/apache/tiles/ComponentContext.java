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
     * @return ComponentAttribute associated with the given name.
     */
    ComponentAttribute getAttribute(String name);

    /**
     * Iterator of all attribute names.
     *
     * @return iterator of all names.
     */
    Iterator<String> getAttributeNames();

    /**
     * Add the specified attribute.
     *
     * @param name name of the attribute
     * @param value value of the attribute
     */
    void putAttribute(String name, ComponentAttribute value);

    /**
     * Find the attribute
     *
     * @param beanName name of the bean
     * @param pageContext current pageContext.
     * @return search for the attribute in one of the scopes.
     */
    ComponentAttribute findAttribute(String beanName, PageContext pageContext);

    /**
     * Find the named attribute.
     *
     * @param beanName name of the bean
     * @param scope scope of the bean
     * @param pageContext current pageContext
     * @return component attribute - if found.
     */
    ComponentAttribute getAttribute(
        String beanName,
        int scope,
        PageContext pageContext);

    /**
     * Clear the attributes
     */
    void clear();
}
