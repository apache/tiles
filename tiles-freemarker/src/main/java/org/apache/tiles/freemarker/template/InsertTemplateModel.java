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

import org.apache.tiles.Attribute;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;

/**
 * This is the tag handler for &lt;tiles:insertTemplate&gt;, which includes a
 * template ready to be filled.
 *
 * @version $Rev$ $Date$
 */
public class InsertTemplateModel extends AbstractRenderModel {

    /** {@inheritDoc} */
    @Override
    protected void render(Environment env, Map<String, TemplateModel> params,
            TemplateModel[] loopVars, TemplateDirectiveBody body) {
        String template = FreeMarkerUtil.getAsString(params.get("template"));
        String role = FreeMarkerUtil.getAsString(params.get("role"));
        String preparer = FreeMarkerUtil.getAsString(params.get("preparer"));
        Attribute templateAttribute = Attribute
                .createTemplateAttribute(template);
        templateAttribute.setRole(role);
        attributeContext.setPreparer(preparer);
        attributeContext.setTemplateAttribute(templateAttribute);
        renderContext(env, params, loopVars, body);
    }

    /**
     * Renders the current context.
     *
     * @throws IOException if an io exception occurs.
     */
    protected void renderContext(Environment env, Map<String, TemplateModel> params,
            TemplateModel[] loopVars, TemplateDirectiveBody body) {
        FreeMarkerUtil.setForceInclude(env, true);
        container.renderContext(env);
    }
}
