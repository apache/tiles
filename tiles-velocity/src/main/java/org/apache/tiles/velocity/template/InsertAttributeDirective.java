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
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.InsertAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;

/**
 * Wraps {@link InsertAttributeModel} to be used in Velocity. For the list of
 * parameters, see
 * {@link InsertAttributeModel#start(java.util.Stack, org.apache.tiles.TilesContainer, boolean,
 * String, String, Object, String, String, String, Attribute, Object...)}
 * , {@link InsertAttributeModel#end(java.util.Stack, org.apache.tiles.TilesContainer, boolean, Object...)} and
 * {@link InsertAttributeModel#execute(org.apache.tiles.TilesContainer, boolean, String, String,
 * Object, String, String, String, Attribute, Object...)}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.2
 */
public class InsertAttributeDirective extends BlockDirective {

    /**
     * The template model.
     */
    private InsertAttributeModel model = new InsertAttributeModel(
            new DefaultAttributeResolver());

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "tiles_insertAttribute";
    }

    /** {@inheritDoc} */
    @Override
    public void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext)
            throws IOException {
        model.end(ServletUtil.getComposeStack(request), ServletUtil.getCurrentContainer(request,
                servletContext), VelocityUtil.toSimpleBoolean((Boolean) params
                        .get("ignore"), false), context, request, response,
                writer);
    }

    /** {@inheritDoc} */
    @Override
    public void start(InternalContextAdapter context,
            Writer writer, Map<String, Object> params,
            HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
        model.start(ServletUtil.getComposeStack(request), ServletUtil.getCurrentContainer(request,
                servletContext), VelocityUtil.toSimpleBoolean((Boolean) params
                        .get("ignore"), false), (String) params
                .get("preparer"), (String) params.get("role"), params
                .get("defaultValue"), (String) params.get("defaultValueRole"),
                (String) params.get("defaultValueType"), (String) params
                        .get("name"), (Attribute) params.get("value"), context,
                request, response);
    }

}
