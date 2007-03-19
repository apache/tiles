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
package org.apache.tiles.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.apache.tiles.TilesException;
import org.apache.tiles.factory.TilesContainerFactory;
import org.easymock.EasyMock;


/**
 * @version $Rev$ $Date$
 */
public class BasicTilesContainerTest extends TestCase {
    
    /**
     * The container.
     */
    private BasicTilesContainer container;

    /** {@inheritDoc} */
    @Override
    public void setUp() {
        ServletContext context = EasyMock.createMock(ServletContext.class);
        URL url = getClass().getResource("/org/apache/tiles/factory/test-defs.xml");
        
        Vector<String> v = new Vector<String>();

        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.DEFINITIONS_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(EasyMock.isA(String.class))).andReturn(null).anyTimes();
        EasyMock.expect(context.getInitParameterNames()).andReturn(v.elements()).anyTimes();
        try {
            EasyMock.expect(context.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error getting Tiles configuration URL",
                    e);
        }
        EasyMock.replay(context);
        try {
            TilesContainerFactory factory = TilesContainerFactory.getFactory(context);
            container = (BasicTilesContainer) factory.createContainer(context);
        } catch (TilesException e) {
            throw new RuntimeException("Error initializing factory", e);
        }
    }
    
    /**
     * Tests basic Tiles container initialization.
     */
    public void testInitialization() {
        assertNotNull(container);
        assertNotNull(container.getContextFactory());
        assertNotNull(container.getPreparerFactory());
        assertNotNull(container.getDefinitionsFactory());
    }
}
