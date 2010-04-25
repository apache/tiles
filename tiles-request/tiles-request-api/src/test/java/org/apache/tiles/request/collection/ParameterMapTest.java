/**
 *
 */
package org.apache.tiles.request.collection;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;
import java.util.HashMap;

import org.apache.tiles.request.collection.extractor.EnumeratedValuesExtractor;
import org.apache.tiles.request.collection.extractor.HasKeys;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ParameterMap}.
 *
 * @version $Rev$ $Date$
 */
public class ParameterMapTest {

    private HasKeys<String> extractor;

    private ParameterMap map;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        extractor = createMock(EnumeratedValuesExtractor.class);
        map = new ParameterMap(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#clear()}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testClear() {
        map.clear();
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#containsKey(java.lang.Object)}.
     */
    @Test
    public void testContainsKey() {
        expect(extractor.getValue("one")).andReturn("value1");
        expect(extractor.getValue("two")).andReturn(null);

        replay(extractor);
        assertTrue(map.containsKey("one"));
        assertFalse(map.containsKey("two"));
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#containsValue(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testContainsValue() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");

        expect(extractor.getValue("one")).andReturn("value1");
        expect(extractor.getValue("two")).andReturn("value2");

        replay(extractor, keys);
        assertTrue(map.containsValue("value2"));
        verify(extractor, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#containsValue(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testContainsValueFalse() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");
        expect(keys.hasMoreElements()).andReturn(false);

        expect(extractor.getValue("one")).andReturn("value1");
        expect(extractor.getValue("two")).andReturn("value2");

        replay(extractor, keys);
        assertFalse(map.containsValue("value3"));
        verify(extractor, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#get(java.lang.Object)}.
     */
    @Test
    public void testGet() {
        expect(extractor.getValue("two")).andReturn("value2");

        replay(extractor);
        assertEquals("value2", map.get("two"));
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#isEmpty()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsEmpty() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);

        replay(extractor, keys);
        assertFalse(map.isEmpty());
        verify(extractor, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#isEmpty()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsEmptyTrue() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(false);

        replay(extractor, keys);
        assertTrue(map.isEmpty());
        verify(extractor, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#keySet()}.
     */
    @Test
    public void testKeySet() {
        replay(extractor);
        assertTrue(map.keySet() instanceof KeySet);
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#put(java.lang.String, java.lang.String[])}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testPut() {
        map.put("one", "value1");
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#putAll(java.util.Map)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testPutAll() {
        map.putAll(new HashMap<String, String>());
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#remove(java.lang.Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testRemove() {
        map.remove("one");
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ParameterMap#size()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSize() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");
        expect(keys.hasMoreElements()).andReturn(false);

        replay(extractor, keys);
        assertEquals(2, map.size());
        verify(extractor, keys);
    }
}
