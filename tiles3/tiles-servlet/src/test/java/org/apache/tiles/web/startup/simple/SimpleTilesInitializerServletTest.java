/**
 *
 */
package org.apache.tiles.web.startup.simple;

import static org.junit.Assert.*;

import org.apache.tiles.startup.DefaultTilesInitializer;
import org.junit.Test;

/**
 * Tests {@link SimpleTilesInitializerServlet}.
 *
 * @version $Rev$ $Date$
 */
public class SimpleTilesInitializerServletTest {

    /**
     * Test method for {@link org.apache.tiles.web.startup.simple.SimpleTilesInitializerServlet#createTilesInitializer()}.
     */
    @Test
    public void testCreateTilesInitializer() {
        SimpleTilesInitializerServlet servlet = new SimpleTilesInitializerServlet();
        assertTrue(servlet.createTilesInitializer() instanceof DefaultTilesInitializer);
    }

}
