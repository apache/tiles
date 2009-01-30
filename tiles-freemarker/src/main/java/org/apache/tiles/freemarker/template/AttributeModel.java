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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;

/**
 * Support for Scoped tags.
 *
 * @version $Rev$ $Date$
 */
public abstract class AttributeModel implements TemplateDirectiveModel {

    /**
     * The logging object.
     */
    private final Log log = LogFactory.getLog(AttributeModel.class);


    /**
     * The Tiles container to use.
     */
    protected TilesContainer container;

    /**
     * The current attribute context.
     */
    protected AttributeContext attributeContext;

    /**
     * The found attribute.
     */
    protected Attribute attribute;

    /**
     * The attribute value.
     *
     * @since 2.1.0
     */
    protected Object attributeValue;

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        container = FreeMarkerUtil.getCurrentContainer(env);
        attributeContext = container.getAttributeContext(env);

        String name = FreeMarkerUtil.getAsString((TemplateModel) params.get("name"));
        boolean ignore = FreeMarkerUtil.getAsBoolean((TemplateModel) params.get("ignore"), false);
        // Some tags allow for unspecified attribues.  This
        // implies that the tag should use all of the attributes.
        if (name != null) {
            attribute = attributeContext.getAttribute(name);
            if ((attribute == null || attribute.getValue() == null) && ignore) {
                return;
            }

            if (attribute == null) {
                throw new FreeMarkerTilesException("Attribute with name '" + name
                        + "' not found");
            }

            try {
                attributeValue = container.evaluate(attribute, env);
            } catch (TilesException e) {
                if (!ignore) {
                    throw e;
                } else if (log.isDebugEnabled()) {
                    log.debug("Ignoring Tiles Exception", e);
                }
            }

            if (attributeValue == null) {
                throw new FreeMarkerTilesException("Attribute with name '" + name
                        + "' has a null value.");
            }
        }

        try {
            evaluate(env, params, loopVars, body);
        } catch (IOException e) {
            throw new FreeMarkerTilesException("io error while executing tag '"
                    + getClass().getName() + "'.", e);
        }
    }

    /**
     * Execute this tag. It is called inside {@link #doEndTag()}.
     *
     * @throws TilesJspException If something goes wrong during rendering.
     * @throws IOException If something goes wrong during writing content.
     */
    public abstract void evaluate(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws IOException;
}
