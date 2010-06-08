/**
 *
 */
package org.apache.tiles.web.startup;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.tiles.request.servlet.ServletApplicationContext;
import org.apache.tiles.startup.TilesInitializer;
import org.junit.Test;

/**
 * Tests {@link AbstractTilesListener}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractTilesListenerTest {

    /**
     * Test method for {@link org.apache.tiles.web.startup.AbstractTilesListener#contextInitialized(javax.servlet.ServletContextEvent)}.
     */
    @Test
    public void testContextInitialized() {
        AbstractTilesListener listener = createMockBuilder(AbstractTilesListener.class).createMock();
        ServletContextEvent event = createMock(ServletContextEvent.class);
        ServletContext servletContext = createMock(ServletContext.class);
        TilesInitializer initializer = createMock(TilesInitializer.class);

        expect(event.getServletContext()).andReturn(servletContext);
        expect(listener.createTilesInitializer()).andReturn(initializer);
        initializer.initialize(isA(ServletApplicationContext.class));
        initializer.destroy();

        replay(listener, event, servletContext, initializer);
        listener.contextInitialized(event);
        listener.contextDestroyed(event);
        verify(listener, event, servletContext, initializer);
    }

}
