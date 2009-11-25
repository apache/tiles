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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.shale.test.mock.MockServletContext;
import org.apache.tiles.request.ApplicationContext;
import org.easymock.classextension.EasyMock;

/**
 * @version $Rev$ $Date$
 */
public class ServletTilesRequestContextTest extends TestCase {

    /**
     * Test path to check forward and include.
     */
    private static final String TEST_PATH = "testPath.jsp";

    /**
     * The request context.
     */
    private ServletTilesRequestContext context;

    /**
     * The servlet context.
     */
    private MockServletContext servletContext;

    /**
     * The Tiles application context.
     */
    private ApplicationContext applicationContext;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = new MockServletContext();
        applicationContext = EasyMock.createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("applicationAttribute1", "applicationValue1");
        applicationScope.put("applicationAttribute2", "applicationValue2");
        EasyMock.expect(applicationContext.getApplicationScope()).andReturn(
                applicationScope);
        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("initParameter1", "initParameterValue1");
        EasyMock.expect(applicationContext.getInitParams()).andReturn(
                initParams);
        MockHttpSession session = new MockHttpSession(servletContext);
        MockHttpServletRequest request = new MockHttpServletRequest(session);
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Content-Type", "text/html");
        request.addParameter("myParam", "value1");
        request.addParameter("myParam", "value2");

        context = new ServletTilesRequestContext(applicationContext, request,
                response);

        Map<String, Object> requestScope = context.getRequestScope();
        requestScope.put("attribute1", "value1");
        requestScope.put("attribute2", "value2");

        Map<String, Object> sessionScope = context.getSessionScope();
        sessionScope.put("sessionAttribute1", "sessionValue1");
        sessionScope.put("sessionAttribute2", "sessionValue2");
        EasyMock.replay(applicationContext);
    }

    /**
     * Tests getting the header.
     */
    public void testGetHeader() {
        Map<String, String> map = context.getHeader();
        assertTrue("The header does not contain a set value", "text/html"
                .equals(map.get("Content-Type")));
        doTestReadMap(map, String.class, String.class, "header map");
    }

    /**
     * Tests getting the header value.
     */
    public void testGetHeaderValues() {
        Map<String, String[]> map = context.getHeaderValues();
        String[] array = map.get("Content-Type");
        assertTrue("The header does not contain a set value", array.length == 1
                && "text/html".equals(array[0]));
        doTestReadMap(map, String.class, String[].class, "header values map");
    }

    /**
     * Tests getting the parameters.
     */
    public void testGetParam() {
        Map<String, String> map = context.getParam();
        assertTrue("The parameters do not contain a set value", "value1"
                .equals(map.get("myParam"))
                || "value2".equals(map.get("myParam")));
        doTestReadMap(map, String.class, String.class, "parameter map");
    }

    /**
     * Tests getting the parameter values.
     */
    public void testGetParamValues() {
        Map<String, String[]> map = context.getParamValues();
        String[] array = map.get("myParam");
        assertTrue(
                "The parameters not contain a set value",
                array.length == 2
                        && (("value1".equals(array[0]) && "value2"
                                .equals(array[1])) || ("value1"
                                .equals(array[1]) && "value2".equals(array[0]))));
        doTestReadMap(map, String.class, String[].class, "parameter values map");
    }

    /**
     * Tests getting request scope attributes.
     */
    public void testGetRequestScope() {
        Map<String, Object> map = context.getRequestScope();
        assertTrue("The request scope does not contain a set value", "value1"
                .equals(map.get("attribute1")));
        assertTrue("The request scope does not contain a set value", "value2"
                .equals(map.get("attribute2")));
        doTestReadMap(map, String.class, Object.class, "request scope map");
    }

    /**
     * Tests getting session scope attributes.
     */
    public void testGetSessionScope() {
        Map<String, Object> map = context.getSessionScope();
        assertTrue("The session scope does not contain a set value",
                "sessionValue1".equals(map.get("sessionAttribute1")));
        assertTrue("The session scope does not contain a set value",
                "sessionValue2".equals(map.get("sessionAttribute2")));
        doTestReadMap(map, String.class, Object.class, "session scope map");
    }

    /**
     * Tests {@link ServletTilesRequestContext#getApplicationContext()}.
     */
    public void testGetApplicationContext() {
        assertTrue("The objects are not the same",
                applicationContext == context.getApplicationContext());
    }

    /**
     * Tests getting application scope attributes.
     */
    public void testGetApplicationScope() {
        Map<String, Object> map = ((ApplicationContext) context)
                .getApplicationScope();
        assertTrue("The application scope does not contain a set value",
                "applicationValue1".equals(map.get("applicationAttribute1")));
        assertTrue("The application scope does not contain a set value",
                "applicationValue2".equals(map.get("applicationAttribute2")));
        doTestReadMap(map, String.class, Object.class, "application scope map");
    }

    /**
     * Tests getting init parameters.
     */
    public void testGetInitParams() {
        Map<String, String> map = ((ApplicationContext) context)
                .getInitParams();
        assertTrue("The init parameters do not contain a set value",
                "initParameterValue1".equals(map.get("initParameter1")));
        doTestReadMap(map, String.class, String.class,
                "init parameters scope map");
    }

    /**
     * Tests {@link ServletTilesRequestContext#getOutputStream()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testGetOutputStream() throws IOException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock
                .createMock(HttpServletResponse.class);
        ApplicationContext applicationContext = EasyMock
                .createMock(ApplicationContext.class);
        ServletOutputStream os = EasyMock.createMock(ServletOutputStream.class);
        EasyMock.expect(response.getOutputStream()).andReturn(os);
        EasyMock.replay(request, response, applicationContext, os);
        ServletTilesRequestContext requestContext = new ServletTilesRequestContext(
                applicationContext, request, response);
        assertEquals(os, requestContext.getOutputStream());
        EasyMock.verify(request, response, applicationContext, os);
    }

    /**
     * Tests {@link ServletTilesRequestContext#getWriter()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testGetWriter() throws IOException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock
                .createMock(HttpServletResponse.class);
        ApplicationContext applicationContext = EasyMock
                .createMock(ApplicationContext.class);
        PrintWriter writer = EasyMock.createMock(PrintWriter.class);
        EasyMock.expect(response.getWriter()).andReturn(writer);
        EasyMock.replay(request, response, applicationContext, writer);
        ServletTilesRequestContext requestContext = new ServletTilesRequestContext(
                applicationContext, request, response);
        assertEquals(writer, requestContext.getWriter());
        EasyMock.verify(request, response, applicationContext, writer);
    }

    /**
     * Tests {@link ServletTilesRequestContext#getPrintWriter()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testGetPrintWriter() throws IOException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock
                .createMock(HttpServletResponse.class);
        ApplicationContext applicationContext = EasyMock
                .createMock(ApplicationContext.class);
        PrintWriter writer = EasyMock.createMock(PrintWriter.class);
        EasyMock.expect(response.getWriter()).andReturn(writer);
        EasyMock.replay(request, response, applicationContext, writer);
        ServletTilesRequestContext requestContext = new ServletTilesRequestContext(
                applicationContext, request, response);
        assertEquals(writer, requestContext.getPrintWriter());
        EasyMock.verify(request, response, applicationContext, writer);
    }

    /**
     * Tests the forced inclusion in the request.
     *
     * @throws IOException If something goes wrong.
     */
    public void testForceInclude() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new CommitSupportMockHttpServletResponse();
        MockServletTilesRequestContext context = new MockServletTilesRequestContext(
                applicationContext, request, response);
        context.dispatch(TEST_PATH);
        assertEquals("Forward has not been called", 1, context.getForwardCount());
        assertEquals("Include has been called", 0, context.getIncludeCount());
        assertFalse("Force include has been incorrectly set.", ServletUtil
                .isForceInclude(request));
        ServletUtil.setForceInclude(request, true);
        context.dispatch(TEST_PATH);
        assertEquals("Forward has been called", 1, context.getForwardCount());
        assertEquals("Include has not been called", 1, context.getIncludeCount());
    }

    /**
     * Tests a generic map.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param currentMap The map to check.
     * @param keyClass The key class.
     * @param valueClass The value class.
     * @param mapName The name of the map to test (for messages).
     */
    private <K, V> void doTestReadMap(Map<K, V> currentMap, Class<K> keyClass,
            Class<V> valueClass, String mapName) {
        int size1, size2;
        size1 = currentMap.keySet().size();
        size2 = currentMap.entrySet().size();
        assertEquals("The map" + mapName
                + " has keySet and entrySet of different size", size1, size2);
        for (K key : currentMap.keySet()) {
            assertTrue("The key is not of class" + keyClass.getName(), keyClass
                    .isInstance(key));
            V value = currentMap.get(key);
            assertTrue("The value is not of class" + valueClass.getName(),
                    valueClass.isInstance(value));
            assertTrue("The map " + mapName
                    + " does not return the correct value for 'containsValue'",
                    currentMap.containsValue(value));
        }
    }

    /**
     * Extends {@link MockHttpServletResponse} to override
     * {@link MockHttpServletResponse#isCommitted()} method.
     */
    private static class CommitSupportMockHttpServletResponse extends
            MockHttpServletResponse {

        /** {@inheritDoc} */
        @Override
        public boolean isCommitted() {
            return false;
        }
    }

    /**
     * Extends {@link ServletTilesRequestContext} to check forward and include.
     */
    private static class MockServletTilesRequestContext extends
            ServletTilesRequestContext {

        /**
         * The number of times that forward has been called.
         */
        private int forwardCount = 0;

        /**
         * The number of times that include has been called.
         */
        private int includeCount = 0;

        /**
         * Constructor.
         *
         * @param applicationContext The Tiles application context.
         * @param request The request.
         * @param response The response.
         */
        public MockServletTilesRequestContext(
                ApplicationContext applicationContext,
                HttpServletRequest request, HttpServletResponse response) {
            super(applicationContext, request, response);
        }

        /** {@inheritDoc} */
        @Override
        protected void forward(String path) {
            forwardCount++;
        }

        /** {@inheritDoc} */
        @Override
        public void include(String path) {
            includeCount++;
        }

        /**
         * Returns the forward count.
         *
         * @return The forward count.
         */
        public int getForwardCount() {
            return forwardCount;
        }

        /**
         * Returns the include count.
         *
         * @return The include count.
         */
        public int getIncludeCount() {
            return includeCount;
        }
    }
}
