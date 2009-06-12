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

package org.apache.tiles.definition.pattern;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Set;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;

/**
 * Utilities for pattern matching and substitution.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public final class PatternUtil {

    /**
     * Private constructor to avoid instantiation.
     */
    private PatternUtil() {
    }

    /**
     * Creates a definition given its representation with wildcards.
     *
     * @param d The definition to replace.
     * @param name The name of the definition to be created.
     * @param vars The variables to be substituted.
     * @return The definition that can be rendered.
     * @since 2.2.0
     */
    public static Definition replaceDefinition(Definition d, String name,
            Object... vars) {
        Definition nudef = new Definition();

        nudef.setExtends(replace(d.getExtends(), vars));
        nudef.setName(name);
        nudef.setPreparer(replace(d.getPreparer(), vars));
        nudef.setTemplateAttribute(replaceVarsInAttribute(d
                .getTemplateAttribute(), vars));

        Set<String> localAttributeNames = d.getLocalAttributeNames();
        if (localAttributeNames != null && !localAttributeNames.isEmpty()) {
            for (String attributeName : localAttributeNames) {
                Attribute attr = d.getLocalAttribute(attributeName);
                Attribute nuattr = replaceVarsInAttribute(attr, vars);

                nudef.putAttribute(replace(attributeName, vars), nuattr);
            }
        }

        return nudef;
    }

    /**
     * Replaces a string with placeholders using values of a variable map.
     *
     * @param st The string to replace.
     * @param vars The variables.
     * @return The replaced string.
     * @since 2.2.0
     */
    public static String replace(String st, Object... vars) {
        if (st != null && st.indexOf('{') >= 0) {
            MessageFormat format = new MessageFormat(st, Locale.ROOT);
            return format.format(vars, new StringBuffer(), null).toString();
        }
        return st;
    }

    /**
     * Replaces variables into an attribute.
     *
     * @param attr The attribute to be used as a basis, containing placeholders
     * for variables.
     * @param vars The variables to replace.
     * @return A new instance of an attribute, whose properties have been
     * replaced with variables' values.
     * @since 2.2.0
     */
    public static Attribute replaceVarsInAttribute(Attribute attr,
            Object... vars) {
        Attribute nuattr = new Attribute();

        nuattr.setRole(replace(attr.getRole(), vars));
        nuattr.setRenderer(attr.getRenderer());
        nuattr.setExpression(attr.getExpression());

        Object value = attr.getValue();
        if (value instanceof String) {
            value = replace((String) value, vars);
        }
        nuattr.setValue(value);
        return nuattr;
    }
}
