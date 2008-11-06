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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.mock.RepeaterTilesApplicationContextFactory;
import org.easymock.EasyMock;

/**
 * Tests {@link ChainedTilesApplicationContextFactory}.
 *
 * @version $Rev$ $Date$
 */
public class ChainedTilesApplicationContextFactoryTest extends TestCase {

    /**
     * The Tiles application context.
     */
    private TilesApplicationContext appContext;

    /**
     * The request context.
     */
    private TilesRequestContext requestContext;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        appContext = EasyMock.createMock(TilesApplicationContext.class);
        requestContext = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.replay(appContext);
    }

    /**
     * Tests the initialization method.
     */
    public void testInit() {
        Map<String, String> config = new HashMap<String, String>();
        config.put(ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES,
                "this.is.not.a.class.Name,"
                + RepeaterTilesApplicationContextFactory.class.getName());
        ChainedTilesApplicationContextFactory factory = new ChainedTilesApplicationContextFactory();
        factory.init(config);
        TilesApplicationContext context = factory.createApplicationContext(appContext);
        assertNotNull("The request context is not correct",
                context == appContext);
    }

    /**
     * Tests {@link ChainedTilesContextFactory#setFactories(java.util.List)}.
     */
    public void testSetFactories() {
        ChainedTilesApplicationContextFactory factory = new ChainedTilesApplicationContextFactory();
        List<TilesApplicationContextFactory> factories = new ArrayList<TilesApplicationContextFactory>();
        RepeaterTilesApplicationContextFactory repFactory = new RepeaterTilesApplicationContextFactory();
        factories.add(repFactory);
        factory.setFactories(factories);
        TilesApplicationContext context = factory
                .createApplicationContext(appContext);
        assertNotNull("The request context is not correct",
                context == requestContext);
    }
}
