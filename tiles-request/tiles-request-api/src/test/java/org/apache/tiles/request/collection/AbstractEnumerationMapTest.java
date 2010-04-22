/**
 *
 */
package org.apache.tiles.request.collection;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import org.apache.tiles.request.collection.extractor.HasKeys;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AbstractEnumerationMap}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractEnumerationMapTest {

    private HasKeys<Integer> request;

    private AbstractEnumerationMap<Integer> map;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        request = createMock(HasKeys.class);
        map = createMockBuilder(AbstractEnumerationMap.class).withConstructor(
                request).createMock();
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.AbstractEnumerationMap#hashCode()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testHashCode() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(request.getKeys()).andReturn(keys);

        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("first");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("second");
        expect(keys.hasMoreElements()).andReturn(false);

        Integer value1 = 1;

        expect(request.getValue("first")).andReturn(value1);
        expect(request.getValue("second")).andReturn(null);

        replay(request, map, keys);
        assertEquals(("first".hashCode() ^ value1.hashCode())
                + ("second".hashCode() ^ 0), map.hashCode());
        verify(request, map, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.AbstractEnumerationMap#equals(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEqualsObject() {
        HasKeys<Integer> otherRequest = createMock(HasKeys.class);
        AbstractEnumerationMap<Integer> otherMap = createMockBuilder(
                AbstractEnumerationMap.class).withConstructor(otherRequest)
                .createMock();
        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> otherKeys = createMock(Enumeration.class);

        expect(request.getKeys()).andReturn(keys);
        expect(otherRequest.getKeys()).andReturn(otherKeys);

        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("first");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("second");
        expect(keys.hasMoreElements()).andReturn(false);

        expect(request.getValue("first")).andReturn(1);
        expect(request.getValue("second")).andReturn(2);

        expect(otherKeys.hasMoreElements()).andReturn(true);
        expect(otherKeys.nextElement()).andReturn("first");
        expect(otherKeys.hasMoreElements()).andReturn(true);
        expect(otherKeys.nextElement()).andReturn("second");
        expect(otherKeys.hasMoreElements()).andReturn(false);

        expect(otherRequest.getValue("first")).andReturn(1);
        expect(otherRequest.getValue("second")).andReturn(2);

        replay(request, map, otherRequest, otherMap, keys, otherKeys);
        assertTrue(map.equals(otherMap));
        verify(request, map, otherRequest, otherMap, keys, otherKeys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.AbstractEnumerationMap#equals(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEqualsObjectFalse() {
        HasKeys<Integer> otherRequest = createMock(HasKeys.class);
        AbstractEnumerationMap<Integer> otherMap = createMockBuilder(
                AbstractEnumerationMap.class).withConstructor(otherRequest)
                .createMock();
        Enumeration<String> keys = createMock(Enumeration.class);
        Enumeration<String> otherKeys = createMock(Enumeration.class);

        expect(request.getKeys()).andReturn(keys);
        expect(otherRequest.getKeys()).andReturn(otherKeys);

        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("first");
        expect(keys.hasMoreElements()).andReturn(true);
        expect(keys.nextElement()).andReturn("second");
        expect(keys.hasMoreElements()).andReturn(false);

        expect(request.getValue("first")).andReturn(1);
        expect(request.getValue("second")).andReturn(2);

        expect(otherKeys.hasMoreElements()).andReturn(true);
        expect(otherKeys.nextElement()).andReturn("first");
        expect(otherKeys.hasMoreElements()).andReturn(true);
        expect(otherKeys.nextElement()).andReturn("second");
        expect(otherKeys.hasMoreElements()).andReturn(false);

        expect(otherRequest.getValue("first")).andReturn(1);
        expect(otherRequest.getValue("second")).andReturn(3);

        replay(request, map, otherRequest, otherMap, keys, otherKeys);
        assertFalse(map.equals(otherMap));
        verify(request, map, otherRequest, otherMap, keys, otherKeys);
    }
}
