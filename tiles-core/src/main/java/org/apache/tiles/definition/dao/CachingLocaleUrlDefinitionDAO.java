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

package org.apache.tiles.definition.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.definition.Refreshable;
import org.apache.tiles.util.LocaleUtil;
import org.apache.tiles.util.WildcardHelper;

/**
 * <p>
 * A definitions DAO (loading URLs and using Locale as a customization key) that
 * caches definitions that have been loaded in a raw way (i.e. with inheritance
 * that is not resolved).
 * </p>
 * <p>
 * It can check if the URLs change, but by default this feature is turned off.
 * </p>
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class CachingLocaleUrlDefinitionDAO extends BaseLocaleUrlDefinitionDAO
        implements Refreshable {

    /**
     * Initialization parameter to set whether we want to refresh URLs when they
     * change.
     *
     * @since 2.1.0
     */
    public static final String CHECK_REFRESH_INIT_PARAMETER =
        "org.apache.tiles.definition.dao.LocaleUrlDefinitionDAO.CHECK_REFRESH";

    /**
     * The locale-specific set of definitions objects.
     *
     * @since 2.1.0
     */
    protected Map<Locale, Map<String, Definition>> locale2definitionMap;

    /**
     * Flag that, when <code>true</code>, enables automatic checking of URLs
     * changing.
     *
     * @since 2.1.0
     */
    protected boolean checkRefresh = false;

    /**
     * An object that helps in resolving definitions with wildcards.
     *
     * @since 2.1.0
     */
    protected WildcardHelper wildcardHelper = new WildcardHelper();

    /**
     * Stores patterns depending on the locale they refer to.
     *
     * @since 2.1.0
     */
    protected Map<Locale, List<WildcardMapping>> localePatternPaths =
        new HashMap<Locale, List<WildcardMapping>>();

    /**
     * Constructor.
     *
     * @since 2.1.0
     */
    public CachingLocaleUrlDefinitionDAO() {
        locale2definitionMap = new HashMap<Locale, Map<String, Definition>>();
    }

    /** {@inheritDoc} */
    @Override
    public void init(Map<String, String> params) {
        super.init(params);

        String param = params.get(CHECK_REFRESH_INIT_PARAMETER);
        checkRefresh = "true".equals(param);
    }

    /** {@inheritDoc} */
    public Definition getDefinition(String name, Locale customizationKey) {
        Definition retValue = null;
        if (customizationKey == null) {
            customizationKey = LocaleUtil.NULL_LOCALE;
        }
        Map<String, Definition> definitions = getDefinitions(customizationKey);
        if (definitions != null) {
            retValue = definitions.get(name);

            if (retValue == null
                    && localePatternPaths.containsKey(customizationKey)) {
                retValue = resolveWildcardDefinition(localePatternPaths
                        .get(customizationKey), name);

                if (retValue != null) {
                    try {
                        synchronized (definitions) {
                            definitions.put(name, retValue);
                        }
                    } catch (NoSuchDefinitionException ex) {
                        throw new IllegalStateException("Unable to resolve wildcard mapping", ex);
                    }
                }
            }
        }

        return retValue;
    }

    /** {@inheritDoc} */
    public Map<String, Definition> getDefinitions(Locale customizationKey) {
        if (customizationKey == null) {
            customizationKey = LocaleUtil.NULL_LOCALE;
        }
        Map<String, Definition> retValue = locale2definitionMap
                .get(customizationKey);
        if (retValue == null || (checkRefresh && refreshRequired())) {
            retValue = checkAndloadDefinitions(customizationKey);
        }
        return retValue;
    }

    /** {@inheritDoc} */
    public synchronized void refresh() {
        if (refreshRequired()) {
            locale2definitionMap.clear();
        }
    }

    /**
     * Sets the flag to check URL refresh. If not called, the default is
     * <code>false</code>.
     *
     * @param checkRefresh When <code>true</code>, enables automatic checking
     * of URLs changing.
     * @since 2.1.0
     */
    public void setCheckRefresh(boolean checkRefresh) {
        this.checkRefresh = checkRefresh;
    }

    /**
     * Checks if URLs have changed. If yes, it clears the cache. Then continues
     * loading definitions.
     *
     * @param customizationKey The locale to use when loading URLs.
     * @return The loaded definitions.
     * @since 2.1.0
     */
    protected synchronized Map<String, Definition> checkAndloadDefinitions(
            Locale customizationKey) {
        if (checkRefresh && refreshRequired()) {
            locale2definitionMap.clear();
        }
        return loadDefinitions(customizationKey);
    }

    /**
     * Tries to load definitions if necessary.
     *
     * @param customizationKey The locale to use when loading URLs.
     * @return The loaded definitions.
     * @since 2.1.0
     */
    protected Map<String, Definition> loadDefinitions(Locale customizationKey) {
        Map<String, Definition> localeDefsMap = locale2definitionMap
                .get(customizationKey);
        if (localeDefsMap != null) {
            return localeDefsMap;
        }

        localeDefsMap = loadDefinitionsFromURLs(customizationKey);
        return localeDefsMap;
    }

    /**
     * Loads definitions from the URLs.
     *
     * @param customizationKey The locale to use when loading URLs.
     * @return The loaded definitions.
     * @since 2.1.0
     */
    protected Map<String, Definition> loadDefinitionsFromURLs(Locale customizationKey) {
        Map<String, Definition> localeDefsMap = loadRawDefinitionsFromURLs(customizationKey);
        postDefinitionLoadOperations(localeDefsMap, customizationKey);

        return localeDefsMap;
    }

    /**
     * Loads the raw definitions from the URLs associated with a locale.
     *
     * @param customizationKey The locale to use when loading URLs.
     * @return The loaded definitions.
     * @since 2.1.3
     */
    protected Map<String, Definition> loadRawDefinitionsFromURLs(
            Locale customizationKey) {
        Map<String, Definition> localeDefsMap;

        String postfix = LocaleUtil.calculatePostfix(customizationKey);
        Locale parentLocale = LocaleUtil.getParentLocale(customizationKey);
        localeDefsMap = new HashMap<String, Definition>();
        if (parentLocale != null) {
            Map<String, Definition> parentDefs = loadParentDefinitions(parentLocale);
            if (parentDefs != null) {
                localeDefsMap.putAll(parentDefs);
            }
        }
        // For each source, the URL must be loaded.
        for (URL url : sourceURLs) {
            String path = url.toExternalForm();

            String newPath = LocaleUtil.concatPostfix(path, postfix);
            try {
                URL newUrl = new URL(newPath);
                Map<String, Definition> defsMap = loadDefinitionsFromURL(newUrl);
                if (defsMap != null) {
                    localeDefsMap.putAll(defsMap);
                }
            } catch (MalformedURLException e) {
                throw new DefinitionsFactoryException("Error parsing URL "
                        + newPath, e);
            }
        }
        locale2definitionMap.put(customizationKey, localeDefsMap);
        return localeDefsMap;
    }

    /**
     * Loads parent definitions, i.e. definitions mapped to a parent locale.
     *
     * @param parentLocale The locale to use when loading URLs.
     * @return The loaded parent definitions.
     * @since 2.1.0
     */
    protected Map<String, Definition> loadParentDefinitions(Locale parentLocale) {
        return loadDefinitions(parentLocale);
    }

    /**
     * Operations to be done after definitions are loaded.
     *
     * @param localeDefsMap The loaded definitions.
     * @param customizationKey The locale to use when loading URLs.
     * @since 2.1.0
     */
    protected void postDefinitionLoadOperations(
            Map<String, Definition> localeDefsMap, Locale customizationKey) {

        List<WildcardMapping> lpaths = localePatternPaths
                .get(customizationKey);
        if (lpaths == null) {
            lpaths = new ArrayList<WildcardMapping>();
            localePatternPaths.put(customizationKey, lpaths);
        }

        addWildcardPaths(lpaths, localeDefsMap);
    }

    /**
     * Adds wildcard paths that are stored inside a normal definition map.
     *
     * @param paths The list containing the currently stored paths.
     * @param defsMap The definition map to parse.
     * @since 2.1.0
     */
    protected void addWildcardPaths(List<WildcardMapping> paths,
            Map<String, Definition> defsMap) {
        for (Map.Entry<String, Definition> de : defsMap.entrySet()) {
            if (de.getKey().
                    indexOf('*') != -1) {
                paths.add(new WildcardMapping(de.getKey(), de.getValue()));
            }
        }
    }

    /**
     * Try to resolve a wildcard definition.
     *
     * @param paths The list containing the currently stored paths.
     * @param name The name of the definition to resolve.
     * @return A definition, if found, or <code>null</code> if not.
     * @since 2.1.0
     */
    protected Definition resolveWildcardDefinition(
            List<WildcardMapping> paths, String name) {
        Map<Integer, String> vars = new HashMap<Integer, String>();
        Definition d = null;

        for (WildcardMapping wm : paths) {
            if (wildcardHelper.match(vars, name, wm.getPattern())) {
                d = replaceDefinition(wm.getDefinition(), name, vars);
                break;
            }
        }

        return d;
    }

    /**
     * Creates a definition given its representation with wildcards.
     *
     * @param d The definition to replace.
     * @param name The name of the definition to be created.
     * @param vars The variables to be substituted.
     * @return The definition that can be rendered.
     * @since 2.1.0
     */
    protected Definition replaceDefinition(Definition d, String name,
            Map<Integer, String> vars) {
        Definition nudef = new Definition();

        nudef.setExtends(replace(d.getExtends(), vars));
        nudef.setName(name);
        nudef.setPreparer(replace(d.getPreparer(), vars));
        nudef.setTemplateAttribute(replaceVarsInAttribute(d
                .getTemplateAttribute(), vars));

        Set<String> localAttributeNames = d.getLocalAttributeNames();
        if (localAttributeNames != null && !localAttributeNames.isEmpty()) {
            for (String attributeName : localAttributeNames) {
                Attribute attr = d.getLocalAttribute(attributeName);
                Attribute nuattr = replaceVarsInAttribute(attr, vars);
    
                nudef.putAttribute(replace(attributeName, vars), nuattr);
            }
        }

        return nudef;
    }

    /**
     * Replaces variables into an attribute.
     *
     * @param attr The attribute to be used as a basis, containing placeholders
     * for variables.
     * @param vars The variables to replace.
     * @return A new instance of an attribute, whose properties have been
     * replaced with variables' values.
     */
    private Attribute replaceVarsInAttribute(Attribute attr,
            Map<Integer, String> vars) {
        Attribute nuattr = new Attribute();

        nuattr.setRole(replace(attr.getRole(), vars));
        nuattr.setRenderer(attr.getRenderer());
        nuattr.setExpression(attr.getExpression());

        Object value = attr.getValue();
        if (value instanceof String) {
            value = replace((String) value, vars);
        }
        nuattr.setValue(value);
        return nuattr;
    }

    /**
     * Replaces a string with placeholders using values of a variable map.
     *
     * @param st The string to replace.
     * @param vars The variables.
     * @return The replaced string.
     * @since 2.1.0
     */
    protected String replace(String st, Map<Integer, String> vars) {
        return org.apache.tiles.util.WildcardHelper.convertParam(st, vars);
    }

    /**
     * Maps a pattern with a definition in cache.
     *
     * @since 2.1.0
     */
    protected class WildcardMapping {

        /**
         * The compiled pattern.
         */
        private int[] pattern;

        /**
         * The definition that matches the pattern.
         */
        private Definition definition;

        /**
         * Constructor.
         *
         * @param pattern The compiled pattern.
         * @param definition A definition that matches the pattern.
         *
         * @since 2.1.0
         */
        public WildcardMapping(String pattern, Definition definition) {
            this.pattern = wildcardHelper.compilePattern(pattern);
            this.definition = definition;
        }

        /**
         * Returns the definition.
         *
         * @return The definition.
         * @since 2.1.0
         */
        public Definition getDefinition() {
            return definition;
        }

        /**
         * Returns the compiled pattern.
         *
         * @return The pattern.
         * @since 2.1.0
         */
        public int[] getPattern() {
            return pattern;
        }
    }
}
