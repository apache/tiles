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

import java.util.Map;
import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;

/**
 * Deprecated implementation for <code>AttributeContext</code>, maintained
 * for compatibility reasons.
 *
 * @version $Rev$ $Date$
 * @deprecated Use {@link org.apache.tiles.BasicAttributeContext}.
 */
@Deprecated
public class BasicAttributeContext extends
        org.apache.tiles.BasicAttributeContext {

    /**
     * Name used to store attribute context stack.
     */
    private static final String ATTRIBUTE_CONTEXT_STACK =
        "org.apache.tiles.AttributeContext.STACK";

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
        super(attributes);
    }

    /**
     * Copy constructor.
     *
     * @param context The constructor to copy.
     */
    public BasicAttributeContext(AttributeContext context) {
        super(context);
    }

    /**
     * Copy constructor.
     *
     * @param context The constructor to copy.
     */
    public BasicAttributeContext(BasicAttributeContext context) {
        super(context);
    }

    /**
     * Get attribute context from request.
     *
     * @param tilesContext current Tiles application context.
     * @return BasicAttributeContext or null if context is not found.
     * @deprecated Use {@link org.apache.tiles.TilesContainer#getAttributeContext(Object...)}.
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
     * @deprecated Use {@link org.apache.tiles.TilesContainer#getAttributeContext(Object...)},
     * {@link org.apache.tiles.TilesContainer#startContext(Object...)} or
     * {@link org.apache.tiles.TilesContainer#endContext(Object...)}.
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
     * @deprecated Use {@link org.apache.tiles.TilesContainer#startContext(Object...)}.
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
     * @deprecated Use {@link org.apache.tiles.TilesContainer#endContext(Object...)}.
     */
    @Deprecated
    public static AttributeContext popContext(TilesRequestContext tilesContext) {
        Stack<AttributeContext> contextStack = getContextStack(tilesContext);
        return contextStack.pop();
    }
}
