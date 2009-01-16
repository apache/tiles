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
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;

/**
 * <p>
 * Support for all tags which render (an attribute, a template, or definition).
 * </p>
 * <p>
 * Properly invokes the defined preparer and invokes the abstract render method
 * upon completion.
 * </p>
 * This tag takes special care to ensure that the attribute context is reset to
 * it's original state after the execution of the tag is complete. This ensures
 * that all all included attributes in subsequent tiles are scoped properly and
 * do not bleed outside their intended scope.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public abstract class AbstractRenderModel extends NestableTemplateDirectiveModel implements
        PutAttributeModelParent, PutListAttributeModelParent {

    /**
     * The log instance for this tag.
     */
    private final Log log = LogFactory.getLog(AbstractRenderModel.class);

    protected TilesContainer container;
    
    private AttributeContext attributeContext;
    
    /** {@inheritDoc} */
    public void doStart(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        container = FreeMarkerUtil.getCurrentContainer(env);
        if (container != null) {
            startContext(env, params, loopVars, body);
        } else {
            throw new FreeMarkerTilesException("TilesContainer not initialized");
        }
    }

    /** {@inheritDoc} */
    public void doEnd(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        try {
            render(env, params, loopVars, body);
            if (FreeMarkerUtil.getAsBoolean(params.get("flush"), false)) {
                env.getOut().flush();
            }
        } catch (IOException io) {
            String message = "IO Error executing tag: " + io.getMessage();
            log.error(message, io);
            throw new FreeMarkerTilesException(message, io);
        } finally {
            endContext(env, params, loopVars, body);
        }
    }

    /**
     * Render the specified content.
     *
     * @throws TilesJspException if a jsp exception occurs.
     * @throws IOException if an io exception occurs.
     */
    protected abstract void render(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body);

    /**
     * Starts the context when entering the tag.
     *
     * @param env The page context to use.
     */
    protected void startContext(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        if (container != null) {
            attributeContext = container.startContext(env);
        }
    }

    /**
     * Ends the context when exiting the tag.
     *
     * @param context The page context to use.
     */
    protected void endContext(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        if (attributeContext != null && container != null) {
            container.endContext(env);
        }
    }

    /**
     * <p>
     * Process nested &lg;put&gt; tag.
     * <p/>
     * <p>
     * Places the value of the nested tag within the
     * {@link org.apache.tiles.AttributeContext}.It is the responsibility
     * of the descendent to check security. Security will be managed by
     * called tags.
     * </p>
     *
     * @param nestedTag the put tag desciendent.
     */
    public void processNestedModel(PutAttributeModel nestedTag) {
        Attribute attribute = new Attribute(
            nestedTag.getValue(), nestedTag.getRole(),
            nestedTag.getType());

        attributeContext.putAttribute(nestedTag.getName(), attribute, nestedTag
                .isCascade());
    }

    /** {@inheritDoc} */
    public void processNestedModel(PutListAttributeModel nestedTag) {
        ListAttribute attribute = new ListAttribute(nestedTag.getAttributes());
        attribute.setRole(nestedTag.getRole());
        attribute.setInherit(nestedTag.getInherit());

        attributeContext.putAttribute(nestedTag.getName(), attribute, nestedTag
                .isCascade());
    }
}
