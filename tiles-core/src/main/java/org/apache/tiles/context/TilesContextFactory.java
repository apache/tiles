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

import java.util.Map;

/**
 * Creates an instance of the appropriate TilesApplicationContext implementation.
 *
 * @since Sep 21, 2006
 * @version $Rev$ $Date$
 */
public interface TilesContextFactory {

    /**
     * Initialize the factory.
     *
     * @param configurationParameters A map of parameters.
     */
    void init(Map<String, String> configurationParameters);

    /**
     * Create a TilesApplicationContext for the given context.
     *
     * @param context The (application) context to use.
     * @return TilesApplicationContext The Tiles application context.
     */
    TilesApplicationContext createApplicationContext(Object context);

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
