/**
 *
 */
package org.apache.tiles.request.portlet.delegate;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.portlet.MimeResponse;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link MimeResponseDelegate}.
 *
 * @version $Rev$ $Date$
 */
public class MimeResponseDelegateTest {

    private MimeResponse response;

    private MimeResponseDelegate delegate;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        response = createMock(MimeResponse.class);
        delegate = new MimeResponseDelegate(response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.MimeResponseDelegate#getOutputStream()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetOutputStream() throws IOException {
        OutputStream os = createMock(OutputStream.class);

        expect(response.getPortletOutputStream()).andReturn(os);

        replay(response, os);
        assertEquals(os, delegate.getOutputStream());
        verify(response, os);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.MimeResponseDelegate#getPrintWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetPrintWriter() throws IOException {
        PrintWriter os = createMock(PrintWriter.class);

        expect(response.getWriter()).andReturn(os);

        replay(response, os);
        assertEquals(os, delegate.getPrintWriter());
        verify(response, os);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.MimeResponseDelegate#getWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetWriter() throws IOException {
        PrintWriter os = createMock(PrintWriter.class);

        expect(response.getWriter()).andReturn(os);

        replay(response, os);
        assertEquals(os, delegate.getWriter());
        verify(response, os);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.MimeResponseDelegate#isResponseCommitted()}.
     */
    @Test
    public void testIsResponseCommitted() {
        expect(response.isCommitted()).andReturn(true);

        replay(response);
        assertTrue(delegate.isResponseCommitted());
        verify(response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.MimeResponseDelegate#setContentType(java.lang.String)}.
     */
    @Test
    public void testSetContentType() {
        response.setContentType("text/html");

        replay(response);
        delegate.setContentType("text/html");
        verify(response);
    }

}
