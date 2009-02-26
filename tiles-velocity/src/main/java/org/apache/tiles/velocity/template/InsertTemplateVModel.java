package org.apache.tiles.velocity.template;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertTemplateModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;

public class InsertTemplateVModel implements Executable, BodyExecutable {

    private InsertTemplateModel model;

    private ServletContext servletContext;

    public InsertTemplateVModel(InsertTemplateModel model,
            ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }

    public void execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        model.execute(ServletUtil.getCurrentContainer(request, servletContext),
                (String) params.get("template"), (String) params.get("role"),
                (String) params.get("preparer"), velocityContext, request,
                response);
    }

    public void end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(
                velocityContext).pop();
        model.end(ServletUtil.getCurrentContainer(request, servletContext),
                (String) params.get("template"), (String) params.get("role"),
                (String) params.get("preparer"), velocityContext, request,
                response);
    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        model.start(ServletUtil.getCurrentContainer(request, servletContext),
                velocityContext, request, response);
    }

}
