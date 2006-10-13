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
package org.apache.tiles.taglib;

import java.io.Serializable;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Common base class for tags dealing with Tiles definitions.
 * This class defines properties used in Definition Tags.
 * It also extends TagSupport.
 */
public class DefinitionTagSupport extends TagSupport implements Serializable {
    /**
     * Associated ViewPreparer type
     */
    protected String preparerType;
    /**
     * Associated ViewPreparer name (classname or url)
     */
    protected String preparerName;
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
        preparerType = null;
        preparerName = null;
        role = null;
    }

    /**
     * Get preparer type.
     * Type can be 'classname', 'url'.
     *
     * @return ViewPreparer type.
     */
    public String getPreparerType() {
        return preparerType;
    }

    /**
     * Get preparer name.
     * Name denotes a fully qualified classname, or an url.
     * Exact type can be specified with {@link #setPreparerType}.
     *
     * @return ViewPreparer name.
     */
    public String getPreparerName() {
        return preparerName;
    }

    /**
     * Set associated preparer type.
     * Type denotes a fully qualified classname.
     *
     * @param preparerType Type of associated preparer.
     */
    public void setPreparerType(String preparerType) {
        this.preparerType = preparerType;
    }

    /**
     * Set associated preparer name.
     * Name denotes a fully qualified classname, or an url.
     * Exact type can be specified with {@link #setPreparerType}.
     *
     * @param preparer ViewPreparer classname or url.
     */
    public void setPreparer(String preparer) {
        setPreparerName(preparer);
    }

    /**
     * Set associated preparer name.
     * Name denote a fully qualified classname, or an url.
     * Exact type can be specified with setPreparerType.
     *
     * @param preparer ViewPreparer classname or url
     */
    public void setPreparerName(String preparer) {
        this.preparerName = preparer;
    }

    /**
     * Set associated preparer name as an url, and preparer
     * type as "url".
     * Name must be an url (not checked).
     * Convenience method.
     *
     * @param preparer ViewPreparer url
     */
    public void setPreparerUrl(String preparer) {
        setPreparerName(preparer);
        setPreparerType("url");
    }

    /**
     * Set associated preparer name as a classtype and preparer
     * type as "classname".
     * Name denotes a fully qualified classname.
     * Convenience method.
     *
     * @param preparer ViewPreparer classname.
     */
    public void setPreparerClass(String preparer) {
        setPreparerName(preparer);
        setPreparerType("classname");
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
