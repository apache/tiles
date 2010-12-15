package org.apache.tiles.request.collection;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.attribute.EnumeratedValuesExtractor;
import org.junit.Before;
import org.junit.Test;

public class HeaderValuesMapEntrySetTest {


    private EnumeratedValuesExtractor extractor;

    private HeaderValuesMap map;

    private Set<Map.Entry<String, String[]>> entrySet;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        extractor = createMock(EnumeratedValuesExtractor.class);
        map = new HeaderValuesMap(extractor);
        entrySet = map.entrySet();
    }

    /**
     * Tests {@link Set#add(Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testAdd() {
        entrySet.add(null);
    }

    /**
     * Tests {@link Set#addAll(Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testAddAll() {
        entrySet.addAll(null);
    }

    /**
     * Tests {@link Set#clear(Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testClear() {
        entrySet.clear();
    }

    /**
     * Tests {@link Set#contains(Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testContains() {
        Map.Entry<String, String[]> entry = createMock(Map.Entry.class);
        Enumeration<String> values2 = createMock(Enumeration.class);

        expect(entry.getKey()).andReturn("two");
        expect(entry.getValue()).andReturn(new String[] {"value2", "value3"});

        expect(extractor.getValues("two")).andReturn(values2);
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value2");
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value3");
        expect(values2.hasMoreElements()).andReturn(false);

        replay(extractor, entry, values2);
        assertTrue(entrySet.contains(entry));
        verify(extractor, entry, values2);
    }

    /**
     * Tests {@link Set#containsAll(Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testContainsAll() {
        Enumeration<String> values1 = createMock(Enumeration.class);
        Enumeration<String> values2 = createMock(Enumeration.class);
        Map.Entry<String, String[]> entry1 = createMock(Map.Entry.class);
        Map.Entry<String, String[]> entry2 = createMock(Map.Entry.class);

        expect(entry1.getKey()).andReturn("one");
        expect(entry1.getValue()).andReturn(new String[] {"value1"});
        expect(entry2.getKey()).andReturn("two");
        expect(entry2.getValue()).andReturn(new String[] {"value2", "value3"});

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

        replay(extractor, values1, values2, entry1, entry2);
        List<Map.Entry<String, String[]>> coll = new ArrayList<Map.Entry<String,String[]>>();
        coll.add(entry1);
        coll.add(entry2);
        assertTrue(entrySet.containsAll(coll));
        verify(extractor, values1, values2, entry1, entry2);
    }

    /**
     * Tests {@link Set#containsAll(Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testContainsAllFalse() {
        Enumeration<String> values1 = createMock(Enumeration.class);
        Map.Entry<String, String[]> entry1 = createMock(Map.Entry.class);

        expect(entry1.getKey()).andReturn("one");
        expect(entry1.getValue()).andReturn(new String[] {"value4"});

        expect(extractor.getValues("one")).andReturn(values1);
        expect(values1.hasMoreElements()).andReturn(true);
        expect(values1.nextElement()).andReturn("value1");

        replay(extractor, values1, entry1);
        List<Map.Entry<String, String[]>> coll = new ArrayList<Map.Entry<String,String[]>>();
        coll.add(entry1);
        assertFalse(entrySet.containsAll(coll));
        verify(extractor, values1, entry1);
    }

    /**
     * Test method for {@link Set#isEmpty()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsEmpty() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);

        replay(extractor, keys);
        assertFalse(entrySet.isEmpty());
        verify(extractor, keys);
    }

    /**
     * Test method for {@link Set#iterator()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIterator() {
        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> values2 = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");

        expect(extractor.getValues("two")).andReturn(values2);
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value2");
        expect(values2.hasMoreElements()).andReturn(true);
        expect(values2.nextElement()).andReturn("value3");
        expect(values2.hasMoreElements()).andReturn(false);

        replay(extractor, keys, values2);
        Iterator<Map.Entry<String, String[]>> entryIt = entrySet.iterator();
        assertTrue(entryIt.hasNext());
        MapEntryArrayValues<String, String> entry = new MapEntryArrayValues<String, String>(
                "two", new String[] { "value2", "value3" }, false);
        assertEquals(entry, entryIt.next());
        verify(extractor, keys, values2);
    }

    /**
     * Test method for {@link Set#iterator()}.
     */
    @SuppressWarnings("unchecked")
    @Test(expected=UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);

        try {
            replay(extractor, keys);
            entrySet.iterator().remove();
        } finally {
            verify(extractor, keys);
        }
    }

    /**
     * Tests {@link Set#remove(Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testRemove() {
        entrySet.remove(null);
    }

    /**
     * Tests {@link Set#removeAll(java.util.Collection)}
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testRemoveAll() {
        entrySet.removeAll(null);
    }

    /**
     * Tests {@link Set#retainAll(java.util.Collection)}
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testRetainAll() {
        entrySet.retainAll(null);
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
        assertEquals(2, entrySet.size());
        verify(extractor, keys);
    }

    /**
     * Test method for {@link Set#toArray()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testToArray() {
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

        MapEntryArrayValues<String, String>[] entryArray = new MapEntryArrayValues[2];
        entryArray[0] = new MapEntryArrayValues<String, String>("one", new String[] {"value1"}, false);
        entryArray[1] = new MapEntryArrayValues<String, String>("two", new String[] {"value2", "value3"}, false);

        replay(extractor, keys, values1, values2);
        assertArrayEquals(entryArray, entrySet.toArray());
        verify(extractor, keys, values1, values2);
    }

    /**
     * Test method for {@link Set#toArray(Object[])}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testToArrayTArray() {
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

        MapEntryArrayValues<String, String>[] entryArray = new MapEntryArrayValues[2];
        entryArray[0] = new MapEntryArrayValues<String, String>("one", new String[] {"value1"}, false);
        entryArray[1] = new MapEntryArrayValues<String, String>("two", new String[] {"value2", "value3"}, false);
        MapEntryArrayValues<String, String>[] realArray = new MapEntryArrayValues[2];

        replay(extractor, keys, values1, values2);
        assertArrayEquals(entryArray, entrySet.toArray(realArray));
        verify(extractor, keys, values1, values2);
    }
}
