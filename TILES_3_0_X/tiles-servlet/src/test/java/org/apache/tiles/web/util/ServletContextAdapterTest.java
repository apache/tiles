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
package org.apache.tiles.web.util;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ServletContextAdapter}.
 *
 * @version $Rev$ $Date$
 */
public class ServletContextAdapterTest {

    /**
     * The servlet configuration.
     */
    private ServletConfig config;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * First set of param names.
     */
    private Enumeration<String> names1;

    /**
     * Second set of param names.
     */
    private Enumeration<String> names2;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        config = createMock(ServletConfig.class);
        servletContext = createMock(ServletContext.class);
        names1 = createMock(Enumeration.class);
        names2 = createMock(Enumeration.class);

        expect(config.getServletContext()).andReturn(servletContext);
        expect(names1.hasMoreElements()).andReturn(true);
        expect(names1.nextElement()).andReturn("one");
        expect(names1.hasMoreElements()).andReturn(true);
        expect(names1.nextElement()).andReturn("two");
        expect(names1.hasMoreElements()).andReturn(false);
        expect(names2.hasMoreElements()).andReturn(true);
        expect(names2.nextElement()).andReturn("two");
        expect(names2.hasMoreElements()).andReturn(true);
        expect(names2.nextElement()).andReturn("three");
        expect(names2.hasMoreElements()).andReturn(false);
        expect(servletContext.getInitParameterNames()).andReturn(names1);
        expect(servletContext.getInitParameter("one")).andReturn("value1");
        expect(servletContext.getInitParameter("two")).andReturn("value2");
        expect(config.getInitParameterNames()).andReturn(names2);
        expect(config.getInitParameter("two")).andReturn("otherValue2");
        expect(config.getInitParameter("three")).andReturn("otherValue3");

        replay(names1, names2);
    }

    /**
     * Tears down the test.
     */
    @After
    public void tearDown() {
        verify(config, servletContext, names1, names2);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getContext(java.lang.String)}.
     */
    @Test
    public void testGetContext() {
        ServletContext otherContext = createMock(ServletContext.class);
        expect(servletContext.getContext("whatever")).andReturn(otherContext);

        replay(servletContext, config, otherContext);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(otherContext, adapter.getContext("whatever"));
        verify(otherContext);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getMajorVersion()}.
     */
    @Test
    public void testGetMajorVersion() {
        expect(servletContext.getMajorVersion()).andReturn(2);

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(2, adapter.getMajorVersion());
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getMinorVersion()}.
     */
    @Test
    public void testGetMinorVersion() {
        expect(servletContext.getMinorVersion()).andReturn(5);

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(5, adapter.getMinorVersion());
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getMimeType(java.lang.String)}.
     */
    @Test
    public void testGetMimeType() {
        expect(servletContext.getMimeType("whatever")).andReturn("mymime");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals("mymime", adapter.getMimeType("whatever"));
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getResourcePaths(java.lang.String)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetResourcePaths() {
        Set<URL> urls = createMock(Set.class);

        expect(servletContext.getResourcePaths("whatever")).andReturn(urls);

        replay(servletContext, config, urls);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(urls, adapter.getResourcePaths("whatever"));
        verify(urls);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getResource(java.lang.String)}.
     * @throws MalformedURLException If something goes wrong.
     */
    @Test
    public void testGetResource() throws MalformedURLException {
        URL url = new URL("file:///temporary");

        expect(servletContext.getResource("whatever")).andReturn(url);

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(url, adapter.getResource("whatever"));
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getResourceAsStream(java.lang.String)}.
     */
    @Test
    public void testGetResourceAsStream() {
        InputStream is = createMock(InputStream.class);

        expect(servletContext.getResourceAsStream("whatever")).andReturn(is);

        replay(servletContext, config, is);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(is, adapter.getResourceAsStream("whatever"));
        verify(is);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getRequestDispatcher(java.lang.String)}.
     */
    @Test
    public void testGetRequestDispatcher() {
        RequestDispatcher is = createMock(RequestDispatcher.class);

        expect(servletContext.getRequestDispatcher("whatever")).andReturn(is);

        replay(servletContext, config, is);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(is, adapter.getRequestDispatcher("whatever"));
        verify(is);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getNamedDispatcher(java.lang.String)}.
     */
    @Test
    public void testGetNamedDispatcher() {
        RequestDispatcher is = createMock(RequestDispatcher.class);

        expect(servletContext.getNamedDispatcher("whatever")).andReturn(is);

        replay(servletContext, config, is);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(is, adapter.getNamedDispatcher("whatever"));
        verify(is);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getServlet(java.lang.String)}.
     * @throws ServletException If something goes wrong.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGetServlet() throws ServletException {
        Servlet is = createMock(Servlet.class);

        expect(servletContext.getServlet("whatever")).andReturn(is);

        replay(servletContext, config, is);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(is, adapter.getServlet("whatever"));
        verify(is);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getServlets()}.
     */
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    public void testGetServlets() {
        Enumeration<Servlet> is = createMock(Enumeration.class);

        expect(servletContext.getServlets()).andReturn(is);

        replay(servletContext, config, is);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(is, adapter.getServlets());
        verify(is);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getServletNames()}.
     */
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    public void testGetServletNames() {
        Enumeration<String> is = createMock(Enumeration.class);

        expect(servletContext.getServletNames()).andReturn(is);

        replay(servletContext, config, is);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(is, adapter.getServletNames());
        verify(is);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#log(java.lang.String)}.
     */
    @Test
    public void testLogString() {
        servletContext.log("whatever");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        adapter.log("whatever");
    }

    /**
     * Test method for {@link ServletContextAdapter#log(java.lang.Exception, java.lang.String)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testLogExceptionString() {
        Exception e = new Exception("It does not matter");
        servletContext.log(e, "whatever");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        adapter.log(e, "whatever");
    }

    /**
     * Test method for {@link ServletContextAdapter#log(java.lang.String, java.lang.Throwable)}.
     */
    @Test
    public void testLogStringThrowable() {
        Throwable e = new Throwable("It does not matter");
        servletContext.log("whatever", e);

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        adapter.log("whatever", e);
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getRealPath(java.lang.String)}.
     */
    @Test
    public void testGetRealPath() {
        expect(servletContext.getRealPath("whatever")).andReturn("mypath");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals("mypath", adapter.getRealPath("whatever"));
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getServerInfo()}.
     */
    @Test
    public void testGetServerInfo() {
        expect(servletContext.getServerInfo()).andReturn("info");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals("info", adapter.getServerInfo());
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getInitParameter(java.lang.String)}.
     */
    @Test
    public void testGetInitParameter() {
        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals("value1", adapter.getInitParameter("one"));
        assertEquals("otherValue2", adapter.getInitParameter("two"));
        assertEquals("otherValue3", adapter.getInitParameter("three"));
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getInitParameterNames()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetInitParameterNames() {
        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        Set<String> names = new HashSet<String>();
        names.add("one");
        names.add("two");
        names.add("three");
        for (Enumeration<String> enumeration = adapter.getInitParameterNames(); enumeration.hasMoreElements();) {
            String name = enumeration.nextElement();
            assertTrue(names.remove(name));
        }
        assertTrue(names.isEmpty());
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getAttribute(java.lang.String)}.
     */
    @Test
    public void testGetAttribute() {
        expect(servletContext.getAttribute("whatever")).andReturn("value");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals("value", adapter.getAttribute("whatever"));
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getAttributeNames()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAttributeNames() {
        Enumeration<String> is = createMock(Enumeration.class);

        expect(servletContext.getAttributeNames()).andReturn(is);

        replay(servletContext, config, is);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals(is, adapter.getAttributeNames());
        verify(is);
    }

    /**
     * Test method for {@link ServletContextAdapter#setAttribute(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetAttribute() {
        servletContext.setAttribute("whatever", "value");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        adapter.setAttribute("whatever", "value");
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#removeAttribute(java.lang.String)}.
     */
    @Test
    public void testRemoveAttribute() {
        servletContext.removeAttribute("whatever");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        adapter.removeAttribute("whatever");
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getServletContextName()}.
     */
    @Test
    public void testGetServletContextName() {
        expect(servletContext.getServletContextName()).andReturn("value");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals("value", adapter.getServletContextName());
    }

    /**
     * Test method for {@link org.apache.tiles.web.util.ServletContextAdapter#getContextPath()}.
     */
    @Test
    public void testGetContextPath() {
        expect(servletContext.getContextPath()).andReturn("value");

        replay(servletContext, config);
        ServletContextAdapter adapter = new ServletContextAdapter(config);
        assertEquals("value", adapter.getContextPath());
    }

}
