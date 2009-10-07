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

package org.apache.tiles.definition;

import java.util.Locale;
import java.util.Map;

import org.apache.tiles.Definition;
import org.apache.tiles.Initializable;
import org.apache.tiles.awareness.TilesApplicationContextAware;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.DefinitionDAO;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.locale.impl.DefaultLocaleResolver;
import org.apache.tiles.reflect.ClassUtil;

/**
 * {@link DefinitionsFactory DefinitionsFactory} implementation that manages
 * Definitions configuration data from URLs, but resolving definition
 * inheritance when a definition is returned.. <p/>
 * <p>
 * The Definition objects are read from the
 * {@link org.apache.tiles.definition.digester.DigesterDefinitionsReader DigesterDefinitionsReader}
 * class unless another implementation is specified.
 * </p>
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
@SuppressWarnings("deprecation")
public class LocaleDefinitionsFactory extends
        UnresolvingLocaleDefinitionsFactory implements Initializable {

    /**
     * Initializes the DefinitionsFactory and its subcomponents.
     * <p/>
     * Implementations may support configuration properties to be passed in via
     * the params Map.
     *
     * @param params The Map of configuration properties.
     * @throws DefinitionsFactoryException if an initialization error occurs.
     */
    @SuppressWarnings("unchecked")
    public void init(Map<String, String> params) {
        String definitionDaoClassName = params
                .get(DefinitionsFactory.DEFINITION_DAO_INIT_PARAM);
        if (definitionDaoClassName != null) {
            definitionDao = (DefinitionDAO<Locale>) ClassUtil
                    .instantiate(definitionDaoClassName);
        } else {
            definitionDao = createDefaultDefinitionDAO();
        }
        if (definitionDao instanceof TilesApplicationContextAware) {
            ((TilesApplicationContextAware) definitionDao)
                    .setApplicationContext(applicationContext);
        }
        if (definitionDao instanceof Initializable) {
            ((Initializable) definitionDao).init(params);
        }

        String resolverClassName = params
                .get(DefinitionsFactory.LOCALE_RESOLVER_IMPL_PROPERTY);
        if (resolverClassName != null) {
            localeResolver = (LocaleResolver) ClassUtil.instantiate(resolverClassName);
        } else {
            localeResolver = createDefaultLocaleResolver();
        }
        localeResolver.init(params);
    }

    /** {@inheritDoc} */
    public Definition getDefinition(String name,
            TilesRequestContext tilesContext) {
        Definition retValue;
        Locale locale = null;

        if (tilesContext != null) {
            locale = localeResolver.resolveLocale(tilesContext);
        }

        retValue = definitionDao.getDefinition(name, locale);
        if (retValue != null) {
            retValue = new Definition(retValue);
            String parentDefinitionName = retValue.getExtends();
            while (parentDefinitionName != null) {
                Definition parent = definitionDao.getDefinition(
                        parentDefinitionName, locale);
                if (parent == null) {
                    throw new NoSuchDefinitionException("Cannot find definition '"
                            + parentDefinitionName + "' ancestor of '"
                            + retValue.getName() + "'");
                }
                retValue.inherit(parent);
                parentDefinitionName = parent.getExtends();
            }
        }

        return retValue;
    }

    /**
     * Creates the default locale resolver, if it has not been specified
     * outside.
     *
     * @return The default locale resolver.
     * @since 2.1.0
     */
    protected LocaleResolver createDefaultLocaleResolver() {
        return new DefaultLocaleResolver();
    }

    /**
     * Creates the default definition DAO, if it has not been specified outside.
     *
     * @return The default definition DAO.
     * @since 2.1.0
     */
    protected DefinitionDAO<Locale> createDefaultDefinitionDAO() {
        return new CachingLocaleUrlDefinitionDAO();
    }
}
