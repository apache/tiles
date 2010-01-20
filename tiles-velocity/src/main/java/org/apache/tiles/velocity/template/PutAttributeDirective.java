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

import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.PutAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;

/**
 * Wraps {@link PutAttributeModel} to be used in Velocity. For the list of
 * parameters, see
 * {@link PutAttributeModel#start(java.util.Stack)}
 * , {@link PutAttributeModel#end(org.apache.tiles.TilesContainer,
 * java.util.Stack, String, Object, String, String, String, String, boolean, Object...)} and
 * {@link PutAttributeModel#execute(org.apache.tiles.TilesContainer,
 * java.util.Stack, String, Object, String, String, String, String, boolean, Object...)}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.2
 */
public class PutAttributeDirective extends BodyBlockDirective {

    /**
     * The template model.
     */
    private PutAttributeModel model = new PutAttributeModel();

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "tiles_putAttribute";
    }

    /** {@inheritDoc} */
    @Override
    public void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, String body,
            HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext) {
        model.end(ServletUtil.getCurrentContainer(request, servletContext),
                ServletUtil.getComposeStack(request), (String) params.get("name"), params.get("value"),
                (String) params.get("expression"), body, (String) params
                        .get("role"), (String) params.get("type"), VelocityUtil
                        .toSimpleBoolean((Boolean) params.get("cascade"), false),
                context, request, response, writer);
    }

    /** {@inheritDoc} */
    @Override
    public void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {
        model.start(ServletUtil.getComposeStack(request));
    }

}
