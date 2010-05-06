/**
 *
 */
package org.apache.tiles.request.jsp.extractor;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class ScopeExtractorTest {

    private JspContext context;

    private ScopeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        context = createMock(JspContext.class);
        extractor = new ScopeExtractor(context, PageContext.PAGE_SCOPE);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValue() {
        context.removeAttribute("key", PageContext.PAGE_SCOPE);

        replay(context);
        extractor.removeValue("key");
        verify(context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Enumeration<String> keys = createMock(Enumeration.class);
        expect(context.getAttributeNamesInScope(PageContext.PAGE_SCOPE)).andReturn(keys);

        replay(context);
        assertEquals(keys, extractor.getKeys());
        verify(context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(context.getAttribute("key", PageContext.PAGE_SCOPE)).andReturn("value");

        replay(context);
        assertEquals("value", extractor.getValue("key"));
        verify(context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        context.setAttribute("key", "value", PageContext.PAGE_SCOPE);

        replay(context);
        extractor.setValue("key", "value");
        verify(context);
    }

}
