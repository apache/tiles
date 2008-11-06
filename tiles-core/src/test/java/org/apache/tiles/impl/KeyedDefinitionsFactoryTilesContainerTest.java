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
package org.apache.tiles.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.ChainedTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.KeyedDefinitionsFactoryTilesContainerFactory;
import org.apache.tiles.factory.TilesContainerFactory;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer.DefaultKeyExtractor;
import org.apache.tiles.mock.RepeaterTilesApplicationContextFactory;
import org.apache.tiles.mock.RepeaterTilesRequestContextFactory;
import org.apache.tiles.util.RollingVectorEnumeration;
import org.easymock.EasyMock;


/**
 * @version $Rev$ $Date$
 */
public class KeyedDefinitionsFactoryTilesContainerTest extends TestCase {

    /**
     * The Tiles container.
     */
    private KeyedDefinitionsFactoryTilesContainer container;

    /**
     * Default configuration parameters.
     */
    private Map<String, String> defaults;

    /** {@inheritDoc} */
    @Override
    public void setUp() {
        ContextLikeTilesApplicationContext context = EasyMock
                .createMock(ContextLikeTilesApplicationContext.class);

        Vector<String> v = new Vector<String>();
        v.add(AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM);
        v.add(KeyedDefinitionsFactoryTilesContainerFactory.CONTAINER_KEYS_INIT_PARAM);
        v.add(KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "one");
        v.add(KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "two");
        v.add(ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES);
        v.add(ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES);

        Map<String, String> initParameters = new HashMap<String, String>();
        EasyMock.expect(context.getInitParams()).andReturn(initParameters);

        EasyMock.expect(context.getInitParameter(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM))
                .andReturn(KeyedDefinitionsFactoryTilesContainerFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .CONTAINER_FACTORY_MUTABLE_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .APPLICATION_CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .REQUEST_CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.DEFINITIONS_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(
                KeyedDefinitionsFactoryTilesContainerFactory.CONTAINER_KEYS_INIT_PARAM))
                .andReturn("one,two").anyTimes();
        EasyMock.expect(context.getInitParameter(
                KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "one")).andReturn("/WEB-INF/tiles-one.xml").anyTimes();
        EasyMock.expect(context.getInitParameter(
                KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "two")).andReturn("/WEB-INF/tiles-two.xml").anyTimes();
        EasyMock.expect(context.getInitParameter(EasyMock.isA(String.class))).andReturn(null).anyTimes();
        EasyMock.expect(context.getInitParameterNames()).andReturn(new RollingVectorEnumeration<String>(v)).anyTimes();
        initParameters.put(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM,
                KeyedDefinitionsFactoryTilesContainerFactory.class.getName());
        initParameters.put(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesApplicationContextFactory.class.getName());
        initParameters.put(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES,
                RepeaterTilesRequestContextFactory.class.getName());
        initParameters.put(
                KeyedDefinitionsFactoryTilesContainerFactory.CONTAINER_KEYS_INIT_PARAM,
                "one,two");
        initParameters.put(
                KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                        + "one", "/WEB-INF/tiles-one.xml");
        initParameters.put(
                KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "two", "/WEB-INF/tiles-two.xml");
        try {
            Set<URL> urlSet;
            URL url = getClass().getResource("/org/apache/tiles/factory/test-defs.xml");
            urlSet = new HashSet<URL>();
            urlSet.add(url);
            EasyMock.expect(context.getResources("/WEB-INF/tiles.xml")).andReturn(urlSet);
            url = getClass().getResource("/org/apache/tiles/factory/test-defs-key-one.xml");
            urlSet = new HashSet<URL>();
            urlSet.add(url);
            EasyMock.expect(context.getResources("/WEB-INF/tiles-one.xml")).andReturn(urlSet);
            url = getClass().getResource("/org/apache/tiles/factory/test-defs-key-two.xml");
            urlSet = new HashSet<URL>();
            urlSet.add(url);
            EasyMock.expect(context.getResources("/WEB-INF/tiles-two.xml")).andReturn(urlSet);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error getting Tiles configuration URL",
                    e);
        } catch (IOException e) {
            throw new RuntimeException("Error getting Tiles configuration URL",
                    e);
        }
        EasyMock.replay(context);
        TilesContainerFactory factory = (TilesContainerFactory) AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        container = (KeyedDefinitionsFactoryTilesContainer) factory.createContainer(context);
    }

    /**
     * Tests container initialization.
     */
    public void testInitialization() {
        assertNotNull(container);
        assertNotNull(container.getRequestContextFactory());
        assertNotNull(container.getPreparerFactory());
        assertNotNull(container.getDefinitionsFactory());
        assertNotNull(container.getProperDefinitionsFactory("one"));
        assertNotNull(container.getProperDefinitionsFactory("two"));
    }

    /**
     * Tests initialization for postponed definitions factories.
     *
     * @throws IOException If something goes wrong.
     */
    @SuppressWarnings("deprecation")
    public void testPostponedDefinitionsFactoryInitialization()
            throws IOException {
        KeyedDefinitionsFactoryTilesContainer container;
        ContextLikeTilesApplicationContext context = EasyMock
                .createMock(ContextLikeTilesApplicationContext.class);


        Vector<String> v = new Vector<String>();
        v.add(AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM);
        v.add(ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES);
        v.add(ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES);

        EasyMock.expect(context.getInitParameter(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM))
                .andReturn(KeyedDefinitionsFactoryTilesContainerFactory.class.getName())
                .anyTimes();
        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.DEFINITIONS_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(DefinitionsFactory.DEFINITIONS_CONFIG)).andReturn(null);
        EasyMock.expect(context.getInitParameter(BasicTilesContainer.DEFINITIONS_CONFIG)).andReturn(null);
        EasyMock.expect(context.getInitParameter("definitions-config")).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .CONTAINER_FACTORY_MUTABLE_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(
                KeyedDefinitionsFactoryTilesContainerFactory.CONTAINER_KEYS_INIT_PARAM))
                .andReturn(null);
        Set<URL> urlSet = new HashSet<URL>();
        URL url = getClass().getResource("/org/apache/tiles/factory/test-defs.xml");
        urlSet = new HashSet<URL>();
        urlSet.add(url);
        EasyMock.expect(context.getResources("/WEB-INF/tiles.xml")).andReturn(urlSet);
        url = getClass().getResource("/org/apache/tiles/factory/test-defs-key-one.xml");
        urlSet = new HashSet<URL>();
        urlSet.add(url);
        EasyMock.expect(context.getResources("/WEB-INF/tiles-one.xml")).andReturn(urlSet);
        url = getClass().getResource("/org/apache/tiles/factory/test-defs-key-two.xml");
        urlSet = new HashSet<URL>();
        urlSet.add(url);
        EasyMock.expect(context.getResources("/WEB-INF/tiles-two.xml")).andReturn(urlSet);
        EasyMock.expect(context.getInitParameterNames()).andReturn(v.elements()).anyTimes();
        EasyMock.replay(context);
        KeyedDefinitionsFactoryTilesContainerFactory factory =
            (KeyedDefinitionsFactoryTilesContainerFactory)
            AbstractTilesContainerFactory.getTilesContainerFactory(context);
        factory.setDefaultConfiguration(defaults);
        container = (KeyedDefinitionsFactoryTilesContainer) factory.createContainer(context);

        assertNotNull(container);
        assertNotNull(container.getDefinitionsFactory());
        assertNull(container.getProperDefinitionsFactory("one"));
        assertNull(container.getProperDefinitionsFactory("two"));

        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put(DefinitionsFactory.DEFINITIONS_CONFIG,
                "/WEB-INF/tiles-one.xml");
        DefinitionsFactory defsFactory = factory.createDefinitionsFactory(context);
        defsFactory.init(initParams);
        container.setDefinitionsFactory("one", defsFactory);
        initParams.put(DefinitionsFactory.DEFINITIONS_CONFIG,
                "/WEB-INF/tiles-two.xml");
        defsFactory = factory.createDefinitionsFactory(context);
        defsFactory.init(initParams);
        container.setDefinitionsFactory("two", defsFactory);
        assertNotNull(container.getProperDefinitionsFactory("one"));
        assertNotNull(container.getProperDefinitionsFactory("two"));
    }

    /**
     * Tests if the definitions factory has been used.
     */
    public void testDefinitionsFactoryUse() {
        TilesRequestContext request = EasyMock.createMock(TilesRequestContext.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        EasyMock.expect(request.getRequestScope()).andReturn(requestScope)
                .anyTimes();
        EasyMock.expect(request.getSessionScope()).andReturn(sessionScope)
                .anyTimes();
        EasyMock.expect(request.getRequestLocale()).andReturn(null).anyTimes();
        EasyMock.replay(request);
        assertTrue(container.isValidDefinition("test.def1", request));
        assertFalse(container.isValidDefinition("test.def.one", request));
        assertFalse(container.isValidDefinition("test.def.two", request));

        EasyMock.reset(request);
        requestScope.clear();
        requestScope.put(
                DefaultKeyExtractor.DEFINITIONS_FACTORY_KEY_ATTRIBUTE_NAME,
                "one");
        EasyMock.expect(request.getRequestScope()).andReturn(requestScope)
                .anyTimes();
        EasyMock.expect(request.getSessionScope()).andReturn(sessionScope)
                .anyTimes();
        EasyMock.expect(request.getRequestLocale()).andReturn(null).anyTimes();
        EasyMock.replay(request);
        assertTrue(container.isValidDefinition("test.def1", request));
        assertTrue(container.isValidDefinition("test.def.one", request));
        assertFalse(container.isValidDefinition("test.def.two", request));

        EasyMock.reset(request);
        requestScope.clear();
        requestScope.put(
                DefaultKeyExtractor.DEFINITIONS_FACTORY_KEY_ATTRIBUTE_NAME,
                "two");
        EasyMock.expect(request.getRequestScope()).andReturn(requestScope)
                .anyTimes();
        EasyMock.expect(request.getSessionScope()).andReturn(sessionScope)
                .anyTimes();
        EasyMock.expect(request.getRequestLocale()).andReturn(null).anyTimes();
        EasyMock.replay(request);
        assertTrue(container.isValidDefinition("test.def1", request));
        assertFalse(container.isValidDefinition("test.def.one", request));
        assertTrue(container.isValidDefinition("test.def.two", request));
    }

    /**
     * Extends {@link TilesApplicationContext} to act like a ServletContext.
     */
    private static interface ContextLikeTilesApplicationContext extends TilesApplicationContext {

        /**
         * Returns an initialization parameter.
         *
         * @param parameterName The name of the parameter.
         * @return The value of the parameter.
         */
        String getInitParameter(String parameterName);

        /**
         * Returns the init parameter names.
         *
         * @return The parameter names.
         */
        Enumeration<String> getInitParameterNames();
    }
}
