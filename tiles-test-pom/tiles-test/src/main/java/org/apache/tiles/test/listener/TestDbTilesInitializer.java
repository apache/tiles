package org.apache.tiles.test.listener;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.startup.AbstractTilesInitializer;
import org.apache.tiles.test.factory.TestDbTilesContainerFactory;

/**
 * Test Tiles initializer for Tiles initialization of the db-based container.
 *
 * @version $Rev$ $Date$
 */
class TestDbTilesInitializer extends AbstractTilesInitializer {

    /** {@inheritDoc} */
    @Override
    protected AbstractTilesContainerFactory createContainerFactory(
            TilesApplicationContext context) {
        return new TestDbTilesContainerFactory();
    }

    /** {@inheritDoc} */
    @Override
    protected String getContainerKey(
            TilesApplicationContext applicationContext) {
        return "db";
    }
}