/**
 *
 */
package org.apache.tiles.mvel;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.tiles.mvel.ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ReadOnlyVariableResolver}.
 *
 * @version $Rev$ $Date$
 */
public class ReadOnlyVariableResolverTest {

    private ReadOnlyVariableResolver resolver;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        resolver = createMockBuilder(ReadOnlyVariableResolver.class).withConstructor("name").createMock();
    }

    /**
     * Test method for {@link org.apache.tiles.mvel.ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver#getFlags()}.
     */
    @Test
    public void testGetFlags() {
        replay(resolver);
        assertEquals(0, resolver.getFlags());
        verify(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.mvel.ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver#getName()}.
     */
    @Test
    public void testGetName() {
        replay(resolver);
        assertEquals("name", resolver.getName());
        verify(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.mvel.ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver#setStaticType(java.lang.Class)}.
     */
    @Test
    public void testSetStaticType() {
        replay(resolver);
        resolver.setStaticType(Object.class);
        verify(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.mvel.ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver#setValue(java.lang.Object)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testSetValue() {
        replay(resolver);
        resolver.setValue("whatever");
        verify(resolver);
    }

}
