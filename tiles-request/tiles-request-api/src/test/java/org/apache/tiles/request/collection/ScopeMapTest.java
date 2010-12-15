/**
 *
 */
package org.apache.tiles.request.collection;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tiles.request.attribute.AttributeExtractor;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ScopeMap}.
 *
 * @version $Rev$ $Date$
 */
public class ScopeMapTest {

    private ScopeMap map;

    private AttributeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        extractor = createMock(AttributeExtractor.class);
        map = new ScopeMap(extractor);
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
        map.clear();
        verify(extractor, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ScopeMap#keySet()}.
     */
    @Test
    public void testKeySet() {
        replay(extractor);
        assertTrue(map.keySet() instanceof RemovableKeySet);
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ScopeMap#put(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testPutStringObject() {
        expect(extractor.getValue("one")).andReturn(null);
        extractor.setValue("one", 1);

        replay(extractor);
        assertNull(map.put("one", 1));
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ScopeMap#putAll(java.util.Map)}.
     */
    @Test
    public void testPutAllMapOfQextendsStringQextendsObject() {
        Map<String, Object> items = new LinkedHashMap<String, Object>();
        items.put("one", 1);
        items.put("two", 2);

        extractor.setValue("one", 1);
        extractor.setValue("two", 2);

        replay(extractor);
        map.putAll(items);
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.ScopeMap#remove(java.lang.Object)}.
     */
    @Test
    public void testRemoveObject() {
        expect(extractor.getValue("one")).andReturn(1);
        extractor.removeValue("one");

        replay(extractor);
        assertEquals(new Integer(1), map.remove("one"));
        verify(extractor);
    }
}
