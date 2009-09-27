package org.apache.tiles.test.listener;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.startup.AbstractTilesInitializer;
import org.apache.tiles.test.factory.TestAlternateTilesContainerFactory;

/**
 * Test Tiles initializer for Tiles initialization of the alternate container.
 *
 * @version $Rev$ $Date$
 */
class TestAlternateTilesInitializer extends AbstractTilesInitializer {

    /** {@inheritDoc} */
    @Override
    protected AbstractTilesContainerFactory createContainerFactory(
            TilesApplicationContext context) {
        return new TestAlternateTilesContainerFactory();
    }

    /** {@inheritDoc} */
    @Override
    protected String getContainerKey(
            TilesApplicationContext applicationContext) {
        return "alternate";
    }
}