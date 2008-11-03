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

package org.apache.tiles.jsp.context;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesContextFactoryAware;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.Map;

/**
 * Creates an instance of the appropriate TilesApplicationContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class JspTilesContextFactory implements TilesContextFactory, TilesContextFactoryAware {

    /**
     * Parent Tiles context factory.
     */
    private TilesContextFactory parent;

    /** {@inheritDoc} */
    public void init(Map<String, String> configParameters) {
    }

    /** {@inheritDoc} */
    public void setContextFactory(TilesContextFactory contextFactory) {
        parent = contextFactory;
    }

    /** {@inheritDoc} */
    @Deprecated
    public TilesApplicationContext createApplicationContext(Object context) {
        if (context instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) context;
            return new ServletTilesApplicationContext(servletContext);
        }

        return null;
    }

    /** {@inheritDoc} */
    public TilesRequestContext createRequestContext(TilesApplicationContext context,
                                                    Object... requestItems) {
        if (requestItems.length == 1 && requestItems[0] instanceof PageContext) {
            PageContext pageContext = (PageContext) requestItems[0];
            ServletRequest request = pageContext.getRequest();
            ServletResponse response = pageContext.getResponse();
            TilesRequestContext enclosedRequest;
            if (parent != null) {
                enclosedRequest = parent.createRequestContext(context, request,
                        response);
            } else {
                enclosedRequest = new ServletTilesRequestContext(context,
                        (HttpServletRequest) request,
                        (HttpServletResponse) response);
            }
            return new JspTilesRequestContext(enclosedRequest, pageContext);
        }

        return null;
    }

    /**
     * Returns the original servlet context.
     *
     * @param context The application context.
     * @return The original servlet context, if found.
     * @deprecated Use {@link TilesApplicationContext#getContext()}.
     */
    protected ServletContext getServletContext(TilesApplicationContext context) {
        return (ServletContext) context.getContext();
    }
}
