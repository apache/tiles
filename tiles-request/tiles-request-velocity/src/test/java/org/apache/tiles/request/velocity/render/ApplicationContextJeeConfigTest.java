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
package org.apache.tiles.request.velocity.render;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.tiles.request.servlet.ServletApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ApplicationContextJeeConfig}.
 *
 * @version $Rev$ $Date$
 */
public class ApplicationContextJeeConfigTest {

    /**
     * The configuration to test.
     */
    private ApplicationContextJeeConfig config;

    /**
     * The application context.
     */
    private ServletApplicationContext applicationContext;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * Custom parameters.
     */
    private Map<String, String> params;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        servletContext = createMock(ServletContext.class);
        applicationContext = new ServletApplicationContext(servletContext);
    }

    /**
     * Tears down the test.
     */
    @After
    public void tearDown() {
        verify(servletContext);
    }

    /**
     * Tests {@link ApplicationContextJeeConfig#getInitParameter(String)}.
     */
    @Test
    public void testGetInitParameter() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        assertEquals("value1", config.getInitParameter("one"));
    }

    /**
     * Tests {@link ApplicationContextJeeConfig#findInitParameter(String)}.
     */
    @Test
    public void testFindInitParameter() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        assertEquals("value1", config.findInitParameter("one"));
    }

    /**
     * Tests {@link ApplicationContextJeeConfig#getInitParameterNames()}.
     */
    @Test
    public void testGetInitParameterNames() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        @SuppressWarnings("unchecked")
        Enumeration<String> names = config.getInitParameterNames();
        assertTrue(names.hasMoreElements());
        assertEquals("one", names.nextElement());
        assertFalse(names.hasMoreElements());
    }

    /**
     * Tests {@link ApplicationContextJeeConfig#getName()}.
     */
    @Test
    public void testGetName() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        assertEquals("Application Context JEE Config", config.getName());
    }

    /**
     * Tests {@link ApplicationContextJeeConfig#getServletContext()}.
     */
    @Test
    public void testGetServletContext() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        assertEquals(servletContext, config.getServletContext());
    }

}
