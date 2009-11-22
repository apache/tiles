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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletUtil;
import org.apache.tiles.template.AddAttributeModel;
import org.apache.tiles.velocity.context.VelocityTilesRequestContext;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.Renderable;

/**
 * Wraps {@link AddAttributeModel} to be used in Velocity. For the list of
 * parameters, see {@link AddAttributeModel#start(Request)},
 * {@link AddAttributeModel#end(Object, String, String, String, String, Request)}
 * and {@link AddAttributeModel#execute(Object, String, String, String, String, Request)}
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class AddAttributeVModel implements Executable, BodyExecutable {

    /**
     * The template model.
     */
    private AddAttributeModel model;

    /**
     * The Servlet context.
     */
    private ServletContext servletContext;

    /**
     * Constructor.
     *
     * @param model The template model.
     * @param servletContext TODO
     * @since 2.2.0
     */
    public AddAttributeVModel(AddAttributeModel model, ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }

    /** {@inheritDoc} */
    public Renderable execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        Request currentRequest = VelocityTilesRequestContext
                .createVelocityRequest(ServletUtil
                        .getApplicationContext(servletContext), request,
                        response, velocityContext, null);
        model.execute(params.get("value"),
                (String) params.get("expression"), null, (String) params.get("role"),
                (String) params.get("type"), currentRequest);
        return VelocityUtil.EMPTY_RENDERABLE;
    }

    /** {@inheritDoc} */
    public Renderable end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(
                velocityContext).pop();
        Request currentRequest = VelocityTilesRequestContext
                .createVelocityRequest(ServletUtil
                        .getApplicationContext(servletContext), request,
                        response, velocityContext, null);
        model.end(params.get("value"), (String) params.get("expression"),
                null, (String) params
                        .get("role"), (String) params.get("type"), currentRequest);
        return VelocityUtil.EMPTY_RENDERABLE;
    }

    /** {@inheritDoc} */
    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        Request currentRequest = VelocityTilesRequestContext
                .createVelocityRequest(ServletUtil
                        .getApplicationContext(servletContext), request,
                        response, velocityContext, null);
        model.start(currentRequest);
    }
}
