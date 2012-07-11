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
package org.apache.tiles.web.startup;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.tiles.request.servlet.ServletApplicationContext;
import org.apache.tiles.startup.TilesInitializer;
import org.junit.Test;

/**
 * Tests {@link AbstractTilesListener}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractTilesListenerTest {

    /**
     * Test method for {@link AbstractTilesListener#contextInitialized(ServletContextEvent)}.
     */
    @Test
    public void testContextInitialized() {
        AbstractTilesListener listener = createMockBuilder(AbstractTilesListener.class).createMock();
        ServletContextEvent event = createMock(ServletContextEvent.class);
        ServletContext servletContext = createMock(ServletContext.class);
        TilesInitializer initializer = createMock(TilesInitializer.class);

        expect(event.getServletContext()).andReturn(servletContext);
        expect(listener.createTilesInitializer()).andReturn(initializer);
        initializer.initialize(isA(ServletApplicationContext.class));
        initializer.destroy();

        replay(listener, event, servletContext, initializer);
        listener.contextInitialized(event);
        listener.contextDestroyed(event);
        verify(listener, event, servletContext, initializer);
    }

}
