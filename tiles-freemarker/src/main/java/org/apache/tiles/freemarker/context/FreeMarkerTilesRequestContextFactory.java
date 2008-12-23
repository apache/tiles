package org.apache.tiles.freemarker.context;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;

public class FreeMarkerTilesRequestContextFactory implements
        TilesRequestContextFactory, TilesRequestContextFactoryAware {

    private static final Log LOG = LogFactory.getLog(FreeMarkerTilesRequestContextFactory.class);
    
    /**
     * Parent Tiles context factory.
     */
    private TilesRequestContextFactory parent;

    /** {@inheritDoc} */
    public void setRequestContextFactory(
            TilesRequestContextFactory contextFactory) {
        parent = contextFactory;
    }

    public TilesRequestContext createRequestContext(
            TilesApplicationContext context, Object... requestItems) {
        if (requestItems.length == 1 && requestItems[0] instanceof Environment) {
            Environment env = (Environment) requestItems[0];
            HttpRequestHashModel requestModel;
            try {
                requestModel = FreeMarkerUtil.getRequestHashModel(env);
            } catch (FreeMarkerTilesException e) {
                LOG.warn("Cannot evaluate as a FreeMarker in Servlet Environment, skipping", e);
                return null;
            }
            HttpServletRequest request = requestModel.getRequest();
            HttpServletResponse response = requestModel.getResponse();
            TilesRequestContext enclosedRequest;
            if (parent != null) {
                enclosedRequest = parent.createRequestContext(context, request,
                        response);
            } else {
                enclosedRequest = new ServletTilesRequestContext(context,
                        (HttpServletRequest) request,
                        (HttpServletResponse) response);
            }
            return new FreeMarkerTilesRequestContext(enclosedRequest, env);
        }
        return null;
    }

    public void init(Map<String, String> configurationParameters) {
        // Nothing to initialize.
    }
}
