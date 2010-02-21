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

import org.apache.tiles.Attribute;
import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.InsertAttributeModel;

import freemarker.template.TemplateModel;

/**
 * Wraps {@link InsertAttributeModel} to be used in FreeMarker. For the list of
 * parameters, see
 * {@link InsertAttributeModel #start(boolean, String, String, Object, String, String, String, Attribute, Request)}
 * and
 * {@link InsertAttributeModel #end(boolean, Request)}
 * .
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class InsertAttributeFMModel extends BodyFMModel {

    /**
     * The template model.
     */
    private InsertAttributeModel model;

    /**
     * Constructor.
     *
     * @param model
     *            The template model.
     * @since 2.2.0
     */
    public InsertAttributeFMModel(InsertAttributeModel model) {
        this.model = model;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(Map<String, TemplateModel> parms, Request request,
            ModelBody modelBody) throws IOException {
        model.execute(FreeMarkerUtil.getAsBoolean(parms.get("ignore"), false),
                FreeMarkerUtil.getAsString(parms.get("preparer")),
                FreeMarkerUtil.getAsString(parms.get("role")), FreeMarkerUtil
                        .getAsObject(parms.get("defaultValue")), FreeMarkerUtil
                        .getAsString(parms.get("defaultValueRole")),
                FreeMarkerUtil.getAsString(parms.get("defaultValueType")),
                FreeMarkerUtil.getAsString(parms.get("name")),
                (Attribute) FreeMarkerUtil.getAsObject(parms.get("value")),
                request, modelBody);
    }

}
