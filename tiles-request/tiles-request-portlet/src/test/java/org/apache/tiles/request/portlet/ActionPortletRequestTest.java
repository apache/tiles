/**
 *
 */
package org.apache.tiles.request.portlet;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.StateAwareRequestDelegate;
import org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate;
import org.junit.Test;

/**
 * Tests {@link ActionPortletRequest}.
 *
 * @version $Rev$ $Date$
 */
public class ActionPortletRequestTest {

    /**
     * Test method for {@link org.apache.tiles.request.portlet.ActionPortletRequest#ActionPortletRequest(org.apache.tiles.request.ApplicationContext, javax.portlet.PortletContext, javax.portlet.ActionRequest, javax.portlet.ActionResponse)}.
     * @throws NoSuchFieldException If something goes wrong.
     * @throws SecurityException If something goes wrong.
     * @throws IllegalAccessException If something goes wrong.
     * @throws IllegalArgumentException If something goes wrong.
     */
    @Test
    public void testActionPortletRequest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        PortletContext portletContext = createMock(PortletContext.class);
        ActionRequest request = createMock(ActionRequest.class);
        ActionResponse response = createMock(ActionResponse.class);

        replay(applicationContext, portletContext, request, response);
        ActionPortletRequest req = new ActionPortletRequest(applicationContext,
                portletContext, request, response);
        Class<? extends ActionPortletRequest> clazz = req.getClass();
        Field field = clazz.getSuperclass().getDeclaredField("requestDelegate");
        assertTrue(field.get(req) instanceof StateAwareRequestDelegate);
        field = clazz.getSuperclass().getDeclaredField("responseDelegate");
        assertTrue(field.get(req) instanceof StateAwareResponseDelegate);
        verify(applicationContext, portletContext, request, response);
    }

}
