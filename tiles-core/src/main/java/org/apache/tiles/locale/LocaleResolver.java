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
package org.apache.tiles.locale;

import java.util.Locale;
import java.util.Map;

import org.apache.tiles.TilesException;
import org.apache.tiles.context.TilesRequestContext;

/**
 * It represents an object able to resolve the current locale for the current
 * request, where its strategy depends on its implementation.
 *
 * @version $Rev$ $Date$
 */
public interface LocaleResolver {

    /**
     * Initializes the <code>LocaleResolver</code> object. <p/> This method
     * must be called before the {@link #resolveLocale(TilesRequestContext)}
     * method is called.
     *
     * @param params A map of properties used to set up the resolver.
     * @throws TilesException if required properties are not passed
     * in or the initialization fails.
     */
    void init(Map<String, String> params) throws TilesException;

    /**
     * Resolves the locale.
     *
     * @param request The Tiles request object.
     * @return The current locale for the current request.
     */
    Locale resolveLocale(TilesRequestContext request);
}
