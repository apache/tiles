/*
 * $Id$
 *
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tiles.factory;

import junit.framework.TestCase;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.servlet.ServletTilesApplicationContext;
import org.apache.tiles.impl.BasicTilesContainer;


public class TilesContainerFactoryTest extends TestCase {

    public void testCreateContainer() throws ConfigurationNotSupportedException {
        ServletContext context = (ServletContext)EasyMock.createMock(ServletContext.class);
        TilesContainer container = TilesContainerFactory.createContainer(context);

        assertEquals(BasicTilesContainer.class, container.getClass());
        assertEquals(ServletTilesApplicationContext.class, container.getApplicationContext().getClass());
    }

}
