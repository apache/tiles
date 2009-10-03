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
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.Attribute;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.renderer.RendererException;
import org.apache.tiles.renderer.TypeDetectingAttributeRenderer;

/**
 * Renders an attribute that has no associated renderer using delegation to
 * other renderers.
 *
 * @version $Rev$ $Date$
 * @since 2.2.1
 */
public class ChainedDelegateAttributeRenderer extends AbstractBaseAttributeRenderer {

    /**
     * The list of chained renderers.
     */
    private List<TypeDetectingAttributeRenderer> renderers;

    /**
     * Constructor.
     *
     * @since 2.2.1
     */
    public ChainedDelegateAttributeRenderer() {
        renderers = new ArrayList<TypeDetectingAttributeRenderer>();
    }

    /**
     * Adds an attribute renderer to the list. The first inserted this way, the
     * first is checked when rendering.
     *
     * @param renderer The renderer to add.
     */
    public void addAttributeRenderer(TypeDetectingAttributeRenderer renderer) {
        renderers.add(renderer);
    }

    /** {@inheritDoc} */
    @Override
    public void write(Object value, Attribute attribute,
            TilesRequestContext request)
            throws IOException {
        if (value == null) {
            throw new NullPointerException("The attribute value is null");
        }

        for (TypeDetectingAttributeRenderer renderer : renderers) {
            if (renderer.isRenderable(value, attribute, request)) {
                renderer.render(attribute, request);
                return;
            }
        }

        throw new RendererException("Type of the attribute not found, class '"
                + value.getClass() + "' value '" + value.toString() + "'");
    }
}
