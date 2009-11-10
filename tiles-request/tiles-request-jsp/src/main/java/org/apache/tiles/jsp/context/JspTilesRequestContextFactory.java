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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;
import org.apache.tiles.request.servlet.ServletTilesRequestContext;

/**
 * Creates an instance of the appropriate {@link Request}
 * implementation under a JSP environment.
 *
 * @version $Rev$ $Date$
 * @since 2.1.1
 */
public class JspTilesRequestContextFactory implements TilesRequestContextFactory,
        TilesRequestContextFactoryAware {

    /**
     * Parent Tiles context factory.
     */
    private TilesRequestContextFactory parent;

    /** {@inheritDoc} */
    public void setRequestContextFactory(
            TilesRequestContextFactory contextFactory) {
        parent = contextFactory;
    }

    /** {@inheritDoc} */
    public Request createRequestContext(
            ApplicationContext context, Object... requestItems) {
        if (requestItems.length == 1 && requestItems[0] instanceof PageContext) {
            PageContext pageContext = (PageContext) requestItems[0];
            ServletRequest request = pageContext.getRequest();
            ServletResponse response = pageContext.getResponse();
            Request enclosedRequest;
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
}
