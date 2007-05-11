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
package org.apache.tiles.impl.mgmt;

import org.apache.tiles.Definition;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.mgmt.MutableTilesContainer;

/**
 * Mutable container which caches (in memory) the definitions
 * registered to it.  If a definition is not found in cache, it
 * will revert back to it's definitions factory.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public class CachingTilesContainer extends BasicTilesContainer
    implements MutableTilesContainer {

    /**
     * The definition manager to store custom and main definitions.
     */
    private DefinitionManager mgr = new DefinitionManager();

    /** {@inheritDoc} */
    public void register(Definition definition, Object... requestItems) throws TilesException {
        TilesRequestContext requestContext = getContextFactory().createRequestContext(
                getApplicationContext(),
                requestItems
            );
        register(definition, requestContext);
    }

    /** {@inheritDoc} */
    @Override
    protected Definition getDefinition(String definition,
                                                TilesRequestContext context)
        throws DefinitionsFactoryException {
        return mgr.getDefinition(definition, context);
    }


    /** {@inheritDoc} */
    @Override
    public DefinitionsFactory getDefinitionsFactory() {
        return mgr.getFactory();
    }

    /** {@inheritDoc} */
    @Override
    public void setDefinitionsFactory(DefinitionsFactory definitionsFactory) {
        super.setDefinitionsFactory(definitionsFactory);
        mgr.setFactory(definitionsFactory);
    }

    /**
     * Registers a custom definition.
     *
     * @param definition The definition to register.
     * @param request The request inside which the definition should be
     * registered.
     * @throws DefinitionsFactoryException If something goes wrong during adding
     * a definition, such as missing parent definitions.
     */
    protected void register(Definition definition,
            TilesRequestContext request) throws DefinitionsFactoryException {
        Definition def = new Definition(definition);
        mgr.addDefinition(def, request);
    }
}
