/**
 *
 */
package org.apache.tiles.request.portlet.extractor;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link SessionScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class SessionScopeExtractorTest {

    private PortletRequest request;

    private PortletSession session;

    private SessionScopeExtractor extractor;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(PortletRequest.class);
        session = createMock(PortletSession.class);
        extractor = new SessionScopeExtractor(request, PortletSession.PORTLET_SCOPE);
    }


    /**
     * Tests {@link SessionScopeExtractor#SessionScopeExtractor(PortletRequest, int)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalScope() {
        replay(request, session);
        new SessionScopeExtractor(request, 0);
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.SessionScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        expect(request.getPortletSession()).andReturn(session);
        session.setAttribute("name", "value", PortletSession.PORTLET_SCOPE);

        replay(request, session);
        extractor.setValue("name", "value");
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.SessionScopeExtractor#removeValue(java.lang.String)}.
     */
    @Test
    public void testRemoveValue() {
        expect(request.getPortletSession(false)).andReturn(session);
        session.removeAttribute("name", PortletSession.PORTLET_SCOPE);

        replay(request, session);
        extractor.removeValue("name");
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.SessionScopeExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Enumeration<String> keys = createMock(Enumeration.class);

        expect(request.getPortletSession(false)).andReturn(session);
        expect(session.getAttributeNames(PortletSession.PORTLET_SCOPE)).andReturn(keys);

        replay(request, session, keys);
        assertEquals(keys, extractor.getKeys());
        verify(request, session, keys);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.SessionScopeExtractor#getKeys()}.
     */
    @Test
    public void testGetKeysNoSession() {
        expect(request.getPortletSession(false)).andReturn(null);

        replay(request, session);
        assertNull(extractor.getKeys());
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.SessionScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        expect(request.getPortletSession(false)).andReturn(session);
        expect(session.getAttribute("name", PortletSession.PORTLET_SCOPE)).andReturn("value");

        replay(request, session);
        assertEquals("value", extractor.getValue("name"));
        verify(request, session);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.SessionScopeExtractor#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValueNoSession() {
        expect(request.getPortletSession(false)).andReturn(null);

        replay(request, session);
        assertNull(extractor.getValue("name"));
        verify(request, session);
    }

}
