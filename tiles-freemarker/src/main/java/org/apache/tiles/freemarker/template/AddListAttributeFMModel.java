package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.template.AddListAttributeModel;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class AddListAttributeFMModel implements TemplateDirectiveModel {

    private AddListAttributeModel model;
    
    public AddListAttributeFMModel(AddListAttributeModel model) {
        this.model = model;
    }

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        Map<String, TemplateModel> parms = (Map<String, TemplateModel>) params;
        Stack<Object> composeStack = FreeMarkerUtil.getComposeStack(env);
        model.start(composeStack, FreeMarkerUtil.getAsString(parms.get("role")));
        FreeMarkerUtil.evaluateBody(body);
        model.end(composeStack);
    }

}
