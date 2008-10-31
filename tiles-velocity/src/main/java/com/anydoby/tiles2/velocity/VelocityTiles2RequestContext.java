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
package com.anydoby.tiles2.velocity;

import java.io.IOException;

import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.view.context.ChainedContext;

import static org.apache.tiles.access.TilesAccess.getContainer;

/**
 * 
 * @author SergeyZ
 * 
 */
public class VelocityTiles2RequestContext extends ServletTilesRequestContext {

    private final ChainedContext ctx;

    public VelocityTiles2RequestContext(ChainedContext ctx) {
        super(ctx.getServletContext(), ctx.getRequest(), ctx.getResponse());
        this.ctx = ctx;
        ctx.put("tiles", new Tiles2Tool(getContainer(ctx.getServletContext()), this));
    }

    public void dispatch(String path) throws IOException {
        include(path);
    }

    public ChainedContext getContext() {
        return ctx;
    }

    public VelocityEngine getEngine() {
        VelocityEngine velocityEngine = ctx.getVelocityEngine();
        return velocityEngine;
    }

    @Override
    public void include(String path) throws IOException {
        try {
            Template template = ctx.getVelocityEngine().getTemplate(path);
            template.merge(ctx, getResponse().getWriter());
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void put(String toName, Object attribute) {
        ctx.put(toName, attribute);
    }

}
