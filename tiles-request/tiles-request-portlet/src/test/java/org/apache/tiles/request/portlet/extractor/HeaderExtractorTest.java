/**
 *
 */
package org.apache.tiles.request.portlet.extractor;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link HeaderExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class HeaderExtractorTest {

    private PortletRequest request;

    private PortletResponse response;

    private HeaderExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(PortletRequest.class);
        response = createMock(PortletResponse.class);
        extractor = new HeaderExtractor(request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.HeaderExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(request.getPropertyNames()).andReturn(keys);

        replay(request, response, keys);
        assertEquals(keys, extractor.getKeys());
        verify(request, response, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.HeaderExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(request.getProperty("name")).andReturn("value");

        replay(request, response);
        assertEquals("value", extractor.getValue("name"));
        verify(request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.HeaderExtractor#getValues(java.lang.String)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetValues() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(request.getProperties("name")).andReturn(keys);

        replay(request, response, keys);
        assertEquals(keys, extractor.getValues("name"));
        verify(request, response, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.HeaderExtractor#setValue(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testSetValue() {
        response.setProperty("name", "value");

        replay(request, response);
        extractor.setValue("name", "value");
        verify(request, response);
    }

}
