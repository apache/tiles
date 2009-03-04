package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertDefinitionModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Renderable;

public class InsertDefinitionVModel implements Executable, BodyExecutable {
    
    private InsertDefinitionModel model;

    private ServletContext servletContext;

    public InsertDefinitionVModel(InsertDefinitionModel model,
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
                model.execute(ServletUtil.getCurrentContainer(request, servletContext),
                        (String) params.get("name"), (String) params.get("template"),
                        (String) params.get("role"), (String) params.get("preparer"),
                        velocityContext, request, response, writer);
                return true;
            }
        };
    }

    public Renderable end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(velocityContext).pop();
        return new AbstractDefaultToStringRenderable(velocityContext, params,
                response, request) {

            public boolean render(InternalContextAdapter context, Writer writer)
                    throws IOException, MethodInvocationException,
                    ParseErrorException, ResourceNotFoundException {
                model.end(ServletUtil.getCurrentContainer(request,
                        servletContext), (String) params.get("name"),
                        (String) params.get("template"), (String) params
                                .get("role"), (String) params.get("preparer"),
                        velocityContext, request, response, writer);
                return true;
            }
        };
    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        model.start(ServletUtil.getCurrentContainer(request,
                servletContext), velocityContext, request, response);
    }
}
