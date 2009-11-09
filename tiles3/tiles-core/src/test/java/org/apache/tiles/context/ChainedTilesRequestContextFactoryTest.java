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
import java.util.List;

import junit.framework.TestCase;

import org.apache.tiles.mock.RepeaterTilesRequestContextFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;
import org.easymock.EasyMock;

/**
 * Tests {@link ChainedTilesRequestContextFactory}.
 *
 * @version $Rev$ $Date$
 */
public class ChainedTilesRequestContextFactoryTest extends TestCase {

    /**
     * The Tiles application context.
     */
    private ApplicationContext appContext;

    /**
     * The request context.
     */
    private Request requestContext;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        appContext = EasyMock.createMock(ApplicationContext.class);
        requestContext = EasyMock.createMock(Request.class);
        EasyMock.replay(appContext);
    }

    /**
     * Tests {@link ChainedTilesRequestContextFactory#setFactories(java.util.List)}.
     */
    public void testSetFactories() {
        ChainedTilesRequestContextFactory factory = new ChainedTilesRequestContextFactory();
        List<TilesRequestContextFactory> factories = new ArrayList<TilesRequestContextFactory>();
        RepeaterTilesRequestContextFactory repFactory = new RepeaterTilesRequestContextFactory();
        repFactory.setRequestContextFactory(factory);
        factories.add(repFactory);
        factory.setFactories(factories);
        Request context = factory.createRequestContext(appContext, requestContext);
        assertNotNull("The request context is not correct",
                context == requestContext);
    }
}