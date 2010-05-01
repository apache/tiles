/**
 *
 */
package org.apache.tiles.request.portlet.extractor;

import static org.easymock.classextension.EasyMock.*;

import javax.portlet.PortletRequest;
import javax.portlet.StateAwareResponse;

import org.junit.Test;

/**
 * Tests {@link StateAwareParameterExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class StateAwareParameterExtractorTest {

    /**
     * Test method for {@link org.apache.tiles.request.portlet.extractor.StateAwareParameterExtractor#setValue(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testSetValue() {
        PortletRequest request = createMock(PortletRequest.class);
        StateAwareResponse response = createMock(StateAwareResponse.class);

        response.setRenderParameter("name", "value");

        replay(request, response);
        StateAwareParameterExtractor extractor = new StateAwareParameterExtractor(request, response);
        extractor.setValue("name", "value");
        verify(request, response);
    }

}
