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

package org.apache.tiles.request.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;

/**
 * Creates an instance of the appropriate {@link Request}
 * implementation in a servlet environment.
 *
 * @version $Rev$ $Date$
 * @since 2.1.1
 */
public class ServletTilesRequestContextFactory implements
        TilesRequestContextFactory {

    /** {@inheritDoc} */
    public Request createRequestContext(ApplicationContext context,
                                                    Object... requestItems) {
        if (requestItems.length == 2
                && requestItems[0] instanceof HttpServletRequest
                && requestItems[1] instanceof HttpServletResponse) {
            return new ServletTilesRequestContext(context,
                (HttpServletRequest) requestItems[0],
                (HttpServletResponse) requestItems[1]);
        }

        return null;
    }
}
