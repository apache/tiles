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
 *
 */
package org.apache.tiles.context;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;

import javax.servlet.jsp.PageContext;
import java.util.Map;

/**
 * Creates an instance of the appropriate TilesApplicationContext implementation.
 *
 * @version $Id$
 * @since Sep 21, 2006
 */
public interface TilesContextFactory {

    /**
     * Initialize the impl
     */
    void init(Map configurationParameters);

    /**
     * Create a TilesApplicationContext for the given context.
     *
     * @param context
     * @return TilesApplicationContext
     */
    TilesApplicationContext createApplicationContext(Object context);

    /**
     * Create a TilesRequestContext for the given context,
     * request, and response.
     *
     * @param context  the associated {@link TilesApplicationContext}
     * @param request  the associated request.  Typically a ServletRequest or PortletRequest.
     * @param response the associated response.  Typically a ServletResponse or PortletResponse.
     * @return TilesRequestContext
     */
    TilesRequestContext createRequestContext(TilesApplicationContext context,
                                             Object request, Object response);

    /**
     * Create a TilesRequestContext for the given tiles and page contexts.
     *
     * @param context
     * @param pageContext
     * @return
     */
    TilesRequestContext createRequestContext(TilesApplicationContext context,
                                             PageContext pageContext);
}
