/**
 *
 */
package org.apache.tiles.request.jsp.extractor;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link SessionScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class SessionScopeExtractorTest {

    private PageContext context;

    private HttpSession session;

    private SessionScopeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        context = createMock(PageContext.class);
        session = createMock(HttpSession.class);
        extractor = new SessionScopeExtractor(context);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValue() {
        expect(context.getSession()).andReturn(session);
        context.removeAttribute("key", PageContext.SESSION_SCOPE);

        replay(context, session);
        extractor.removeValue("key");
        verify(context, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        expect(context.getSession()).andReturn(session);
        Enumeration<String> keys = createMock(Enumeration.class);
        expect(context.getAttributeNamesInScope(PageContext.SESSION_SCOPE)).andReturn(keys);

        replay(context, session);
        assertEquals(keys, extractor.getKeys());
        verify(context, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(context.getSession()).andReturn(session);
        expect(context.getAttribute("key", PageContext.SESSION_SCOPE)).andReturn("value");

       replay(context, session);
       assertEquals("value", extractor.getValue("key"));
       verify(context, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        expect(context.getSession()).andReturn(session);
        context.setAttribute("key", "value", PageContext.SESSION_SCOPE);

        replay(context, session);
        extractor.setValue("key", "value");
        verify(context, session);
    }


    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValueNoSession() {
        expect(context.getSession()).andReturn(null);

        replay(context, session);
        extractor.removeValue("key");
        verify(context, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#getKeys()}.
     */
    @Test
    public void testGetKeysNoSession() {
        expect(context.getSession()).andReturn(null);

        replay(context, session);
        assertNull(extractor.getKeys());
        verify(context, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValueNoSession() {
       expect(context.getSession()).andReturn(null);

       replay(context, session);
       assertNull(extractor.getValue("key"));
       verify(context, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.extractor.ScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValueNoSession() {
        expect(context.getSession()).andReturn(null);

        replay(context, session);
        extractor.setValue("key", "value");
        verify(context, session);
    }
}
