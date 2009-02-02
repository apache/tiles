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
import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Attribute;
import org.apache.tiles.TilesException;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;

/**
 * Import attribute(s) into the specified scope. If not explicitly named, all
 * attributes are imported. If the scope is not specified, page scope is
 * assumed.
 * 
 * @since Tiles 1.0
 * @version $Rev$ $Date$
 */
public class ImportAttributeModel extends AttributeModel {

    /**
     * The logging object.
     */
    private final Log log = LogFactory.getLog(ImportAttributeModel.class);

    /**
     * Expose the requested property from attribute context.
     * 
     * @throws TilesJspException On errors processing tag.
     */
    public void evaluate(Environment env, Map<String, TemplateModel> params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws IOException {
        if (attributeValue != null) {
            String name = FreeMarkerUtil.getAsString(params.get("name"));
            String toName = FreeMarkerUtil.getAsString(params.get("toName"));
            if (toName == null) {
                toName = name;
            }
            String scope = FreeMarkerUtil.getAsString(params.get("scope"));
            FreeMarkerUtil.setAttribute(env, name, attributeValue, scope);
        } else {
            importAttributes(env, params, attributeContext
                    .getCascadedAttributeNames());
            importAttributes(env, params, attributeContext
                    .getLocalAttributeNames());
        }
    }

    /**
     * Imports an attribute set.
     * 
     * @param names The names of the attributes to be imported.
     * @throws TilesJspException If something goes wrong during the import.
     */
    private void importAttributes(Environment env,
            Map<String, TemplateModel> params, Collection<String> names) {
        if (names == null || names.isEmpty()) {
            return;
        }

        boolean ignore = FreeMarkerUtil.getAsBoolean(params.get("ignore"),
                false);
        String scope = FreeMarkerUtil.getAsString(params.get("scope"));
        for (String name : names) {
            if (name == null && !ignore) {
                throw new FreeMarkerTilesException(
                        "Error importing attributes. "
                                + "Attribute with null key found.");
            } else if (name == null) {
                continue;
            }

            Attribute attr = attributeContext.getAttribute(name);

            if (attr != null) {
                try {
                    Object attributeValue = container.evaluate(attr, env);
                    if (attributeValue == null) {
                        throw new FreeMarkerTilesException(
                                "Error importing attributes. " + "Attribute '"
                                        + name + "' has a null value ");
                    }
                    FreeMarkerUtil.setAttribute(env, name, attributeValue,
                            scope);
                } catch (TilesException e) {
                    if (!ignore) {
                        throw e;
                    } else if (log.isDebugEnabled()) {
                        log.debug("Ignoring Tiles Exception", e);
                    }
                }
            } else if (!ignore) {
                throw new FreeMarkerTilesException(
                        "Error importing attributes. " + "Attribute '" + name
                                + "' is null");
            }

        }
    }
}
