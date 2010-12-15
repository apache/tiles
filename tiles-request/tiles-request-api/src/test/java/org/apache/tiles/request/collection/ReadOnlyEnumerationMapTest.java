/**
 *
 */
package org.apache.tiles.request.collection;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;
import java.util.HashMap;

import org.apache.tiles.request.attribute.HasKeys;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ReadOnlyEnumerationMap}.
 *
 * @version $Rev$ $Date$
 */
public class ReadOnlyEnumerationMapTest {

    private HasKeys<Integer> extractor;

    private ReadOnlyEnumerationMap<Integer> map;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        extractor = createMock(HasKeys.class);
        map = new ReadOnlyEnumerationMap<Integer>(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#clear()}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testClear() {
        map.clear();
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#containsKey(java.lang.Object)}.
     */
    @Test
    public void testContainsKey() {
        expect(extractor.getValue("one")).andReturn(1);
        expect(extractor.getValue("two")).andReturn(null);

        replay(extractor);
        assertTrue(map.containsKey("one"));
        assertFalse(map.containsKey("two"));
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#containsValue(java.lang.Object)}.
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

        expect(extractor.getValue("one")).andReturn(1);
        expect(extractor.getValue("two")).andReturn(2);

        replay(extractor, keys);
        assertTrue(map.containsValue(2));
        verify(extractor, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#containsValue(java.lang.Object)}.
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

        expect(extractor.getValue("one")).andReturn(1);
        expect(extractor.getValue("two")).andReturn(1);

        replay(extractor, keys);
        assertFalse(map.containsValue(3));
        verify(extractor, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#get(java.lang.Object)}.
     */
    @Test
    public void testGet() {
        expect(extractor.getValue("two")).andReturn(2);

        replay(extractor);
        assertEquals(new Integer(2), map.get("two"));
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#isEmpty()}.
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
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#isEmpty()}.
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
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#keySet()}.
     */
    @Test
    public void testKeySet() {
        replay(extractor);
        assertTrue(map.keySet() instanceof KeySet);
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#put(java.lang.String, java.lang.String[])}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testPut() {
        map.put("one", 1);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#putAll(java.util.Map)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testPutAll() {
        map.putAll(new HashMap<String, Integer>());
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#remove(java.lang.Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testRemove() {
        map.remove("one");
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ReadOnlyEnumerationMap#size()}.
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

    /**
     * Test method for {@link org.apache.tiles.extractor.collection.AbstractEnumerationMap#hashCode()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testHashCode() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);

        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("first");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("second");
        expect(keys.hasMoreElements()).andReturn(false);

        Integer value1 = 1;

        expect(extractor.getValue("first")).andReturn(value1);
        expect(extractor.getValue("second")).andReturn(null);

        replay(extractor, keys);
        assertEquals(("first".hashCode() ^ value1.hashCode())
                + ("second".hashCode() ^ 0), map.hashCode());
        verify(extractor, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.extractor.collection.AbstractEnumerationMap#equals(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEqualsObject() {
        HasKeys<Integer> otherRequest = createMock(HasKeys.class);
        ReadOnlyEnumerationMap<Integer> otherMap = createMockBuilder(
                ReadOnlyEnumerationMap.class).withConstructor(otherRequest)
                .createMock();
        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> otherKeys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(otherRequest.getKeys()).andReturn(otherKeys);

        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("first");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("second");
        expect(keys.hasMoreElements()).andReturn(false);

        expect(extractor.getValue("first")).andReturn(1);
        expect(extractor.getValue("second")).andReturn(2);

        expect(otherKeys.hasMoreElements()).andReturn(true);
        expect(otherKeys.nextElement()).andReturn("first");
        expect(otherKeys.hasMoreElements()).andReturn(true);
        expect(otherKeys.nextElement()).andReturn("second");
        expect(otherKeys.hasMoreElements()).andReturn(false);

        expect(otherRequest.getValue("first")).andReturn(1);
        expect(otherRequest.getValue("second")).andReturn(2);

        replay(extractor, otherRequest, otherMap, keys, otherKeys);
        assertTrue(map.equals(otherMap));
        verify(extractor, otherRequest, otherMap, keys, otherKeys);
    }

    /**
     * Test method for {@link org.apache.tiles.extractor.collection.AbstractEnumerationMap#equals(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEqualsObjectFalse() {
        HasKeys<Integer> otherRequest = createMock(HasKeys.class);
        ReadOnlyEnumerationMap<Integer> otherMap = createMockBuilder(
                ReadOnlyEnumerationMap.class).withConstructor(otherRequest)
                .createMock();
        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> otherKeys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(otherRequest.getKeys()).andReturn(otherKeys);

        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("first");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("second");
        expect(keys.hasMoreElements()).andReturn(false);

        expect(extractor.getValue("first")).andReturn(1);
        expect(extractor.getValue("second")).andReturn(2);

        expect(otherKeys.hasMoreElements()).andReturn(true);
        expect(otherKeys.nextElement()).andReturn("first");
        expect(otherKeys.hasMoreElements()).andReturn(true);
        expect(otherKeys.nextElement()).andReturn("second");
        expect(otherKeys.hasMoreElements()).andReturn(false);

        expect(otherRequest.getValue("first")).andReturn(1);
        expect(otherRequest.getValue("second")).andReturn(3);

        replay(extractor, otherRequest, otherMap, keys, otherKeys);
        assertFalse(map.equals(otherMap));
        verify(extractor, otherRequest, otherMap, keys, otherKeys);
    }
}
