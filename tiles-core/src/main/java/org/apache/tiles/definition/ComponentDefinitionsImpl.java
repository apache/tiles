/*
 * $Id$
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.definition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.ComponentDefinition;
import org.apache.tiles.ComponentDefinitions;
import org.apache.tiles.NoSuchDefinitionException;

/**
 *
 * @version $Rev$ $Date$ 
 */
public class ComponentDefinitionsImpl implements ComponentDefinitions {
    /**
     * The base set of ComponentDefinition objects not discriminated by locale.
     */
    private Map baseDefinitions;
    /**
     * The locale-specific set of definitions objects.
     */
    private Map localeSpecificDefinitions;
    
    /** Creates a new instance of ComponentDefinitionsImpl */
    public ComponentDefinitionsImpl() {
        baseDefinitions = new HashMap();
        localeSpecificDefinitions = new HashMap();
    }

    /**
     * Returns a ComponentDefinition object that matches the given name.
     * 
     * @param name The name of the ComponentDefinition to return.
     * @return the ComponentDefinition matching the given name or null if none
     *  is found.
     */
    public ComponentDefinition getDefinition(String name) {
        return (ComponentDefinition) baseDefinitions.get(name);
    }

    /**
     * Adds new ComponentDefinition objects to the internal collection and 
     * resolves inheritance attraibutes.
     * 
     * @param defsMap The new definitions to add.
     */
    public void addDefinitions(Map defsMap) throws NoSuchDefinitionException {
        this.baseDefinitions.putAll(defsMap);
        resolveAttributeDependencies();
        resolveInheritances();
    }

    /**
     * Adds new locale-specific ComponentDefinition objects to the internal 
     * collection and resolves inheritance attraibutes.
     *
     * @param defsMap The new definitions to add.
     * @param locale The locale to add the definitions to.
     */
    public void addDefinitions(Map defsMap, Locale locale) throws NoSuchDefinitionException {
        localeSpecificDefinitions.put(locale, defsMap);
        resolveAttributeDependencies(locale);
        resolveInheritances(locale);
    }
    
    /**
     * Returns a ComponentDefinition object that matches the given name and locale.
     *
     * @param name The name of the ComponentDefinition to return.
     * @param locale The locale to use to resolve the definition.
     * @return the ComponentDefinition matching the given name or null if none
     *  is found.
     */
    public ComponentDefinition getDefinition(String name, Locale locale) {
        ComponentDefinition definition = null;
        Map localeSpecificMap = (Map) localeSpecificDefinitions.get(locale);
        if (localeSpecificMap != null) {
            definition = (ComponentDefinition) localeSpecificMap.get(name);
        }
        
        if (definition == null) {
            definition = getDefinition(name);
        }
        
        return definition;
    }
    
    /**
     * Resolve extended instances.
     */
    public void resolveInheritances() throws NoSuchDefinitionException {
        Iterator i = baseDefinitions.values().iterator();
        while( i.hasNext() ) {
            ComponentDefinition definition = (ComponentDefinition)i.next();
            definition.resolveInheritance( this );
        }  // end loop
    }
    
    /**
     * Resolve locale-specific extended instances.
     */
    public void resolveInheritances(Locale locale) throws NoSuchDefinitionException {
        resolveInheritances();
        
        Map map = (Map) localeSpecificDefinitions.get(locale);
        if (map != null) {
            Iterator i = map.values().iterator();
            while( i.hasNext() ) {
                ComponentDefinition definition = (ComponentDefinition)i.next();
                definition.resolveInheritance( this, locale );
            }  // end loop
        }
    }
    
    /**
     * Clears definitions.
     */
    public void reset() {
        this.baseDefinitions = new HashMap();
        this.localeSpecificDefinitions = new HashMap();
    }

    /**
     * Returns base definitions collection;
     */
    public Map getBaseDefinitions() {
        return this.baseDefinitions;
    }
    
    public void resolveAttributeDependencies() {
        Iterator i = this.baseDefinitions.values().iterator();
        
        // FIXME:  Need to repeat the following for locale-specific defs.
        while (i.hasNext()) {
            ComponentDefinition def = (ComponentDefinition) i.next();
            Map attributes = def.getAttributes();
            Iterator j = attributes.values().iterator();
            while (j.hasNext()) {
                ComponentAttribute attr = (ComponentAttribute) j.next();
                if (attr.getType() != null) {
                    if (attr.getType().equalsIgnoreCase("definition") ||
                            attr.getType().equalsIgnoreCase("instance")) {
                    	ComponentDefinition subDef =
                                getDefinitionByAttribute(attr);
                        attr.setValue(subDef);
                    }
                } else {
                    ComponentDefinition subDef = getDefinitionByAttribute(attr);
                    if (subDef != null) {
                        attr.setValue(subDef);
                    }
                }
            }
        }
    }
    
    public void resolveAttributeDependencies(Locale locale) {
        resolveAttributeDependencies(); // FIXME Is it necessary?
        Map defsMap = (Map) localeSpecificDefinitions.get(locale);
        if (defsMap == null) {
            return;
        }
        
        Iterator i = defsMap.values().iterator();
        
        while (i.hasNext()) {
            ComponentDefinition def = (ComponentDefinition) i.next();
            Map attributes = def.getAttributes();
            Iterator j = attributes.values().iterator();
            while (j.hasNext()) {
                ComponentAttribute attr = (ComponentAttribute) j.next();
                if (attr.getType() != null) {
                    if (attr.getType().equalsIgnoreCase("definition") ||
                            attr.getType().equalsIgnoreCase("instance")) {
                        ComponentDefinition subDef = getDefinitionByAttribute(
                                attr, locale);
                        attr.setValue(subDef);
                    }
                } else {
                    ComponentDefinition subDef = getDefinitionByAttribute(attr,
                            locale);
                    if (subDef != null) {
                        attr.setValue(subDef);
                    }
                }
            }
        }
    }
    
    /**
     * Searches for a definition specified as an attribute.
     * 
     * @param attr The attribute to use.
     * @return The required definition if found, otherwise it returns
     * <code>null</code>.
     */
    private ComponentDefinition getDefinitionByAttribute(
            ComponentAttribute attr) {
        ComponentDefinition retValue = null;
        
        Object attrValue = attr.getValue();
        if (attrValue instanceof ComponentDefinition) {
            retValue = (ComponentDefinition) attrValue;
        } else if (attrValue instanceof String) {
            retValue = this.getDefinition((String) attr
                    .getValue());
        }
        
        return retValue;
    }
    
    /**
     * Searches for a definition specified as an attribute.
     * 
     * @param attr The attribute to use.
     * @param locale The locale to search into.
     * @return The required definition if found, otherwise it returns
     * <code>null</code>.
     */
    private ComponentDefinition getDefinitionByAttribute(
            ComponentAttribute attr, Locale locale) {
        ComponentDefinition retValue = null;
        
        Object attrValue = attr.getValue();
        if (attrValue instanceof ComponentDefinition) {
            retValue = (ComponentDefinition) attrValue;
        } else if (attrValue instanceof String) {
            retValue = this.getDefinition((String) attr
                    .getValue(), locale);
        }
        
        return retValue;
    }
}
