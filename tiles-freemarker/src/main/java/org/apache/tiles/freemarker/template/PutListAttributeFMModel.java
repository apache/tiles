package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.template.PutListAttributeModel;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PutListAttributeFMModel implements TemplateDirectiveModel {

    private PutListAttributeModel model;
    
    public PutListAttributeFMModel(PutListAttributeModel model) {
        this.model = model;
    }

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        Map<String, TemplateModel> parms = (Map<String, TemplateModel>) params;
        Stack<Object> composeStack = FreeMarkerUtil.getComposeStack(env);
        model.start(composeStack, FreeMarkerUtil.getAsString(parms.get("role")),
                FreeMarkerUtil.getAsBoolean(parms.get("role"), false));
        FreeMarkerUtil.evaluateBody(body);
        model.end(FreeMarkerUtil.getCurrentContainer(env), composeStack,
                FreeMarkerUtil.getAsString(parms.get("name")), FreeMarkerUtil
                        .getAsBoolean(parms.get("cascade"), false), env);
    }

}
