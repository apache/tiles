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

package org.apache.tiles.velocity.template;

import java.util.Map;

import org.apache.tiles.autotag.velocity.runtime.BodylessDirective;
import org.apache.tiles.autotag.velocity.runtime.VelocityUtil;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.ImportAttributeModel;

/**
 * Wraps {@link ImportAttributeModel} to be used in Velocity. For the list of
 * parameters, see
 * {@link ImportAttributeModel#getImportedAttributes(org.apache.tiles.TilesContainer, String, String, boolean, Object...)}
 * .
 *
 * @version $Rev$ $Date$
 * @since 2.2.2
 */
public class ImportAttributeDirective extends BodylessDirective {

    /**
     * The template model.
     */
    private ImportAttributeModel model = new ImportAttributeModel();

    /**
     * Default constructor.
     *
     * @since 2.2.2
     */
    public ImportAttributeDirective() {
        // Does nothing.
    }

    /**
     * Constructor.
     *
     * @param model The used model.
     * @since 2.2.2
     */
    public ImportAttributeDirective(ImportAttributeModel model) {
        this.model = model;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "tiles_importAttribute";
    }

    /** {@inheritDoc} */
    @Override
    public int getType() {
        return LINE;
    }

    /** {@inheritDoc} */
    @Override
    protected void execute(Map<String, Object> params, Request request) {
        model.execute((String) params.get("name"),
                (String) params.get("scope"), (String) params.get("toName"),
                VelocityUtil.toSimpleBoolean((Boolean) params.get("ignore"),
                        false), request);
    }

}
