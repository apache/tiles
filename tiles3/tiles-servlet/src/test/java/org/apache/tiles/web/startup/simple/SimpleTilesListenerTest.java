/**
 *
 */
package org.apache.tiles.web.startup.simple;

import static org.junit.Assert.*;

import org.apache.tiles.startup.DefaultTilesInitializer;
import org.junit.Test;

/**
 * Tests {@link SimpleTilesListener}.
 *
 * @version $Rev$ $Date$
 */
public class SimpleTilesListenerTest {

    /**
     * Test method for {@link org.apache.tiles.web.startup.simple.SimpleTilesListener#createTilesInitializer()}.
     */
    @Test
    public void testCreateTilesInitializer() {
        SimpleTilesListener servlet = new SimpleTilesListener();
        assertTrue(servlet.createTilesInitializer() instanceof DefaultTilesInitializer);
    }

}
