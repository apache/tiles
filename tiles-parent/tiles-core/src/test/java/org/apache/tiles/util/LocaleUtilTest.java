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

import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * Tests {@link LocaleUtil}.
 *
 * @version $Rev$ $Date$
 */
public class LocaleUtilTest extends TestCase {

    /**
     * The number of foreseen URLs with postfixes.
     */
    private static final int POSTFIX_COUNT = 3;

    /**
     * Test method for {@link LocaleUtil#calculatePostfixes(java.util.Locale)}.
     */
    public void testCalculatePostfixes() {
        Locale locale = Locale.US;

        List<String> posts = LocaleUtil.calculatePostfixes(locale);
        assertEquals(POSTFIX_COUNT, posts.size());
        assertTrue(posts.contains("_en_US"));
        assertTrue(posts.contains("_en"));

        locale = Locale.ENGLISH;
        posts = LocaleUtil.calculatePostfixes(locale);
        assertEquals(2, posts.size());
        assertTrue(posts.contains("_en"));

        locale = Locale.ROOT;
        posts = LocaleUtil.calculatePostfixes(locale);
        assertEquals(1, posts.size());
        assertTrue(posts.contains(""));

        posts = LocaleUtil.calculatePostfixes(null);
        assertEquals(1, posts.size());
        assertTrue(posts.contains(""));

        locale = new Locale("it", "IT", "pidgin");
        posts = LocaleUtil.calculatePostfixes(locale);
        assertEquals(4, posts.size());
        assertTrue(posts.contains("_it_IT"));
        assertTrue(posts.contains("_it_IT_pidgin"));
    }

    /**
     * Test method for {@link LocaleUtil#calculatePostfix(java.util.Locale)}.
     */
    public void testCalculatePostfix() {
        assertEquals("The English locale is not correct", "_en", LocaleUtil
                .calculatePostfix(Locale.ENGLISH));
        assertEquals("The US locale is not correct", "_en_US", LocaleUtil
                .calculatePostfix(Locale.US));
        Locale locale = new Locale("es", "ES", "Traditional_WIN");
        assertEquals("The Spain Traditional_WIN locale is not correct",
                "_es_ES_Traditional_WIN", LocaleUtil.calculatePostfix(locale));
        assertEquals("", LocaleUtil.calculatePostfix(null));
    }

    /**
     * Test method for {@link LocaleUtil#concatPostfix(java.lang.String, java.lang.String)}.
     */
    public void testConcatPostfix() {
        String postfix = "_en_US";
        assertEquals("a_en_US", LocaleUtil.concatPostfix("a", postfix));
        assertEquals("a_en_US.jsp", LocaleUtil.concatPostfix("a.jsp", postfix));
        assertEquals("file_en_US.jsp", LocaleUtil.concatPostfix("file.jsp", postfix));
        assertEquals("./path/file_en_US.jsp", LocaleUtil.concatPostfix("./path/file.jsp", postfix));
        assertEquals("a", LocaleUtil.concatPostfix("a", null));
    }

    /**
     * Test method for {@link LocaleUtil#getParentLocale(Locale)}.
     */
    public void testGetParentLocale() {
        assertNull("The parent locale of NULL_LOCALE is not correct",
                LocaleUtil.getParentLocale(LocaleUtil.NULL_LOCALE));
        assertEquals("The parent locale of 'en' is not correct",
                LocaleUtil.NULL_LOCALE, LocaleUtil
                        .getParentLocale(Locale.ENGLISH));
        assertEquals("The parent locale of 'en_US' is not correct",
                Locale.ENGLISH, LocaleUtil.getParentLocale(Locale.US));
        Locale locale = new Locale("es", "ES", "Traditional_WIN");
        Locale parentLocale = new Locale("es", "ES");
        assertEquals("The parent locale of 'es_ES_Traditional_WIN' is not correct",
                parentLocale, LocaleUtil.getParentLocale(locale));
    }
}
