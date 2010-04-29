/**
 *
 */
package org.apache.tiles.request.servlet.extractor;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link SessionScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class SessionScopeExtractorTest {

    private HttpServletRequest request;

    private HttpSession session;

    private SessionScopeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(HttpServletRequest.class);
        session = createMock(HttpSession.class);
        extractor = new SessionScopeExtractor(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.SessionScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        expect(request.getSession()).andReturn(session);
        session.setAttribute("name", "value");

        replay(request, session);
        extractor.setValue("name", "value");
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.SessionScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValue() {
        expect(request.getSession(false)).andReturn(session);
        session.removeAttribute("name");

        replay(request, session);
        extractor.removeValue("name");
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.SessionScopeExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(request.getSession(false)).andReturn(session);
        expect(session.getAttributeNames()).andReturn(keys);

        replay(request, session, keys);
        assertEquals(keys, extractor.getKeys());
        verify(request, session, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.SessionScopeExtractor#getKeys()}.
     */
    @Test
    public void testGetKeysNoSession() {
        expect(request.getSession(false)).andReturn(null);

        replay(request, session);
        assertNull(extractor.getKeys());
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.SessionScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(request.getSession(false)).andReturn(session);
        expect(session.getAttribute("name")).andReturn("value");

        replay(request, session);
        assertEquals("value", extractor.getValue("name"));
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.extractor.SessionScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValueNoSession() {
        expect(request.getSession(false)).andReturn(null);

        replay(request, session);
        assertNull(extractor.getValue("name"));
        verify(request, session);
    }

}
