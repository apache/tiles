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
package org.apache.tiles.request.portlet;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.portlet.PortletContext;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.MimeResponseDelegate;
import org.apache.tiles.request.portlet.delegate.PortletRequestDelegate;
import org.junit.Test;

/**
 * Tests {@link ResourcePortletRequest}.
 *
 * @version $Rev$ $Date$
 */
public class ResourcePortletRequestTest {

    /**
     * Test method for
     * {@link ResourcePortletRequest#ResourcePortletRequest(ApplicationContext, PortletContext,
     * ResourceRequest, ResourceResponse)}.
     * @throws NoSuchFieldException If something goes wrong.
     * @throws SecurityException If something goes wrong.
     * @throws IllegalAccessException If something goes wrong.
     * @throws IllegalArgumentException If something goes wrong.
     */
    @Test
    public void testResourcePortletRequest() throws NoSuchFieldException, IllegalAccessException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        PortletContext portletContext = createMock(PortletContext.class);
        ResourceRequest request = createMock(ResourceRequest.class);
        ResourceResponse response = createMock(ResourceResponse.class);

        replay(applicationContext, portletContext, request, response);
        ResourcePortletRequest req = new ResourcePortletRequest(applicationContext,
                portletContext, request, response);
        Class<? extends ResourcePortletRequest> clazz = req.getClass();
        Field field = clazz.getSuperclass().getDeclaredField("requestDelegate");
        assertTrue(field.get(req) instanceof PortletRequestDelegate);
        field = clazz.getSuperclass().getDeclaredField("responseDelegate");
        assertTrue(field.get(req) instanceof MimeResponseDelegate);
        verify(applicationContext, portletContext, request, response);
    }

}
