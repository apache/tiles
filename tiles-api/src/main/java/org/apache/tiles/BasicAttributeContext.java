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

package org.apache.tiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Basic implementation for <code>AttributeContext</code>.
 *
 * @version $Rev$ $Date$
 */
public class BasicAttributeContext implements AttributeContext, Serializable {

    /**
     * Template attributes.
     */
    protected Map<String, Attribute> attributes = null;

    /**
     * Cascaded template attributes.
     */
    protected Map<String, Attribute> cascadedAttributes = null;

    /**
     * Constructor.
     */
    public BasicAttributeContext() {
        super();
    }

    /**
     * Constructor.
     * Create a context and set specified attributes.
     *
     * @param attributes Attributes to initialize context.
     */
    public BasicAttributeContext(Map<String, Attribute> attributes) {
        if (attributes != null) {
            this.attributes = new HashMap<String, Attribute>(attributes);
        }
    }

    /**
     * Copy constructor.
     *
     * @param context The constructor to copy.
     */
    public BasicAttributeContext(AttributeContext context) {
        if (context instanceof BasicAttributeContext) {
            copyBasicAttributeContext((BasicAttributeContext) context);
        } else {
            this.attributes = new HashMap<String, Attribute>();
            for (String name : context.getLocalAttributeNames()) {
                attributes.put(name, context.getLocalAttribute(name));
            }
            inheritCascadedAttributes(context);
        }
    }

    /**
     * Copy constructor.
     *
     * @param context The constructor to copy.
     */
    public BasicAttributeContext(BasicAttributeContext context) {
        copyBasicAttributeContext(context);
    }

    /** {@inheritDoc} */
    public void inheritCascadedAttributes(AttributeContext context) {
        if (context instanceof BasicAttributeContext) {
            copyCascadedAttributes((BasicAttributeContext) context);
        } else {
            this.cascadedAttributes = new HashMap<String, Attribute>();
            for (String name : context.getCascadedAttributeNames()) {
                cascadedAttributes
                        .put(name, context.getCascadedAttribute(name));
            }
        }
    }

    /** {@inheritDoc} */
    public void inherit(AttributeContext parent) {
        if (parent instanceof BasicAttributeContext) {
            inherit((BasicAttributeContext) parent);
        } else {
            Set<String> names = parent.getCascadedAttributeNames();
            if (names != null && !names.isEmpty()) {
                for (String name : names) {
                    Attribute attribute = parent.getCascadedAttribute(name);
                    putAttribute(name, attribute, true);
                }
            }
            names = parent.getLocalAttributeNames();
            if (names != null && !names.isEmpty()) {
                for (String name : names) {
                    Attribute attribute = parent.getLocalAttribute(name);
                    putAttribute(name, attribute, false);
                }
            }
        }
    }

    /**
     * Inherits the attribute context, inheriting, i.e. copying if not present,
     * the attributes.
     *
     * @param parent The attribute context to inherit.
     */
    public void inherit(BasicAttributeContext parent) {
        cascadedAttributes = addMissingAttributes(
                ((BasicAttributeContext) parent).cascadedAttributes,
                cascadedAttributes);
        attributes = addMissingAttributes(
                ((BasicAttributeContext) parent).attributes, attributes);
    }

    /**
     * Add all attributes to this context.
     * Copies all of the mappings from the specified attribute map to this context.
     * New attribute mappings will replace any mappings that this context had for any of the keys
     * currently in the specified attribute map.
     *
     * @param newAttributes Attributes to add.
     */
    public void addAll(Map<String, Attribute> newAttributes) {
        if (newAttributes == null) {
            return;
        }

        if (attributes == null) {
            attributes = new HashMap<String, Attribute>(newAttributes);
            return;
        }

        attributes.putAll(newAttributes);
    }

    /**
     * Add all missing attributes to this context.
     * Copies all of the mappings from the specified attributes map to this context.
     * New attribute mappings will be added only if they don't already exist in
     * this context.
     *
     * @param defaultAttributes Attributes to add.
     */
    public void addMissing(Map<String, Attribute> defaultAttributes) {
        if (defaultAttributes == null) {
            return;
        }

        if (attributes == null) {
            attributes = new HashMap<String, Attribute>(defaultAttributes);
            if (cascadedAttributes == null || cascadedAttributes.isEmpty()) {
                return;
            }
        }

        Set<Map.Entry<String, Attribute>> entries = defaultAttributes.entrySet();
        for (Map.Entry<String, Attribute> entry : entries) {
            String key = entry.getKey();
            if (!attributes.containsKey(key)
                    && (cascadedAttributes == null || cascadedAttributes
                            .containsKey(key))) {
                attributes.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /** {@inheritDoc} */
    public Attribute getAttribute(String name) {
        Attribute retValue = null;
        if (attributes != null) {
            retValue = attributes.get(name);
        }

        if (retValue == null && cascadedAttributes != null) {
            retValue = cascadedAttributes.get(name);
        }

        return retValue;
    }

    /** {@inheritDoc} */
    public Attribute getLocalAttribute(String name) {
        if (attributes == null) {
            return null;
        }

        return attributes.get(name);
    }

    /** {@inheritDoc} */
    public Attribute getCascadedAttribute(String name) {
        if (cascadedAttributes == null) {
            return null;
        }

        return cascadedAttributes.get(name);
    }

    /** {@inheritDoc} */
    public Iterator<String> getAttributeNames() {
        Set<String> attributeSet = null;

        if (attributes != null && !attributes.isEmpty()) {
            attributeSet = new HashSet<String>(attributes
                    .keySet());
            if (cascadedAttributes != null && !cascadedAttributes.isEmpty()) {
                attributeSet.addAll(cascadedAttributes.keySet());
            }
        } else if (cascadedAttributes != null && !cascadedAttributes.isEmpty()) {
            attributeSet = new HashSet<String>(cascadedAttributes.keySet());
        }

        if (attributeSet != null) {
            return attributeSet.iterator();
        } else {
            return new ArrayList<String>().iterator();
        }
    }

    /** {@inheritDoc} */
    public Set<String> getLocalAttributeNames() {
        if (attributes != null && !attributes.isEmpty()) {
            return attributes.keySet();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public Set<String> getCascadedAttributeNames() {
        if (cascadedAttributes != null && !cascadedAttributes.isEmpty()) {
            return cascadedAttributes.keySet();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public void putAttribute(String name, Attribute value) {
        if (attributes == null) {
            attributes = new HashMap<String, Attribute>();
        }

        attributes.put(name, value);
    }

    /** {@inheritDoc} */
    public void putAttribute(String name, Attribute value, boolean cascade) {
        Map<String, Attribute> mapToUse;
        if (cascade) {
            if (cascadedAttributes == null) {
                cascadedAttributes = new HashMap<String, Attribute>();
            }
            mapToUse = cascadedAttributes;
        } else {
            if (attributes == null) {
                attributes = new HashMap<String, Attribute>();
            }
            mapToUse = attributes;
        }
        mapToUse.put(name, value);
    }

    /** {@inheritDoc} */
    public void clear() {
        attributes.clear();
        cascadedAttributes.clear();
    }

    /**
     * Copies a BasicAttributeContext in an easier way.
     *
     * @param context The context to copy.
     */
    private void copyBasicAttributeContext(BasicAttributeContext context) {
        if (context.attributes != null && !context.attributes.isEmpty()) {
            attributes = new HashMap<String, Attribute>(context.attributes);
        }
        copyCascadedAttributes(context);
    }

    /**
     * Copies the cascaded attributes to the current context.
     *
     * @param context The context to copy from.
     */
    private void copyCascadedAttributes(BasicAttributeContext context) {
        if (context.cascadedAttributes != null
                && !context.cascadedAttributes.isEmpty()) {
            cascadedAttributes = new HashMap<String, Attribute>(
                    context.cascadedAttributes);
        }
    }

    /**
     * Adds missing attributes to the destination map.
     *
     * @param source The source attribute map.
     * @param destination The destination attribute map.
     * @return The destination attribute map if not null, a new one otherwise.
     */
    private Map<String, Attribute> addMissingAttributes(Map<String, Attribute> source,
            Map<String, Attribute> destination) {
        if (source != null && !source.isEmpty()) {
            if (destination == null) {
                destination = new HashMap<String, Attribute>();
            }
            for (Map.Entry<String, Attribute> entry : source.entrySet()) {
                String key = entry.getKey();
                if (!destination.containsKey(key)) {
                    destination.put(key, entry.getValue());
                }
            }
        }

        return destination;
    }
}
