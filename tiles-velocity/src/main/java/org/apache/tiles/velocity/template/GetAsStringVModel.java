package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.GetAsStringModel;
import org.apache.tiles.velocity.TilesVelocityException;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.Renderable;

public class GetAsStringVModel implements Executable, BodyExecutable {

    private GetAsStringModel model;

    private ServletContext servletContext;
    
    public GetAsStringVModel(GetAsStringModel model, ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }
    
    public Renderable execute(HttpServletRequest request, HttpServletResponse response, Context velocityContext, Map<String, Object> params) {
        TilesContainer container = ServletUtil.getCurrentContainer(request,
                servletContext);
        try {
            model.execute(container, response.getWriter(), VelocityUtil
                    .toSimpleBoolean((Boolean) params.get("ignore"), false),
                    (String) params.get("preparer"), (String) params.get("role"),
                    params.get("defaultValue"), (String) params
                            .get("defaultValueRole"), (String) params
                            .get("defaultValueType"), (String) params.get("name"),
                    (Attribute) params.get("value"), velocityContext, request,
                    response);
        } catch (IOException e) {
            throw new TilesVelocityException("Cannot execute getAsString", e);
        }
        return null;
    }

    public void start(HttpServletRequest request, HttpServletResponse response, Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        model.start(ServletUtil.getComposeStack(request), ServletUtil
                .getCurrentContainer(request, servletContext), VelocityUtil
                .toSimpleBoolean((Boolean) params.get("ignore"), false),
                (String) params.get("preparer"), (String) params.get("role"),
                params.get("defaultValue"), (String) params
                        .get("defaultValueRole"), (String) params
                        .get("defaultValueType"), (String) params.get("name"),
                (Attribute) params.get("value"), velocityContext, request,
                response);
    }

    public void end(HttpServletRequest request, HttpServletResponse response, Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(
                velocityContext).pop();
        try {
            model.end(ServletUtil.getComposeStack(request), ServletUtil
                    .getCurrentContainer(request, servletContext), response
                    .getWriter(), VelocityUtil.toSimpleBoolean((Boolean) params
                    .get("ignore"), false), velocityContext, request, response);
        } catch (IOException e) {
            throw new TilesVelocityException("Cannot end getAsString", e);
        }
    }
}
