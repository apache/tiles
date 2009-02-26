package org.apache.tiles.velocity;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.AddAttributeModel;
import org.apache.velocity.context.Context;

public class AddAttributeVModel implements Executable, BodyExecutable {

    private AddAttributeModel model;

    public AddAttributeVModel(AddAttributeModel model) {
        this.model = model;
    }

    public void execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        model.execute(ServletUtil.getComposeStack(request),
                params.get("value"), (String) params.get("expression"), null,
                (String) params.get("role"), (String) params.get("type"));

    }

    public void end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(velocityContext).pop();
        model.end(ServletUtil.getComposeStack(request),
                params.get("value"), (String) params.get("expression"), null,
                (String) params.get("role"), (String) params.get("type"));
    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        model.start(ServletUtil.getComposeStack(request));
    }

}
