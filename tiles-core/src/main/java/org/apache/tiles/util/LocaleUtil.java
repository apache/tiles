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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Utilities for locale manipulation.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public final class LocaleUtil {

    /**
     * The "null" Locale, i.e. a Locale that points to no real locale.
     *
     * @since 2.1.0
     */
    public static final Locale NULL_LOCALE = new Locale("");

    /**
     * Private constructor to avoid instantiation.
     */
    private LocaleUtil() {
    }

    /**
     * Calculate the postfixes along the search path from the base bundle to the
     * bundle specified by baseName and locale. Method copied from
     * java.util.ResourceBundle
     *
     * @param locale The locale.
     * @return a list of postfixes to add to filenames.
     * @since 2.1.0
     */
    public static List<String> calculatePostfixes(Locale locale) {
        final List<String> result = new ArrayList<String>();
        // The default configuration file must be loaded to allow correct
        // definition inheritance.
        result.add("");

        if (locale == null) {
            return result;
        }

        final String language = locale.getLanguage();
        final int languageLength = language.length();
        final String country = locale.getCountry();
        final int countryLength = country.length();
        final String variant = locale.getVariant();
        final int variantLength = variant.length();

        if (languageLength + countryLength + variantLength == 0) {
            // The locale is "", "", "".
            return result;
        }

        final StringBuffer temp = new StringBuffer();
        temp.append('_');
        temp.append(language);

        if (languageLength > 0) {
            result.add(temp.toString());
        }

        if (countryLength + variantLength == 0) {
            return result;
        }

        temp.append('_');
        temp.append(country);

        if (countryLength > 0) {
            result.add(temp.toString());
        }

        if (variantLength == 0) {
            return result;
        } else {
            temp.append('_');
            temp.append(variant);
            result.add(temp.toString());
            return result;
        }
    }

    /**
     * Calculate the postfix to append to a filename to load the correct single
     * filename for that Locale.
     *
     * @param locale The locale.
     * @return The postfix to append to the filename.
     * @since 2.1.0
     */
    public static String calculatePostfix(Locale locale) {
        if (locale == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        if (!"".equals(language)) {
            builder.append("_");
            builder.append(language);
            if (!"".equals(country)) {
                builder.append("_");
                builder.append(country);
                if (!"".equals(variant)) {
                    builder.append("_");
                    builder.append(variant);
                }
            }
        }
        return builder.toString();
    }

    /**
     * Concat postfix to the name. Take care of existing filename extension.
     * Transform the given name "name.ext" to have "name" + "postfix" + "ext".
     * If there is no ext, return "name" + "postfix".
     *
     * @param name Filename.
     * @param postfix Postfix to add.
     * @return Concatenated filename.
     * @since 2.1.0
     */
    public static String concatPostfix(String name, String postfix) {
        if (postfix == null || "".equals(postfix)) {
            return name;
        }

        // Search file name extension.
        // take care of Unix files starting with .
        int dotIndex = name.lastIndexOf(".");
        int lastNameStart = name.lastIndexOf(java.io.File.pathSeparator);
        if (dotIndex < 1 || dotIndex < lastNameStart) {
            return name + postfix;
        }

        String ext = name.substring(dotIndex);
        name = name.substring(0, dotIndex);
        return name + postfix + ext;
    }

    /**
     * <p>
     * Returns the "parent" locale of a given locale.
     * </p>
     * <p>
     * If the original locale is only language-based, the {@link #NULL_LOCALE}
     * object is returned.
     * </p>
     * <p>
     * If the original locale is {@link #NULL_LOCALE}, then <code>null</code>
     * is returned.
     * </p>
     *
     * @param locale The original locale.
     * @return The parent locale.
     */
    public static Locale getParentLocale(Locale locale) {
        Locale retValue = null;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        if (!"".equals(variant)) {
            retValue = new Locale(language, country);
        } else if (!"".equals(country)) {
            retValue = new Locale(language);
        } else if (!"".equals(language)) {
            retValue = NULL_LOCALE;
        }

        return retValue;
    }
}
