package org.apache.tiles.freemarker.template;

import java.util.Map;

import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;

public class InsertDefinitionModel extends InsertTemplateModel {

    @Override
    protected void renderContext(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        String name = FreeMarkerUtil.getAsString(params.get("name"));
        container.render(name, env);
    }
}
