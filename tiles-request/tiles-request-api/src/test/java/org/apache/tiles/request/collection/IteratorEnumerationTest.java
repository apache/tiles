/**
 *
 */
package org.apache.tiles.request.collection;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link IteratorEnumeration}.
 *
 * @version $Rev$ $Date$
 */
public class IteratorEnumerationTest {

    private Iterator<Integer> iterator;

    private IteratorEnumeration<Integer> enumeration;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        iterator = createMock(Iterator.class);
        enumeration = new IteratorEnumeration<Integer>(iterator);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.IteratorEnumeration#hasMoreElements()}.
     */
    @Test
    public void testHasMoreElements() {
        expect(iterator.hasNext()).andReturn(true);

        replay(iterator);
        assertTrue(enumeration.hasMoreElements());
        verify(iterator);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.IteratorEnumeration#nextElement()}.
     */
    @Test
    public void testNextElement() {
        expect(iterator.next()).andReturn(1);

        replay(iterator);
        assertEquals(new Integer(1), enumeration.nextElement());
        verify(iterator);
    }

    /**
     * Test method for {@link org.apache.tiles.request.collection.IteratorEnumeration#getIterator()}.
     */
    @Test
    public void testGetIterator() {
        replay(iterator);
        assertEquals(iterator, enumeration.getIterator());
        verify(iterator);
    }

}
