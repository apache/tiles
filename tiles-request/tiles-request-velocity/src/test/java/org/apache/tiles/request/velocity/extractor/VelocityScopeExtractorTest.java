/**
 *
 */
package org.apache.tiles.request.velocity.extractor;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VelocityScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class VelocityScopeExtractorTest {

    private Context request;

    private VelocityScopeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(Context.class);
        extractor = new VelocityScopeExtractor(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.extractor.VelocityScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValue() {
        expect(request.remove("key")).andReturn("value");

        replay(request);
        extractor.removeValue("key");
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.extractor.VelocityScopeExtractor#getKeys()}.
     */
    @Test
    public void testGetKeys() {
        expect(request.getKeys()).andReturn(new Object[] {"one", "two"});

        replay(request);
        Enumeration<String> keys = extractor.getKeys();
        assertTrue(keys.hasMoreElements());
        assertEquals("one", keys.nextElement());
        assertTrue(keys.hasMoreElements());
        assertEquals("two", keys.nextElement());
        assertFalse(keys.hasMoreElements());
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.extractor.VelocityScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(request.get("key")).andReturn("value");

        replay(request);
        assertEquals("value", extractor.getValue("key"));
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.velocity.extractor.VelocityScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        expect(request.put("key", "value")).andReturn(null);

        replay(request);
        extractor.setValue("key", "value");
        verify(request);
    }
}
