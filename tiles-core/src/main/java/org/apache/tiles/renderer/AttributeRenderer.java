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

import java.io.IOException;
import java.io.Writer;

import org.apache.tiles.Attribute;

/**
 * An object that can render an attribute. For each attribute, if it needs to be
 * rendered, has an associated renderer.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public interface AttributeRenderer {

    /**
     * Renders an attribute.
     *
     * @param attribute The attribute to render.
     * @param writer The writer to use.
     * @param requestItems The request items.
     * @throws IOException If something goes wrong during rendition.
     * @throws RendererException If something goes wrong during rendition.
     * @since 2.1.0
     */
    void render(Attribute attribute, Writer writer, Object... requestItems)
            throws IOException;
}
