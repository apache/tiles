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
package org.apache.tiles.context.enhanced;

import junit.framework.TestCase;
import org.apache.tiles.TilesApplicationContext;
import org.easymock.EasyMock;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import java.util.HashSet;


/**
 * @version $Rev$ $Date$
 */
public class EnhancedTilesApplicationContextTest extends TestCase {

    /**
     * Number of properties container inside the test.properties file.
     */
    private static final int TEST_PROPERTIES_SIZE = 4;

    /**
     * The root Tiles application context.
     */
    private TilesApplicationContext root;

    /**
     * The enhanced Tiles application context.
     */
    private EnhancedTilesApplicationContext context;

    /** {@inheritDoc} */
    @Override
    public void setUp() {
        root = EasyMock.createMock(TilesApplicationContext.class);
        context = new EnhancedTilesApplicationContext(root);
    }

    /**
     * Tests resource getting.
     *
     * @throws IOException If something goes wrong.
     */
    public void testGetResources() throws IOException {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            String url = "test.properties";
            HashSet<URL> set = new HashSet<URL>();
            URL u = new URL("file://tiles/test.properties");
            set.add(u);
            EasyMock.expect(root.getResources(url)).andReturn(set);
            EasyMock.replay(root);
            Thread.currentThread().setContextClassLoader(new MockClassLoader());

            assertEquals(TEST_PROPERTIES_SIZE, context.getResources(
                    "test.properties").size());
            EasyMock.verify(root);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    /**
     * An mock class loader.
     */
    public class MockClassLoader extends ClassLoader {

        /**
         * A vector of resources.
         */
        private Vector<URL> resources;

        /**
         * Constructor.
         *
         * @throws MalformedURLException If the URL is not valid (that should
         * not happen).
         */
        public MockClassLoader() throws MalformedURLException {
            resources = new Vector<URL>();
            resources.add(new URL("file://tiles/test/test.properties"));
            resources.add(new URL("file://tiles/two/test.properties"));
            resources.add(new URL("file://tiles/three/test.properties"));
        }

        /** {@inheritDoc} */
        @Override
        public Enumeration<URL> findResources(String path) {
            return resources.elements();
        }
    }


}
