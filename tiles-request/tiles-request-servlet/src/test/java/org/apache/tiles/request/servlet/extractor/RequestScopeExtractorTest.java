/**
 *
 */
package org.apache.tiles.request.servlet.extractor;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link RequestScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class RequestScopeExtractorTest {

    private HttpServletRequest request;

    private RequestScopeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(HttpServletRequest.class);
        extractor = new RequestScopeExtractor(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.RequestScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        request.setAttribute("name", "value");

        replay(request);
        extractor.setValue("name", "value");
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.RequestScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValue() {
        request.removeAttribute("name");

        replay(request);
        extractor.removeValue("name");
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.RequestScopeExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(request.getAttributeNames()).andReturn(keys);

        replay(request, keys);
        assertEquals(keys, extractor.getKeys());
        verify(request, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.RequestScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(request.getAttribute("name")).andReturn("value");

        replay(request);
        assertEquals("value", extractor.getValue("name"));
        verify(request);
    }

}
