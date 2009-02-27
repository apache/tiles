package org.apache.tiles.velocity.template;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.DefinitionModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.Renderable;

public class DefinitionVModel implements Executable, BodyExecutable {

    private DefinitionModel model;

    private ServletContext servletContext;

    public DefinitionVModel(DefinitionModel model, ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }

    public Renderable execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        model.execute((MutableTilesContainer) ServletUtil.getCurrentContainer(
                request, servletContext), ServletUtil.getComposeStack(request),
                (String) params.get("name"), (String) params.get("template"),
                (String) params.get("role"), (String) params.get("extends"),
                (String) params.get("preparer"), velocityContext, request,
                response);
        return null;
    }

    public void end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(
                velocityContext).pop();
        model.end((MutableTilesContainer) ServletUtil.getCurrentContainer(
                request, servletContext), ServletUtil
                .getComposeStack(request), (String) params.get("name"),
                velocityContext, request, response);

    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        model.start(ServletUtil.getComposeStack(request), (String) params
                .get("name"), (String) params.get("template"), (String) params
                .get("role"), (String) params.get("extends"), (String) params
                .get("preparer"));
    }
}
