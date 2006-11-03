/*
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
 *
 */

package org.apache.tiles.context;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.jsp.JspTilesRequestContext;
import org.apache.tiles.context.portlet.PortletTilesApplicationContext;
import org.apache.tiles.context.portlet.PortletTilesRequestContext;
import org.apache.tiles.context.servlet.ServletTilesApplicationContext;
import org.apache.tiles.context.servlet.ServletTilesRequestContext;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.Map;

/**
 * Creates an instance of the appropriate TilesApplicationContext implementation.
 *
 * @version $Rev: 405486 $ $Date$
 */
public class BasicTilesContextFactory implements TilesContextFactory {

    public void init(Map configParameters) {
    }

    /**
     * Creates a TilesApplicationContext from the given context.
     */
    public TilesApplicationContext createApplicationContext(Object context) {
        if (context instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) context;
            return new ServletTilesApplicationContext(servletContext);

        } else if (context instanceof PortletContext) {
            PortletContext portletContext = (PortletContext) context;
            return new PortletTilesApplicationContext(portletContext);
        } else {
            throw new IllegalArgumentException("Invalid context specified. "
                + context.getClass().getName());
        }
    }

    public TilesRequestContext createRequestContext(TilesApplicationContext context,
                                                    Object request,
                                                    Object response) {
        ServletContext servletContext = getServletContext(context);
        PortletContext portletContext = getPortletContext(context);
        if (servletContext != null) {
            return new ServletTilesRequestContext(servletContext,
                (HttpServletRequest) request,
                (HttpServletResponse) response);
        } else if (portletContext != null) {
            PortletTilesApplicationContext app = (PortletTilesApplicationContext) context;
            return new PortletTilesRequestContext(app.getPortletContext(),
                (PortletRequest) request,
                (PortletResponse) response);
        } else {
            throw new IllegalArgumentException("Invalid context specified. "
                + context.getClass().getName());
        }
    }

    public TilesRequestContext createRequestContext(TilesApplicationContext context,
                                                    PageContext pageContext) {
        ServletContext servletContext = getServletContext(context);
        if (servletContext != null) {
            return new JspTilesRequestContext(servletContext, pageContext);
        }
        throw new IllegalArgumentException("The context/pageContext combination is not supported.");
    }

    protected ServletContext getServletContext(TilesApplicationContext context) {
        if (context instanceof ServletTilesApplicationContext) {
            ServletTilesApplicationContext app = (ServletTilesApplicationContext) context;
            return app.getServletContext();
        }
        return null;

    }

    protected PortletContext getPortletContext(TilesApplicationContext context) {
        if (context instanceof PortletTilesApplicationContext) {
            PortletTilesApplicationContext app = (PortletTilesApplicationContext) context;
            return app.getPortletContext();
        }
        return null;
    }
}
