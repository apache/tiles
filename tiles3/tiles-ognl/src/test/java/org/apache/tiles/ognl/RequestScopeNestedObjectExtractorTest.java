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

package org.apache.tiles.ognl;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.ognl.NestedObjectExtractor;
import org.apache.tiles.ognl.RequestScopeNestedObjectExtractor;
import org.apache.tiles.ognl.SessionScopeNestedObjectExtractor;
import org.apache.tiles.request.Request;
import org.junit.Test;

/**
 * Tests {@link RequestScopeNestedObjectExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class RequestScopeNestedObjectExtractorTest {

    /**
     * Tests {@link SessionScopeNestedObjectExtractor#getNestedObject(Request)}.
     */
    @Test
    public void testGetNestedObject() {
        Request request = createMock(Request.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("attribute1", "value1");
        expect(request.getContext("session")).andReturn(map);

        replay(request);
        NestedObjectExtractor<Request> extractor = new SessionScopeNestedObjectExtractor();
        assertEquals(map, extractor.getNestedObject(request));
        verify(request);
    }

}
