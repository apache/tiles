package com.anydoby.tiles2.velocity;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ChainedContext;

/**
 * 
 * @author anydoby
 * 
 * @since Mar 15, 2008
 */
public class VelocityContextFactory implements TilesContextFactory {

    public TilesApplicationContext createApplicationContext(Object context) {
        if (context instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) context;
            return new ServletTilesApplicationContext(servletContext);
        }
        return null;
    }

    public TilesRequestContext createRequestContext(TilesApplicationContext context, Object... requestItems) {
        if (requestItems.length == 1) {
            if (requestItems[0] instanceof Context) {
                ChainedContext ctx = (ChainedContext) requestItems[0];
                return new VelocityTiles2RequestContext(ctx);
            } else if (requestItems[0] instanceof VelocityTiles2RequestContext) {
                VelocityTiles2RequestContext ctx = (VelocityTiles2RequestContext) requestItems[0];
                return ctx;
            }
        }
        return null;
    }

    public void init(Map<String, String> configurationParameters) {
        // TODO Auto-generated method stub

    }

}
