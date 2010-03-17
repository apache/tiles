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

import java.util.Map;

import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.SetCurrentContainerModel;

import freemarker.template.TemplateModel;

/**
 * Sets the current container, to be used in Tiles templates. Use the
 * "containerKey" parameter to indicate the name of the container to use.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class SetCurrentContainerFMModel extends BodylessFMModel {

    private SetCurrentContainerModel model;

    public SetCurrentContainerFMModel(SetCurrentContainerModel model) {
        this.model = model;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(Map<String, TemplateModel> parms, Request request) {
        model.execute(FreeMarkerUtil.getAsString(parms.get("containerKey")),
                request);
    }
}