package org.apache.tiles.velocity.template;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;



public interface BodyExecutable {

    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * @param request TODO
     * @param response TODO
     * @param velocityContext TODO
     */
    public abstract void start(HttpServletRequest request, HttpServletResponse response, Context velocityContext, Map<String, Object> params);

    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * @param request TODO
     * @param response TODO
     * @param velocityContext TODO
     */
    public abstract void end(HttpServletRequest request, HttpServletResponse response, Context velocityContext);

}