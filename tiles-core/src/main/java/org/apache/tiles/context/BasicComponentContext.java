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
 *
 */

package org.apache.tiles.context;

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.ComponentConstants;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.ComponentAttribute;

import java.io.Serializable;
import java.util.*;

/**
 * Component context.
 *
 * @version $Rev$ $Date$
 */
public class BasicComponentContext implements ComponentContext, Serializable {

    /**
     * Component attributes.
     */
    private Map<String, ComponentAttribute> attributes = null;

    /**
     * Constructor.
     */
    public BasicComponentContext() {
        super();
    }

    /**
     * Constructor.
     * Create a context and set specified attributes.
     *
     * @param attributes Attributes to initialize context.
     */
    public BasicComponentContext(Map<String, ComponentAttribute> attributes) {
        if (attributes != null) {
            this.attributes = new HashMap<String, ComponentAttribute>(attributes);
        }
    }


    public BasicComponentContext(ComponentContext context) {
        this.attributes = new HashMap<String, ComponentAttribute>();
        Iterator<String> names = context.getAttributeNames();
        while(names.hasNext()) {
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
    public void addAll(Map<String, ComponentAttribute> newAttributes) {
        if (attributes == null) {
            attributes = new HashMap<String, ComponentAttribute>(newAttributes);
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
    public void addMissing(Map<String, ComponentAttribute> defaultAttributes) {
        if (defaultAttributes == null) {
            return;
        }

        if (attributes == null) {
            attributes = new HashMap<String, ComponentAttribute>(defaultAttributes);
            return;
        }

        Set<Map.Entry<String, ComponentAttribute>> entries = defaultAttributes.entrySet();
        for (Map.Entry<String, ComponentAttribute> entry : entries) {
            if (!attributes.containsKey(entry.getKey())) {
                attributes.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Get an attribute from context.
     *
     * @param name Name of the attribute.
     * @return <{ComponentAttribute}>
     */
    public ComponentAttribute getAttribute(String name) {
        if (attributes == null) {
            return null;
        }

        return attributes.get(name);
    }

    /**
     * Get names of all attributes.
     *
     * @return <{ComponentAttribute}>
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
    public void putAttribute(String name, ComponentAttribute value) {
        if (attributes == null) {
            attributes = new HashMap<String, ComponentAttribute>();
        }

        attributes.put(name, value);
    }

    /**
     * Get component context from request.
     *
     * @param tilesContext current Tiles application context.
     * @return BasicComponentContext or null if context is not found or an
     *         jspException is present in the request.
     */
    static public ComponentContext getContext(TilesRequestContext tilesContext) {
        Stack<ComponentContext> contextStack = getContextStack(tilesContext);
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
     */
    @SuppressWarnings("unchecked")
    static public Stack<ComponentContext> getContextStack(TilesRequestContext tilesContext) {
        Stack<ComponentContext> contextStack =
            (Stack<ComponentContext>) tilesContext.getRequestScope().get(
                ComponentConstants.COMPONENT_CONTEXT_STACK);
        if (contextStack == null) {
            contextStack = new Stack<ComponentContext>();
            tilesContext.getRequestScope().put(ComponentConstants.COMPONENT_CONTEXT_STACK,
                    contextStack);
        }
        
        return contextStack;
    }
    
    /**
     * Pushes a context object in the stack.
     *
     * @param context The context to push.
     * @param tilesContext The Tiles context object to use.
     */
    static public void pushContext(ComponentContext context,
            TilesRequestContext tilesContext) {
        Stack<ComponentContext> contextStack = getContextStack(tilesContext);
        contextStack.push(context);
    }
    
    /**
     * Pops a context object out of the stack
     *
     * @param tilesContext The Tiles context object to use.
     * @return The popped context object.
     */
    static public ComponentContext popContext(TilesRequestContext tilesContext) {
        Stack<ComponentContext> contextStack = getContextStack(tilesContext);
        return contextStack.pop();
    }

    public void clear() {
        attributes.clear();
    }
}
