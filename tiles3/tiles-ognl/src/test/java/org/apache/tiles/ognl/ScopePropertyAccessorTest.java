/**
 *
 */
package org.apache.tiles.ognl;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ScopePropertyAccessor}.
 *
 * @version $Rev$ $Date$
 */
public class ScopePropertyAccessorTest {

    private ScopePropertyAccessor accessor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        accessor = new ScopePropertyAccessor();
    }

    /**
     * Test method for {@link org.apache.tiles.ognl.ScopePropertyAccessor#getProperty(java.util.Map, java.lang.Object, java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetProperty() {
        Request request = createMock(Request.class);
        Map<String, Object> oneScope = createMock(Map.class);

        expect(request.getContext("one")).andReturn(oneScope);

        replay(request);
        assertEquals(oneScope, accessor.getProperty(null, request, "oneScope"));
        assertNull(accessor.getProperty(null, request, "whatever"));
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.ognl.ScopePropertyAccessor#getSourceAccessor(ognl.OgnlContext, java.lang.Object, java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSourceAccessor() {
        Request request = createMock(Request.class);
        @SuppressWarnings("unused")
        Map<String, Object> oneScope = createMock(Map.class);

        replay(request);
        assertEquals(".getContext(\"one\")", accessor.getSourceAccessor(null, request, "oneScope"));
        assertNull(accessor.getSourceAccessor(null, request, "whatever"));
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.ognl.ScopePropertyAccessor#getSourceSetter(ognl.OgnlContext, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testGetSourceSetter() {
        assertNull(accessor.getSourceSetter(null, null, "whatever"));
    }

    /**
     * Test method for {@link org.apache.tiles.ognl.ScopePropertyAccessor#setProperty(java.util.Map, java.lang.Object, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testSetProperty() {
        accessor.setProperty(null, null, "whatever", "whateverValue");
    }

}
