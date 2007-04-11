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

package org.apache.tiles.util;

import java.util.Enumeration;
import java.util.Vector;

/**
 * It represents an vector-based enumeration when, when it has finished
 * enumerating items, it starts from the beginning.
 *
 * @param <E> The type of the element of this enumeration.
 * @version $Rev$ $Date$
 */
public class RollingVectorEnumeration<E> implements Enumeration<E> {

    /**
     * The vector.
     */
    private Vector<E> vector;

    /**
     * The elements.
     */
    private Enumeration<E> elements;

    /**
     * Constructor.
     *
     * @param vector The vector.
     */
    public RollingVectorEnumeration(Vector<E> vector) {
        this.vector = vector;
    }

    /** {@inheritDoc} */
    public boolean hasMoreElements() {
        if (elements == null) {
            elements = vector.elements();
            return false;
        }
        return elements.hasMoreElements();
    }

    /** {@inheritDoc} */
    public E nextElement() {
        E retValue = elements.nextElement();

        if (!elements.hasMoreElements()) {
            elements = null;
        }

        return retValue;
    }

}
