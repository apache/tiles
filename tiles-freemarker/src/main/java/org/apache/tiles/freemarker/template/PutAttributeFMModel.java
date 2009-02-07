package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.template.PutAttributeModel;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PutAttributeFMModel implements TemplateDirectiveModel {

    private PutAttributeModel model;

    public PutAttributeFMModel(PutAttributeModel model) {
        this.model = model;
    }

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        Stack<Object> composeStack = FreeMarkerUtil.getComposeStack(env);
        model.start(composeStack);
        String bodyString = FreeMarkerUtil.renderAsString(body);
        Map<String, TemplateModel> parms = (Map<String, TemplateModel>) params;
        model.end(FreeMarkerUtil.getCurrentContainer(env), composeStack,
                FreeMarkerUtil.getAsString(parms.get("name")), FreeMarkerUtil
                        .getAsObject(parms.get("value")), FreeMarkerUtil
                        .getAsString(parms.get("expression")), bodyString,
                FreeMarkerUtil.getAsString(parms.get("role")), FreeMarkerUtil
                        .getAsString(parms.get("type")), FreeMarkerUtil
                        .getAsBoolean(parms.get("cascade"), false), env);
    }

}
