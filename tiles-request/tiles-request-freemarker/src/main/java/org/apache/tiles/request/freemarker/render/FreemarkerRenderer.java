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

package org.apache.tiles.request.freemarker.render;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.freemarker.FreemarkerRequestException;
import org.apache.tiles.request.render.CannotRenderException;
import org.apache.tiles.request.render.Renderer;
import org.apache.tiles.request.servlet.ExternalWriterHttpServletResponse;
import org.apache.tiles.request.servlet.ServletRequest;

/**
 * FreeMarker renderer for rendering FreeMarker templates as Tiles attributes.
 * It is only usable under a Servlet environment, because it uses
 * {@link AttributeValueFreemarkerServlet} internally to forward the request.<br/>
 * To initialize it correctly, call {@link #setParameter(String, String)} for all the
 * parameters that you want to set, and then call {@link #commit()}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class FreemarkerRenderer implements Renderer {

    /**
     * The servlet that is used to forward the request to.
     */
    private AttributeValueFreemarkerServlet servlet;

    /**
     * Constructor.
     *
     * @param servlet The servlet to use.
     */
    public FreemarkerRenderer(AttributeValueFreemarkerServlet servlet) {
        this.servlet = servlet;
    }

    /** {@inheritDoc} */
    @Override
    public void render(String path, Request request) throws IOException {
        if (path == null) {
            throw new CannotRenderException("Cannot dispatch a null path");
        }
        ServletRequest servletRequest = org.apache.tiles.request.servlet.ServletUtil.getServletRequest(request);
        HttpServletRequest httpRequest = servletRequest.getRequest();
        HttpServletResponse httpResponse = servletRequest.getResponse();
        servlet.setValue(path);
        try {
            servlet.doGet(httpRequest,
                    new ExternalWriterHttpServletResponse(httpResponse,
                            request.getPrintWriter()));
        } catch (ServletException e) {
            throw new FreemarkerRequestException("Exception when rendering a FreeMarker attribute", e);
        }
    }

    /** {@inheritDoc} */
    public boolean isRenderable(String path, Request request) {
        return path != null && path.startsWith("/") && path.endsWith(".ftl");
    }
}
