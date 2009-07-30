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

package org.apache.tiles.extras.complete;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import javax.servlet.ServletContext;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.servlet.context.wildcard.WildcardServletTilesApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 * @version $Rev$ $Date$
 */
public class CompleteAutoloadTilesInitializerTest {

    /**
     * The object to test.
     */
    private CompleteAutoloadTilesInitializer initializer;

    /**
     * Sets up the object to test.
     */
    @Before
    public void setUp() {
        initializer = new CompleteAutoloadTilesInitializer();
    }

    /**
     * Test method for {@link CompleteAutoloadTilesInitializer#createTilesApplicationContext(TilesApplicationContext)}.
     */
    @Test
    public void testCreateTilesApplicationContext() {
        TilesApplicationContext preliminaryContext = createMock(TilesApplicationContext.class);
        ServletContext servletContext = createMock(ServletContext.class);

        expect(preliminaryContext.getContext()).andReturn(servletContext);

        replay(preliminaryContext, servletContext);
        assertTrue(initializer
                .createTilesApplicationContext(preliminaryContext) instanceof WildcardServletTilesApplicationContext);
        verify(preliminaryContext, servletContext);
    }

    /**
     * Test method for {@link CompleteAutoloadTilesInitializer#createContainerFactory(TilesApplicationContext)}.
     */
    @Test
    public void testCreateContainerFactory() {
        TilesApplicationContext context = createMock(TilesApplicationContext.class);

        replay(context);
        assertTrue(initializer.createContainerFactory(context) instanceof CompleteAutoloadTilesContainerFactory);
        verify(context);
    }
}
