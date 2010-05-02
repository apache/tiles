/**
 *
 */
package org.apache.tiles.request.portlet;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.portlet.PortletContext;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.MimeResponseDelegate;
import org.apache.tiles.request.portlet.delegate.PortletRequestDelegate;
import org.junit.Test;

/**
 * Tests {@link ResourcePortletRequest}.
 *
 * @version $Rev$ $Date$
 */
public class ResourcePortletRequestTest {

    /**
     * Test method for {@link org.apache.tiles.request.portlet.ResourcePortletRequest#ResourcePortletRequest(org.apache.tiles.request.ApplicationContext, javax.portlet.PortletContext, javax.portlet.ResourceRequest, javax.portlet.ResourceResponse)}.
     * @throws NoSuchFieldException If something goes wrong.
     * @throws SecurityException If something goes wrong.
     * @throws IllegalAccessException If something goes wrong.
     * @throws IllegalArgumentException If something goes wrong.
     */
    @Test
    public void testResourcePortletRequest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        PortletContext portletContext = createMock(PortletContext.class);
        ResourceRequest request = createMock(ResourceRequest.class);
        ResourceResponse response = createMock(ResourceResponse.class);

        replay(applicationContext, portletContext, request, response);
        ResourcePortletRequest req = new ResourcePortletRequest(applicationContext,
                portletContext, request, response);
        Class<? extends ResourcePortletRequest> clazz = req.getClass();
        Field field = clazz.getSuperclass().getDeclaredField("requestDelegate");
        assertTrue(field.get(req) instanceof PortletRequestDelegate);
        field = clazz.getSuperclass().getDeclaredField("responseDelegate");
        assertTrue(field.get(req) instanceof MimeResponseDelegate);
        verify(applicationContext, portletContext, request, response);
    }

}
