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
package org.apache.tiles.request.portlet.delegate;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.StateAwareResponse;

import org.apache.tiles.request.collection.AddableParameterMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link StateAwareRequestDelegate}.
 *
 * @version $Rev$ $Date$
 */
public class StateAwareRequestDelegateTest {

    /**
     * The request.
     */
    private PortletRequest request;

    /**
     * The response.
     */
    private StateAwareResponse response;

    /**
     * The delegate to test.
     */
    private StateAwareRequestDelegate delegate;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(PortletRequest.class);
        response = createMock(StateAwareResponse.class);
        delegate = new StateAwareRequestDelegate(request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareRequestDelegate#getParam()}.
     */
    @Test
    public void testGetParam() {
        replay(request);
        assertTrue(delegate.getParam() instanceof AddableParameterMap);
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareRequestDelegate#getParamValues()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetParamValues() {
        Map<String, String[]> requestMap = createMock(Map.class);
        Map<String, String[]> responseMap = createMock(Map.class);

        expect(request.getParameterMap()).andReturn(requestMap);
        expect(response.getRenderParameterMap()).andReturn(responseMap);

        replay(request);
        assertTrue(delegate.getParamValues() instanceof StateAwareParameterMap);
        verify(request);
    }
}
