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

import org.apache.tiles.Definition;
import org.apache.tiles.request.Request;

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
public class LocaleDefinitionsFactory extends
        UnresolvingLocaleDefinitionsFactory {

    /** {@inheritDoc} */
    @Override
    public Definition getDefinition(String name,
            Request tilesContext) {
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
}
