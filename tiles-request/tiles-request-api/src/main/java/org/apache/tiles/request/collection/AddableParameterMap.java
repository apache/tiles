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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.collection.extractor.EnumeratedValuesExtractor;

/**
 * <p>Private implementation of <code>Map</code> for servlet request
 * name-value.</p>
 *
 * @version $Rev$ $Date$
 */

public class AddableParameterMap extends ParameterMap {

    private EnumeratedValuesExtractor request;

    /**
     * Constructor.
     *
     * @param request The request object to use.
     * @param response The response object to use.
     * @since 2.2.0
     */
    public AddableParameterMap(EnumeratedValuesExtractor request) {
        super(request);
        this.request = request;
    }

    /** {@inheritDoc} */
    public Set<Map.Entry<String, String>> entrySet() {
        return new HeaderEntrySet();
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        EnumeratedValuesExtractor otherRequest = ((AddableParameterMap) o).request;
        boolean retValue = true;
        synchronized (request) {
            for (Enumeration<String> attribs = request.getKeys(); attribs
                    .hasMoreElements()
                    && retValue;) {
                String parameterName = attribs.nextElement();
                retValue = request.getValue(parameterName).equals(
                        otherRequest.getValue(parameterName));
            }
        }

        return retValue;
    }

    /** {@inheritDoc} */
    public String put(String key, String value) {
        request.setValue(key, value);
        return value;
    }


    /** {@inheritDoc} */
    public void putAll(Map<? extends String, ? extends String> map) {
        for (Map.Entry<? extends String, ? extends String> entry : map
                .entrySet()) {
            request.setValue(entry.getKey(), entry.getValue());
        }
    }


    private class HeaderEntrySet extends ParameterMap.HeaderEntrySet {

        @Override
        public boolean add(java.util.Map.Entry<String, String> e) {
            request.setValue(e.getKey(), e.getValue());
            return true;
        }

        @Override
        public boolean addAll(
                Collection<? extends java.util.Map.Entry<String, String>> c) {
            for (Map.Entry<String, String> entry : c) {
                request.setValue(entry.getKey(), entry.getValue());
            }
            return true;
        }
    }
}
