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

/**
 * Wraps {@link PutAttributeModel} to be used in FreeMarker. For the list of
 * parameters, see
 * {@link PutAttributeModel#start(Stack)}
 * and
 * {@link PutAttributeModel
 * #end(org.apache.tiles.TilesContainer, Stack, String, Object, String, String, String, String, boolean, Object...)}
 * .
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class PutAttributeFMModel implements TemplateDirectiveModel {

    /**
     * The template model.
     */
    private PutAttributeModel model;

    /**
     * Constructor.
     *
     * @param model The template model.
     * @since 2.2.0
     */
    public PutAttributeFMModel(PutAttributeModel model) {
        this.model = model;
    }

    /** {@inheritDoc} */
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
                FreeMarkerUtil.getAsString(parms.get("role")), FreeMarkerUtil.getAsString(parms.get("type")),
                FreeMarkerUtil
                        .getAsBoolean(parms.get("cascade"), false), env);
    }

}
