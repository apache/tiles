/**
 *
 */
package org.apache.tiles.request;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests {@link AbstractRequest}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractRequestTest {

    /**
     * Test method for {@link org.apache.tiles.request.AbstractRequest#setForceInclude(boolean)}.
     */
    @Test
    public void testSetForceInclude() {
        AbstractRequest request = createMockBuilder(AbstractRequest.class).createMock();
        Map<String, Object> scope = new HashMap<String, Object>();

        expect(request.getContext("request")).andReturn(scope).anyTimes();

        replay(request);
        assertFalse(request.isForceInclude());
        request.setForceInclude(true);
        assertTrue(request.isForceInclude());
        verify(request);
    }
}
