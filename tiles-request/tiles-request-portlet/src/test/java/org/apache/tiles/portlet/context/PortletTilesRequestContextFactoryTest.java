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

package org.apache.tiles.portlet.context;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link PortletTilesRequestContextFactory}.
 *
 * @version $Rev$ $Date$
 */
public class PortletTilesRequestContextFactoryTest {

    /**
     * The factory to test.
     */
    private PortletTilesRequestContextFactory factory;

    /**
     * Sets up the test object.
     */
    @Before
    public void setUp() {
        factory = new PortletTilesRequestContextFactory();
    }

    /**
     * Test method for
     * {@link PortletTilesRequestContextFactory
     * #createRequestContext(org.apache.tiles.TilesApplicationContext, java.lang.Object[])}
     * .
     */
    @Test
    public void testCreateRequestContext2Params() {
        PortletTilesApplicationContext applicationContext = createMock(PortletTilesApplicationContext.class);
        PortletRequest request = createMock(PortletRequest.class);
        PortletResponse response = createMock(PortletResponse.class);
        PortletContext portletContext = createMock(PortletContext.class);

        expect(applicationContext.getPortletContext())
                .andReturn(portletContext);

        replay(applicationContext, request, response, portletContext);
        PortletTilesRequestContext tilesRequest = (PortletTilesRequestContext) factory
                .createRequestContext(applicationContext, request, response);
        Object[] objs = tilesRequest.getRequestObjects();
        assertEquals(2, objs.length);
        assertEquals(request, objs[0]);
        assertEquals(response, objs[1]);
        verify(applicationContext, request, response, portletContext);
    }

    /**
     * Test method for
     * {@link PortletTilesRequestContextFactory
     * #createRequestContext(org.apache.tiles.TilesApplicationContext, java.lang.Object[])}
     * .
     */
    @Test
    public void testCreateRequestContext3Params() {
        PortletTilesApplicationContext applicationContext = createMock(PortletTilesApplicationContext.class);
        PortletRequest request = createMock(PortletRequest.class);
        PortletResponse response = createMock(PortletResponse.class);
        PortletContext portletContext = createMock(PortletContext.class);

        replay(applicationContext, request, response, portletContext);
        PortletTilesRequestContext tilesRequest = (PortletTilesRequestContext) factory
                .createRequestContext(applicationContext, request, response,
                        portletContext);
        Object[] objs = tilesRequest.getRequestObjects();
        assertEquals(2, objs.length);
        assertEquals(request, objs[0]);
        assertEquals(response, objs[1]);
        verify(applicationContext, request, response, portletContext);
    }

    /**
     * Test method for
     * {@link org.apache.tiles.portlet.context.PortletTilesRequestContextFactory
     * #getPortletContext(org.apache.tiles.TilesApplicationContext)}
     * .
     */
    @Test
    public void testGetPortletContext() {
        PortletTilesApplicationContext applicationContext = createMock(PortletTilesApplicationContext.class);
        PortletContext portletContext = createMock(PortletContext.class);

        expect(applicationContext.getPortletContext())
                .andReturn(portletContext);

        replay(applicationContext, portletContext);
        assertEquals(portletContext, factory.getPortletContext(applicationContext));
        verify(applicationContext, portletContext);
    }

}
