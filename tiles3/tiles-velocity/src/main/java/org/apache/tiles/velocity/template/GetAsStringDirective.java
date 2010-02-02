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
import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletUtil;
import org.apache.tiles.request.velocity.VelocityRequest;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.GetAsStringModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;

/**
 * Wraps {@link GetAsStringModel} to be used in Velocity. For the list of
 * parameters, see
 * {@link GetAsStringModel#start(java.util.Stack, org.apache.tiles.TilesContainer, boolean, String, String, Object, String, String, String, Attribute, Object...)}
 * ,
 * {@link GetAsStringModel#end(java.util.Stack, org.apache.tiles.TilesContainer, Writer, boolean, Object...)}
 * and
 * {@link GetAsStringModel#execute(TilesContainer, Writer, boolean, String, String, Object, String, String, String, Attribute, Object...)}
 * .
 *
 * @version $Rev$ $Date$
 * @since 2.2.2
 */
public class GetAsStringDirective extends BlockDirective {

    /**
     * The template model.
     */
    private GetAsStringModel model = new GetAsStringModel(
            new DefaultAttributeResolver());

    /**
     * Default constructor.
     *
     * @since 2.2.2
     */
    public GetAsStringDirective() {
        // Does nothing.
    }

    /**
     * Constructor.
     *
     * @param model The used model.
     * @since 2.2.2
     */
    public GetAsStringDirective(GetAsStringModel model) {
        this.model = model;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "tiles_getAsString";
    }

    /** {@inheritDoc} */
    @Override
    protected void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext)
            throws IOException {
        Request currentRequest = VelocityRequest.createVelocityRequest(
                ServletUtil.getApplicationContext(servletContext), request,
                response, context, writer);
        model.end(VelocityUtil.toSimpleBoolean((Boolean) params.get("ignore"),
                false), currentRequest);
    }

    /** {@inheritDoc} */
    @Override
    protected void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {
        Request currentRequest = VelocityRequest.createVelocityRequest(
                ServletUtil.getApplicationContext(servletContext), request,
                response, context, writer);
        model.start(VelocityUtil.toSimpleBoolean(
                (Boolean) params.get("ignore"), false), (String) params
                .get("preparer"), (String) params.get("role"), params
                .get("defaultValue"), (String) params.get("defaultValueRole"),
                (String) params.get("defaultValueType"), (String) params
                        .get("name"), (Attribute) params.get("value"),
                currentRequest);
    }

}