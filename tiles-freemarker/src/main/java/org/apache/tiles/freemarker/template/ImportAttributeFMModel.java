package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.util.Map;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.template.ImportAttributeModel;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ImportAttributeFMModel implements TemplateDirectiveModel {

    private ImportAttributeModel model;
    
    public ImportAttributeFMModel(ImportAttributeModel model) {
        this.model = model;
    }

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        Map<String, TemplateModel> parms = (Map<String, TemplateModel>) params;
        TilesContainer container = FreeMarkerUtil.getCurrentContainer(env);
        Map<String, Object> attributes = model.getImportedAttributes(
                container, FreeMarkerUtil.getAsString(parms.get("name")),
                FreeMarkerUtil.getAsString(parms.get("toName")),
                FreeMarkerUtil.getAsBoolean(parms.get("ignore"), false),
                env);
        String scope = FreeMarkerUtil.getAsString(parms.get("scope"));
        for (Map.Entry<String, Object> entry: attributes.entrySet()) {
            FreeMarkerUtil.setAttribute(env, entry.getKey(), entry.getValue(),
                    scope);
        }
    }

}
