package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.GetAsStringModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewContext;

public class GetAsStringDirective extends BlockDirective {

    private GetAsStringModel model = new GetAsStringModel(
            new DefaultAttributeResolver());

    @Override
    public String getName() {
        return "tiles_getAsString";
    }

    @Override
    public void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext)
            throws IOException {
        model.end(ServletUtil.getComposeStack(request), ServletUtil
                .getCurrentContainer(request, servletContext), writer,
                VelocityUtil.toSimpleBoolean((Boolean) params.get("ignore"),
                        false), context, request, response, writer);
    }

    @Override
    public void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {
        model.start(ServletUtil.getComposeStack(request), ServletUtil
                .getCurrentContainer(request, servletContext), VelocityUtil
                .toSimpleBoolean((Boolean) params.get("ignore"), false),
                (String) params.get("preparer"), (String) params.get("role"),
                params.get("defaultValue"), (String) params
                        .get("defaultValueRole"), (String) params
                        .get("defaultValueType"), (String) params.get("name"),
                (Attribute) params.get("value"), context, request, response,
                writer);
    }

}
