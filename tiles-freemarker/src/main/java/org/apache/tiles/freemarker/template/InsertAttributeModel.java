package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;

public class InsertAttributeModel extends AbstractRenderModel {

    /**
     * The evaluated attribute.
     *
     * @since 2.1.0
     */
    protected Attribute attribute;

    @Override
    public void doStart(Environment env, Map<String, TemplateModel> params,
            TemplateModel[] loopVars, TemplateDirectiveBody body) {
        if (FreeMarkerUtil.getAsObject(params.get("value")) == null
                && FreeMarkerUtil.getAsString(params.get("name")) == null) {
            throw new FreeMarkerTilesException(
                    "No attribute name or value has been provided.");
        }
        super.doStart(env, params, loopVars, body);
    }

    /** {@inheritDoc} */
    protected void render(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        HttpServletRequest req = FreeMarkerUtil.getRequestHashModel(env)
                .getRequest();

        String role = FreeMarkerUtil.getAsString(params.get("role"));
        boolean ignore = FreeMarkerUtil.getAsBoolean(params.get("ignore"), false);
        // Checks if the attribute can be rendered with the current user.
        if ((role != null && !req.isUserInRole(role))
                || (attribute == null && ignore)) {
            return;
        }
        render(attribute, env);
    }

    /** {@inheritDoc} */
    @Override
    protected void startContext(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        String preparer = FreeMarkerUtil.getAsString(params.get("preparer"));
        if (preparer != null) {
            container.prepare(preparer, env);
        }

        attribute = (Attribute) FreeMarkerUtil.getAsObject(params.get("value"));
        boolean ignore = FreeMarkerUtil.getAsBoolean(params.get("ignore"), false);
        if (attribute == null) {
            AttributeContext evaluatingContext = container
                    .getAttributeContext(env);
            String name = FreeMarkerUtil.getAsString(params.get("name"));
            attribute = evaluatingContext.getAttribute(name);
            if (attribute == null && !ignore) {
                throw new FreeMarkerTilesException("Attribute '" + name
                        + "' not found.");
            }
        }

        super.startContext(env, params, loopVars, body);
    }

    /**
     * Renders an attribute for real.
     *
     * @param attr The attribute to render.
     * @throws IOException If something goes wrong during the reading of
     * definition files.
     */
    protected void render(Attribute attr, Environment env) {
        try {
            container.render(attr, env.getOut(), env);
        } catch (IOException e) {
            throw new FreeMarkerTilesException(
                    "I/O Exception during rendition of the attribute", e);
        }
    }
}
