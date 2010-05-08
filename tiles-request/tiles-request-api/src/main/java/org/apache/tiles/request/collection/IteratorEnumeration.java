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

package org.apache.tiles.request.collection;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Copied and modified from Apache Commons Collections 3.2.1.<br>
 *
 * Adapter to make an {@link Iterator Iterator} instance appear to be an
 * {@link Enumeration Enumeration} instance.
 *
 * @param <E> The type of the enumerated elements.
 * @since Commons Collections 1.0
 * @version $Revision$ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr
 * 2008) $
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public class IteratorEnumeration<E> implements Enumeration<E> {

    /** The iterator being decorated. */
    private Iterator<E> iterator;

    /**
     * Constructs a new <code>IteratorEnumeration</code> that will use the given
     * iterator.
     *
     * @param iterator the iterator to use
     */
    public IteratorEnumeration(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    // Iterator interface
    // -------------------------------------------------------------------------

    /**
     * Returns true if the underlying iterator has more elements.
     *
     * @return true if the underlying iterator has more elements
     */
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    /**
     * Returns the next element from the underlying iterator.
     *
     * @return the next element from the underlying iterator.
     * @throws java.util.NoSuchElementException if the underlying iterator has
     * no more elements
     */
    public E nextElement() {
        return iterator.next();
    }
    
    public Iterator<E> getIterator() {
        return iterator;
    }
}
