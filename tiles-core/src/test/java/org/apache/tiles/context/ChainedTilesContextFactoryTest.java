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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.shale.test.mock.MockServletContext;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class ChainedTilesContextFactoryTest extends TestCase {

    /**
     * The request object.
     */
    private HttpServletRequest request;

    /**
     * The request object.
     */
    private HttpServletResponse response;

    /**
     * The Tiles application context.
     */
    private TilesApplicationContext appContext;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockServletContext servletContext = new MockServletContext();
        appContext = new ServletTilesApplicationContext(servletContext);
        MockHttpSession session = new MockHttpSession(servletContext);
        MockHttpServletRequest request = new MockHttpServletRequest(session);
        MockHttpServletResponse response = new MockHttpServletResponse();
        this.request = request;
        this.response = response;
    }

    /**
     * Tests the initialization method.
     *
     * @throws Exception If something goes wrong during testing.
     */
    public void testInit() throws Exception {
        Map<String, String> config = new HashMap<String, String>();
        config.put(ChainedTilesContextFactory.FACTORY_CLASS_NAMES,
                "this.is.not.a.class.Name,"
                + "org.apache.tiles.servlet.context.ServletTilesContextFactory");
        ChainedTilesContextFactory factory = new ChainedTilesContextFactory();
        factory.init(config);
        TilesRequestContext context = factory.createRequestContext(appContext,
                request, response);
        assertNotNull("The request context cannot be null", context);
    }
}
