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

public class ApplicationContextJeeConfigTest {

    private ApplicationContextJeeConfig config;

    private ServletApplicationContext applicationContext;

    private ServletContext servletContext;

    private Map<String, String> params;

    @Before
    public void setUp() {
        servletContext = createMock(ServletContext.class);
        applicationContext = new ServletApplicationContext(servletContext);
    }

    @After
    public void tearDown() {
        verify(servletContext);
    }

    @Test
    public void testGetInitParameter() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        assertEquals("value1", config.getInitParameter("one"));
    }

    @Test
    public void testFindInitParameter() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        assertEquals("value1", config.findInitParameter("one"));
    }

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

    @Test
    public void testGetName() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        assertEquals("Application Context JEE Config", config.getName());
    }

    @Test
    public void testGetServletContext() {
        params = new HashMap<String, String>();
        params.put("one", "value1");
        config = new ApplicationContextJeeConfig(applicationContext, params);
        replay(servletContext);
        assertEquals(servletContext, config.getServletContext());
    }

}
