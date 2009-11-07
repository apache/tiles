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
package org.apache.tiles.mock;

import java.util.Map;

import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;

/**
 * "Repeats" (i.e. returns back) the context as a
 * {@link ApplicationContext}, or the request as a
 * {@link Request}.
 *
 * @version $Rev$ $Date$
 */
public class RepeaterTilesRequestContextFactory implements TilesRequestContextFactory,
        TilesRequestContextFactoryAware {

    /**
     * The parent context factory.
     */
    private TilesRequestContextFactory parent;

    /** {@inheritDoc} */
    public void setRequestContextFactory(
            TilesRequestContextFactory contextFactory) {
        this.parent = contextFactory;
    }

    /** {@inheritDoc} */
    public Request createRequestContext(
            ApplicationContext context, Object... requestItems) {
        if (parent == null) {
            throw new RuntimeException("The parent is null");
        }
        Request retValue = null;
        if (requestItems.length > 0) {
            retValue = (Request) requestItems[0];
        }
        return retValue;
    }
}
