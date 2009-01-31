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

import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p><strong>Put an attribute in enclosing attribute container tag.</strong></p>
 * <p>Enclosing attribute container tag can be :
 * <ul>
 * <li>&lt;initContainer&gt;</li>
 * <li>&lt;definition&gt;</li>
 * <li>&lt;insertAttribute&gt;</li>
 * <li>&lt;insertDefinition&gt;</li>
 * <li>&lt;putList&gt;</li>
 * </ul>
 * (or any other tag which implements the <code>{@link PutAttributeTagParent}</code> interface.
 * Exception is thrown if no appropriate tag can be found.</p>
 * <p>Put tag can have following atributes :
 * <ul>
 * <li>name : Name of the attribute</li>
 * <li>value : value to put as attribute</li>
 * <li>type : value type. Only valid if value is a String and is set by
 * value="something" or by a bean.
 * Possible type are : string (value is used as direct string),
 * template (value is used as a page url to insert),
 * definition (value is used as a definition name to insert)</li>
 * <li>direct : Specify if value is to be used as a direct string or as a
 * page url to insert. This is another way to specify the type. It only apply
 * if value is set as a string, and type is not present.</li>
 * <li>beanName : Name of a bean used for setting value. Only valid if value is not set.
 * If property is specified, value come from bean's property. Otherwise, bean
 * itself is used for value.</li>
 * <li>beanProperty : Name of the property used for retrieving value.</li>
 * <li>beanScope : Scope containing bean. </li>
 * <li>role : Role to check when 'insert' will be called. If enclosing tag is
 * &lt;insert&gt;, role is checked immediately. If enclosing tag is
 * &lt;definition&gt;, role will be checked when this definition will be
 * inserted.</li>
 * </ul></p>
 * <p>Value can also come from tag body. Tag body is taken into account only if
 * value is not set by one of the tag attributes. In this case Attribute type is
 * "string", unless tag body define another type.</p>
 *
 * @version $Rev$ $Date$
 */
public class PutAttributeTag extends AddAttributeTag {

    /**
     * Name of attribute to put in attribute context.
     */
    protected String name = null;

    /**
     * If <code>true</code>, the attribute will be cascaded to all nested
     * definitions.
     */
    private boolean cascade = false;

    /**
     * Returns  the name of the attribute.
     *
     * @return The name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the attribute.
     *
     * @param name The name of the attribute.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if the attribute should be cascaded to nested definitions.
     *
     * @return <code>true</code> if the attribute will be cascaded.
     * @since 2.1.0
     */
    public boolean isCascade() {
        return cascade;
    }

    /**
     * Sets the property that tells if the attribute should be cascaded to
     * nested definitions.
     *
     * @param cascade <code>true</code> if the attribute will be cascaded.
     * @since 2.1.0
     */
    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        name = null;
        cascade = false;
    }

    /** {@inheritDoc} */
    @Override
    protected void execute() throws TilesJspException {
        PutAttributeTagParent parent = (PutAttributeTagParent)
            TagSupport.findAncestorWithClass(this, PutAttributeTagParent.class);


        if (parent == null) {
            throw new TilesJspException(
                    "Error: no enclosing tag accepts 'putAttribute' tag.");
        }

        parent.processNestedTag(this);
    }
}
