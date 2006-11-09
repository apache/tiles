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
package org.apache.tiles.taglib.definition;

import javax.servlet.jsp.tagext.TagSupport;
import java.io.Serializable;

/**
 * Common base class for tags dealing with Tiles definitions.
 * This class defines properties used in TileDefinition Tags.
 * It also extends TagSupport.
 */
public class DefinitionTagSupport extends TagSupport implements Serializable {
    /**
     * Associated ViewPreparer name (classname or url)
     */
    protected String preparer;
    /**
     * Role associated to definition.
     */
    protected String role;

    /**
     * JSP page that implements the definition.
     */
    protected String template;

    /**
     * Release class properties.
     */
    public void release() {
        super.release();
        preparer = null;
        role = null;
    }

    /**
     * Get preparerInstance type.
     * Type can be 'classname', 'url'.
     *
     * @return ViewPreparer type.
     */

    /**
     * Get preparerInstance name.
     * Name denotes a fully qualified classname, or an url.
     * Exact type can be specified with {@link #setPreparer}.
     *
     * @return ViewPreparer name.
     */
    public String getPreparer() {
        return preparer;
    }


    /**
     * Set associated preparerInstance name.
     * Name denote a fully qualified classname, or an url.
     * Exact type can be specified with setPreparerType.
     *
     * @param preparer ViewPreparer classname or url
     */
    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    /**
     * Get associated role.
     *
     * @return Associated role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Set associated role.
     *
     * @param role Associated role.
     */
    public void setRole(String role) {
        this.role = role;
    }


    /**
     * Get the template.
     *
     * @return Template.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the template.
     *
     * @param template Template.
     */
    public void setTemplate(String template) {
        this.template = template;
    }
}
