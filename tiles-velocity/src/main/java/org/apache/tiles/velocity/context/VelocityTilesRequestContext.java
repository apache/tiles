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

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextWrapper;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.velocity.context.Context;

/**
 * 
 * @author SergeyZ
 * 
 */
public class VelocityTilesRequestContext extends TilesRequestContextWrapper {

    private final Context ctx;
    
    private Object[] requestObjects;
    
    private Writer writer;

    public VelocityTilesRequestContext(
            TilesRequestContext enclosedRequest, Context ctx, Writer writer) {
        super(enclosedRequest);
        this.ctx = ctx;
        this.writer = writer;
    }

    public void dispatch(String path) throws IOException {
        include(path);
    }

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

    @Override
    public Writer getWriter() throws IOException {
        if (writer == null) {
            throw new IllegalStateException(
                    "A writer-less Tiles request has been created, cannot return a PrintWriter");
        }
        return writer;
    }

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
                requestObjects[i+1] = parentRequestObjects[i];
            }
            if (writer != null) {
                requestObjects[parentRequestObjects.length + 1] = writer;
            }
        }
        return requestObjects;
    }
}
