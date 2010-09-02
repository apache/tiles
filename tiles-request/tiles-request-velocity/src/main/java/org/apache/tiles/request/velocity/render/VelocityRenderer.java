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

package org.apache.tiles.request.velocity.render;

import java.io.IOException;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.InvalidTemplateException;
import org.apache.tiles.request.render.TypeDetectingRenderer;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityView;

/**
 * Attribute renderer for rendering Velocity templates as attributes. <br>
 * It is available only to Servlet-based environment.<br>
 * It uses {@link VelocityView} to render the response.<br>
 * To initialize it correctly, call {@link #setParameter(String, String)} for
 * all the parameters that you want to set, and then call {@link #commit()}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class VelocityRenderer implements TypeDetectingRenderer {

    /**
     * The VelocityView object to use.
     */
    private VelocityView velocityView;

    public VelocityRenderer(VelocityView velocityView) {
        this.velocityView = velocityView;
    }

    /** {@inheritDoc} */
    @Override
    public void render(String value, Request request) throws IOException {
        if (value != null) {
            if (value instanceof String) {
                ServletRequest servletRequest = ServletUtil.getServletRequest(request);
                // then get a context
                Context context = velocityView.createContext(servletRequest
                        .getRequest(), servletRequest.getResponse());

                // get the template
                Template template = velocityView.getTemplate((String) value);

                // merge the template and context into the writer
                velocityView.merge(template, context, request.getWriter());
            } else {
                throw new InvalidTemplateException(
                        "Cannot render a template that is not a string: "
                                + value.toString());
            }
        } else {
            throw new InvalidTemplateException("Cannot render a null template");
        }
    }

    /** {@inheritDoc} */
    public boolean isRenderable(String string, Request request) {
        return string.startsWith("/") && string.endsWith(".vm");
    }
}
