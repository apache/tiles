/**
 *
 */
package org.apache.tiles.request.portlet.delegate;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link StateAwareParameterMap}.
 *
 * @version $Rev$ $Date$
 */
public class StateAwareParameterMapTest {

    private Map<String, String[]> requestMap;

    private Map<String, String[]> responseMap;

    private StateAwareParameterMap map;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        requestMap = createMock(Map.class);
        responseMap = createMock(Map.class);
        map = new StateAwareParameterMap(requestMap, responseMap);

    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#clear()}.
     */
    @Test
    public void testClear() {
        responseMap.clear();

        replay(requestMap, responseMap);
        map.clear();
        verify(requestMap, responseMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#containsKey(java.lang.Object)}.
     */
    @Test
    public void testContainsKey() {
        expect(requestMap.containsKey("key")).andReturn(true);

        replay(requestMap, responseMap);
        assertTrue(map.containsKey("key"));
        verify(requestMap, responseMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#containsValue(java.lang.Object)}.
     */
    @Test
    public void testContainsValue() {
        String[] values = new String[] {"value1", "value2"};
        expect(requestMap.containsValue(values)).andReturn(true);

        replay(requestMap, responseMap);
        assertTrue(map.containsValue(values));
        verify(requestMap, responseMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#entrySet()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEntrySet() {
        Set<Map.Entry<String, String[]>> entrySet = createMock(Set.class);

        expect(requestMap.entrySet()).andReturn(entrySet);

        replay(requestMap, responseMap, entrySet);
        assertEquals(entrySet, map.entrySet());
        verify(requestMap, responseMap, entrySet);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#get(java.lang.Object)}.
     */
    @Test
    public void testGet() {
        String[] values = new String[] {"value1", "value2"};
        expect(requestMap.get("key")).andReturn(values);

        replay(requestMap, responseMap);
        assertArrayEquals(values, map.get("key"));
        verify(requestMap, responseMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        expect(requestMap.isEmpty()).andReturn(false);

        replay(requestMap, responseMap);
        assertFalse(map.isEmpty());
        verify(requestMap, responseMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#keySet()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testKeySet() {
        Set<String> keySet = createMock(Set.class);

        expect(requestMap.keySet()).andReturn(keySet);

        replay(requestMap, responseMap, keySet);
        assertEquals(keySet, map.keySet());
        verify(requestMap, responseMap, keySet);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#put(java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testPut() {
        String[] values = new String[] {"value1", "value2"};
        expect(responseMap.put(eq("key"), aryEq(values))).andReturn(null);

        replay(requestMap, responseMap);
        assertNull(map.put("key", values));
        verify(requestMap, responseMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#putAll(java.util.Map)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPutAll() {
        Map<String, String[]> entries = createMock(Map.class);
        responseMap.putAll(entries);

        replay(requestMap, responseMap, entries);
        map.putAll(entries);
        verify(requestMap, responseMap, entries);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#remove(java.lang.Object)}.
     */
    @Test
    public void testRemove() {
        String[] values = new String[] {"value1", "value2"};
        expect(responseMap.remove("key")).andReturn(values);

        replay(requestMap, responseMap);
        assertArrayEquals(values, map.remove("key"));
        verify(requestMap, responseMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#size()}.
     */
    @Test
    public void testSize() {
        expect(requestMap.size()).andReturn(1);

        replay(requestMap, responseMap);
        assertEquals(1, map.size());
        verify(requestMap, responseMap);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareParameterMap#values()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testValues() {
        Collection<String[]> values = createMock(Collection.class);

        expect(requestMap.values()).andReturn(values);

        replay(requestMap, responseMap, values);
        assertEquals(values, map.values());
        verify(requestMap, responseMap, values);
    }
}
