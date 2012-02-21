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

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AbstractViewRequest}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractViewRequestTest {

    /**
     * The request to test.
     */
    private AbstractViewRequest request;

    /**
     * The internal request.
     */
    private DispatchRequest wrappedRequest;

    /**
     * The application context.
     */
    private ApplicationContext applicationContext;

    /**
     * The application scope.
     */
    private Map<String, Object> applicationScope;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        wrappedRequest = createMock(DispatchRequest.class);
        request = createMockBuilder(AbstractViewRequest.class).withConstructor(
                wrappedRequest).createMock();
        applicationContext = createMock(ApplicationContext.class);
        applicationScope = new HashMap<String, Object>();

        expect(wrappedRequest.getApplicationContext()).andReturn(applicationContext).anyTimes();
        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
    }

    /**
     * Test method for {@link org.apache.tiles.request.AbstractViewRequest#dispatch(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDispatch() throws IOException {
        Map<String, Object> requestScope = new HashMap<String, Object>();

        expect(request.getContext("request")).andReturn(requestScope);
        wrappedRequest.include("/my/path.html");

        replay(wrappedRequest, request, applicationContext);
        request.dispatch("/my/path.html");
        assertTrue((Boolean) requestScope.get(AbstractRequest.FORCE_INCLUDE_ATTRIBUTE_NAME));
        verify(wrappedRequest, request, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.AbstractViewRequest#include(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testInclude() throws IOException {
        Map<String, Object> requestScope = new HashMap<String, Object>();

        expect(request.getContext("request")).andReturn(requestScope);
        wrappedRequest.include("/my/path.html");

        replay(wrappedRequest, request, applicationContext);
        request.include("/my/path.html");
        assertTrue((Boolean) requestScope.get(AbstractRequest.FORCE_INCLUDE_ATTRIBUTE_NAME));
        verify(wrappedRequest, request, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.AbstractViewRequest#doInclude(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDoInclude() throws IOException {
        wrappedRequest.include("/my/path.html");

        replay(wrappedRequest, request, applicationContext);
        request.doInclude("/my/path.html");
        verify(wrappedRequest, request, applicationContext);
    }

}
