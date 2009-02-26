package org.apache.tiles.velocity.template;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;

public class ImportAttributeVModel implements Executable {

    private ImportAttributeModel model;

    private ServletContext servletContext;

    public ImportAttributeVModel(ImportAttributeModel model,
            ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }

    public void execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        Map<String, Object> attributes = model.getImportedAttributes(
                ServletUtil.getCurrentContainer(request, servletContext),
                (String) params.get("name"), (String) params.get("toName"),
                VelocityUtil.toSimpleBoolean((Boolean) params.get("ignore"),
                        false), velocityContext, request, response);
        String scope = (String) params.get("scope");
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            VelocityUtil.setAttribute(velocityContext, request, servletContext,
                    entry.getKey(), entry.getValue(), scope);
        }
    }

}
