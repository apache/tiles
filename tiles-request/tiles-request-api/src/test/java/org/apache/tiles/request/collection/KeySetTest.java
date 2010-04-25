package org.apache.tiles.request.collection;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.tiles.request.collection.extractor.HasKeys;
import org.junit.Before;
import org.junit.Test;

public class KeySetTest {


    private HasKeys<Integer> extractor;

    private Set<String> entrySet;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        extractor = createMock(HasKeys.class);
        entrySet = new KeySet(extractor);
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
    @Test
    public void testContains() {
        expect(extractor.getValue("one")).andReturn(1);

        replay(extractor);
        assertTrue(entrySet.contains("one"));
        verify(extractor);
    }

    /**
     * Tests {@link Set#contains(Object)}.
     */
    @Test
    public void testContainsFalse() {
        expect(extractor.getValue("one")).andReturn(null);

        replay(extractor);
        assertFalse(entrySet.contains("one"));
        verify(extractor);
    }

    /**
     * Tests {@link Set#containsAll(Object)}.
     */
    @Test
    public void testContainsAll() {
        expect(extractor.getValue("one")).andReturn(1);
        expect(extractor.getValue("two")).andReturn(1);

        replay(extractor);
        List<String> coll = new ArrayList<String>();
        coll.add("one");
        coll.add("two");
        assertTrue(entrySet.containsAll(coll));
        verify(extractor);
    }

    /**
     * Tests {@link Set#containsAll(Object)}.
     */
    @Test
    public void testContainsAllFalse() {
        expect(extractor.getValue("one")).andReturn(1);
        expect(extractor.getValue("two")).andReturn(null);

        replay(extractor);
        List<String> coll = new ArrayList<String>();
        coll.add("one");
        coll.add("two");
        assertFalse(entrySet.containsAll(coll));
        verify(extractor);
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
     * Test method for {@link Set#isEmpty()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsEmptyTrue() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(false);

        replay(extractor, keys);
        assertTrue(entrySet.isEmpty());
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

        replay(extractor, keys, values2);
        Iterator<String> entryIt = entrySet.iterator();
        assertTrue(entryIt.hasNext());
        assertEquals("two", entryIt.next());
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
        replay(extractor, keys, values1, values2);
        assertArrayEquals(new String[] {"one", "two"}, entrySet.toArray());
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

        replay(extractor, keys, values1, values2);
        String[] realArray = new String[2];
        assertArrayEquals(new String[] {"one", "two"}, entrySet.toArray(realArray));
        verify(extractor, keys, values1, values2);
    }
}
