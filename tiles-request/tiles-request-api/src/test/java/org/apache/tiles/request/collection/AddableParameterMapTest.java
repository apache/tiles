/**
 *
 */
package org.apache.tiles.request.collection;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.request.collection.extractor.HasAddableKeys;
import org.apache.tiles.request.util.MapEntry;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AddableParameterMap}.
 *
 * @version $Rev$ $Date$
 */
public class AddableParameterMapTest {

    private AddableParameterMap map;

    private HasAddableKeys<String> extractor;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        extractor = createMock(HasAddableKeys.class);
        map = new AddableParameterMap(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.AddableParameterMap#entrySet()}.
     */
    @Test
    public void testEntrySet() {
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        MapEntry<String, String> entry1 = new MapEntry<String, String>("one", "value1", false);
        MapEntry<String, String> entry2 = new MapEntry<String, String>("two", "value2", false);
        List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String,String>>(2);
        entries.add(entry1);
        entries.add(entry2);

        extractor.setValue("one", "value1");
        expectLastCall().times(2);
        extractor.setValue("two", "value2");
        replay(extractor);
        entrySet.add(entry1);
        entrySet.addAll(entries);
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.AddableParameterMap#put(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testPut() {
        expect(extractor.getValue("one")).andReturn(null);
        extractor.setValue("one", "value1");

        replay(extractor);
        assertNull(map.put("one", "value1"));
        verify(extractor);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.AddableParameterMap#putAll(java.util.Map)}.
     */
    @Test
    public void testPutAll() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("one", "value1");
        map.put("two", "value2");

        extractor.setValue("one", "value1");
        extractor.setValue("two", "value2");

        replay(extractor);
        this.map.putAll(map);
        verify(extractor);
    }
}
