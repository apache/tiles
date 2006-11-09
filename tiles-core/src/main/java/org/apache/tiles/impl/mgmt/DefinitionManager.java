package org.apache.tiles.impl.mgmt;

import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.mgmt.TileDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class DefinitionManager {

    private static final Log LOG =
        LogFactory.getLog(DefinitionManager.class);

    private Map<String, ComponentDefinition> definitions;
    private DefinitionsFactory factory;

    public DefinitionManager() {
        definitions = new HashMap<String, ComponentDefinition>();
    }


    public DefinitionsFactory getFactory() {
        return factory;
    }

    public void setFactory(DefinitionsFactory factory) {
        this.factory = factory;
    }

    public ComponentDefinition getDefinition(String definition, TilesRequestContext request)
        throws DefinitionsFactoryException {
        if (definitions.containsKey(definition)) {
            return definitions.get(definition);
        }
        return getFactory().getDefinition(definition, request);
    }

    public void addDefinition(ComponentDefinition definition) {
        validate(definition);

        if(definition.getExtends() != null) {
        }

        definitions.put(definition.getName(), definition);
    }

    private void validate(TileDefinition definition) {
        Map<String, ComponentAttribute> attrs = definition.getAttributes();
        for (ComponentAttribute attribute : attrs.values()) {
            if (attribute.getName() == null) {
                throw new IllegalArgumentException("Attribute name not defined");
            }

            if (attribute.getValue() == null) {
                throw new IllegalArgumentException("Attribute value not defined");
            }
        }
    }

    /**
     * Resolve inheritance.
     * First, resolve parent's inheritance, then set template to the parent's
     * template.
     * Also copy attributes setted in parent, and not set in child
     * If instance doesn't extend anything, do nothing.
     *
     * @throws NoSuchDefinitionException If an inheritance can not be solved.
     * @param definition def
     */
    protected void resolveInheritance(ComponentDefinition definition)
        throws DefinitionsFactoryException  {
        // Already done, or not needed ?
        if (definition.isIsVisited() || !definition.isExtending())
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Resolve definition for child name='"
                + definition.getName()
                + "' extends='" + definition.getExtends() + "'.");

        // Set as visited to avoid endless recurisvity.
        definition.setIsVisited(true);

        // TODO Factories our factory implementations will be context agnostic,
        //  however, this may cause errors for other implementations.
        //  we should probably make all factories agnostic and allow the manager to
        //  utilize the correct factory based on the context.
        ComponentDefinition parent = getDefinition(definition.getExtends(), null);


        if (parent == null) { // error
            String msg = "Error while resolving definition inheritance: child '"
                + definition.getName()
                + "' can't find its ancestor '"
                + definition.getExtends()
                + "'. Please check your description file.";
            LOG.error(msg);
            // to do : find better exception
            throw new NoSuchDefinitionException(msg);
        }

        // Resolve parent before itself.
        resolveInheritance(parent);
        overload(parent, definition);
    }

    /**
     * Overloads a child definition with a given parent.
     * All attributes present in child are kept. All missing attributes are
     * copied from the parent.
     * Special attribute 'template','role' and 'extends' are overloaded in child
     * if not defined
     *
     * @param parent The parent definition.
     * @param child  The child that will be overloaded.
     */
    protected void overload(ComponentDefinition parent,
                            ComponentDefinition child) {
        // Iterate on each parent's attribute and add it if not defined in child.
        for(Map.Entry<String, ComponentAttribute> entry : parent.getAttributes().entrySet()) {
            child.putAttribute(entry.getKey(), new ComponentAttribute(entry.getValue()));
        }

        if (child.getTemplate() == null)
            child.setTemplate(parent.getTemplate());

        if (child.getRole() == null)
            child.setRole(parent.getRole());

        if (child.getPreparer() == null) {
            child.setPreparer(parent.getPreparer());
        }
    }
}
