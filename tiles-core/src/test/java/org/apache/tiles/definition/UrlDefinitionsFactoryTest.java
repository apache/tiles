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
package org.apache.tiles.definition;

import junit.framework.TestCase;

import java.util.Locale;
import java.util.List;


/**
 * @version $Rev$ $Date$
 */
public class UrlDefinitionsFactoryTest extends TestCase {

    private UrlDefinitionsFactory factory; 

    public void setUp() {
        factory = new UrlDefinitionsFactory();
    }

    public void testCalculatePostfixes() {
        Locale locale = Locale.US;

        List<String> posts = UrlDefinitionsFactory.calculatePostfixes(locale);
        assertEquals(3, posts.size());
        assertTrue(posts.contains("_en_US"));
        assertTrue(posts.contains("_en"));

        locale = Locale.ENGLISH;
        posts = UrlDefinitionsFactory.calculatePostfixes(locale);
        assertEquals(2, posts.size());
        assertTrue(posts.contains("_en"));
    }

    public void testCancatPostfix() {
        String postfix = "_en_US";
        assertEquals("a_en_US", factory.concatPostfix("a", postfix));
        assertEquals("a_en_US.jsp", factory.concatPostfix("a.jsp", postfix));
        assertEquals("file_en_US.jsp", factory.concatPostfix("file.jsp", postfix));
        assertEquals("./path/file_en_US.jsp", factory.concatPostfix("./path/file.jsp", postfix));
    }

}
