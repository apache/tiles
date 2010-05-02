/**
 *
 */
package org.apache.tiles.request.portlet.delegate;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link StateAwareResponseDelegate}.
 *
 * @version $Rev$ $Date$
 */
public class StateAwareResponseDelegateTest {

    private StateAwareResponseDelegate delegate;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        delegate = new StateAwareResponseDelegate();
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate#getOutputStream()}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testGetOutputStream() {
        delegate.getOutputStream();
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate#getPrintWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testGetPrintWriter() {
        delegate.getPrintWriter();
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate#getWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testGetWriter() {
        delegate.getWriter();
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate#isResponseCommitted()}.
     */
    @Test
    public void testIsResponseCommitted() {
        assertFalse(delegate.isResponseCommitted());
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate#setContentType(java.lang.String)}.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testSetContentType() {
        delegate.setContentType("text/html");
    }
}
