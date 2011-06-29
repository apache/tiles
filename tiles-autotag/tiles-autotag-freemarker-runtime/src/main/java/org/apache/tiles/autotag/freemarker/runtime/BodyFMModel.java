/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tiles.autotag.freemarker.runtime;

import java.io.IOException;
import java.util.Map;

import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.freemarker.FreemarkerRequest;
import org.apache.tiles.request.freemarker.FreemarkerRequestUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;

/**
 * Base directive model for models with a body.
 *
 * @version $Rev$ $Date$
 */
public abstract class BodyFMModel implements TemplateDirectiveModel {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws IOException {
        Request request = FreemarkerRequest.createServletFreemarkerRequest(
                FreemarkerRequestUtil.getApplicationContext(env), env);
        ModelBody modelBody = new FreemarkerModelBody(env.getOut(), body);
        execute(params, request, modelBody);
    }

    /**
     * Executes the model.
     *
     * @param parms Parameters.
     * @param request The request.
     * @param modelBody The body.
     * @throws IOException If something goes wrong.
     */
    protected abstract void execute(Map<String, TemplateModel> parms,
            Request request, ModelBody modelBody) throws IOException;
}
