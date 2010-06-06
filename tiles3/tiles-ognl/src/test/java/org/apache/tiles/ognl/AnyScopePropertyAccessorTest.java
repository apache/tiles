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
 * Tests {@link AnyScopePropertyAccessor}.
 *
 * @version $Rev$ $Date$
 */
public class AnyScopePropertyAccessorTest {

    private AnyScopePropertyAccessor accessor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        accessor = new AnyScopePropertyAccessor();
    }

    /**
     * Test method for {@link org.apache.tiles.ognl.AnyScopePropertyAccessor#getProperty(java.util.Map, java.lang.Object, java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetProperty() {
        Request request = createMock(Request.class);
        Map<String, Object> oneScope = createMock(Map.class);
        Map<String, Object> twoScope = createMock(Map.class);

        expect(request.getAvailableScopes()).andReturn(new String[] {"one", "two"}).anyTimes();
        expect(request.getContext("one")).andReturn(oneScope).anyTimes();
        expect(request.getContext("two")).andReturn(twoScope).anyTimes();
        expect(oneScope.containsKey("name1")).andReturn(true);
        expect(oneScope.get("name1")).andReturn("value1");
        expect(oneScope.containsKey("name2")).andReturn(false);
        expect(oneScope.containsKey("name3")).andReturn(false);
        expect(twoScope.containsKey("name2")).andReturn(true);
        expect(twoScope.get("name2")).andReturn("value2");
        expect(twoScope.containsKey("name3")).andReturn(false);

        replay(request, oneScope, twoScope);
        assertEquals("value1", accessor.getProperty(null, request, "name1"));
        assertEquals("value2", accessor.getProperty(null, request, "name2"));
        assertNull(accessor.getProperty(null, request, "name3"));
        verify(request, oneScope, twoScope);
    }

    /**
     * Test method for {@link org.apache.tiles.ognl.AnyScopePropertyAccessor#getSourceAccessor(ognl.OgnlContext, java.lang.Object, java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSourceAccessor() {
        Request request = createMock(Request.class);
        Map<String, Object> oneScope = createMock(Map.class);
        Map<String, Object> twoScope = createMock(Map.class);

        expect(request.getAvailableScopes()).andReturn(new String[] {"one", "two"}).anyTimes();
        expect(request.getContext("one")).andReturn(oneScope).anyTimes();
        expect(request.getContext("two")).andReturn(twoScope).anyTimes();
        expect(oneScope.containsKey("name1")).andReturn(true);
        expect(oneScope.containsKey("name2")).andReturn(false);
        expect(oneScope.containsKey("name3")).andReturn(false);
        expect(twoScope.containsKey("name2")).andReturn(true);
        expect(twoScope.containsKey("name3")).andReturn(false);

        replay(request, oneScope, twoScope);
        assertEquals(".getContext(\"one\").get(index)", accessor.getSourceAccessor(null, request, "name1"));
        assertEquals(".getContext(\"two\").get(index)", accessor.getSourceAccessor(null, request, "name2"));
        assertNull(accessor.getSourceAccessor(null, request, "name3"));
        verify(request, oneScope, twoScope);
    }

    /**
     * Test method for {@link org.apache.tiles.ognl.AnyScopePropertyAccessor#getSourceSetter(ognl.OgnlContext, java.lang.Object, java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSourceSetter() {
        Request request = createMock(Request.class);
        Map<String, Object> oneScope = createMock(Map.class);
        Map<String, Object> twoScope = createMock(Map.class);

        expect(request.getAvailableScopes()).andReturn(new String[] {"one", "two"}).anyTimes();
        expect(request.getContext("one")).andReturn(oneScope).anyTimes();
        expect(request.getContext("two")).andReturn(twoScope).anyTimes();
        expect(oneScope.containsKey("name1")).andReturn(true);
        expect(oneScope.containsKey("name2")).andReturn(false);
        expect(oneScope.containsKey("name3")).andReturn(false);
        expect(twoScope.containsKey("name2")).andReturn(true);
        expect(twoScope.containsKey("name3")).andReturn(false);

        replay(request, oneScope, twoScope);
        assertEquals(".getContext(\"one\").put(index, target)", accessor.getSourceSetter(null, request, "name1"));
        assertEquals(".getContext(\"two\").put(index, target)", accessor.getSourceSetter(null, request, "name2"));
        assertEquals(".getContext(\"one\").put(index, target)", accessor.getSourceSetter(null, request, "name3"));
        verify(request, oneScope, twoScope);
    }

    /**
     * Test method for {@link org.apache.tiles.ognl.AnyScopePropertyAccessor#setProperty(java.util.Map, java.lang.Object, java.lang.Object, java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSetProperty() {
        Request request = createMock(Request.class);
        Map<String, Object> oneScope = createMock(Map.class);
        Map<String, Object> twoScope = createMock(Map.class);

        expect(request.getAvailableScopes()).andReturn(new String[] {"one", "two"}).anyTimes();
        expect(request.getContext("one")).andReturn(oneScope).anyTimes();
        expect(request.getContext("two")).andReturn(twoScope).anyTimes();
        expect(oneScope.containsKey("name1")).andReturn(true);
        expect(oneScope.put("name1", "otherValue1")).andReturn("value1");
        expect(oneScope.containsKey("name2")).andReturn(false);
        expect(oneScope.containsKey("name3")).andReturn(false);
        expect(twoScope.containsKey("name2")).andReturn(true);
        expect(twoScope.put("name2", "otherValue2")).andReturn("value2");
        expect(twoScope.containsKey("name3")).andReturn(false);
        expect(oneScope.put("name3", "otherValue3")).andReturn(null);

        replay(request, oneScope, twoScope);
        accessor.setProperty(null, request, "name1", "otherValue1");
        accessor.setProperty(null, request, "name2", "otherValue2");
        accessor.setProperty(null, request, "name3", "otherValue3");
        verify(request, oneScope, twoScope);
    }

}
