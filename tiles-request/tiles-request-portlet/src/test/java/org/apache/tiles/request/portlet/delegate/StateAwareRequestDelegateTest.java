/**
 *
 */
package org.apache.tiles.request.portlet.delegate;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.StateAwareResponse;

import org.apache.tiles.request.collection.AddableParameterMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link StateAwareRequestDelegate}.
 *
 * @version $Rev$ $Date$
 */
public class StateAwareRequestDelegateTest {

    private PortletRequest request;

    private StateAwareResponse response;

    private StateAwareRequestDelegate delegate;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        request = createMock(PortletRequest.class);
        response = createMock(StateAwareResponse.class);
        delegate = new StateAwareRequestDelegate(request, response);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareRequestDelegate#getParam()}.
     */
    @Test
    public void testGetParam() {
        replay(request);
        assertTrue(delegate.getParam() instanceof AddableParameterMap);
        verify(request);
    }

    /**
     * Test method for {@link org.apache.tiles.request.portlet.delegate.StateAwareRequestDelegate#getParamValues()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetParamValues() {
        Map<String, String[]> requestMap = createMock(Map.class);
        Map<String, String[]> responseMap = createMock(Map.class);

        expect(request.getParameterMap()).andReturn(requestMap);
        expect(response.getRenderParameterMap()).andReturn(responseMap);

        replay(request);
        assertTrue(delegate.getParamValues() instanceof StateAwareParameterMap);
        verify(request);
    }
}
