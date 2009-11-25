/**
 *
 */
package org.apache.tiles.template;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.NoSuchContainerException;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Test;

/**
 * Tests {@link SetCurrentContainerModel}.
 *
 * @version $Rev$ $Date$
 */
public class SetCurrentContainerModelTest {

    /**
     * Test method for {@link org.apache.tiles.template.SetCurrentContainerModel#execute(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testSetCurrentContainer() {
        Request request = createMock(Request.class);
        ApplicationContext context = createMock(ApplicationContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> attribs = new HashMap<String, Object>();
        attribs.put("myKey", container);
        Map<String, Object> requestScope = new HashMap<String, Object>();

        expect(context.getApplicationScope()).andReturn(attribs).anyTimes();
        expect(request.getContext("request")).andReturn(requestScope);
        expect(request.getApplicationContext()).andReturn(context);
        replay(request, context, container);
        SetCurrentContainerModel model = new SetCurrentContainerModel();
        model.execute("myKey", request);
        assertEquals(container, requestScope.get(TilesAccess.CURRENT_CONTAINER_ATTRIBUTE_NAME));
        verify(request, context, container);
    }

    /**
     * Test method for {@link org.apache.tiles.template.SetCurrentContainerModel#execute(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test(expected=NoSuchContainerException.class)
    public void testSetCurrentContainerException() {
        Request request = createMock(Request.class);
        ApplicationContext context = createMock(ApplicationContext.class);
        Map<String, Object> attribs = new HashMap<String, Object>();

        expect(request.getApplicationContext()).andReturn(context);
        expect(context.getApplicationScope()).andReturn(attribs).anyTimes();
        replay(request, context);
        try {
            SetCurrentContainerModel model = new SetCurrentContainerModel();
            model.execute("myKey", request);
        } finally {
            verify(request, context);
        }
    }

}
