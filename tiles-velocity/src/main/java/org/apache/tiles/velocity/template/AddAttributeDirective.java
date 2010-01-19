package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.AddAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewContext;

public class AddAttributeDirective extends BodyBlockDirective {

    private AddAttributeModel model = new AddAttributeModel();

    @Override
    public String getName() {
        return "tiles_addAttribute";
    }

    @Override
    public void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, String body,
            HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext) throws IOException {
        model.end(ServletUtil.getComposeStack(request), params.get("value"), (String) params
                .get("expression"), body, (String) params.get("role"),
                (String) params.get("type"));
    }

    @Override
    public void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {
        model.start(ServletUtil.getComposeStack(request));
    }

}
