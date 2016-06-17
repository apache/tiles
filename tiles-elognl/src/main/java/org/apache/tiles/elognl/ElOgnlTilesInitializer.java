package org.apache.tiles.elognl;

import javax.servlet.ServletContext;

import org.apache.tiles.startup.AbstractTilesInitializer;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.servlet.ServletApplicationContext;

public class ElOgnlTilesInitializer extends AbstractTilesInitializer {

    /** {@inheritDoc} */
    @Override
    protected ApplicationContext createTilesApplicationContext(
            ApplicationContext preliminaryContext) {
        return new ServletApplicationContext(
                (ServletContext) preliminaryContext.getContext());
    }
    
    
    /** {@inheritDoc} */
    @Override
    protected AbstractTilesContainerFactory createContainerFactory(
            ApplicationContext context) {
        return new ElOgnlTilesContainerFactory();
    }
}
