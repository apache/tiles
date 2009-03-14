package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Renderable;

public class ImportAttributeVModel implements Executable {

    private ImportAttributeModel model;

    private ServletContext servletContext;

    public ImportAttributeVModel(ImportAttributeModel model,
            ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }

    public Renderable execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        return new AbstractDefaultToStringRenderable(velocityContext, params,
                response, request) {

            public boolean render(InternalContextAdapter context, Writer writer)
                    throws IOException, MethodInvocationException,
                    ParseErrorException, ResourceNotFoundException {
                Map<String, Object> attributes = model.getImportedAttributes(
                        ServletUtil
                                .getCurrentContainer(request, servletContext),
                        (String) params.get("name"), (String) params
                                .get("toName"), VelocityUtil.toSimpleBoolean(
                                (Boolean) params.get("ignore"), false),
                        velocityContext, request, response);
                String scope = (String) params.get("scope");
                for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                    VelocityUtil.setAttribute(context, request,
                            servletContext, entry.getKey(), entry.getValue(),
                            scope);
                }
                
                return true;
            }
        };
    }

}
