/**
 *
 */
package org.apache.tiles.request.velocity;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VelocityScopeMap}.
 *
 * @version $Rev$ $Date$
 */
public class VelocityScopeMapTest {

    private Context request;

    private VelocityScopeMap map;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(Context.class);
        map = new VelocityScopeMap(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.VelocityScopeMap#containsKey(java.lang.Object)}.
     */
    @Test
    public void testContainsKey() {
        expect(request.containsKey("key")).andReturn(true);

        replay(request);
        assertTrue(map.containsKey("key"));
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.VelocityScopeMap#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        expect(request.getKeys()).andReturn(new Object[0]);

        replay(request);
        assertTrue(map.isEmpty());
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.VelocityScopeMap#isEmpty()}.
     */
    @Test
    public void testIsEmptyFalse() {
        expect(request.getKeys()).andReturn(new Object[] {"one", "two"});

        replay(request);
        assertFalse(map.isEmpty());
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.VelocityScopeMap#keySet()}.
     */
    @Test
    public void testKeySet() {
        expect(request.getKeys()).andReturn(new Object[] {"one", "two"});

        replay(request);
        Set<String> set = map.keySet();
        assertEquals(2, set.size());
        assertTrue(set.contains("one"));
        assertTrue(set.contains("two"));
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.VelocityScopeMap#size()}.
     */
    @Test
    public void testSize() {
        expect(request.getKeys()).andReturn(new Object[] {"one", "two"});

        replay(request);
        assertEquals(2, map.size());
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.VelocityScopeMap#put(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testPutStringObject() {
        expect(request.put("key", "value")).andReturn("oldValue");

        replay(request);
        assertEquals("oldValue", map.put("key", "value"));
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.VelocityScopeMap#remove(java.lang.Object)}.
     */
    @Test
    public void testRemoveObject() {
        expect(request.remove("key")).andReturn("value");

        replay(request);
        assertEquals("value", map.remove("key"));
        verify(request);
    }
}
