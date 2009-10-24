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


import javax.servlet.ServletContext;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.servlet.context.ServletTilesContextFactory;

/**
 * Creates an instance of the appropriate TilesApplicationContext implementation.
 *
 * @version $Rev$ $Date$
 * @deprecated Use {@link JspTilesRequestContextFactory}.
 */
@Deprecated
public class JspTilesContextFactory extends ServletTilesContextFactory {

    /**
     * The real factory.
     */
    private JspTilesRequestContextFactory factory;

    /**
     * Constructor.
     *
     * @deprecated Do not use! No replacement.
     */
    @Deprecated
    public JspTilesContextFactory() {
        factory = new JspTilesRequestContextFactory();
    }

    /** {@inheritDoc} */
    @Override
	public TilesRequestContext createRequestContext(TilesApplicationContext context,
                                                    Object... requestItems) {
        return factory.createRequestContext(context, requestItems);
    }

    /**
     * Returns the original servlet context.
     *
     * @param context The application context.
     * @return The original servlet context, if found.
     * @deprecated Use {@link TilesApplicationContext#getContext()}.
     */
    @Deprecated
	@Override
	protected ServletContext getServletContext(TilesApplicationContext context) {
        return (ServletContext) context.getContext();
    }
}
