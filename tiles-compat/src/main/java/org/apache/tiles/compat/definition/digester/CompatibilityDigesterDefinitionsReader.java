package org.apache.tiles.compat.definition.digester;

import org.apache.commons.digester.Digester;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader.FillAttributeRule;

public class CompatibilityDigesterDefinitionsReader extends
        DigesterDefinitionsReader {
    /**
     * The set of public identifiers, and corresponding resource names for the
     * versions of the configuration file DTDs we know about. There <strong>MUST</strong>
     * be an even number of Strings in this list!
     */
    protected String registrations[] = {
            "-//Apache Software Foundation//DTD Tiles Configuration 2.0//EN",
            "/org/apache/tiles/resources/tiles-config_2_0.dtd",
            "-//Apache Software Foundation//DTD Tiles Configuration 1.1//EN",
            "/org/apache/tiles/compat/resources/tiles-config_1_1.dtd",
            "-//Apache Software Foundation//DTD Tiles Configuration 1.3//EN",
            "/org/apache/tiles/compat/resources/tiles-config_1_3.dtd",
            "-//Apache Software Foundation//DTD Tiles Configuration 1.4//EN",
            "/org/apache/tiles/compat/resources/tiles-config_1_4.dtd" };

    @Override
    protected void initSyntax(Digester digester) {
        super.initSyntax(digester);
        initDigesterForComponentsDefinitionsSyntax(digester);
    }

    /**
     * Init digester for components syntax. This is an old set of rules, left
     * for backward compatibility.
     * 
     * @param digester Digester instance to use.
     */
    private void initDigesterForComponentsDefinitionsSyntax(Digester digester) {
        // Common constants
        String DEFINITION_TAG = "component-definitions/definition";

        String PUT_TAG = DEFINITION_TAG + "/put";

        String LIST_TAG = DEFINITION_TAG + "/putList";

        String ADD_LIST_ELE_TAG = LIST_TAG + "/add";

        // syntax rules
        digester.addObjectCreate(DEFINITION_TAG, DEFINITION_HANDLER_CLASS);
        digester.addSetProperties(DEFINITION_TAG);
        digester.addSetNext(DEFINITION_TAG, "putDefinition",
                DEFINITION_HANDLER_CLASS);
        // put / putAttribute rules
        digester.addObjectCreate(PUT_TAG, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(PUT_TAG, new FillAttributeRule());
        digester.addSetNext(PUT_TAG, "addAttribute",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addCallMethod(PUT_TAG, "setBody", 0);
        // list rules
        digester.addObjectCreate(LIST_TAG, LIST_HANDLER_CLASS);
        digester.addSetProperties(LIST_TAG);
        digester.addSetNext(LIST_TAG, "addAttribute",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        // list elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        digester.addObjectCreate(ADD_LIST_ELE_TAG, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(ADD_LIST_ELE_TAG, new FillAttributeRule());
        digester.addSetNext(ADD_LIST_ELE_TAG, "add",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addCallMethod(ADD_LIST_ELE_TAG, "setBody", 0);
    }

    /**
     * Init digester for Tiles syntax. Same as components, but with first
     * element = tiles-definitions
     * 
     * @param digester Digester instance to use.
     */
    private void initDigesterForTilesDefinitionsSyntax(Digester digester) {
        // Common constants
        String DEFINITION_TAG = "tiles-definitions/definition";

        String PUT_TAG = DEFINITION_TAG + "/put";

        // String LIST_TAG = DEFINITION_TAG + "/putList";
        // List tag value
        String LIST_TAG = "putList";
        String DEF_LIST_TAG = DEFINITION_TAG + "/" + LIST_TAG;
        // Tag value for adding an element in a list
        String ADD_LIST_ELE_TAG = "*/" + LIST_TAG + "/add";

        // syntax rules
        digester.addObjectCreate(DEFINITION_TAG, DEFINITION_HANDLER_CLASS);
        digester.addSetProperties(DEFINITION_TAG);
        digester.addSetNext(DEFINITION_TAG, "putDefinition",
                DEFINITION_HANDLER_CLASS);
        // put / putAttribute rules
        // Rules for a same pattern are called in order, but rule.end() are
        // called
        // in reverse order.
        // SetNext and CallMethod use rule.end() method. So, placing SetNext in
        // first position ensure it will be called last (sic).
        digester.addObjectCreate(PUT_TAG, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(PUT_TAG, new FillAttributeRule());
        digester.addSetNext(PUT_TAG, "addAttribute",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addCallMethod(PUT_TAG, "setBody", 0);
        // Definition level list rules
        // This is rules for lists nested in a definition
        digester.addObjectCreate(DEF_LIST_TAG, LIST_HANDLER_CLASS);
        digester.addSetProperties(DEF_LIST_TAG);
        digester.addSetNext(DEF_LIST_TAG, "addAttribute",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        // list elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        digester.addObjectCreate(ADD_LIST_ELE_TAG, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(ADD_LIST_ELE_TAG, new FillAttributeRule());
        digester.addSetNext(ADD_LIST_ELE_TAG, "add",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addCallMethod(ADD_LIST_ELE_TAG, "setBody", 0);

        // nested list elements rules
        // Create a list handler, and add it to parent list
        String NESTED_LIST = "*/" + LIST_TAG + "/" + LIST_TAG;
        digester.addObjectCreate(NESTED_LIST, LIST_HANDLER_CLASS);
        digester.addSetProperties(NESTED_LIST);
        digester.addSetNext(NESTED_LIST, "add", PUT_ATTRIBUTE_HANDLER_CLASS);

        // item elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        // String ADD_WILDCARD = LIST_TAG + "/addItem";
        // non String ADD_WILDCARD = LIST_TAG + "/addx*";
        String ADD_WILDCARD = "*/item";
        String menuItemDefaultClass = "org.apache.struts.tiles.beans.SimpleMenuItem";
        digester.addObjectCreate(ADD_WILDCARD, menuItemDefaultClass,
                "classtype");
        digester.addSetNext(ADD_WILDCARD, "add", "java.lang.Object");
        digester.addSetProperties(ADD_WILDCARD);

        // bean elements rules
        String BEAN_TAG = "*/bean";
        String beanDefaultClass = "org.apache.struts.tiles.beans.SimpleMenuItem";
        digester.addObjectCreate(BEAN_TAG, beanDefaultClass, "classtype");
        digester.addSetNext(BEAN_TAG, "add", "java.lang.Object");
        digester.addSetProperties(BEAN_TAG);

        // Set properties to surrounding element
        digester
                .addSetProperty(BEAN_TAG + "/set-property", "property", "value");
    }

    /**
     * Init digester in order to parse instances definition file syntax.
     * Instances is an old name for "definition". This method is left for
     * backwards compatibility.
     * 
     * @param digester Digester instance to use.
     */
    private void initDigesterForInstancesSyntax(Digester digester) {
        // Build a digester to process our configuration resource
        String INSTANCE_TAG = "component-instances/instance";

        String PUT_TAG = INSTANCE_TAG + "/put";
        String PUTATTRIBUTE_TAG = INSTANCE_TAG + "/putAttribute";

        String LIST_TAG = INSTANCE_TAG + "/putList";

        String ADD_LIST_ELE_TAG = LIST_TAG + "/add";

        // component instance rules
        digester.addObjectCreate(INSTANCE_TAG, DEFINITION_HANDLER_CLASS);
        digester.addSetProperties(INSTANCE_TAG);
        digester
                .addSetNext(INSTANCE_TAG, "putDefinition", DEFINITION_HANDLER_CLASS);
        // put / putAttribute rules
        digester.addObjectCreate(PUTATTRIBUTE_TAG, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(PUT_TAG, new FillAttributeRule());
        digester.addSetNext(PUTATTRIBUTE_TAG, "addAttribute",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        // put / putAttribute rules
        digester.addObjectCreate(PUT_TAG, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addSetProperties(PUT_TAG);
        digester.addSetNext(PUT_TAG, "addAttribute", PUT_ATTRIBUTE_HANDLER_CLASS);
        // list rules
        digester.addObjectCreate(LIST_TAG, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addSetProperties(LIST_TAG);
        digester.addSetNext(LIST_TAG, "addAttribute", PUT_ATTRIBUTE_HANDLER_CLASS);
        // list elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        digester.addObjectCreate(ADD_LIST_ELE_TAG, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(ADD_LIST_ELE_TAG, new FillAttributeRule());
        digester.addSetNext(ADD_LIST_ELE_TAG, "add", PUT_ATTRIBUTE_HANDLER_CLASS);
    }
}
