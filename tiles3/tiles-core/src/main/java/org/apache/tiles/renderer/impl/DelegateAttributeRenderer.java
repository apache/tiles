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
package org.apache.tiles.renderer.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.tiles.Attribute;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.renderer.RendererException;
import org.apache.tiles.renderer.TypeDetectingAttributeRenderer;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.TypeDetectingRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delegates a {@link TypeDetectingRenderer} to render the value of an
 * attribute.
 *
 * @version $Rev$ $Date$
 * @since 3.0.0
 */
public class DelegateAttributeRenderer implements
        TypeDetectingAttributeRenderer {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(AbstractBaseAttributeRenderer.class);

    private TypeDetectingRenderer renderer;

    /**
     * The attribute evaluator factory.
     */
    private AttributeEvaluatorFactory attributeEvaluatorFactory;

    public DelegateAttributeRenderer(TypeDetectingRenderer renderer,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        this.renderer = renderer;
        this.attributeEvaluatorFactory = attributeEvaluatorFactory;
    }

    /** {@inheritDoc} */
    public void render(Attribute attribute, Request request) throws IOException {
        Set<String> roles = attribute.getRoles();
        if (!isPermitted(request, roles)) {
            log.debug("Access to attribute denied.  User not in role '{}'",
                    roles);
            return;
        }

        Object value = getValue(attribute, request);

        if (value == null) {
            throw new NullPointerException("Attribute value is null");
        }

        if (!(value instanceof String)) {
            throw new RendererException("The attribute value is not a string, to string is: " + value.toString());
        }
        renderer.render((String) value, request);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRenderable(Attribute attribute, Request request) {
        return isRenderable(getValue(attribute, request), attribute, request);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRenderable(Object value, Attribute attribute,
            Request request) {
        return value != null && value instanceof String && renderer.isRenderable((String) value, request);
    }

    /**
     * Checks if the current user is in one of the comma-separated roles
     * specified in the <code>role</code> parameter.
     *
     * @param request The request context.
     * @param roles The list of roles.
     * @return <code>true</code> if the current user is in one of those roles.
     */
    private boolean isPermitted(Request request, Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return true;
        }

        boolean retValue = false;

        for (Iterator<String> roleIt = roles.iterator(); roleIt.hasNext()
                && !retValue;) {
            retValue = request.isUserInRole(roleIt.next());
        }

        return retValue;
    }

    private Object getValue(Attribute attribute, Request request) {
        AttributeEvaluator evaluator = attributeEvaluatorFactory
                .getAttributeEvaluator(attribute);
        return evaluator.evaluate(attribute, request);
    }
}
