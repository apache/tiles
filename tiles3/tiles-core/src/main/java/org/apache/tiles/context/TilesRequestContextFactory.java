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
package org.apache.tiles.context;

import org.apache.tiles.TilesApplicationContext;

/**
 * Creates an instance of the appropriate {@link TilesRequestContext}
 * implementation.
 *
 * @version $Rev$ $Date$
 * @since 2.1.1
 */
public interface TilesRequestContextFactory {

    /**
     * Create a TilesRequestContext for the given context,
     * request, and response.
     *
     * @param context  the associated {@link TilesApplicationContext}
     * @param requestItems  the associated request items, typically a request and a response.
     * @return TilesRequestContext
     */
    TilesRequestContext createRequestContext(TilesApplicationContext context,
            Object... requestItems);
}
