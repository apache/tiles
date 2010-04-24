/**
 *
 */
package org.apache.tiles.request.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link MapEntryArrayValues}.
 *
 * @version $Rev$ $Date$
 */
public class MapEntryArrayValuesTest {

    /**
     * Test method for {@link org.apache.tiles.request.util.MapEntryArrayValues#hashCode()}.
     */
    @Test
    public void testHashCode() {
        MapEntryArrayValues<String, String> entry = new MapEntryArrayValues<String, String>(
                "key", new String[] { "value1", "value2" }, false);
        assertEquals("key".hashCode() ^ ("value1".hashCode() + "value2".hashCode()), entry.hashCode());
        entry = new MapEntryArrayValues<String, String>(
                null, new String[] { "value1", "value2" }, false);
        assertEquals(0 ^ ("value1".hashCode() + "value2".hashCode()), entry.hashCode());
        entry = new MapEntryArrayValues<String, String>(
                "key", null, false);
        assertEquals("key".hashCode() ^ 0, entry.hashCode());
        entry = new MapEntryArrayValues<String, String>(
                null, null, false);
        assertEquals(0 ^ 0, entry.hashCode());
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.MapEntryArrayValues#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        MapEntryArrayValues<String, String> entry = new MapEntryArrayValues<String, String>(
                "key", new String[] { "value1", "value2" }, false);
        assertFalse(entry.equals(null));
        assertFalse(entry.equals("whatever"));
        MapEntryArrayValues<String, String> entry2 = new MapEntryArrayValues<String, String>(
                "key", new String[] { "value1", "value2" }, false);
        assertTrue(entry.equals(entry2));
        entry2 = new MapEntryArrayValues<String, String>(
                "key", null, false);
        assertFalse(entry.equals(entry2));
        entry2 = new MapEntryArrayValues<String, String>("key2", new String[] {
                "value1", "value2" }, false);
        assertFalse(entry.equals(entry2));
        entry2 = new MapEntryArrayValues<String, String>("key", new String[] {
                "value1", "value3" }, false);
        assertFalse(entry.equals(entry2));
        entry = new MapEntryArrayValues<String, String>(null, new String[] {
                "value1", "value2" }, false);
        entry2 = new MapEntryArrayValues<String, String>(null, new String[] {
                "value1", "value2" }, false);
        assertTrue(entry.equals(entry2));
        entry = new MapEntryArrayValues<String, String>("key", null, false);
        entry2 = new MapEntryArrayValues<String, String>("key", null, false);
        assertTrue(entry.equals(entry2));
        entry2 = new MapEntryArrayValues<String, String>("key", new String[] {
                "value1", "value2" }, false);
        assertFalse(entry.equals(entry2));
        entry = new MapEntryArrayValues<String, String>(null, new String[] {
                null, "value2" }, false);
        entry2 = new MapEntryArrayValues<String, String>(null, new String[] {
                null, "value2" }, false);
        assertTrue(entry.equals(entry2));
        entry2 = new MapEntryArrayValues<String, String>(null, new String[] {
                "value1", "value2" }, false);
        assertFalse(entry.equals(entry2));
    }

}
