/**
 *
 */
package org.apache.tiles.request.collection;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;
import java.util.HashMap;

import org.apache.tiles.request.attribute.EnumeratedValuesExtractor;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link HeaderValuesMap}.
 *
 * @version $Rev$ $Date$
 */
public class HeaderValuesMapTest {

    private EnumeratedValuesExtractor extractor;

    private HeaderValuesMap map;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        extractor = createMock(EnumeratedValuesExtractor.class);
        map = new HeaderValuesMap(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#hashCode()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testHashCode() {
        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> values1 = createMock(Enumeration.class);
        Enumeration<String> values2 = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");
        expect(keys.hasMoreElements()).andReturn(false);

        expect(extractor.getValues("one")).andReturn(values1);
        expect(values1.hasMoreElements()).andReturn(true);
        expect(values1.nextElement()).andReturn("value1");
        expect(values1.hasMoreElements()).andReturn(false);

        expect(extractor.getValues("two")).andReturn(values2);
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value2");
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value3");
        expect(values2.hasMoreElements()).andReturn(false);

        replay(extractor, keys, values1, values2);
        assertEquals(("one".hashCode() ^ "value1".hashCode()) +
                ("two".hashCode() ^ ("value2".hashCode() + "value3".hashCode())),
                map.hashCode());
        verify(extractor, keys, values1, values2);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#clear()}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testClear() {
        map.clear();
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#containsKey(java.lang.Object)}.
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
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#containsValue(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testContainsValue() {
        assertFalse(map.containsValue(1));

        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> values1 = createMock(Enumeration.class);
        Enumeration<String> values2 = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");

        expect(extractor.getValues("one")).andReturn(values1);
        expect(values1.hasMoreElements()).andReturn(true);
        expect(values1.nextElement()).andReturn("value1");
        expect(values1.hasMoreElements()).andReturn(false);

        expect(extractor.getValues("two")).andReturn(values2);
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value2");
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value3");
        expect(values2.hasMoreElements()).andReturn(false);

        replay(extractor, keys, values1, values2);
        assertTrue(map.containsValue(new String[] {"value2", "value3"}));
        verify(extractor, keys, values1, values2);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#containsValue(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testContainsValueFalse() {
        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> values1 = createMock(Enumeration.class);
        Enumeration<String> values2 = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");
        expect(keys.hasMoreElements()).andReturn(false);

        expect(extractor.getValues("one")).andReturn(values1);
        expect(values1.hasMoreElements()).andReturn(true);
        expect(values1.nextElement()).andReturn("value1");
        expect(values1.hasMoreElements()).andReturn(false);

        expect(extractor.getValues("two")).andReturn(values2);
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value2");
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value3");
        expect(values2.hasMoreElements()).andReturn(false);

        replay(extractor, keys, values1, values2);
        assertFalse(map.containsValue(new String[] {"value2", "value4"}));
        verify(extractor, keys, values1, values2);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#equals(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEqualsObject() {
        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> values1 = createMock(Enumeration.class);
        Enumeration<String> values2 = createMock(Enumeration.class);
        EnumeratedValuesExtractor otherExtractor = createMock(EnumeratedValuesExtractor.class);
        Enumeration<String> otherValues1 = createMock(Enumeration.class);
        Enumeration<String> otherValues2 = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");
        expect(keys.hasMoreElements()).andReturn(false);

        expect(extractor.getValues("one")).andReturn(values1);
        expect(values1.hasMoreElements()).andReturn(true);
        expect(values1.nextElement()).andReturn("value1");
        expect(values1.hasMoreElements()).andReturn(false);

        expect(extractor.getValues("two")).andReturn(values2);
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value2");
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value3");
        expect(values2.hasMoreElements()).andReturn(false);

        expect(otherExtractor.getValues("one")).andReturn(otherValues1);
        expect(otherValues1.hasMoreElements()).andReturn(true);
        expect(otherValues1.nextElement()).andReturn("value1");
        expect(otherValues1.hasMoreElements()).andReturn(false);

        expect(otherExtractor.getValues("two")).andReturn(otherValues2);
        expect(otherValues2.hasMoreElements()).andReturn(true);
        expect(otherValues2.nextElement()).andReturn("value2");
        expect(otherValues2.hasMoreElements()).andReturn(true);
        expect(otherValues2.nextElement()).andReturn("value3");
        expect(otherValues2.hasMoreElements()).andReturn(false);

        replay(extractor, otherExtractor, keys, values1, values2, otherValues1, otherValues2);
        HeaderValuesMap otherMap = new HeaderValuesMap(otherExtractor);
        assertTrue(map.equals(otherMap));
        verify(extractor, otherExtractor, keys, values1, values2, otherValues1, otherValues2);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#get(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGet() {
        Enumeration<String> values2 = createMock(Enumeration.class);

        expect(extractor.getValues("two")).andReturn(values2);
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value2");
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value3");
        expect(values2.hasMoreElements()).andReturn(false);

        replay(extractor, values2);
        assertArrayEquals(new String[] {"value2", "value3"}, map.get("two"));
        verify(extractor, values2);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#isEmpty()}.
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
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#isEmpty()}.
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
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#keySet()}.
     */
    @Test
    public void testKeySet() {
        replay(extractor);
        assertTrue(map.keySet() instanceof KeySet);
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#put(java.lang.String, java.lang.String[])}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testPut() {
        map.put("one", new String[] {"value1", "value2"});
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#putAll(java.util.Map)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testPutAll() {
        map.putAll(new HashMap<String, String[]>());
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#remove(java.lang.Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testRemove() {
        map.remove("one");
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.HeaderValuesMap#size()}.
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
