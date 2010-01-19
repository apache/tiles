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
import org.apache.velocity.context.InternalContextAdapter;

public class PutAttributeDirective extends BodyBlockDirective {

    private PutAttributeModel model = new PutAttributeModel();

    @Override
    public String getName() {
        return "tiles_putAttribute";
    }

    @Override
    public void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, String body,
            HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext) throws IOException {
        model.end(ServletUtil.getCurrentContainer(request, servletContext),
                ServletUtil.getComposeStack(request), (String) params.get("name"), params.get("value"),
                (String) params.get("expression"), body, (String) params
                        .get("role"), (String) params.get("type"), VelocityUtil
                        .toSimpleBoolean((Boolean) params.get("cascade"), false),
                context, request, response, writer);
    }

    @Override
    public void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {
        model.start(ServletUtil.getComposeStack(request));
    }

}
