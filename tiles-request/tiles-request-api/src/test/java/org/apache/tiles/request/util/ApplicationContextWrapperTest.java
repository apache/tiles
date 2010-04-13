/**
 *
 */
package org.apache.tiles.request.util;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.ApplicationContext;
import org.junit.Test;

/**
 * Tests {@link ApplicationContextWrapper}.
 *
 * @version $Rev$ $Date$
 */
public class ApplicationContextWrapperTest {

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationContextWrapper#getWrappedApplicationContext()}.
     */
    @Test
    public void testGetWrappedApplicationContext() {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);

        replay(wrappedContext);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(wrappedContext, wrapper.getWrappedApplicationContext());
        verify(wrappedContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationContextWrapper#getApplicationScope()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetApplicationScope() {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = createMock(Map.class);

        expect(wrappedContext.getApplicationScope()).andReturn(applicationScope);

        replay(wrappedContext, applicationScope);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(applicationScope, wrapper.getApplicationScope());
        verify(wrappedContext, applicationScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationContextWrapper#getContext()}.
     */
    @Test
    public void testGetContext() {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        Object obj = createMock(Object.class);

        expect(wrappedContext.getContext()).andReturn(obj);

        replay(wrappedContext, obj);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(obj, wrapper.getContext());
        verify(wrappedContext, obj);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationContextWrapper#getInitParams()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetInitParams() {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        Map<String, String> obj = createMock(Map.class);

        expect(wrappedContext.getInitParams()).andReturn(obj);

        replay(wrappedContext, obj);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(obj, wrapper.getInitParams());
        verify(wrappedContext, obj);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationContextWrapper#getResource(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetResource() throws IOException {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        URL obj = new URL("file:///home/neverland/whatever.html");

        expect(wrappedContext.getResource("whatever.html")).andReturn(obj);

        replay(wrappedContext);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(obj, wrapper.getResource("whatever.html"));
        verify(wrappedContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationContextWrapper#getResources(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetResources() throws IOException {
        ApplicationContext wrappedContext = createMock(ApplicationContext.class);
        Set<URL> obj = createMock(Set.class);

        expect(wrappedContext.getResources("whatever.html")).andReturn(obj);

        replay(wrappedContext, obj);
        ApplicationContextWrapper wrapper = new ApplicationContextWrapper(wrappedContext);
        assertEquals(obj, wrapper.getResources("whatever.html"));
        verify(wrappedContext, obj);
    }

}
