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

package org.apache.tiles.velocity.context;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletTilesRequestContext;
import org.apache.tiles.request.servlet.ServletUtil;
import org.apache.tiles.request.servlet.ExternalWriterHttpServletResponse;
import org.apache.tiles.request.util.TilesRequestContextWrapper;
import org.apache.velocity.context.Context;

/**
 * The implementation of the Tiles request context specific for Velocity.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class VelocityTilesRequestContext extends TilesRequestContextWrapper {

    /**
     * The Velocity current context.
     */
    private final Context ctx;

    /**
     * The request objects.
     */
    private Object[] requestObjects;

    /**
     * The writer to use to render the response. It may be null, if not necessary.
     */
    private Writer writer;

    /**
     * Constructor.
     *
     * @param enclosedRequest The request that exposes non-Velocity specific properties
     * @param ctx The Velocity current context.
     * @param writer The writer to use to render the response. It may be null, if not necessary.
     * @since 2.2.0
     */
    public VelocityTilesRequestContext(
            Request enclosedRequest, Context ctx, Writer writer) {
        super(enclosedRequest);
        this.ctx = ctx;
        this.writer = writer;
    }

    /** {@inheritDoc} */
    @Override
	public void dispatch(String path) throws IOException {
        include(path);
    }

    /** {@inheritDoc} */
    @Override
    public void include(String path) throws IOException {
        Object[] requestObjects = super.getRequestObjects();
        HttpServletRequest request = (HttpServletRequest) requestObjects[0];
        HttpServletResponse response = (HttpServletResponse) requestObjects[1];
        ServletUtil.setForceInclude(request, true);
        RequestDispatcher rd = request.getRequestDispatcher(path);

        if (rd == null) {
            throw new IOException("No request dispatcher returned for path '"
                    + path + "'");
        }

        PrintWriter printWriter = getPrintWriter();
        try {
            rd.include(request, new ExternalWriterHttpServletResponse(response,
                    printWriter));
        } catch (ServletException ex) {
            throw ServletUtil.wrapServletException(ex, "ServletException including path '"
                    + path + "'.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public PrintWriter getPrintWriter() throws IOException {
        if (writer == null) {
            throw new IllegalStateException(
                    "A writer-less Tiles request has been created, cannot return a PrintWriter");
        }
        if (writer instanceof PrintWriter) {
            return (PrintWriter) writer;
        } else {
            return new PrintWriter(writer);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Writer getWriter() throws IOException {
        if (writer == null) {
            throw new IllegalStateException(
                    "A writer-less Tiles request has been created, cannot return a PrintWriter");
        }
        return writer;
    }

    /** {@inheritDoc} */
    @Override
    public Object[] getRequestObjects() {
        if (requestObjects == null) {
            Object[] parentRequestObjects = super.getRequestObjects();
            if (writer == null) {
                requestObjects = new Object[parentRequestObjects.length + 1];
            } else {
                requestObjects = new Object[parentRequestObjects.length + 2];
            }
            requestObjects[0] = ctx;
            for (int i = 0; i < parentRequestObjects.length; i++) {
                requestObjects[i + 1] = parentRequestObjects[i];
            }
            if (writer != null) {
                requestObjects[parentRequestObjects.length + 1] = writer;
            }
        }
        return requestObjects;
    }

	public static Request createVelocityRequest(ApplicationContext applicationContext,
			HttpServletRequest request, HttpServletResponse response,
			Context velocityContext, Writer writer) {
		Request servletRequest = new ServletTilesRequestContext(
				applicationContext, request, response);
		Request velocityRequest = new VelocityTilesRequestContext(
				servletRequest, velocityContext, writer);
		return velocityRequest;
	}
}
