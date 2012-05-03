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
package org.apache.tiles.request.portlet.extractor;

import java.util.Enumeration;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.tiles.request.attribute.EnumeratedValuesExtractor;

/**
 * Extracts and puts headers in portlet requests and responses.
 *
 * @version $Rev$ $Date$
 */
public class HeaderExtractor implements EnumeratedValuesExtractor {

    /**
     * The request.
     */
    private PortletRequest request;

    /**
     * The response.
     */
    private PortletResponse response;

    /**
     * Constructor.
     *
     * @param request The request.
     * @param response The response.
     */
    public HeaderExtractor(PortletRequest request,
            PortletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Enumeration<String> getKeys() {
        return request.getPropertyNames();
   }

    @Override
    public String getValue(String key) {
        return request.getProperty(key);
    }

    @Override
    public Enumeration<String> getValues(String key) {
        return request.getProperties(key);
    }

    @Override
    public void setValue(String key, String value) {
        response.setProperty(key, value);
    }
}
