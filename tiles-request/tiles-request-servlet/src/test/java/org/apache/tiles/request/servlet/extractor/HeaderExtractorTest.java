/**
 *
 */
package org.apache.tiles.request.servlet.extractor;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link HeaderExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class HeaderExtractorTest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HeaderExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        extractor = new HeaderExtractor(request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.HeaderExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(request.getHeaderNames()).andReturn(keys);

        replay(request, response, keys);
        assertEquals(keys, extractor.getKeys());
        verify(request, response, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.HeaderExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(request.getHeader("name")).andReturn("value");

        replay(request, response);
        assertEquals("value", extractor.getValue("name"));
        verify(request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.HeaderExtractor#getValues(java.lang.String)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetValues() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(request.getHeaders("name")).andReturn(keys);

        replay(request, response, keys);
        assertEquals(keys, extractor.getValues("name"));
        verify(request, response, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.HeaderExtractor#setValue(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testSetValue() {
        response.setHeader("name", "value");

        replay(request, response);
        extractor.setValue("name", "value");
        verify(request, response);
    }

}
