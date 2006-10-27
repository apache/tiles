/*
 * $Id$
 *
 * Copyright 2006 The Apache Software Foundation.
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

package org.apache.tiles.factory;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesRequestContext;
import org.apache.tiles.context.servlet.ServletTilesApplicationContext;
import org.apache.tiles.context.servlet.ServletTilesRequestContext;
import org.apache.tiles.context.portlet.PortletTilesApplicationContext;
import org.apache.tiles.context.portlet.PortletTilesRequestContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

public class TilesContextFactory {

    public static TilesApplicationContext createApplicationContext(Object context)
        throws ConfigurationNotSupportedException {
        if (context instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) context;
            return new ServletTilesApplicationContext(servletContext);

        } else if (context instanceof PortletContext) {
            PortletContext portletContext = (PortletContext) context;
            return new PortletTilesApplicationContext(portletContext);
        } else {
            throw new ConfigurationNotSupportedException("Invalid context specified. "
                    + context.getClass().getName());
        }
    }

    public static TilesRequestContext createRequestContext(TilesApplicationContext context,
                                                    Object request,
                                                    Object response)
        throws ConfigurationNotSupportedException {
        if (context instanceof ServletTilesApplicationContext) {
            ServletTilesApplicationContext app = (ServletTilesApplicationContext) context;
            return new ServletTilesRequestContext(app.getServletContext(),
                    (HttpServletRequest) request,
                    (HttpServletResponse) response);
        } else if (context instanceof PortletContext) {
            PortletTilesApplicationContext app = (PortletTilesApplicationContext) context;
            return new PortletTilesRequestContext(app.getPortletContext(),
                    (PortletRequest) request,
                    (PortletResponse) response);
        } else {
            throw new ConfigurationNotSupportedException("Invalid context specified. "
                    + context.getClass().getName());
        }
    }
}
