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
package com.anydoby.tiles2.spring;

import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ChainedContext;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

/**
 * 
 * @author anydoby
 * 
 * @since Mar 16, 2008
 */
public class VelocityTiles2View extends VelocityToolboxView {

    private static class TilesTemplate extends Template {

        public TilesTemplate(String definitionName) {
            setName(definitionName);
        }

    }

    protected void doRender(Context context, HttpServletResponse response) throws Exception {
        TilesContainer container = TilesAccess.getContainer(getServletContext());
        if (context instanceof ChainedContext) {
            ChainedContext ctx = (ChainedContext) context;
            container.render(getUrl(), ctx);
        } else {
            throw new UnsupportedOperationException("Web context is required");
        }
    }

    @Override
    protected Template getTemplate(final String definitionName) throws Exception {
        return new TilesTemplate(definitionName);
    }

}
