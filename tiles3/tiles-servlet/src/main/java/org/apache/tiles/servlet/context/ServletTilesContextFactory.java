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

package org.apache.tiles.servlet.context;

import org.apache.tiles.Initializable;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.AbstractTilesApplicationContextFactory;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * Creates an instance of the appropriate TilesApplicationContext
 * implementation.
 *
 * @version $Rev$ $Date$
 * @deprecated Use {@link ServletTilesApplicationContext} or
 * {@link ServletTilesRequestContext}.
 */
/**
 * @author PTRNTN77A26E506O
 *
 */
/**
 * @author PTRNTN77A26E506O
 *
 */
/**
 * @author PTRNTN77A26E506O
 *
 */
@Deprecated
public class ServletTilesContextFactory implements TilesContextFactory {

    /**
     * The application context factory.
     */
    private AbstractTilesApplicationContextFactory contextFactory;

    /**
     * The request context factory.
     */
    private TilesRequestContextFactory requestContextFactory;

    /**
     * Constructor.
     *
     * @deprecated Do not use! No replacement.
     */
    @Deprecated
    public ServletTilesContextFactory() {
        contextFactory = new ServletTilesApplicationContextFactory();
        requestContextFactory = new ServletTilesRequestContextFactory();
    }

    /** {@inheritDoc} */
    public void init(Map<String, String> configParameters) {
        if (contextFactory instanceof Initializable) {
            ((Initializable) contextFactory).init(configParameters);
        }
        requestContextFactory.init(configParameters);
    }

    /** {@inheritDoc} */
    public TilesApplicationContext createApplicationContext(Object context) {
        return contextFactory.createApplicationContext(context);
    }

    /** {@inheritDoc} */
    public TilesRequestContext createRequestContext(TilesApplicationContext context,
                                                    Object... requestItems) {
        return requestContextFactory
                .createRequestContext(context, requestItems);
    }

    /**
     * Returns the original servlet context.
     *
     * @param context The application context.
     * @return The original servlet context, if found.
     * @deprecated Use {@link TilesApplicationContext#getContext()}.
     */
    @Deprecated
    protected ServletContext getServletContext(TilesApplicationContext context) {
        if (context instanceof ServletTilesApplicationContext) {
            ServletTilesApplicationContext app = (ServletTilesApplicationContext) context;
            return app.getServletContext();
        }
        return null;

    }
}
