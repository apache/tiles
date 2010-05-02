/**
 *
 */
package org.apache.tiles.request.portlet;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.portlet.PortletContext;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.MimeResponseDelegate;
import org.apache.tiles.request.portlet.delegate.PortletRequestDelegate;
import org.junit.Test;

/**
 * Tests {@link RenderPortletRequest}.
 *
 * @version $Rev$ $Date$
 */
public class RenderPortletRequestTest {

    /**
     * Test method for {@link org.apache.tiles.request.portlet.RenderPortletRequest#RenderPortletRequest(org.apache.tiles.request.ApplicationContext, javax.portlet.PortletContext, javax.portlet.RenderRequest, javax.portlet.RenderResponse)}.
     * @throws NoSuchFieldException If something goes wrong.
     * @throws SecurityException If something goes wrong.
     * @throws IllegalAccessException If something goes wrong.
     * @throws IllegalArgumentException If something goes wrong.
     */
    @Test
    public void testRenderPortletRequest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        PortletContext portletContext = createMock(PortletContext.class);
        RenderRequest request = createMock(RenderRequest.class);
        RenderResponse response = createMock(RenderResponse.class);

        replay(applicationContext, portletContext, request, response);
        RenderPortletRequest req = new RenderPortletRequest(applicationContext,
                portletContext, request, response);
        Class<? extends RenderPortletRequest> clazz = req.getClass();
        Field field = clazz.getSuperclass().getDeclaredField("requestDelegate");
        assertTrue(field.get(req) instanceof PortletRequestDelegate);
        field = clazz.getSuperclass().getDeclaredField("responseDelegate");
        assertTrue(field.get(req) instanceof MimeResponseDelegate);
        verify(applicationContext, portletContext, request, response);
    }

}
