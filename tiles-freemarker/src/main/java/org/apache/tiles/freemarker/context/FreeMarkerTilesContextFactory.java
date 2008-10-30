package org.apache.tiles.freemarker.context;

import java.util.Map;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;

import freemarker.core.Environment;

public class FreeMarkerTilesContextFactory implements TilesContextFactory {

    public TilesApplicationContext createApplicationContext(Object context) {
        throw new UnsupportedOperationException(
                "Cannot create an application context "
                        + "since FreeMarker is only available at request time");
    }

    public TilesRequestContext createRequestContext(
            TilesApplicationContext context, Object... requestItems) {
        if (requestItems.length == 1 && requestItems[0] instanceof Environment) {
            return new FreeMarkerTilesRequestContext(context,
                    (Environment) requestItems[0]);
        }
        return null;
    }

    public void init(Map<String, String> configurationParameters) {
        // Nothing to initialize.
    }
}
