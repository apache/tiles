/**
 *
 */
package org.apache.tiles.request.collection;

import static org.junit.Assert.*;

import org.apache.tiles.request.collection.MapEntry;
import org.junit.Test;

/**
 * Tests {@link MapEntry}.
 *
 * @version $Rev$ $Date$
 */
public class MapEntryTest {

    /**
     * Test method for {@link org.apache.tiles.request.collection.MapEntry#hashCode()}.
     */
    @Test
    public void testHashCode() {
        MapEntry<String, String> entry = new MapEntry<String, String>("key", "value", false);
        assertEquals("key".hashCode() ^ "value".hashCode(), entry.hashCode());
        entry = new MapEntry<String, String>(null, "value", false);
        assertEquals(0 ^ "value".hashCode(), entry.hashCode());
        entry = new MapEntry<String, String>("key", null, false);
        assertEquals("key".hashCode() ^ 0, entry.hashCode());
        entry = new MapEntry<String, String>(null, null, false);
        assertEquals(0 ^ 0, entry.hashCode());
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.MapEntry#getKey()}.
     */
    @Test
    public void testGetKey() {
        MapEntry<String, String> entry = new MapEntry<String, String>("key", "value", false);
        assertEquals("key", entry.getKey());
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.MapEntry#getValue()}.
     */
    @Test
    public void testGetValue() {
        MapEntry<String, String> entry = new MapEntry<String, String>("key", "value", false);
        assertEquals("value", entry.getValue());
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.MapEntry#setValue(java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        MapEntry<String, String> entry = new MapEntry<String, String>("key", "value", true);
        assertEquals("value", entry.getValue());
        entry.setValue("value2");
        assertEquals("value2", entry.getValue());
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.MapEntry#setValue(java.lang.Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testSetValueException() {
        MapEntry<String, String> entry = new MapEntry<String, String>("key", "value", false);
        assertEquals("value", entry.getValue());
        entry.setValue("value2");
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.MapEntry#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        MapEntry<String, String> entry = new MapEntry<String, String>("key", "value", false);
        assertFalse(entry.equals(null));
        assertFalse(entry.equals("whatever"));
        MapEntry<String, String> entry2 = new MapEntry<String, String>("key", "value", false);
        assertTrue(entry.equals(entry2));
        entry2 = new MapEntry<String, String>("key2", "value", false);
        assertFalse(entry.equals(entry2));
        entry2 = new MapEntry<String, String>("key", "value2", false);
        assertFalse(entry.equals(entry2));
        entry = new MapEntry<String, String>(null, "value", false);
        entry2 = new MapEntry<String, String>(null, "value", false);
        assertTrue(entry.equals(entry2));
        entry = new MapEntry<String, String>("key", null, false);
        entry2 = new MapEntry<String, String>("key", null, false);
        assertTrue(entry.equals(entry2));
    }

}
