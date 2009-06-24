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

package org.apache.tiles.evaluator.el;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.reflect.ClassUtil;

/**
 * Contains the bean infos about {@link TilesRequestContext} and
 * {@link TilesApplicationContext} classes.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class TilesRequestContextBeanInfo {
    /**
     * The descriptors of {@link TilesRequestContext} and
     * {@link TilesApplicationContext}.
     */
    private List<FeatureDescriptor> descriptors;

    /**
     * Maps names of properties of {@link TilesRequestContext} to their descriptors.
     */
    private Map<String, PropertyDescriptor> requestDescriptors;

    /**
     * Maps names of properties of {@link TilesApplicationContext} to their descriptors.
     */
    private Map<String, PropertyDescriptor> applicationDescriptors;

    /**
     * Constructor.
     *
     * @since 2.2.0
     */
    public TilesRequestContextBeanInfo() {
        descriptors = new ArrayList<FeatureDescriptor>();
        requestDescriptors = new LinkedHashMap<String, PropertyDescriptor>();
        applicationDescriptors = new LinkedHashMap<String, PropertyDescriptor>();
        ClassUtil.collectBeanInfo(TilesRequestContext.class, requestDescriptors);
        ClassUtil.collectBeanInfo(TilesApplicationContext.class, applicationDescriptors);
        descriptors.addAll(requestDescriptors.values());
        descriptors.addAll(applicationDescriptors.values());
    }

    /**
     * Returns the descriptors read by this object.
     *
     * @return The feature descriptors.
     * @since 2.2.0
     */
    public List<FeatureDescriptor> getDescriptors() {
        return descriptors;
    }

    /**
     * Returns the request property descriptors, mapped to their property names.
     *
     * @return A map in the form: name of the property -> the property
     * descriptor.
     * @since 2.2.0
     */
    public Map<String, PropertyDescriptor> getRequestDescriptors() {
        return requestDescriptors;
    }

    /**
     * Returns the application context property descriptors, mapped to their
     * property names.
     *
     * @return A map in the form: name of the property -> the property
     * descriptor.
     * @since 2.2.0
     */
    public Map<String, PropertyDescriptor> getApplicationDescriptors() {
        return applicationDescriptors;
    }

    /**
     * Returns the set of the property names of {@link TilesRequestContext}.
     *
     * @return The property names.
     * @since 2.2.0
     */
    public Set<String> getRequestProperties() {
        return requestDescriptors.keySet();
    }

    /**
     * Returns the set of the property names of {@link TilesApplicationContext}.
     *
     * @return The property names.
     * @since 2.2.0
     */
    public Set<String> getApplicationProperties() {
        return applicationDescriptors.keySet();
    }
}
