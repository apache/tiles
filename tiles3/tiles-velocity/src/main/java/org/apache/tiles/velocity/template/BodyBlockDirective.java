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

import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewContext;

/**
 * Base abstract directive for those models who need to evaluate and use a
 * body.
 *
 * @version $Rev$ $Date$
 * @since 2.2.2
 */
public abstract class BodyBlockDirective extends Directive {

    /** {@inheritDoc} */
    @Override
    public int getType() {
        return BLOCK;
    }

    /** {@inheritDoc} */

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException {
        ViewContext viewContext = (ViewContext) context
                .getInternalUserContext();
        Map<String, Object> params = VelocityUtil.getParameters(context, node);
        HttpServletRequest request = viewContext.getRequest();
        HttpServletResponse response = viewContext.getResponse();
        ServletContext servletContext = viewContext.getServletContext();
        start(context, writer, params, request, response, servletContext);
        String body = VelocityUtil.getBodyAsString(context, node);
        end(context, writer, params, body, request, response, servletContext);
        return true;
    }

    /**
     * Starts the directive, before evaluating the body.
     *
     * @param context The Velocity context.
     * @param writer The writer user to write the result.
     * @param params The parameters got from the first node of the directive.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param servletContext The servlet context.
     * @since 2.2.2
     */
    protected abstract void start(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext);

    /**
     * Ends the directive, after evaluating the body.
     *
     * @param context The Velocity context.
     * @param writer The writer user to write the result.
     * @param params The parameters got from the first node of the directive.
     * @param body The body contained in this directive, as a string.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param servletContext The servlet context.
     * @throws IOException If something goes wrong when finishing this directive.
     * @since 2.2.2
     */
    protected abstract void end(InternalContextAdapter context, Writer writer,
            Map<String, Object> params, String body,
            HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
            throws IOException;
}
