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
package org.apache.tiles.request.util;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.WebRequest;
import org.junit.Test;

/**
 * Tests {@link WebRequestWrapper}.
 *
 * @version $Rev$ $Date$
 */
public class WebRequestWrapperTest extends DefaultRequestWrapperTest {

    @Override
    protected WebRequest createMockRequest() {
        WebRequest wrappedRequest = createMock(WebRequest.class);
        return wrappedRequest;
    }

    @Override
    protected WebRequestWrapper createRequestWrapper(Request wrappedRequest) {
        WebRequestWrapper request = new WebRequestWrapper((WebRequest) wrappedRequest);
        return request;
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.WebRequestWrapper#dispatch(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDispatch() throws IOException {
        WebRequest wrappedRequest = createMockRequest();

        wrappedRequest.dispatch("/my/path.html");

        replay(wrappedRequest);
        WebRequestWrapper request = createRequestWrapper(wrappedRequest);
        request.dispatch("/my/path.html");
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.WebRequestWrapper#include(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testInclude() throws IOException {
        WebRequest wrappedRequest = createMockRequest();

        wrappedRequest.include("/my/path.html");

        replay(wrappedRequest);
        WebRequestWrapper request = createRequestWrapper(wrappedRequest);
        request.include("/my/path.html");
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.WebRequestWrapper#setContentType(java.lang.String)}.
     */
    @Test
    public void testSetContentType() {
        WebRequest wrappedRequest = createMockRequest();

        wrappedRequest.setContentType("text/html");

        replay(wrappedRequest);
        WebRequestWrapper request = createRequestWrapper(wrappedRequest);
        request.setContentType("text/html");
        verify(wrappedRequest);
    }
}
