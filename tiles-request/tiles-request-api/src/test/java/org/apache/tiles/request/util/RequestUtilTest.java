/**
 *
 */
package org.apache.tiles.request.util;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import org.junit.Test;

/**
 * Test {@link RequestUtil}.
 *
 * @version $Rev$ $Date$
 */
public class RequestUtilTest {

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestUtil#key(java.lang.Object)}.
     */
    @Test
    public void testKey() {
        assertEquals("1", RequestUtil.key(1));
        assertEquals("hello", RequestUtil.key("hello"));
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestUtil#key(java.lang.Object)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyException() {
        RequestUtil.key(null);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestUtil#enumerationSize(java.util.Enumeration)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEnumerationSize() {
        Enumeration<Object> enumeration = createMock(Enumeration.class);

        expect(enumeration.hasMoreElements()).andReturn(true);
        expect(enumeration.nextElement()).andReturn(1);
        expect(enumeration.hasMoreElements()).andReturn(true);
        expect(enumeration.nextElement()).andReturn(1);
        expect(enumeration.hasMoreElements()).andReturn(false);

        replay(enumeration);
        assertEquals(2, RequestUtil.enumerationSize(enumeration));
        verify(enumeration);
    }

}
