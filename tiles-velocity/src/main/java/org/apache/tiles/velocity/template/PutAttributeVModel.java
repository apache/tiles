package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.PutAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Renderable;

public class PutAttributeVModel implements Executable, BodyExecutable {

    private PutAttributeModel model;

    private ServletContext servletContext;
    
    public PutAttributeVModel(PutAttributeModel model,
            ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }
    
    public Renderable execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        return new AbstractDefaultToStringRenderable(velocityContext, params, response, request) {

            public boolean render(InternalContextAdapter context, Writer writer)
                    throws IOException, MethodInvocationException,
                    ParseErrorException, ResourceNotFoundException {
                model.execute(ServletUtil.getCurrentContainer(request,
                        servletContext), ServletUtil.getComposeStack(request),
                        (String) params.get("name"), params.get("value"),
                        (String) params.get("expression"), null,
                        (String) params.get("role"), (String) params
                                .get("type"), VelocityUtil.toSimpleBoolean(
                                (Boolean) params.get("value"), false),
                        velocityContext, request, response, writer);
                return true;
            }
            
        };
    }

    public void end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(
                velocityContext).pop();
        model.end(ServletUtil.getCurrentContainer(request, servletContext), ServletUtil.getComposeStack(request),
                (String) params.get("name"), params.get("value"),
                (String) params.get("expression"), null, (String) params.get("role"),
                (String) params.get("type"), VelocityUtil.toSimpleBoolean((Boolean) params.get("value"), false),
                velocityContext, request, response);
    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        model.start(ServletUtil.getComposeStack(request));
    }
}
