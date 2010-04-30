package org.apache.tiles.request.servlet;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.tiles.request.collection.ReadOnlyEnumerationMap;
import org.apache.tiles.request.collection.ScopeMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ServletApplicationContext}.
 *
 * @version $Rev$ $Date$
 */
public class ServletApplicationContextTest {

    private ServletContext servletContext;

    private ServletApplicationContext context;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        servletContext = createMock(ServletContext.class);
        context = new ServletApplicationContext(servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletApplicationContext#getContext()}.
     */
    @Test
    public void testGetContext() {
        replay(servletContext);
        assertEquals(servletContext, context.getContext());
        verify(servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletApplicationContext#getApplicationScope()}.
     */
    @Test
    public void testGetApplicationScope() {
        replay(servletContext);
        assertTrue(context.getApplicationScope() instanceof ScopeMap);
        verify(servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletApplicationContext#getInitParams()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetInitParams() {
        replay(servletContext);
        assertTrue(context.getInitParams() instanceof ReadOnlyEnumerationMap);
        verify(servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletApplicationContext#getResource(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetResource() throws IOException {
        URL url = new URL("http://tiles.apache.org/");
        expect(servletContext.getResource("/my/path")).andReturn(url);

        replay(servletContext);
        assertEquals(url, context.getResource("/my/path"));
        verify(servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletApplicationContext#getResources(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetResources() throws IOException {
        URL url = new URL("http://tiles.apache.org/");
        expect(servletContext.getResource("/my/path")).andReturn(url);

        replay(servletContext);
        Set<URL> urls = context.getResources("/my/path");
        assertEquals(1, urls.size());
        assertTrue(urls.contains(url));
        verify(servletContext);
    }
}
