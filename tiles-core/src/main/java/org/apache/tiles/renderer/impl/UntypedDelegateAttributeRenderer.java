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

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererException;

/**
 * Renders an attribute that has no associated renderer using delegation to
 * other renderers.
 *
 * @version $Rev$ $Date$
 * @since 2.2.1
 */
public class UntypedDelegateAttributeRenderer extends AbstractBaseAttributeRenderer {

    /**
     * The Tiles container.
     */
    private TilesContainer container;

    /**
     * The renderer for attributes of type "string".
     */
    private AttributeRenderer stringRenderer;

    /**
     * The renderer for attributes of type "template".
     */
    private AttributeRenderer templateRenderer;

    /**
     * The renderer for attributes of type "definition".
     */
    private AttributeRenderer definitionRenderer;

    /**
     * Constructor.
     *
     * @param container The Tiles container.
     * @param stringRenderer The renderer for attributes of type "string".
     * @param templateRenderer The renderer for attributes of type "template".
     * @param definitionRenderer The renderer for attributes of type
     * "definition".
     * @since 2.2.1
     */
    public UntypedDelegateAttributeRenderer(TilesContainer container,
            AttributeRenderer stringRenderer,
            AttributeRenderer templateRenderer,
            AttributeRenderer definitionRenderer) {
        this.container = container;
        this.stringRenderer = stringRenderer;
        this.templateRenderer = templateRenderer;
        this.definitionRenderer = definitionRenderer;
    }

    /** {@inheritDoc} */
    @Override
    public void write(Object value, Attribute attribute,
            TilesRequestContext request)
            throws IOException {
        if (value instanceof String) {
            String valueString = (String) value;
            Object[] requestItems = request.getRequestObjects();
            if (container.isValidDefinition(valueString, requestItems)) {
                definitionRenderer.render(attribute, request);
            } else if (valueString.startsWith("/")) {
                templateRenderer.render(attribute, request);
            } else {
                stringRenderer.render(attribute, request);
            }
        } else {
            throw new RendererException(
                    "Cannot render an untyped object attribute");
        }
    }
}
