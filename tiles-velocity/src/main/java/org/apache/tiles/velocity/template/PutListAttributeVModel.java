package org.apache.tiles.velocity.template;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.PutListAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;

public class PutListAttributeVModel implements BodyExecutable {

    private PutListAttributeModel model;

    private ServletContext servletContext;

    public PutListAttributeVModel(PutListAttributeModel model,
            ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }

    public void end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(
                velocityContext).pop();
        model.end(ServletUtil.getCurrentContainer(request, servletContext),
                ServletUtil.getComposeStack(request), (String) params
                        .get("name"), VelocityUtil.toSimpleBoolean(
                        (Boolean) params.get("cascade"), false),
                velocityContext, request, response);
    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        model.start(ServletUtil.getComposeStack(request), (String) params
                .get("role"), VelocityUtil.toSimpleBoolean((Boolean) params
                .get("inherit"), false));
    }
}
