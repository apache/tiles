package org.apache.tiles.request.collection;


import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.attribute.AttributeExtractor;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ScopeMap#entrySet()}
 *
 * @version $Rev$ $Date$
 */
public class ScopeMapEntrySetTest {

    private ScopeMap map;

    private AttributeExtractor extractor;

    private Set<Map.Entry<String, Object>> entrySet;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        extractor = createMock(AttributeExtractor.class);
        map = new ScopeMap(extractor);
        entrySet = map.entrySet();
    }

    /**
     * Tests {@link Set#add(Object)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAdd() {
        Map.Entry<String, Object> entry = createMock(Map.Entry.class);

        expect(entry.getKey()).andReturn("one");
        expect(entry.getValue()).andReturn(1);
        expect(extractor.getValue("one")).andReturn(null);

        extractor.setValue("one", 1);

        replay(extractor, entry);
        assertTrue(entrySet.add(entry));
        verify(extractor, entry);
    }

    /**
     * Tests {@link Set#add(Object)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddNoEffect() {
        Map.Entry<String, Object> entry = createMock(Map.Entry.class);

        expect(entry.getKey()).andReturn("one");
        expect(entry.getValue()).andReturn(1);
        expect(extractor.getValue("one")).andReturn(1);

        replay(extractor, entry);
        assertFalse(entrySet.add(entry));
        verify(extractor, entry);
    }

    /**
     * Tests {@link Set#addAll(java.util.Collection)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddAll() {
        Map.Entry<String, Object> entry1 = createMock(Map.Entry.class);
        Map.Entry<String, Object> entry2 = createMock(Map.Entry.class);

        expect(entry1.getKey()).andReturn("one");
        expect(entry1.getValue()).andReturn(1);
        expect(entry2.getKey()).andReturn("two");
        expect(entry2.getValue()).andReturn(2);
        expect(extractor.getValue("one")).andReturn(null);
        expect(extractor.getValue("two")).andReturn(null);

        extractor.setValue("one", 1);
        extractor.setValue("two", 2);

        replay(extractor, entry1, entry2);
        List<Map.Entry<String, Object>> coll = new ArrayList<Map.Entry<String,Object>>();
        coll.add(entry1);
        coll.add(entry2);
        assertTrue(entrySet.addAll(coll));
        verify(extractor, entry1, entry2);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ScopeMap#clear()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClear() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");
        expect(keys.hasMoreElements()).andReturn(false);

        extractor.removeValue("one");
        extractor.removeValue("two");

        replay(extractor, keys);
        entrySet.clear();
        verify(extractor, keys);
    }

    /**
     * Tests {@link Set#remove(Object)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRemove() {
        Map.Entry<String, Object> entry = createMock(Map.Entry.class);

        expect(entry.getKey()).andReturn("one");
        expect(entry.getValue()).andReturn(1);
        expect(extractor.getValue("one")).andReturn(1);
        extractor.removeValue("one");

        replay(extractor, entry);
        assertTrue(entrySet.remove(entry));
        verify(extractor, entry);
    }

    /**
     * Tests {@link Set#remove(Object)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveNoEffect() {
        Map.Entry<String, Object> entry = createMock(Map.Entry.class);

        expect(entry.getKey()).andReturn("one");
        expect(extractor.getValue("one")).andReturn(null);

        replay(extractor, entry);
        assertFalse(entrySet.remove(entry));
        verify(extractor, entry);
    }

    /**
     * Tests {@link Set#addAll(java.util.Collection)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveAll() {
        Map.Entry<String, Object> entry1 = createMock(Map.Entry.class);
        Map.Entry<String, Object> entry2 = createMock(Map.Entry.class);

        expect(entry1.getKey()).andReturn("one");
        expect(entry1.getValue()).andReturn(1);
        expect(entry2.getKey()).andReturn("two");
        expect(entry2.getValue()).andReturn(2);
        expect(extractor.getValue("one")).andReturn(1);
        expect(extractor.getValue("two")).andReturn(2);
        extractor.removeValue("one");
        extractor.removeValue("two");

        replay(extractor, entry1, entry2);
        List<Map.Entry<String, Object>> coll = new ArrayList<Map.Entry<String,Object>>();
        coll.add(entry1);
        coll.add(entry2);
        assertTrue(entrySet.removeAll(coll));
        verify(extractor, entry1, entry2);
    }

    /**
     * Tests {@link Set#addAll(java.util.Collection)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRetainAll() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(extractor.getKeys()).andReturn(keys);
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("one");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("two");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("three");
        expect(keys.hasMoreElements()).andReturn(false);

        Map.Entry<String, Object> entry1 = new MapEntry<String, Object>("one", 1, false);
        Map.Entry<String, Object> entry2 = new MapEntry<String, Object>("two", 2, false);

        expect(extractor.getValue("one")).andReturn(1);
        expect(extractor.getValue("two")).andReturn(3);
        expect(extractor.getValue("three")).andReturn(4);
        extractor.removeValue("two");
        extractor.removeValue("three");

        replay(extractor, keys);
        List<Map.Entry<String, Object>> coll = new ArrayList<Map.Entry<String,Object>>();
        coll.add(entry1);
        coll.add(entry2);
        assertTrue(entrySet.retainAll(coll));
        verify(extractor, keys);
    }
}
