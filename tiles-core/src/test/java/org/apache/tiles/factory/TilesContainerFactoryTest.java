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
 *
 */
package org.apache.tiles.factory;

import junit.framework.TestCase;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.servlet.ServletTilesApplicationContext;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.impl.BasicTilesContainer;

import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;


public class TilesContainerFactoryTest extends TestCase {

    private ServletContext context;

    public void setUp() {
        context = EasyMock.createMock(ServletContext.class);
    }

    public void testGetFactory() throws TilesException {
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.replay(context);
        TilesContainerFactory factory = TilesContainerFactory.getFactory(context);
        assertNotNull(factory);
        assertEquals(TilesContainerFactory.class, factory.getClass());

        EasyMock.reset(context);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(TestFactory.class.getName());
        EasyMock.replay(context);
        factory = TilesContainerFactory.getFactory(context);
        assertNotNull(factory);
        assertEquals(TestFactory.class, factory.getClass());

        EasyMock.reset(context);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn("org.missing.Class");
        EasyMock.replay(context);
        try {
            TilesContainerFactory.getFactory(context);
            fail("Invalid classname.  Exception should have been thrown.");
        }
        catch (TilesException e) {
        }
    }

    public void testCreateContainer() throws TilesException, MalformedURLException {
        URL url = getClass().getResource("test-defs.xml");
        Vector enumeration = new Vector();
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.DEFINITIONS_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(EasyMock.isA(String.class))).andReturn(null).anyTimes();
        EasyMock.expect(context.getInitParameterNames()).andReturn(enumeration.elements());
        EasyMock.expect(context.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        EasyMock.replay(context);

        TilesContainerFactory factory = TilesContainerFactory.getFactory(context);
        TilesContainer container = factory.createContainer(context);

        assertNotNull(container);
        //now make sure it's initialized
        try {
            container.init(new HashMap<String, String>());
            fail("Container should have allready been initialized");
        }
        catch (IllegalStateException te) {
        }

    }

    public void testGetInitParameterMap() throws TilesException {
        Vector keys = new Vector();
        keys.add("one");
        keys.add("two");

        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameterNames()).andReturn(keys.elements());
        EasyMock.expect(context.getInitParameter("one")).andReturn("oneValue");
        EasyMock.expect(context.getInitParameter("two")).andReturn("twoValue");
        EasyMock.replay(context);

        TilesContainerFactory factory = TilesContainerFactory.getFactory(context);
        Map map = factory.getInitParameterMap(context);

        assertEquals(2, map.size());
        assertTrue(map.containsKey("one"));
        assertTrue(map.containsKey("two"));
        assertEquals("oneValue", map.get("one"));
        assertEquals("twoValue", map.get("two"));
    }

    public static class TestFactory extends TilesContainerFactory {

    }
}
