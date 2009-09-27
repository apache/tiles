package org.apache.tiles.test.listener;

import javax.servlet.ServletContext;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.servlet.wildcard.WildcardServletTilesApplicationContext;
import org.apache.tiles.startup.AbstractTilesInitializer;
import org.apache.tiles.test.factory.TestTilesContainerFactory;

/**
 * Test Tiles initializer for Tiles initialization of the default container.
 *
 * @version $Rev$ $Date$
 */
class TestTilesListenerInitializer extends AbstractTilesInitializer {

    /** {@inheritDoc} */
    @Override
    protected AbstractTilesContainerFactory createContainerFactory(
            TilesApplicationContext context) {
        return new TestTilesContainerFactory();
    }

    /** {@inheritDoc} */
    @Override
    protected TilesApplicationContext createTilesApplicationContext(
            TilesApplicationContext preliminaryContext) {
        return new WildcardServletTilesApplicationContext(
                (ServletContext) preliminaryContext.getContext());
    }
}