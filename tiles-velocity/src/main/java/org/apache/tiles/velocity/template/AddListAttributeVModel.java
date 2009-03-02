package org.apache.tiles.velocity.template;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.AddListAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.Renderable;

public class AddListAttributeVModel implements BodyExecutable {

    private AddListAttributeModel model;
    
    public AddListAttributeVModel(AddListAttributeModel model) {
        this.model = model;
    }
    
    public Renderable end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        model.end(ServletUtil.getComposeStack(request));
        return VelocityUtil.EMPTY_RENDERABLE;
    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        model.start(ServletUtil.getComposeStack(request), (String) params.get("role"));
    }

}
