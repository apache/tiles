package com.anydoby.tiles2.spring;

import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ChainedContext;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

/**
 * 
 * @author anydoby
 * 
 * @since Mar 16, 2008
 */
public class VelocityTiles2View extends VelocityToolboxView {

    private static class TilesTemplate extends Template {

        public TilesTemplate(String definitionName) {
            setName(definitionName);
        }

    }

    protected void doRender(Context context, HttpServletResponse response) throws Exception {
        TilesContainer container = TilesAccess.getContainer(getServletContext());
        if (context instanceof ChainedContext) {
            ChainedContext ctx = (ChainedContext) context;
            container.render(getUrl(), ctx);
        } else {
            throw new UnsupportedOperationException("Web context is required");
        }
    }

    @Override
    protected Template getTemplate(final String definitionName) throws Exception {
        return new TilesTemplate(definitionName);
    }

}
