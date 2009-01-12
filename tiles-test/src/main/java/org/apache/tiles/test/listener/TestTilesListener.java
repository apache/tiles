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
package org.apache.tiles.test.listener;

import javax.servlet.ServletContext;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.servlet.context.wildcard.WildcardServletTilesApplicationContext;
import org.apache.tiles.startup.BasicTilesInitializer;
import org.apache.tiles.startup.TilesInitializer;
import org.apache.tiles.test.factory.TestTilesContainerFactory;
import org.apache.tiles.web.startup.TilesListener;

/**
 * Test Tiles listener for Tiles initialization of the default container.
 *
 * @version $Rev$ $Date$
 */
public class TestTilesListener extends TilesListener {

    /** {@inheritDoc} */
    @Override
    protected TilesInitializer createTilesInitializer() {
        return new TestTilesListenerInitializer();
    }

    /**
     * Test Tiles initializer for Tiles initialization of the default container.
     */
    private static class TestTilesListenerInitializer extends BasicTilesInitializer {

        /** {@inheritDoc} */
        @Override
        protected AbstractTilesContainerFactory createContainerFactory(
                TilesApplicationContext context) {
            return new TestTilesContainerFactory();
        }

        /** {@inheritDoc} */
        @Override
        protected TilesApplicationContext createTilesApplicationContext(
                TilesApplicationContext preliminaryContext) {
            return new WildcardServletTilesApplicationContext(
                    (ServletContext) preliminaryContext.getContext());
        }
    }
}
