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

package org.apache.tiles;


import java.util.ArrayList;
import java.util.List;

/**
 * An attribute as a <code>List</code>.
 * This attribute associates a name with a list. The list can be found by the
 * property name.
 * Elements in list are retrieved using List methods.
 * This class is used to read configuration files.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class ListAttribute extends Attribute {

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'.
     */
    private boolean inherit = false;

    /**
     * Constructor.
     *
     * @since 2.1.0
     */
    public ListAttribute() {
        setValue(new ArrayList<Object>());
    }

    /**
     * Constructor.
     *
     * @param name  Name.
     * @param value List.
     * @since 2.1.0
     */
    public ListAttribute(String name, List<Object> value) {
        super(name, value);
    }

    /**
     * Add an element in list.
     * We use a property to avoid rewriting a new class.
     *
     * @param element XmlAttribute to add.
     * @since 2.1.0
     */
    @SuppressWarnings("unchecked")
    public void add(Attribute element) {
        ((List<Object>) value).add(element);
    }

    /**
     * Add an element in list.
     *
     * @param value Object to add.
     * @since 2.1.0
     */
    @SuppressWarnings("unchecked")
    public void add(Object value) {
        //list.add( value );
        // To correct a bug in digester, we need to check the object type
        // Digester doesn't call correct method according to object type ;-(
        if (value instanceof Attribute) {
            add((Attribute) value);
        } else {
            ((List<Object>) this.value).add(value);
        }
    }

    /**
     * Add an element in list.
     *
     * @param value Object to add.
     * @since 2.1.0
     */
    @SuppressWarnings("unchecked")
    public void addObject(Object value) {
        ((List<Object>) this.value).add(value);
    }

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'
     *
     * @param inherit The "inherit" value.
     * @since 2.1.0
     */
    public void setInherit(boolean inherit) {
        this.inherit = inherit;
    }

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'
     *
     * @return inherit The "inherit" value.
     * @since 2.1.0
     */
    public boolean isInherit() {
        return inherit;
    }

    /**
     * Inherits elements present in a "parent" list attribute. The elements will
     * be put before the ones already present.
     *
     * @param parent The parent list attribute.
     * @since 2.1.0
     */
    @SuppressWarnings("unchecked")
    public void inherit(ListAttribute parent) {
        List<Object> tempList = new ArrayList<Object>();
        tempList.addAll((List<Object>) parent.value);
        tempList.addAll((List<Object>) value);
        setValue(tempList);
    }
}
