package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewContext;

public abstract class BlockDirective extends Directive {

    /** {@inheritDoc} */

    @Override
    public int getType() {
        return BLOCK;
    }

    /** {@inheritDoc} */

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
        start(context, writer, params, request, response, servletContext);
        VelocityUtil.evaluateBody(context, writer, node);
        end(context, writer, params, request, response, servletContext);
        return true;
    }

    public abstract void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext);

    public abstract void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext)
            throws IOException;
}