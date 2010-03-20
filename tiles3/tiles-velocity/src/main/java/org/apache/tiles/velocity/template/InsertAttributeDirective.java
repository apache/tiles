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

import java.io.IOException;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.autotag.velocity.runtime.BodyDirective;
import org.apache.tiles.autotag.velocity.runtime.VelocityUtil;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.InsertAttributeModel;

/**
 * Wraps {@link InsertAttributeModel} to be used in Velocity. For the list of
 * parameters, see
 * {@link InsertAttributeModel#start(java.util.Stack, org.apache.tiles.TilesContainer, boolean, String, String, Object, String, String, String, Attribute, Object...)}
 * ,
 * {@link InsertAttributeModel#end(java.util.Stack, org.apache.tiles.TilesContainer, boolean, Object...)}
 * and
 * {@link InsertAttributeModel#execute(org.apache.tiles.TilesContainer, boolean, String, String, Object, String, String, String, boolean, Attribute, Object...)}
 * .
 *
 * @version $Rev$ $Date$
 * @since 2.2.2
 */
public class InsertAttributeDirective extends BodyDirective {

    /**
     * The template model.
     */
    private InsertAttributeModel model = new InsertAttributeModel(
            new DefaultAttributeResolver());

    /**
     * Default constructor.
     *
     * @since 2.2.2
     */
    public InsertAttributeDirective() {
        // Does nothing.
    }

    /**
     * Constructor.
     *
     * @param model The used model.
     * @since 2.2.2
     */
    public InsertAttributeDirective(InsertAttributeModel model) {
        this.model = model;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "tiles_insertAttribute";
    }

    /** {@inheritDoc} */
    @Override
    protected void execute(Map<String, Object> params, Request request,
            ModelBody modelBody) throws IOException {
        model.execute(VelocityUtil.toSimpleBoolean((Boolean) params
                .get("ignore"), false), (String) params.get("preparer"),
                (String) params.get("role"), params.get("defaultValue"),
                (String) params.get("defaultValueRole"), (String) params
                        .get("defaultValueType"), (String) params.get("name"),
                (Attribute) params.get("value"), VelocityUtil.toSimpleBoolean(
                        (Boolean) params.get("flush"), false), request,
                modelBody);
    }

}
