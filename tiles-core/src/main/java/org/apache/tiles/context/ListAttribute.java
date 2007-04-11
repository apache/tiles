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

import org.apache.tiles.Attribute;

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
 */
public class ListAttribute extends Attribute {
    /**
     * List.
     * We declare a List to avoid cast.
     * Parent "value" property points to the same list.
     */
    private List<Object> list;

    /**
     * Constructor.
     */
    public ListAttribute() {
        list = new ArrayList<Object>();
        setValue(list);
    }

    /**
     * Constructor.
     *
     * @param name  Name.
     * @param value List.
     */
    public ListAttribute(String name, List<Object> value) {
        super(name, value);
        list = value;
    }

    /**
     * Add an element in list.
     * We use a property to avoid rewriting a new class.
     *
     * @param element XmlAttribute to add.
     */
    public void add(Attribute element) {
        list.add(element);
    }

    /**
     * Add an element in list.
     *
     * @param value Object to add.
     */
    public void add(Object value) {
        //list.add( value );
        // To correct a bug in digester, we need to check the object type
        // Digester doesn't call correct method according to object type ;-(
        if (value instanceof Attribute) {
            add((Attribute) value);
        } else {
            list.add(value);
        }
    }

    /**
     * Add an element in list.
     *
     * @param value Object to add.
     */
    public void addObject(Object value) {
    list.add(value);
  }

}
