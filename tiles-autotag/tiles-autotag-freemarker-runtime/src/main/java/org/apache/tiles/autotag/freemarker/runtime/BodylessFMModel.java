package org.apache.tiles.autotag.freemarker.runtime;

import java.io.IOException;
import java.util.Map;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.freemarker.FreemarkerRequest;
import org.apache.tiles.request.freemarker.FreemarkerRequestUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;

public abstract class BodylessFMModel implements TemplateDirectiveModel {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws IOException {
        Request request = FreemarkerRequest.createServletFreemarkerRequest(
                FreemarkerRequestUtil.getApplicationContext(env), env);
        execute(params, request);
    }

    protected abstract void execute(Map<String, TemplateModel> parms,
            Request request) throws IOException;
}
