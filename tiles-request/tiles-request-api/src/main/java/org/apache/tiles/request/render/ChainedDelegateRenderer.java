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
package org.apache.tiles.request.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.request.Request;

/**
 * Renders an attribute that has no associated renderer using delegation to
 * other renderers.
 *
 * @version $Rev$ $Date$
 * @since 2.2.1
 */
public class ChainedDelegateRenderer implements Renderer {

    /**
     * The list of chained renderers.
     */
    private List<TypeDetectingRenderer> renderers;

    /**
     * Constructor.
     *
     * @since 2.2.1
     */
    public ChainedDelegateRenderer() {
        renderers = new ArrayList<TypeDetectingRenderer>();
    }

    /**
     * Adds an attribute renderer to the list. The first inserted this way, the
     * first is checked when rendering.
     *
     * @param renderer The renderer to add.
     */
    public void addAttributeRenderer(TypeDetectingRenderer renderer) {
        renderers.add(renderer);
    }


    @Override
    public void render(String value, Request request) throws IOException {
        if (value == null) {
            throw new NullPointerException("The attribute value is null");
        }

        for (TypeDetectingRenderer renderer : renderers) {
            if (renderer.isRenderable(value, request)) {
                renderer.render(value, request);
                return;
            }
        }

        throw new CannotRenderException("Cannot renderer value '" + value + "'");
    }
}
