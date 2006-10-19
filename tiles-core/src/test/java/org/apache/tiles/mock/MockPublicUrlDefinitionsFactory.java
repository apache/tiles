/*
 * $Id$
 *
 * Copyright 1999-2006 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.mock;

import org.apache.tiles.ComponentDefinitions;
import org.apache.tiles.DefinitionsFactoryException;
import org.apache.tiles.TilesContext;
import org.apache.tiles.definition.UrlDefinitionsFactory;

/**
 * Mock implementation of UrlDefinitionsFactory that exposes two of its methods
 * as public instead of protected.
 * 
 * @version $Rev$ $Date$
 */
public class MockPublicUrlDefinitionsFactory extends UrlDefinitionsFactory {

    /**
     * Exposes the <code>addDefinitions</code> method of
     * <code>UrlDefinitionsFactory</code>
     * 
     * @see org.apache.tiles.definition.UrlDefinitionsFactory#addDefinitions(org.apache.tiles.ComponentDefinitions,
     *      org.apache.tiles.TilesContext)
     */
    public void addDefinitions(ComponentDefinitions definitions,
            TilesContext tilesContext) throws DefinitionsFactoryException {
        super.addDefinitions(definitions, tilesContext);
    }

    /**
     * 
     * Exposes the <code>isContextProcessed</code> method of
     * <code>UrlDefinitionsFactory</code>
     * 
     * @see org.apache.tiles.definition.UrlDefinitionsFactory#isContextProcessed(org.apache.tiles.TilesContext)
     */
    public boolean isContextProcessed(TilesContext tilesContext) {
        return super.isContextProcessed(tilesContext);
    }
}
