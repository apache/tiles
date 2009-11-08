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

package org.apache.tiles.request.servlet;

import java.util.Vector;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import junit.framework.TestCase;

import org.apache.tiles.request.servlet.ServletSessionScopeMap;
import org.easymock.EasyMock;

/**
 * Tests {@link ServletSessionScopeMap} behaviour.
 *
 * @version $Rev$ $Date$
 */
public class ServletSessionScopeMapTest extends TestCase {

    /**
     * Constructor.
     *
     * @param testName The name of the test.
     */
    public ServletSessionScopeMapTest(String testName) {
        super(testName);
    }

    /**
     * Tests if the session object is used correctly inside
     * {@link ServletSessionScopeMap}.
     */
    public void testSessionUse() {
        HttpServletRequest request = EasyMock.createMock(
                HttpServletRequest.class);
        HttpSession session = EasyMock.createMock(HttpSession.class);
        EasyMock.expect(request.getSession(false)).andReturn(null);
        EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
        EasyMock.expect(session.getAttribute("testAttribute")).andReturn(null);
        session.setAttribute("testAttribute", "testValue");
        EasyMock.expect(request.getSession(false)).andReturn(session).anyTimes();
        Vector<String> v = new Vector<String>();
        v.add("testAttribute");
        EasyMock.expect(session.getAttributeNames()).andReturn(v.elements());
        EasyMock.replay(request);
        EasyMock.replay(session);

        Map<String, Object> map = new ServletSessionScopeMap(request);
        assertEquals("The map is not empty", 0, map.size());
        map.put("testAttribute", "testValue");
        assertEquals("The map has not one attribute", 1, map.size());
    }
}
