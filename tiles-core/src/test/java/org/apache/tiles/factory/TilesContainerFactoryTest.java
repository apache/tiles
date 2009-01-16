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
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.ChainedTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.mock.RepeaterTilesApplicationContextFactory;
import org.apache.tiles.mock.RepeaterTilesRequestContextFactory;
import org.easymock.EasyMock;


/**
 * @version $Rev$ $Date$
 */
public class TilesContainerFactoryTest extends TestCase {

    /**
     * Size of init parameters.
     */
    private static final int INIT_PARAMS_SIZE = 4;

    /**
     * The logging object.
     */
    private final Log log = LogFactory
            .getLog(TilesContainerFactoryTest.class);

    /**
     * The servlet context.
     */
    private TilesApplicationContext context;

    /** {@inheritDoc} */
    @Override
    public void setUp() {
        context = EasyMock.createMock(TilesApplicationContext.class);
    }

    /**
     * Tests getting the factory.
     */
    public void testGetFactory() {
        Map<String, String> initParams = new HashMap<String, String>();
        EasyMock.expect(context.getInitParams()).andReturn(initParams)
                .anyTimes();
        EasyMock.replay(context);
        AbstractTilesContainerFactory factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        assertNotNull(factory);
        assertEquals(TilesContainerFactory.class, factory.getClass());

        EasyMock.reset(context);
        initParams.put(AbstractTilesContainerFactory
                .CONTAINER_FACTORY_INIT_PARAM, TestFactory.class.getName());
        initParams.put(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesApplicationContextFactory.class.getName());
        initParams.put(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParams()).andReturn(initParams)
                .anyTimes();
        EasyMock.replay(context);
        factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        assertNotNull(factory);
        assertEquals(TestFactory.class, factory.getClass());

        Map<String, String> defaults = new HashMap<String, String>();
        EasyMock.reset(context);
        initParams.put(AbstractTilesContainerFactory
                .CONTAINER_FACTORY_INIT_PARAM, TestFactory.class.getName());
        initParams.put(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesApplicationContextFactory.class.getName());
        initParams.put(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParams()).andReturn(initParams)
                .anyTimes();
        EasyMock.replay(context);
        factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        ((TilesContainerFactory) factory).setDefaultConfiguration(defaults);
        assertNotNull(factory);
        assertEquals(TestFactory.class, factory.getClass());

        EasyMock.reset(context);
        initParams.put(AbstractTilesContainerFactory
                .CONTAINER_FACTORY_INIT_PARAM, "org.missing.Class");
        initParams.put(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesApplicationContextFactory.class.getName());
        initParams.put(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParams()).andReturn(initParams)
                .anyTimes();
        EasyMock.replay(context);
        try {
            AbstractTilesContainerFactory.getTilesContainerFactory(context);
            fail("Invalid classname.  Exception should have been thrown.");
        } catch (TilesException e) {
            if (log.isDebugEnabled()) {
                log.debug("The classname is invalid, it is ok", e);
            }
        }
    }

    /**
     * Tests the creation of a container.
     *
     * @throws IOException If something goes wrong when obtaining URL resources.
     */
    public void testCreateContainer() throws IOException {
        Map<String, String> initParams = new HashMap<String, String>();
        URL url = getClass().getResource("test-defs.xml");
        initParams.put(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesApplicationContextFactory.class.getName());
        initParams.put(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesRequestContextFactory.class.getName());
        Set<URL> urls = new HashSet<URL>();
        urls.add(url);
        EasyMock.expect(context.getResources("/WEB-INF/tiles.xml")).andReturn(
                urls);
        EasyMock.expect(context.getInitParams()).andReturn(initParams)
                .anyTimes();
        EasyMock.replay(context);

        AbstractTilesContainerFactory factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        TilesContainer container = factory.createContainer(context);

        assertNotNull(container);
        //now make sure it's initialized
        try {
            container.init(new HashMap<String, String>());
            fail("Container should have already been initialized");
        } catch (IllegalStateException te) {
            if (log.isDebugEnabled()) {
                log.debug("Intercepted an exception, it is OK", te);
            }
        }

    }


    /**
     * Tests getting init parameter map.
     */
    public void testGetInitParameterMap() {
        Map<String, String> initParams = new HashMap<String, String>();
        Vector<String> keys = new Vector<String>();
        keys.add("one");
        keys.add("two");

        initParams.put(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesApplicationContextFactory.class.getName());
        initParams.put(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesRequestContextFactory.class.getName());
        initParams.put("one", "oneValue");
        initParams.put("two", "twoValue");
        EasyMock.expect(context.getInitParams()).andReturn(initParams)
                .anyTimes();
        EasyMock.replay(context);

        Map<String, String> map = context.getInitParams();

        assertEquals(INIT_PARAMS_SIZE, map.size());
        assertTrue(map.containsKey("one"));
        assertTrue(map.containsKey("two"));
        assertEquals("oneValue", map.get("one"));
        assertEquals("twoValue", map.get("two"));
    }

    /**
     * A test factory extending directly from TilesContainerFactory.
     */
    public static class TestFactory extends TilesContainerFactory {

    }
}
