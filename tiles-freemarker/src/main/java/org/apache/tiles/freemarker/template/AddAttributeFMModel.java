package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.template.AddAttributeModel;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class AddAttributeFMModel implements TemplateDirectiveModel {

    private AddAttributeModel addAttributeModel;
    
    /**
     * @param addAttributeModel
     */
    public AddAttributeFMModel(AddAttributeModel addAttributeModel) {
        this.addAttributeModel = addAttributeModel;
    }

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        Stack<Object> composeStack = FreeMarkerUtil.getComposeStack(env);
        addAttributeModel.start(composeStack);
        String bodyString = FreeMarkerUtil.renderAsString(body);
        Map<String, TemplateModel> parms = (Map<String, TemplateModel>) params;
        addAttributeModel.end(composeStack, FreeMarkerUtil.getAsObject(parms.get("value")),
                FreeMarkerUtil.getAsString(parms.get("expression")), bodyString,
                FreeMarkerUtil.getAsString(parms.get("role")),
                FreeMarkerUtil.getAsString(parms.get("type")));
    }

}
