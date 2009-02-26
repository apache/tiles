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

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextWrapper;
import org.apache.velocity.context.Context;

/**
 * 
 * @author SergeyZ
 * 
 */
public class VelocityTilesRequestContext extends TilesRequestContextWrapper {

    private final Context ctx;
    
    private Object[] requestObjects;

    public VelocityTilesRequestContext(TilesRequestContext enclosedRequest, Context ctx) {
        super(enclosedRequest);
        this.ctx = ctx;
        // FIXME This should go into a renderer
        //ctx.put("tiles", new Tiles2Tool(getContainer(ctx.getServletContext()), this));
    }

    public void dispatch(String path) throws IOException {
        include(path);
    }

    @Override
    public Object[] getRequestObjects() {
        if (requestObjects == null) {
            Object[] parentRequestObjects = super.getRequestObjects();
            requestObjects = new Object[parentRequestObjects.length + 1];
            requestObjects[0] = ctx;
            for (int i = 0; i < parentRequestObjects.length; i++) {
                requestObjects[i+1] = parentRequestObjects[i];
            }
        }
        return requestObjects;
    }
}
