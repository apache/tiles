/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.tiles.compat.definition.digester;

import org.apache.commons.digester.Digester;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;

/**
 * Digester reader that can read Tiles 1.1, 1.2, 1.3, 1.4 and 2.0 files.
 *
 * @version $Rev$ $Date$
 */
public class CompatibilityDigesterDefinitionsReader extends
        DigesterDefinitionsReader {
    /**
     * The set of public identifiers, and corresponding resource names for the
     * versions of the configuration file DTDs we know about. There <strong>MUST</strong>
     * be an even number of Strings in this list!
     */
    protected String[] registrations;

    /** {@inheritDoc} */
    @Override
    protected void initSyntax(Digester digester) {
        super.initSyntax(digester);
        initDigesterForComponentsDefinitionsSyntax(digester);
        initDigesterForInstancesSyntax(digester);
        initDigesterForTilesDefinitionsSyntax(digester);
    }

    /**
     * Init digester for components syntax. This is an old set of rules, left
     * for backward compatibility.
     *
     * @param digester Digester instance to use.
     */
    private void initDigesterForComponentsDefinitionsSyntax(Digester digester) {
        // Common constants
        String definitionTag = "component-definitions/definition";

        String putTag = definitionTag + "/put";

        String listTag = definitionTag + "/putList";

        String addListElementTag = listTag + "/add";

        // syntax rules
        digester.addObjectCreate(definitionTag, DEFINITION_HANDLER_CLASS);
        digester.addSetProperties(definitionTag);
        digester.addSetNext(definitionTag, "addDefinition",
                DEFINITION_HANDLER_CLASS);
        // put / putAttribute rules
        digester.addObjectCreate(putTag, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(putTag, new FillAttributeRule());
        digester.addRule(putTag, new PutAttributeRule());
        digester.addCallMethod(putTag, "setBody", 0);
        // list rules
        digester.addObjectCreate(listTag, LIST_HANDLER_CLASS);
        digester.addSetProperties(listTag);
        digester.addRule(listTag, new PutAttributeRule());
        // list elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        digester.addObjectCreate(addListElementTag, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(addListElementTag, new FillAttributeRule());
        digester.addSetNext(addListElementTag, "add",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addCallMethod(addListElementTag, "setBody", 0);
    }

    /**
     * Init digester for Tiles syntax. Same as components, but with first
     * element = tiles-definitions
     *
     * @param digester Digester instance to use.
     */
    private void initDigesterForTilesDefinitionsSyntax(Digester digester) {
        // Common constants
        String definitionTag = "tiles-definitions/definition";

        String putTag = definitionTag + "/put";

        // String LIST_TAG = DEFINITION_TAG + "/putList";
        // List tag value
        String listTag = "putList";
        String definitionListTag = definitionTag + "/" + listTag;
        // Tag value for adding an element in a list
        String addListElementTag = "*/" + listTag + "/add";

        // put / putAttribute rules
        // Rules for a same pattern are called in order, but rule.end() are
        // called
        // in reverse order.
        // SetNext and CallMethod use rule.end() method. So, placing SetNext in
        // first position ensure it will be called last (sic).
        digester.addObjectCreate(putTag, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(putTag, new FillAttributeRule());
        digester.addRule(putTag, new PutAttributeRule());
        digester.addCallMethod(putTag, "setBody", 0);
        // Definition level list rules
        // This is rules for lists nested in a definition
        digester.addObjectCreate(definitionListTag, LIST_HANDLER_CLASS);
        digester.addSetProperties(definitionListTag);
        digester.addRule(definitionListTag, new PutAttributeRule());
        // list elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        digester.addObjectCreate(addListElementTag, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(addListElementTag, new FillAttributeRule());
        digester.addSetNext(addListElementTag, "add",
                PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addCallMethod(addListElementTag, "setBody", 0);

        // nested list elements rules
        // Create a list handler, and add it to parent list
        String nestedList = "*/" + listTag + "/" + listTag;
        digester.addObjectCreate(nestedList, LIST_HANDLER_CLASS);
        digester.addSetProperties(nestedList);
        digester.addSetNext(nestedList, "add", PUT_ATTRIBUTE_HANDLER_CLASS);
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
        String instanceTag = "component-instances/instance";

        String putTag = instanceTag + "/put";
        String putAttributeTag = instanceTag + "/putAttribute";

        String listTag = instanceTag + "/putList";

        String addListElementTag = listTag + "/add";

        // component instance rules
        digester.addObjectCreate(instanceTag, DEFINITION_HANDLER_CLASS);
        digester.addSetProperties(instanceTag);
        digester
                .addSetNext(instanceTag, "addDefinition", DEFINITION_HANDLER_CLASS);
        // put / putAttribute rules
        digester.addObjectCreate(putAttributeTag, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(putTag, new FillAttributeRule());
        digester.addRule(putTag, new PutAttributeRule());
        // put / putAttribute rules
        digester.addObjectCreate(putTag, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addSetProperties(putTag);
        digester.addRule(putTag, new PutAttributeRule());
        // list rules
        digester.addObjectCreate(listTag, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addSetProperties(listTag);
        digester.addRule(listTag, new PutAttributeRule());
        // list elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        digester.addObjectCreate(addListElementTag, PUT_ATTRIBUTE_HANDLER_CLASS);
        digester.addRule(addListElementTag, new FillAttributeRule());
        digester.addSetNext(addListElementTag, "add", PUT_ATTRIBUTE_HANDLER_CLASS);
    }

    /** {@inheritDoc} */
    protected String[] getRegistrations() {
        if (registrations == null) {
            registrations = new String[] {
                "-//Apache Software Foundation//DTD Tiles Configuration 2.0//EN",
                "/org/apache/tiles/resources/tiles-config_2_0.dtd",
                "-//Apache Software Foundation//DTD Tiles Configuration 1.1//EN",
                "/org/apache/tiles/compat/resources/tiles-config_1_1.dtd",
                "-//Apache Software Foundation//DTD Tiles Configuration 1.3//EN",
                "/org/apache/tiles/compat/resources/tiles-config_1_3.dtd",
                "-//Apache Software Foundation//DTD Tiles Configuration 1.4//EN",
                "/org/apache/tiles/compat/resources/tiles-config_1_4.dtd"};
        }
        return registrations;
    }
}
