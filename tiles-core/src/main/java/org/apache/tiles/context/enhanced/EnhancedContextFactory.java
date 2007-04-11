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
package org.apache.tiles.context.enhanced;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.ChainedTilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;

/**
 * Tiles context factory to be used together with
 * {@link EnhancedTilesApplicationContext}.
 *
 * @version $Rev$ $Date$
 */
public class EnhancedContextFactory extends ChainedTilesContextFactory {

    /** {@inheritDoc} */
    @Override
    public TilesApplicationContext createApplicationContext(Object context) {
        TilesApplicationContext root = super.createApplicationContext(context);
        return new EnhancedTilesApplicationContext(root);
    }

    /** {@inheritDoc} */
    @Override
    public TilesRequestContext createRequestContext(TilesApplicationContext context,
            Object... requestItems) {
        if (context instanceof EnhancedTilesApplicationContext) {
            context = ((EnhancedTilesApplicationContext) context).getRootContext();
        }
        return super.createRequestContext(context, requestItems);
    }
}
