package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.PutListAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;

public class PutListAttributeDirective extends BlockDirective {

    private PutListAttributeModel model = new PutListAttributeModel();

    @Override
    public void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext)
            throws IOException {
        model.end(ServletUtil.getCurrentContainer(request, servletContext),
                ServletUtil.getComposeStack(request), (String) params
                        .get("name"), VelocityUtil.toSimpleBoolean(
                        (Boolean) params.get("cascade"), false),
                context, request, response);
    }

    @Override
    public void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {
        model.start(ServletUtil.getComposeStack(request), (String) params
                .get("role"), VelocityUtil.toSimpleBoolean((Boolean) params
                .get("inherit"), false));
    }

    @Override
    public String getName() {
        return "tiles_putListAttribute";
    }

}
