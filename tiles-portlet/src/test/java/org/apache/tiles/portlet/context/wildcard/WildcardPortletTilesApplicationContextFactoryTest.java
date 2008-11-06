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

package org.apache.tiles.portlet.context.wildcard;

import javax.portlet.PortletContext;

import org.apache.tiles.TilesApplicationContext;
import org.easymock.EasyMock;

import junit.framework.TestCase;

/**
 * Tests {@link WildcardPortletTilesApplicationContextFactory}.
 */
public class WildcardPortletTilesApplicationContextFactoryTest extends TestCase {

    /**
     * The factory to test.
     */
    private WildcardPortletTilesApplicationContextFactory factory;

    /**
     * The servlet context.
     */
    private PortletContext servletContext;

    /** {@inheritDoc} */
    public void setUp() {
        servletContext = EasyMock.createMock(PortletContext.class);
        factory = new WildcardPortletTilesApplicationContextFactory();
        EasyMock.replay(servletContext);
    }

    /**
     * Test method for
     * {@link org.apache.tiles.servlet.context.wildcard.WildcardPortletTilesApplicationContextFactory
     * #createApplicationContext(java.lang.Object)}.
     */
    public void testCreateApplicationContext() {
        TilesApplicationContext context = factory
                .createApplicationContext(servletContext);
        assertTrue("The class of the application context is not correct",
                context instanceof WildcardPortletTilesApplicationContext);
    }
}
