package org.apache.tiles.elognl;

import org.apache.tiles.startup.TilesInitializer;
import org.apache.tiles.web.startup.AbstractTilesListener;

public class ElOgnlTilesListener extends AbstractTilesListener {

	    /** {@inheritDoc} */
	    @Override
	    protected TilesInitializer createTilesInitializer() {
	        return new ElOgnlTilesInitializer();
	    }

	}
