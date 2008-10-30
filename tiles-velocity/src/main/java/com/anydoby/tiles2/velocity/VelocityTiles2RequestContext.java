package com.anydoby.tiles2.velocity;

import java.io.IOException;

import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.view.context.ChainedContext;

import static org.apache.tiles.access.TilesAccess.getContainer;

/**
 * 
 * @author SergeyZ
 * 
 */
public class VelocityTiles2RequestContext extends ServletTilesRequestContext {

    private final ChainedContext ctx;

    public VelocityTiles2RequestContext(ChainedContext ctx) {
        super(ctx.getServletContext(), ctx.getRequest(), ctx.getResponse());
        this.ctx = ctx;
        ctx.put("tiles", new Tiles2Tool(getContainer(ctx.getServletContext()), this));
    }

    public void dispatch(String path) throws IOException {
        include(path);
    }

    public ChainedContext getContext() {
        return ctx;
    }

    public VelocityEngine getEngine() {
        VelocityEngine velocityEngine = ctx.getVelocityEngine();
        return velocityEngine;
    }

    @Override
    public void include(String path) throws IOException {
        try {
            Template template = ctx.getVelocityEngine().getTemplate(path);
            template.merge(ctx, getResponse().getWriter());
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void put(String toName, Object attribute) {
        ctx.put(toName, attribute);
    }

}
