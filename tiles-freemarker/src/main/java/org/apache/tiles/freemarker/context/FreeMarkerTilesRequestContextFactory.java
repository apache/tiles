package org.apache.tiles.freemarker.context;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class FreeMarkerTilesRequestContextFactory implements
        TilesRequestContextFactory, TilesRequestContextFactoryAware {

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
            TemplateHashModel dataModel = env.getDataModel();
            try {
                Object requestObj = dataModel.get("Request");
                if (requestObj instanceof HttpRequestHashModel) {
                    HttpRequestHashModel requestModel = (HttpRequestHashModel) requestObj;
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
                } else {
                    throw new FreeMarkerTilesException("The request hash model is not present");
                }
            } catch (TemplateModelException e) {
                throw new FreeMarkerTilesException("Cannot complete the FreeMarker request", e);
            }
        }
        return null;
    }

    public void init(Map<String, String> configurationParameters) {
        // Nothing to initialize.
    }
}
