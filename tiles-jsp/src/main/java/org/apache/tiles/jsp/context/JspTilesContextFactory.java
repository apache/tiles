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
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import java.util.Map;

/**
 * Creates an instance of the appropriate TilesApplicationContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class JspTilesContextFactory implements TilesContextFactory {

    /** {@inheritDoc} */
    public void init(Map<String, String> configParameters) {
    }

    /** {@inheritDoc} */
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
        if (requestItems.length == 1) {
            ServletContext servletContext = getServletContext(context);
            if (servletContext != null) {
                return new JspTilesRequestContext(servletContext, (PageContext) requestItems[0]);
            }
        }

        return null;
    }

    /**
     * Returns the original servlet context.
     *
     * @param context The application context.
     * @return The original servlet context, if found.
     */
    protected ServletContext getServletContext(TilesApplicationContext context) {
        if (context instanceof ServletTilesApplicationContext) {
            ServletTilesApplicationContext app = (ServletTilesApplicationContext) context;
            return app.getServletContext();
        }
        return null;

    }
}
