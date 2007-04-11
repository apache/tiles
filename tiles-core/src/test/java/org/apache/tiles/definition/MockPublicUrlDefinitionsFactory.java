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

package org.apache.tiles.definition;

import org.apache.tiles.context.TilesRequestContext;

/**
 * Mock implementation of UrlDefinitionsFactory that exposes two of its methods
 * as public instead of protected.
 *
 * @version $Rev$ $Date$
 */
public class MockPublicUrlDefinitionsFactory extends UrlDefinitionsFactory {

    /**
     * Exposes the <code>addDefinitions</code> method of
     * <code>UrlDefinitionsFactory</code>.
     *
     * @param definitions The definitions to add.
     * @param tilesContext The request context to use.
     * @throws DefinitionsFactoryException If something goes wrong during the
     * addition.
     * @see org.apache.tiles.definition.UrlDefinitionsFactory#addDefinitions(org.apache.tiles.definition.Definitions,
     *org.apache.tiles.context.TilesRequestContext)
     */
    public void addDefinitions(Definitions definitions,
            TilesRequestContext tilesContext) throws DefinitionsFactoryException {
        super.addDefinitions(definitions, tilesContext);
    }

    /**
     * Exposes the <code>isLocaleProcessed</code> method of
     * <code>UrlDefinitionsFactory</code>.
     *
     * @param tilesContext The request context to use.
     * @return <code>true</code> if the context has been already processed.
     * @see org.apache.tiles.definition.UrlDefinitionsFactory
     * #isContextProcessed(org.apache.tiles.context.TilesRequestContext)
     */
    public boolean isContextProcessed(TilesRequestContext tilesContext) {
        return super.isContextProcessed(tilesContext);
    }
}
