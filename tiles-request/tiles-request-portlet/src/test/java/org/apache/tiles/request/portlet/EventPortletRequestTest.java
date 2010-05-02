/**
 *
 */
package org.apache.tiles.request.portlet;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.StateAwareRequestDelegate;
import org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate;
import org.junit.Test;

/**
 * Tests {@link EventPortletRequest}.
 *
 * @version $Rev$ $Date$
 */
public class EventPortletRequestTest {

    /**
     * Test method for {@link org.apache.tiles.request.portlet.EventPortletRequest#EventPortletRequest(org.apache.tiles.request.ApplicationContext, javax.portlet.PortletContext, javax.portlet.EventRequest, javax.portlet.EventResponse)}.
     * @throws NoSuchFieldException If something goes wrong.
     * @throws SecurityException If something goes wrong.
     * @throws IllegalAccessException If something goes wrong.
     * @throws IllegalArgumentException If something goes wrong.
     */
    @Test
    public void testEventPortletRequest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        PortletContext portletContext = createMock(PortletContext.class);
        EventRequest request = createMock(EventRequest.class);
        EventResponse response = createMock(EventResponse.class);

        replay(applicationContext, portletContext, request, response);
        EventPortletRequest req = new EventPortletRequest(applicationContext,
                portletContext, request, response);
        Class<? extends EventPortletRequest> clazz = req.getClass();
        Field field = clazz.getSuperclass().getDeclaredField("requestDelegate");
        assertTrue(field.get(req) instanceof StateAwareRequestDelegate);
        field = clazz.getSuperclass().getDeclaredField("responseDelegate");
        assertTrue(field.get(req) instanceof StateAwareResponseDelegate);
        verify(applicationContext, portletContext, request, response);
    }

}
