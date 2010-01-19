package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertDefinitionModel;
import org.apache.velocity.context.InternalContextAdapter;

public class InsertDefinitionDirective extends BlockDirective {

    private InsertDefinitionModel model = new InsertDefinitionModel();

    @Override
    public String getName() {
        return "tiles_insertDefinition";
    }

    @Override
    public void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext)
            throws IOException {
        model.end(ServletUtil.getCurrentContainer(request,
                servletContext), (String) params.get("name"), (String) params
                .get("template"), (String) params.get("templateType"),
                (String) params.get("templateExpression"), (String) params
                        .get("role"), (String) params.get("preparer"), context,
                request, response, writer);
    }

    @Override
    public void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {
        model.start(ServletUtil.getCurrentContainer(request,
                servletContext), context, request, response);
    }

}
