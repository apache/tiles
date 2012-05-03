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
package org.apache.tiles.request;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

/**
 * Tests {@link ApplicationContextWrapper}.
 *
 * @version $Rev: 1066446 $ $Date: 2011-02-02 13:38:04 +0100 (Wed, 02 Feb 2011) $
 */
public class ApplicationContextWrapperTest {

    /**
     * Test method for {@link org.apache.tiles.request.ApplicationContextWrapper#getWrappedApplicationContext()}.
     */
    @Test
    public void testGetWrappedApplicationContext() {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);

        replay(wrappedContext);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(wrappedContext, wrapper.getWrappedApplicationContext());
        verify(wrappedContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.ApplicationContextWrapper#getApplicationScope()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetApplicationScope() {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = createMock(Map.class);

        expect(wrappedContext.getApplicationScope()).andReturn(applicationScope);

        replay(wrappedContext, applicationScope);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(applicationScope, wrapper.getApplicationScope());
        verify(wrappedContext, applicationScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.ApplicationContextWrapper#getContext()}.
     */
    @Test
    public void testGetContext() {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        Object obj = createMock(Object.class);

        expect(wrappedContext.getContext()).andReturn(obj);

        replay(wrappedContext, obj);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(obj, wrapper.getContext());
        verify(wrappedContext, obj);
    }

    /**
     * Test method for {@link org.apache.tiles.request.ApplicationContextWrapper#getInitParams()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetInitParams() {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        Map<String, String> obj = createMock(Map.class);

        expect(wrappedContext.getInitParams()).andReturn(obj);

        replay(wrappedContext, obj);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(obj, wrapper.getInitParams());
        verify(wrappedContext, obj);
    }

    /**
     * Test method for {@link org.apache.tiles.request.ApplicationContextWrapper#getResource(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetResource() throws IOException {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        ApplicationResource obj = createMock(ApplicationResource.class);
        ApplicationResource objFr = createMock(ApplicationResource.class);

        expect(wrappedContext.getResource("whatever.html")).andReturn(obj);
        expect(wrappedContext.getResource(obj, Locale.FRENCH)).andReturn(objFr);

        replay(wrappedContext);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(obj, wrapper.getResource("whatever.html"));
        assertEquals(objFr, wrapper.getResource(obj, Locale.FRENCH));
        verify(wrappedContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.ApplicationContextWrapper#getResources(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetResources() throws IOException {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        Collection<ApplicationResource> obj = createMock(Collection.class);

        expect(wrappedContext.getResources("whatever.html")).andReturn(obj);

        replay(wrappedContext, obj);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(obj, wrapper.getResources("whatever.html"));
        verify(wrappedContext, obj);
    }

}
