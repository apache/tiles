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

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.servlet.wildcard.WildcardServletApplicationContext;
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
     * Test method for {@link CompleteAutoloadTilesInitializer#createTilesApplicationContext(ApplicationContext)}.
     */
    @Test
    public void testCreateTilesApplicationContext() {
        ApplicationContext preliminaryContext = createMock(ApplicationContext.class);
        ServletContext servletContext = createMock(ServletContext.class);

        expect(preliminaryContext.getContext()).andReturn(servletContext);

        replay(preliminaryContext, servletContext);
        assertTrue(initializer
                .createTilesApplicationContext(preliminaryContext) instanceof WildcardServletApplicationContext);
        verify(preliminaryContext, servletContext);
    }

    /**
     * Test method for {@link CompleteAutoloadTilesInitializer#createContainerFactory(ApplicationContext)}.
     */
    @Test
    public void testCreateContainerFactory() {
        ApplicationContext context = createMock(ApplicationContext.class);

        replay(context);
        assertTrue(initializer.createContainerFactory(context) instanceof CompleteAutoloadTilesContainerFactory);
        verify(context);
    }
}
