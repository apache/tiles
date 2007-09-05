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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.apache.tiles.TilesException;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.factory.KeyedDefinitionsFactoryTilesContainerFactory;
import org.apache.tiles.factory.TilesContainerFactory;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer.DefaultKeyExtractor;
import org.apache.tiles.locale.impl.DefaultLocaleResolver;
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
        defaults = new HashMap<String, String>();
        defaults.put(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM,
                KeyedDefinitionsFactoryTilesContainerFactory.class.getName());

        ServletContext context = EasyMock.createMock(ServletContext.class);

        Vector<String> v = new Vector<String>();
        v.add(KeyedDefinitionsFactoryTilesContainerFactory.CONTAINER_KEYS_INIT_PARAM);
        v.add(KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "one");
        v.add(KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "two");

        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
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
        try {
            URL url = getClass().getResource("/org/apache/tiles/factory/test-defs.xml");
            EasyMock.expect(context.getResource("/WEB-INF/tiles.xml")).andReturn(url);
            url = getClass().getResource("/org/apache/tiles/factory/test-defs-key-one.xml");
            EasyMock.expect(context.getResource("/WEB-INF/tiles-one.xml")).andReturn(url);
            url = getClass().getResource("/org/apache/tiles/factory/test-defs-key-two.xml");
            EasyMock.expect(context.getResource("/WEB-INF/tiles-two.xml")).andReturn(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error getting Tiles configuration URL",
                    e);
        }
        EasyMock.replay(context);
        try {
            TilesContainerFactory factory = TilesContainerFactory.getFactory(context, defaults);
            container = (KeyedDefinitionsFactoryTilesContainer) factory.createContainer(context);
        } catch (TilesException e) {
            throw new RuntimeException("Error initializing factory", e);
        }
    }

    /**
     * Tests container initialization.
     */
    public void testInitialization() {
        assertNotNull(container);
        assertNotNull(container.getContextFactory());
        assertNotNull(container.getPreparerFactory());
        assertNotNull(container.getDefinitionsFactory());
        assertNotNull(container.getProperDefinitionsFactory("one"));
        assertNotNull(container.getProperDefinitionsFactory("two"));
    }

    /**
     * Tests initialization for postponed definitions factories.
     *
     * @throws MalformedURLException If sources are not valid (that should not
     * happen).
     * @throws TilesException If something goes wrong.
     */
    public void testPostponedDefinitionsFactoryInitialization() throws MalformedURLException, TilesException {
        KeyedDefinitionsFactoryTilesContainer container;
        ServletContext context = EasyMock.createMock(ServletContext.class);

        Vector<String> v = new Vector<String>();

        EasyMock.reset(context);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.DEFINITIONS_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(BasicTilesContainer.DEFINITIONS_CONFIG)).andReturn(null);
        EasyMock.expect(context.getInitParameter("definitions-config")).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .CONTAINER_FACTORY_MUTABLE_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(
                KeyedDefinitionsFactoryTilesContainerFactory.CONTAINER_KEYS_INIT_PARAM))
                .andReturn(null);
        URL url = getClass().getResource("/org/apache/tiles/factory/test-defs.xml");
        EasyMock.expect(context.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        url = getClass().getResource("/org/apache/tiles/factory/test-defs-key-one.xml");
        EasyMock.expect(context.getResource("/WEB-INF/tiles-one.xml")).andReturn(url);
        url = getClass().getResource("/org/apache/tiles/factory/test-defs-key-two.xml");
        EasyMock.expect(context.getResource("/WEB-INF/tiles-two.xml")).andReturn(url);
        EasyMock.expect(context.getInitParameterNames()).andReturn(v.elements()).anyTimes();
        EasyMock.replay(context);
        KeyedDefinitionsFactoryTilesContainerFactory factory =
            (KeyedDefinitionsFactoryTilesContainerFactory)
            TilesContainerFactory.getFactory(context, defaults);
        container = (KeyedDefinitionsFactoryTilesContainer) factory.createContainer(context);

        assertNotNull(container);
        assertNotNull(container.getDefinitionsFactory());
        assertNull(container.getProperDefinitionsFactory("one"));
        assertNull(container.getProperDefinitionsFactory("two"));

        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put(BasicTilesContainer.DEFINITIONS_CONFIG,
                "/WEB-INF/tiles-one.xml");
        DefinitionsFactory defsFactory = factory.createDefinitionsFactory(context);
        container.setDefinitionsFactory("one", defsFactory, initParams);
        initParams.put(BasicTilesContainer.DEFINITIONS_CONFIG,
                "/WEB-INF/tiles-two.xml");
        defsFactory = factory.createDefinitionsFactory(context);
        container.setDefinitionsFactory("two", defsFactory, initParams);
        assertNotNull(container.getProperDefinitionsFactory("one"));
        assertNotNull(container.getProperDefinitionsFactory("two"));
    }

    /**
     * Tests if the definitions factory has been used.
     */
    public void testDefinitionsFactoryUse() {
        HttpServletRequest request = EasyMock.createMock(
                HttpServletRequest.class);
        HttpSession session = EasyMock.createMock(HttpSession.class);
        HttpServletResponse response = EasyMock.createMock(
                HttpServletResponse.class);

        EasyMock.reset(request);
        EasyMock.reset(session);
        EasyMock.reset(response);
        EasyMock.expect(request.getSession(false)).andReturn(session).anyTimes();
        EasyMock.expect(session.getAttribute(DefaultLocaleResolver.LOCALE_KEY)).andReturn(null).anyTimes();
        EasyMock.expect(request.getLocale()).andReturn(null).anyTimes();
        EasyMock.expect(request.getAttribute(
                DefaultKeyExtractor.DEFINITIONS_FACTORY_KEY_ATTRIBUTE_NAME))
                .andReturn(null).anyTimes();
        EasyMock.replay(request);
        EasyMock.replay(session);
        EasyMock.replay(response);
        assertTrue(container.isValidDefinition("test.def1", request, response));
        assertFalse(container.isValidDefinition("test.def.one", request, response));
        assertFalse(container.isValidDefinition("test.def.two", request, response));

        EasyMock.reset(request);
        EasyMock.reset(session);
        EasyMock.reset(response);
        EasyMock.expect(request.getAttribute(
                DefaultKeyExtractor.DEFINITIONS_FACTORY_KEY_ATTRIBUTE_NAME))
                .andReturn("one").anyTimes();
        EasyMock.expect(request.getSession(false)).andReturn(session).anyTimes();
        EasyMock.expect(session.getAttribute(DefaultLocaleResolver.LOCALE_KEY)).andReturn(null).anyTimes();
        EasyMock.expect(request.getLocale()).andReturn(null).anyTimes();
        EasyMock.replay(request);
        EasyMock.replay(session);
        EasyMock.replay(response);
        assertTrue(container.isValidDefinition("test.def1", request, response));
        assertTrue(container.isValidDefinition("test.def.one", request, response));
        assertFalse(container.isValidDefinition("test.def.two", request, response));

        EasyMock.reset(request);
        EasyMock.reset(session);
        EasyMock.reset(response);
        EasyMock.expect(request.getAttribute(
                DefaultKeyExtractor.DEFINITIONS_FACTORY_KEY_ATTRIBUTE_NAME))
                .andReturn("two").anyTimes();
        EasyMock.expect(request.getSession(false)).andReturn(session).anyTimes();
        EasyMock.expect(session.getAttribute(DefaultLocaleResolver.LOCALE_KEY)).andReturn(null).anyTimes();
        EasyMock.expect(request.getLocale()).andReturn(null).anyTimes();
        EasyMock.replay(request);
        EasyMock.replay(session);
        EasyMock.replay(response);
        assertTrue(container.isValidDefinition("test.def1", request, response));
        assertFalse(container.isValidDefinition("test.def.one", request, response));
        assertTrue(container.isValidDefinition("test.def.two", request, response));
    }
}
