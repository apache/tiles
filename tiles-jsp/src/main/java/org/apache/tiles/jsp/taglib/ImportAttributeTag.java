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
package org.apache.tiles.jsp.taglib;

import org.apache.tiles.Attribute;

import javax.servlet.jsp.JspException;
import java.util.Iterator;


/**
 * Import attribute(s) into the specified scope.
 * If not explicitly named, all attributes are imported.
 * If the scope is not specified, page scope is assumed.
 *
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class ImportAttributeTag extends AttributeTagSupport {

    /**
     * The destination attribute name.
     */
    private String toName;

    /**
     * <p>
     * Returns the name of the destination attribute. If not specified, the name
     * will be the same as specified in <code>name</code> attribute
     * </p>
     *
     * @return The destination attribute name.
     */
    public String getToName() {
        return toName;
    }

    /**
     * <p>
     * Sets the name of the destination attribute. If not specified, the name
     * will be the same as specified in <code>name</code> attribute
     * </p>
     *
     * @param toName The destination attribute name.
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * Expose the requested property from attribute context.
     *
     * @throws JspException On errors processing tag.
     */
    public void execute() throws JspException {
        if (attribute != null) {
            pageContext.setAttribute(toName != null ? toName : name,
                    attribute.getValue(), scope);
        } else {
            Iterator<String> names = attributeContext.getAttributeNames();
            while (names.hasNext()) {
                String name = names.next();

                if (name == null && !ignore) {
                    throw new JspException("Error importing attributes. "
                            + "Attribute with null key found.");
                } else if (name == null) {
                    continue;
                }

                Attribute attr = attributeContext.getAttribute(name);

                if ((attr == null || attr.getValue() == null) && !ignore) {
                    throw new JspException("Error importing attributes. "
                            + "Attribute '" + name + "' has a null value ");
                } else if (attr == null || attr.getValue() == null) {
                    continue;
                }

                pageContext.setAttribute(name, attr.getValue(), scope);
            }
        }
    }
}
