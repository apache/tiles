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

package org.apache.tiles.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;

/**
 * Basic implementation for <code>AttributeContext</code>.
 *
 * @version $Rev$ $Date$
 */
public class BasicAttributeContext implements AttributeContext, Serializable {

    /**
     * Name used to store attribute context stack.
     */
    private static final String ATTRIBUTE_CONTEXT_STACK =
        "org.apache.tiles.AttributeContext.STACK";

    /**
     * Template attributes.
     */
    private Map<String, Attribute> attributes = null;

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
        this.attributes = new HashMap<String, Attribute>();
        Iterator<String> names = context.getAttributeNames();
        while (names.hasNext()) {
            String name = names.next();
            attributes.put(name, context.getAttribute(name));
        }
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
            return;
        }

        Set<Map.Entry<String, Attribute>> entries = defaultAttributes.entrySet();
        for (Map.Entry<String, Attribute> entry : entries) {
            if (!attributes.containsKey(entry.getKey())) {
                attributes.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Get an attribute from context.
     *
     * @param name Name of the attribute.
     * @return <{Attribute}>
     */
    public Attribute getAttribute(String name) {
        if (attributes == null) {
            return null;
        }

        return attributes.get(name);
    }

    /**
     * Get names of all attributes.
     *
     * @return <{Attribute}>
     */
    public Iterator<String> getAttributeNames() {
        if (attributes == null) {
            return new ArrayList<String>().iterator();
        }

        return attributes.keySet().iterator();
    }

    /**
     * Put a new attribute to context.
     *
     * @param name  Name of the attribute.
     * @param value Value of the attribute.
     */
    public void putAttribute(String name, Attribute value) {
        if (attributes == null) {
            attributes = new HashMap<String, Attribute>();
        }

        attributes.put(name, value);
    }

    /**
     * Get attribute context from request.
     *
     * @param tilesContext current Tiles application context.
     * @return BasicAttributeContext or null if context is not found or an
     *         jspException is present in the request.
     * @deprecated Use {@link TilesContainer#getAttributeContext(Object...)}.
     */
    @Deprecated
    public static AttributeContext getContext(TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack = getContextStack(tilesContext);
        if (!contextStack.isEmpty()) {
            return contextStack.peek();
        } else {
            return null;
        }
    }

    /**
     * Returns the context stack.
     *
     * @param tilesContext The Tiles context object to use.
     * @return The needed stack of contexts.
     * @deprecated Use {@link TilesContainer#getAttributeContext(Object...)},
     * {@link TilesContainer#startContext(Object...)} or
     * {@link TilesContainer#endContext(Object...)}.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static Stack<AttributeContext> getContextStack(TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack =
            (Stack<AttributeContext>) tilesContext.getRequestScope().get(
                BasicAttributeContext.ATTRIBUTE_CONTEXT_STACK);
        if (contextStack == null) {
            contextStack = new Stack<AttributeContext>();
            tilesContext.getRequestScope().put(BasicAttributeContext.ATTRIBUTE_CONTEXT_STACK,
                    contextStack);
        }

        return contextStack;
    }

    /**
     * Pushes a context object in the stack.
     *
     * @param context The context to push.
     * @param tilesContext The Tiles context object to use.
     * @deprecated Use {@link TilesContainer#startContext(Object...)}.
     */
    @Deprecated
    public static void pushContext(AttributeContext context,
            TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack = getContextStack(tilesContext);
        contextStack.push(context);
    }

    /**
     * Pops a context object out of the stack.
     *
     * @param tilesContext The Tiles context object to use.
     * @return The popped context object.
     * @deprecated Use {@link TilesContainer#endContext(Object...)}.
     */
    public static AttributeContext popContext(TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack = getContextStack(tilesContext);
        return contextStack.pop();
    }

    /** {@inheritDoc} */
    public void clear() {
        attributes.clear();
    }
}
