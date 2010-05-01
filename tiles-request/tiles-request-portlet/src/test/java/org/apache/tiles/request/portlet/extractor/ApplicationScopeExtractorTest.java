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
 * Tests {@link ApplicationScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class ApplicationScopeExtractorTest {

    private PortletContext context;

    private ApplicationScopeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        context = createMock(PortletContext.class);
        extractor = new ApplicationScopeExtractor(context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.ApplicationScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        context.setAttribute("attribute", "value");

        replay(context);
        extractor.setValue("attribute", "value");
        verify(context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.ApplicationScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValue() {
        context.removeAttribute("attribute");

        replay(context);
        extractor.removeValue("attribute");
        verify(context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.ApplicationScopeExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Enumeration<String> keys = createMock(Enumeration.class);
        expect(context.getAttributeNames()).andReturn(keys);

        replay(context, keys);
        assertEquals(keys, extractor.getKeys());
        verify(context, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.ApplicationScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(context.getAttribute("attribute")).andReturn("value");

        replay(context);
        assertEquals("value", extractor.getValue("attribute"));
        verify(context);
    }

}
