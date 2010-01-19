package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewContext;

public class ImportAttributeDirective extends Directive {

    private ImportAttributeModel model = new ImportAttributeModel();

    @Override
    public String getName() {
        return "tiles_importAttribute";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException,
            MethodInvocationException {
        ViewContext viewContext = (ViewContext) context
                .getInternalUserContext();
        Map<String, Object> params = VelocityUtil.getParameters(context, node);
        HttpServletRequest request = viewContext.getRequest();
        HttpServletResponse response = viewContext.getResponse();
        ServletContext servletContext = viewContext.getServletContext();
        Map<String, Object> attributes = model.getImportedAttributes(
                ServletUtil
                        .getCurrentContainer(request, servletContext),
                (String) params.get("name"), (String) params
                        .get("toName"), VelocityUtil.toSimpleBoolean(
                        (Boolean) params.get("ignore"), false),
                context, request, response);
        String scope = (String) params.get("scope");
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            VelocityUtil.setAttribute(context, request,
                    servletContext, entry.getKey(), entry.getValue(),
                    scope);
        }
        return true;
    }

}
