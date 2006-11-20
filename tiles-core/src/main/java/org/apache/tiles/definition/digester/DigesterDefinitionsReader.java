/*
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
 *
 */

package org.apache.tiles.definition.digester;

import org.apache.commons.digester.Digester;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads {@link org.apache.tiles.definition.ComponentDefinition ComponentDefinition} objects
 * from an XML InputStream using Digester.
 * <p/>
 * <p>This <code>DefinitionsReader</code> implementation expects the source to be
 * passed as an <code>InputStream</code>.  It parses XML data from the source and
 * builds a Map of ComponentDefinition objects.</p>
 * <p/>
 * <p>The Digester object can be configured by passing in initialization parameters.
 * Currently the only parameter that is supported is the <code>validating</code>
 * parameter.  This value is set to <code>false</code> by default.  To enable DTD
 * validation for XML ComponentDefinition files, give the init method a parameter
 * with a key of <code>definitions-parser-validate</code> and a value of
 * <code>&quot;true&quot;</code>.
 * <p/>
 * <p>The ComponentDefinition objects are stored internally in a Map.  The Map is
 * stored as an instance variable rather than a local variable in the <code>read</code>
 * method.  This means that instances of this class are <strong>not</strong>
 * thread-safe and access by multiple threads must be synchronized.</p>
 *
 * @version $Rev$ $Date$
 */
public class DigesterDefinitionsReader implements DefinitionsReader {

    /**
     * Digester validation parameter name.
     */
    public static final String PARSER_VALIDATE_PARAMETER_NAME =
        "definitions-parser-validate";
    /**
     * <code>Digester</code> object used to read ComponentDefinition data
     * from the source.
     */
    protected Digester digester;
    /**
     * Stores ComponentDefinition objects.
     */
    Map<String, ComponentDefinition> definitions;
    /**
     * Should we use a validating XML parser to read the configuration file.
     * Default is <code>false</code>.
     */
    protected boolean validating = false;
    /**
     * The set of public identifiers, and corresponding resource names for
     * the versions of the configuration file DTDs we know about.  There
     * <strong>MUST</strong> be an even number of Strings in this list!
     */
    protected String registrations[] = {
        "-//Apache Software Foundation//DTD Tiles Configuration 2.0//EN",
        "/org/apache/tiles/resources/tiles-config_2_0.dtd"
    };

    /**
     * Indicates whether init method has been called.
     */
    private boolean inited = false;

    /**
     * Creates a new instance of DigesterDefinitionsReader
     */
    public DigesterDefinitionsReader() {
        digester = new Digester();
        digester.setValidating(validating);
        digester.setNamespaceAware(true);
        digester.setUseContextClassLoader(true);

        // Register our local copy of the DTDs that we can find
        for (int i = 0; i < registrations.length; i += 2) {
            URL url = this.getClass().getResource(
                registrations[i + 1]);
            if (url != null) {
                digester.register(registrations[i], url.toString());
            }
        }
    }

    /**
     * Reads <code>{@link ComponentDefinition}</code> objects from a source.
     * <p/>
     * Implementations should publish what type of source object is expected.
     *
     * @param source The <code>InputStream</code> source from which definitions
     *               will be read.
     * @return a Map of <code>ComponentDefinition</code> objects read from
     *         the source.
     * @throws org.apache.tiles.definition.DefinitionsFactoryException
     *          if the source is invalid or
     *          an error occurs when reading definitions.
     */
    public Map read(Object source) throws DefinitionsFactoryException {

        // Get out if we have not been initialized.
        if (!inited) {
            throw new DefinitionsFactoryException(
                "Definitions reader has not been initialized.");
        }

        // This is an instance variable instead of a local variable because
        // we want to be able to call the addDefinition method to populate it.
        // But we reset the Map here, which, of course, has threading implications.
        definitions = new HashMap<String, ComponentDefinition>();

        if (source == null) {
            // Perhaps we should throw an exception here.
            return null;
        }

        InputStream input;
        try {
            input = (InputStream) source;
        } catch (ClassCastException e) {
            throw new DefinitionsFactoryException(
                "Invalid source type.  Requires java.io.InputStream.", e);
        }

        try {
            // set first object in stack
            //digester.clear();
            digester.push(this);
            digester.setEntityResolver(TilesEntityResolver.getInstance());
            // parse
            digester.parse(input);

        } catch (SAXException e) {
            throw new DefinitionsFactoryException(
                "XML error reading definitions.", e);
        } catch (IOException e) {
            throw new DefinitionsFactoryException(
                "I/O Error reading definitions.", e);
        }

        return definitions;
    }

    /**
     * Initializes the <code>DefinitionsReader</code> object.
     * <p/>
     * This method must be called before the {@link #read} method is called.
     *
     * @param params A map of properties used to set up the reader.
     * @throws org.apache.tiles.definition.DefinitionsFactoryException
     *          if required properties are not
     *          passed in or the initialization fails.
     */
    public void init(Map params) throws DefinitionsFactoryException {

        if (params != null) {
            String value = (String) params.get(PARSER_VALIDATE_PARAMETER_NAME);
            if (value != null) {
                digester.setValidating(Boolean.valueOf(value));
            }
        }

        initDigesterForTilesDefinitionsSyntax(digester);

        inited = true;
    }


    /**
     * Init digester for Tiles syntax with first element = tiles-definitions
     *
     * @param digester Digester instance to use.
     */
    private void initDigesterForTilesDefinitionsSyntax(Digester digester) {
        // Common constants
        String PACKAGE_NAME = "org.apache.tiles";
        String DEFINITION_TAG = "tiles-definitions/definition";
        String definitionHandlerClass = PACKAGE_NAME + ".definition.ComponentDefinition";

        String PUT_TAG = DEFINITION_TAG + "/put";
        String putAttributeHandlerClass = PACKAGE_NAME + ".ComponentAttribute";

        //String LIST_TAG = DEFINITION_TAG + "/putList";
        // List tag value
        String LIST_TAG = "putList";
        String DEF_LIST_TAG = DEFINITION_TAG + "/" + LIST_TAG;

        String listHandlerClass = PACKAGE_NAME + ".context.ComponentListAttribute";
        // Tag value for adding an element in a list
        String ADD_LIST_ELE_TAG = "*/" + LIST_TAG + "/add";

        // syntax rules
        digester.addObjectCreate(DEFINITION_TAG, definitionHandlerClass);
        digester.addSetProperties(DEFINITION_TAG);
        digester.addSetNext(DEFINITION_TAG, "addDefinition", definitionHandlerClass);
        // put / putAttribute rules
        // Rules for a same pattern are called in order, but rule.end() are called
        // in reverse order.
        // SetNext and CallMethod use rule.end() method. So, placing SetNext in
        // first position ensure it will be called last (sic).
        digester.addObjectCreate(PUT_TAG, putAttributeHandlerClass);
        digester.addSetProperties(PUT_TAG);
        digester.addSetNext(PUT_TAG, "addAttribute", putAttributeHandlerClass);
        digester.addCallMethod(PUT_TAG, "setBody", 0);
        // TileDefinition level list rules
        // This is rules for lists nested in a definition
        digester.addObjectCreate(DEF_LIST_TAG, listHandlerClass);
        digester.addSetProperties(DEF_LIST_TAG);
        digester.addSetNext(DEF_LIST_TAG, "addAttribute", putAttributeHandlerClass);
        // list elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        digester.addObjectCreate(ADD_LIST_ELE_TAG, putAttributeHandlerClass);
        digester.addSetProperties(ADD_LIST_ELE_TAG);
        digester.addSetNext(ADD_LIST_ELE_TAG, "add", putAttributeHandlerClass);
        digester.addCallMethod(ADD_LIST_ELE_TAG, "setBody", 0);

        // nested list elements rules
        // Create a list handler, and add it to parent list
        String NESTED_LIST = "*/" + LIST_TAG + "/" + LIST_TAG;
        digester.addObjectCreate(NESTED_LIST, listHandlerClass);
        digester.addSetProperties(NESTED_LIST);
        digester.addSetNext(NESTED_LIST, "add", putAttributeHandlerClass);

        // item elements rules
        // We use Attribute class to avoid rewriting a new class.
        // Name part can't be used in listElement attribute.
        //String ADD_WILDCARD = LIST_TAG + "/addItem";
        // non String ADD_WILDCARD = LIST_TAG + "/addx*";
        String ADD_WILDCARD = "*/item";
        String menuItemDefaultClass = "org.apache.tiles.beans.SimpleMenuItem";
        digester.addObjectCreate(ADD_WILDCARD, menuItemDefaultClass, "classtype");
        digester.addSetNext(ADD_WILDCARD, "add", "java.lang.Object");
        digester.addSetProperties(ADD_WILDCARD);

        // bean elements rules
        String BEAN_TAG = "*/bean";
        String beanDefaultClass = "org.apache.tiles.beans.SimpleMenuItem";
        digester.addObjectCreate(BEAN_TAG, beanDefaultClass, "classtype");
        digester.addSetProperties(BEAN_TAG);
        digester.addSetNext(BEAN_TAG, "add", "java.lang.Object");

        // Set properties to surrounding element
        digester.addSetProperty(BEAN_TAG + "/set-property", "property", "value");
    }

    /**
     * Adds a new <code>ComponentDefinition</code> to the internal Map or replaces
     * an existing one.
     *
     * @param definition The ComponentDefinition object to be added.
     */
    public void addDefinition(ComponentDefinition definition) {
        definitions.put(definition.getName(), definition);
    }
}
