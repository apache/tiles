/**
 *
 */
package org.apache.tiles.request.portlet.extractor;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.portlet.PortletContext;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link InitParameterExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class InitParameterExtractorTest {

    private PortletContext context;

    private InitParameterExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        context = createMock(PortletContext.class);
        extractor = new InitParameterExtractor(context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.InitParameterExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(context.getInitParameterNames()).andReturn(keys);

        replay(context, keys);
        assertEquals(keys, extractor.getKeys());
        verify(context, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.InitParameterExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(context.getInitParameter("name")).andReturn("value");

        replay(context);
        assertEquals("value", extractor.getValue("name"));
        verify(context);
    }

}
