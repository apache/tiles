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
package org.apache.tiles.el;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.el.ELContext;

import junit.framework.TestCase;

import org.apache.tiles.reflect.ClassUtil;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;

/**
 * Tests {@link TilesContextELResolver}.
 *
 * @version $Rev$ $Date$
 */
public class TilesContextELResolverTest extends TestCase {

    /**
     * The resolver to test.
     */
    private TilesContextELResolver resolver;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = new TilesContextELResolver();
    }

    /**
     * Test method for
     * {@link TilesContextELResolver#getCommonPropertyType(javax.el.ELContext, java.lang.Object)}.
     */
    public void testGetCommonPropertyTypeELContextObject() {
        Class<?> clazz = resolver.getCommonPropertyType(null, null);
        assertEquals("The class is not correct", String.class, clazz);
        clazz = resolver.getCommonPropertyType(null, "Base object");
        assertNull("The class for non root objects must be null", clazz);
    }

    /**
     * Test method for
     * {@link TilesContextELResolver#getFeatureDescriptors(javax.el.ELContext, java.lang.Object)}.
     */
    public void testGetFeatureDescriptorsELContextObject() {
        Map<String, PropertyDescriptor> expected = new LinkedHashMap<String, PropertyDescriptor>();
        ClassUtil.collectBeanInfo(Request.class, expected);
        ClassUtil.collectBeanInfo(ApplicationContext.class, expected);
        Iterator<FeatureDescriptor> featureIt = resolver.getFeatureDescriptors(
                null, null);
        Iterator<? extends FeatureDescriptor> expectedIt = expected.values().iterator();
        while (featureIt.hasNext() && expectedIt.hasNext()) {
            assertEquals("The feature is not the same", expectedIt.next(),
                    featureIt.next());
        }
        assertTrue("The feature descriptors are not of the same size",
                !featureIt.hasNext() && !expectedIt.hasNext());
    }

    /**
     * Test method for
     * {@link TilesContextELResolver#isReadOnly(javax.el.ELContext, java.lang.Object, java.lang.Object)}.
     */
    public void testIsReadOnly() {
        ELContext context = new ELContextImpl(resolver);
        assertTrue("The value is not read only", resolver.isReadOnly(context,
                null, null));
    }
}
