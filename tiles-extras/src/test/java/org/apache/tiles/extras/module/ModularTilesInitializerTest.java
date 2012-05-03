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

package org.apache.tiles.extras.module;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.startup.TilesInitializer;
import org.junit.Test;

/**
 * Tests {@link ModularTilesInitializer}.
 *
 * @version $Rev$ $Date$
 */
public class ModularTilesInitializerTest {

    /**
     * Tests {@link ModularTilesInitializer#initialize(ApplicationContext)}
     * and {@link ModularTilesInitializer#destroy()}.
     *
     * @throws MalformedURLException Never thrown.
     */
    @Test
    public void testInitialize() throws MalformedURLException {
        ApplicationContext preliminaryContext = createMock(ApplicationContext.class);
        ServletContext servletContext = createMock(ServletContext.class);
        URL manifestUrl = getClass().getResource("/FAKE-MANIFEST.MF");

        expect(preliminaryContext.getContext()).andReturn(servletContext);
        expect(servletContext.getResource("/META-INF/MANIFEST.MF")).andReturn(manifestUrl);

        replay(preliminaryContext, servletContext);
        ModularTilesInitializer initializer = new ModularTilesInitializer();
        initializer.initialize(preliminaryContext);
        assertTrue(TilesInitializer1.initialized);
        assertTrue(TilesInitializer2.initialized);
        initializer.destroy();
        assertTrue(TilesInitializer1.destroyed);
        assertTrue(TilesInitializer2.destroyed);
        verify(preliminaryContext, servletContext);
    }

    /**
     * A mock {@link TilesInitializer} with probes.
     *
     * @version $Rev$ $Date$
     */
    public static class TilesInitializer1 implements TilesInitializer {

        /**
         * A probe to see if the initializer has been initialized.
         */
        private static boolean initialized = false;

        /**
         * A probe to see if the initializer has been destroyed.
         */
        private static boolean destroyed = false;

        /** {@inheritDoc} */
        public void initialize(ApplicationContext preliminaryContext) {
            initialized = true;
        }

        /** {@inheritDoc} */
        public void destroy() {
            destroyed = true;
        }
    }

    /**
     * A second mock {@link TilesInitializer} with probes.
     *
     * @version $Rev$ $Date$
     */
    public static class TilesInitializer2 implements TilesInitializer {

        /**
         * A probe to see if the initializer has been initialized.
         */
        private static boolean initialized = false;

        /**
         * A probe to see if the initializer has been destroyed.
         */
        private static boolean destroyed = false;

        /** {@inheritDoc} */
        public void initialize(ApplicationContext preliminaryContext) {
            initialized = true;
        }

        /** {@inheritDoc} */
        public void destroy() {
            destroyed = true;
        }
    }
}
