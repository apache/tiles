/*
 * $Id$
 *
 * Copyright 1999-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.context;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesRequestContext;
import org.apache.tiles.context.portlet.PortletTilesApplicationContext;
import org.apache.tiles.context.portlet.PortletTilesRequestContext;
import org.apache.tiles.context.servlet.ServletTilesApplicationContext;
import org.apache.tiles.context.servlet.ServletTilesRequestContext;
import org.apache.tiles.context.TilesContextFactory;

import java.util.Map;

/**
 * Creates an instance of the appropriate TilesApplicationContext implementation.
 *
 * @version $Rev: 405486 $ $Date$
 */
public class BasicTilesContextFactory implements TilesContextFactory {

    public void init(Map configParameters) {}

    /**
     * Creates a TilesApplicationContext from the given context.
     */
    public TilesApplicationContext createApplicationContext(Object context) {
        if (context instanceof ServletContext) {
            ServletContext servletContext = (ServletContext)context;
            TilesApplicationContext ctx = new ServletTilesApplicationContext(servletContext);
            TilesContextAccess.registerApplicationContext(servletContext, ctx);
            return ctx;

        } else if (context instanceof PortletContext) {
            PortletContext portletContext = (PortletContext)context;
            TilesApplicationContext ctx = new PortletTilesApplicationContext(portletContext);
            TilesContextAccess.registerApplicationContext(portletContext, ctx);
            return ctx;
        } else {
            throw new IllegalArgumentException("Invalid context specified. "
                    + context.getClass().getName());
        }
    }

    public TilesRequestContext createRequestContext(TilesApplicationContext context,
                                                    Object request,
                                                    Object response) {
        if (context instanceof ServletTilesApplicationContext) {
            ServletTilesApplicationContext app = (ServletTilesApplicationContext)context;
            
            return new ServletTilesRequestContext(app.getServletContext(),
                                                  (HttpServletRequest)request,
                                                  (HttpServletResponse)response);
        } else if (context instanceof PortletContext) {
            PortletTilesApplicationContext app = (PortletTilesApplicationContext)context;
            return new PortletTilesRequestContext(app.getPortletContext(),
                                                  (PortletRequest)request,
                                                  (PortletResponse)response);
                } else {
            throw new IllegalArgumentException("Invalid context specified. "
                    + context.getClass().getName());
        }
    }
}
