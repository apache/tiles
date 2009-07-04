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
package org.apache.tiles.factory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.ChainedTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer;
import org.apache.tiles.mock.RepeaterTilesApplicationContextFactory;
import org.apache.tiles.mock.RepeaterTilesRequestContextFactory;
import org.easymock.EasyMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version $Rev$ $Date$
 */
public class KeyedDefinitionsFactoryTilesContainerFactoryTest extends TestCase {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(KeyedDefinitionsFactoryTilesContainerFactoryTest.class);

    /**
     * The application context.
     */
    private TilesApplicationContext context;

    /**
     * Initialization parameters.
     */
    private Map<String, String> initParams;

    /**
     * Default configuration parameters.
     */
    private Map<String, String> defaults;

    /** {@inheritDoc} */
    @Override
    public void setUp() {
        context = EasyMock.createMock(TilesApplicationContext.class);
        initParams = new HashMap<String, String>();
        initParams.put(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM,
                KeyedDefinitionsFactoryTilesContainerFactory.class.getName());
        initParams.put(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesApplicationContextFactory.class.getName());
        initParams.put(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParams()).andReturn(initParams)
                .anyTimes();
        defaults = new HashMap<String, String>();
    }

    /**
     * Tests getting a container factory.
     */
    public void testGetFactory() {
        EasyMock.replay(context);
        TilesContainerFactory factory = (TilesContainerFactory) AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        factory.setDefaultConfiguration(defaults);
        assertNotNull(factory);
        assertEquals(KeyedDefinitionsFactoryTilesContainerFactory.class,
                factory.getClass());
    }

    /**
     * Tests creating a container.
     * @throws IOException If something goes wrong.
     */
    public void testCreateContainer() throws IOException {
        initParams.put(KeyedDefinitionsFactoryTilesContainerFactory
                .CONTAINER_KEYS_INIT_PARAM, "one,two");
        initParams.put(KeyedDefinitionsFactoryTilesContainer
                .DEFINITIONS_CONFIG_PREFIX + "one", "/WEB-INF/tiles-one.xml");
        initParams.put(KeyedDefinitionsFactoryTilesContainer
                .DEFINITIONS_CONFIG_PREFIX + "two", "/WEB-INF/tiles-two.xml");
        URL url = getClass().getResource("test-defs.xml");
        Set<URL> urls = new HashSet<URL>();
        urls.add(url);
        EasyMock.expect(context.getResources("/WEB-INF/tiles.xml")).andReturn(
                urls);
        url = getClass().getResource("test-defs-key-one.xml");
        EasyMock.expect(context.getResources("/WEB-INF/tiles-one.xml"))
                .andReturn(urls);
        url = getClass().getResource("test-defs-key-two.xml");
        EasyMock.expect(context.getResources("/WEB-INF/tiles-two.xml"))
                .andReturn(urls);
        EasyMock.replay(context);

        TilesContainerFactory factory = (TilesContainerFactory) AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        factory.setDefaultConfiguration(defaults);
        TilesContainer container = factory.createContainer(context);

        assertNotNull(container);
        assertTrue("The container is not an instance of KeyedDefinitionsFactoryTilesContainer",
                container instanceof KeyedDefinitionsFactoryTilesContainer);
        KeyedDefinitionsFactoryTilesContainer keyedContainer =
            (KeyedDefinitionsFactoryTilesContainer) container;
        assertNotNull(keyedContainer.getDefinitionsFactory());
        assertNotNull(keyedContainer.getDefinitionsFactory("one"));
        assertNotNull(keyedContainer.getDefinitionsFactory("two"));
        //now make sure it's initialized
        try {
            container.init(new HashMap<String, String>());
            fail("Container should have already been initialized");
        } catch (IllegalStateException te) {
            if (log.isDebugEnabled()) {
                log.debug("The container has been initialized, the exception is ok", te);
            }
        }

    }
}
