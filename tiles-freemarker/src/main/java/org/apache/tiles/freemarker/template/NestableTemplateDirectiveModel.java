package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import org.apache.tiles.freemarker.FreeMarkerTilesException;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public abstract class NestableTemplateDirectiveModel implements
        TemplateDirectiveModel {

    private static final String MODEL_STACK_ATTRIBUTE_NAME = "org.apache.tiles.freemarker.template.NestableTemplateDirectiveModel.MODEL_STACK";

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        Stack<TemplateDirectiveModel> modelStack;
        modelStack = getModelStack(env);
        doStart(env, params, loopVars, body);
        modelStack.push(this);
        evaluateBody(env, params, loopVars, body);
        modelStack.pop();
        doEnd(env, params, loopVars, body);
    }

    protected abstract void doStart(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body);

    protected abstract void doEnd(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body);

    protected void evaluateBody(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        try {
            body.render(env.getOut());
        } catch (TemplateException e) {
            throw new FreeMarkerTilesException(
                    "Error during evaluation of the body of the template directive",
                    e);
        } catch (IOException e) {
            throw new FreeMarkerTilesException(
                    "Error during writing the body the template directive", e);
        }
    }

    protected TemplateDirectiveModel findAncestorWithClass(Environment env,
            Class<?> clazz) {
        TemplateDirectiveModel retValue = null;
        Stack<TemplateDirectiveModel> modelStack = getModelStack(env);
        for (int i=modelStack.size() - 1; i >= 0 && retValue == null; i++) {
            TemplateDirectiveModel ancestor = modelStack.get(i);
            if (clazz.isAssignableFrom(ancestor.getClass())) {
                retValue = ancestor;
            }
        }
        return retValue;
    }

    @SuppressWarnings("unchecked")
    private Stack<TemplateDirectiveModel> getModelStack(Environment env) {
        Stack<TemplateDirectiveModel> modelStack;
        Environment.Namespace namespace = env.getCurrentNamespace();
        try {
            modelStack = (Stack<TemplateDirectiveModel>) namespace
                    .get(MODEL_STACK_ATTRIBUTE_NAME);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException("Cannot find the model stack", e);
        }
        if (modelStack == null) {
            modelStack = new Stack<TemplateDirectiveModel>();
            namespace.put(MODEL_STACK_ATTRIBUTE_NAME, modelStack);
        }
        return modelStack;
    }
}
