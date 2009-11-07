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

package org.apache.tiles.renderer;

import org.apache.tiles.Attribute;
import org.apache.tiles.request.Request;

/**
 * It represents a renderer that identifies attributes that can render.
 *
 * @version $Rev$ $Date$
 * @since 2.2.1
 */
public interface TypeDetectingAttributeRenderer extends AttributeRenderer {

    /**
     * Checks if this renderer can render an attribute. Note that this does not mean
     * it is the <strong>best</strong> renderer available, but checks only its capability.
     *
     * @param attribute The attribute to be renderer.
     * @param request The Tiles request context.
     * @return <code>true</code> if this renderer can render the attribute.
     * @since 2.2.1
     */
    boolean isRenderable(Attribute attribute, Request request);

    /**
     * Checks if this renderer can render an attribute. Note that this does not mean
     * it is the <strong>best</strong> renderer available, but checks only its capability.
     *
     * @param value The attribute value, already evaluated.
     * @param attribute The attribute to be renderer.
     * @param request The Tiles request context.
     * @return <code>true</code> if this renderer can render the attribute.
     * @since 2.2.1
     */
    boolean isRenderable(Object value, Attribute attribute,
            Request request);
}
