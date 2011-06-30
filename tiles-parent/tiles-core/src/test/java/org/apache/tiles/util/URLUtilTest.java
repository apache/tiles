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

package org.apache.tiles.util;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * Tests {@link URLUtil}.
 *
 * @version $Rev$ $Date$
 */
public class URLUtilTest {

    /**
     * The number of valid URLs.
     */
    private static final int URLS_SIZE = 3;

    /**
     * Test method for {@link URLUtil#getBaseTilesDefinitionURLs(java.util.Collection)}.
     *
     * @throws MalformedURLException If something goes wrong.
     */
    @Test
    public void testGetBaseTilesDefinitionURLs() throws MalformedURLException {
        URL url1 = new URL("file:///nonexistent/tiles.xml");
        URL url2 = new URL("file:///nonexistent/tiles_it.xml");
        URL url3 = new URL("file:///nonexistent2/tiles.xml");
        URL url4 = new URL("file:///non_existent/tiles.xml");

        Set<URL> urls = new HashSet<URL>();
        urls.add(url1);
        urls.add(url2);
        urls.add(url3);
        urls.add(url4);

        List<URL> result = URLUtil.getBaseTilesDefinitionURLs(urls);
        assertEquals(URLS_SIZE, result.size());
        assertTrue(result.contains(url1));
        assertFalse(result.contains(url2));
        assertTrue(result.contains(url3));
        assertTrue(result.contains(url4));
    }
}
