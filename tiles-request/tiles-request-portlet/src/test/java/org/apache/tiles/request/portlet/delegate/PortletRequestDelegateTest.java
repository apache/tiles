/**
 *
 */
package org.apache.tiles.request.portlet.delegate;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.tiles.request.collection.ReadOnlyEnumerationMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link PortletRequestDelegate}.
 *
 * @version $Rev$ $Date$
 */
public class PortletRequestDelegateTest {

    private PortletRequest request;

    private PortletRequestDelegate delegate;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(PortletRequest.class);
        delegate = new PortletRequestDelegate(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.PortletRequestDelegate#getParam()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetParam() {
        replay(request);
        assertTrue(delegate.getParam() instanceof ReadOnlyEnumerationMap);
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.PortletRequestDelegate#getParamValues()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetParamValues() {
        Map<String, String[]> params = createMock(Map.class);

        expect(request.getParameterMap()).andReturn(params);

        replay(request, params);
        assertEquals(params, delegate.getParamValues());
        verify(request, params);
    }

}
