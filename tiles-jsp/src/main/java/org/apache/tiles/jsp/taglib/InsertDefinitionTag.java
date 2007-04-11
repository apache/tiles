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

import org.apache.tiles.TilesException;

import javax.servlet.jsp.JspException;

/**
 * This is the tag handler for &lt;tiles:insertDefinition&gt;, which includes a
 * name, eventually overriding or filling attributes of its template.
 *
 * @version $Rev$ $Date$
 */
public class InsertDefinitionTag extends RenderTagSupport implements PutAttributeTagParent {

    /**
     * The definition name.
     */
    private String name;


    /**
     * Returns the name of the definition to insert.
     *
     * @return The name of the definition.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the definition to insert.
     *
     * @param name The name of the definition.
     */
    public void setName(String name) {
        this.name = name;
    }


    /** {@inheritDoc} */
    protected void render() throws JspException, TilesException {
        container.render(name, pageContext);
    }
}
