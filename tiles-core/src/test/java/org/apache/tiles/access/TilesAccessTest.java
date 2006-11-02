/*
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
package org.apache.tiles.access;

import org.easymock.EasyMock;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.impl.BasicTilesContainer;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

public class TilesAccessTest extends TestCase {

    private ServletContext context;

    public void setUp() {
        context = EasyMock.createMock(ServletContext.class);
    }

    public void testSetContext() throws TilesException {
        TilesContainer container = new BasicTilesContainer();
        context.setAttribute(TilesAccess.CONTAINER_ATTRIBUTE,container);
        EasyMock.replay(context);
        TilesAccess.setContainer(context, container);
        EasyMock.verify(context);
    }

    public void testGetContext() throws TilesException {
        TilesContainer container = new BasicTilesContainer();
        EasyMock.expect(context.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container);
        EasyMock.replay(context);
        assertEquals(container, TilesAccess.getContainer(context));
        EasyMock.verify(context);
    }

}
