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

package org.apache.tiles.autotag.velocity.runtime;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.Renderable;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * Utilities for Velocity usage in Tiles.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public final class VelocityUtil {

    /**
     * A renderable object that does not render anything.
     *
     * @since 2.2.0
     */
    public static final Renderable EMPTY_RENDERABLE;

    static {
        EMPTY_RENDERABLE = new Renderable() {

            @Override
            public String toString() {
                return "";
            }

            public boolean render(InternalContextAdapter context, Writer writer) {
                // Does nothing, really!
                return true;
            }
        };
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private VelocityUtil() {
    }

    /**
     * Null-safe conversion from Boolean to boolean.
     *
     * @param obj The Boolean object.
     * @param defaultValue This value will be returned if <code>obj</code> is null.
     * @return The boolean value of <code>obj</code> or, if null, <code>defaultValue</code>.
     * @since 2.2.0
     */
    public static boolean toSimpleBoolean(Boolean obj, boolean defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    /**
     * Evaluates the body (child node at position 1) and returns it as a string.
     *
     * @param context The Velocity context.
     * @param node The node to use.
     * @return The evaluated body.
     * @throws IOException If something goes wrong.
     * @since 2.2.2
     */
    public static String getBodyAsString(InternalContextAdapter context, Node node)
            throws IOException {
        ASTBlock block = (ASTBlock) node.jjtGetChild(1);
        StringWriter stringWriter = new StringWriter();
        block.render(context, stringWriter);
        stringWriter.close();
        String body = stringWriter.toString();
        if (body != null) {
            body = body.replaceAll("^\\s*|\\s*$", "");
            if (body.length() <= 0) {
                body = null;
            }
        }
        return body;
    }

    /**
     * Evaluates the body writing in the passed writer.
     *
     * @param context The Velocity context.
     * @param writer The writer to write into.
     * @param node The node to use.
     * @throws IOException If something goes wrong.
     * @since 2.2.2
     */
    public static void evaluateBody(InternalContextAdapter context, Writer writer,
            Node node) throws IOException {
        ASTBlock block = (ASTBlock) node.jjtGetChild(1);
        block.render(context, writer);
    }

    /**
     * Extracts the parameters from the directives, by getting the child at
     * position 0 supposing it is a map.
     *
     * @param context The Velocity context.
     * @param node The node to use.
     * @return The extracted parameters.
     * @since 2.2.2
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParameters(InternalContextAdapter context,
            Node node) {
        ASTMap astMap = (ASTMap) node.jjtGetChild(0);
        Map<String, Object> params = (Map<String, Object>) astMap
                .value(context);
        return params;
    }

    /**
     * Returns the "value" parameter if it is not null, otherwise returns
     * "defaultValue".
     *
     * @param value The value to return, if it is not null.
     * @param defaultValue The value to return, if <code>value</code> is null.
     * @return The value, defaulted if necessary.
     * @since 3.0.0
     */
    public static Object getObject(Object value, Object defaultValue) {
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }
}
