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

package org.apache.tiles.test.alt;

import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.test.factory.TestTilesContainerFactory;

/**
 * Test alternate Tiles container factory to customize Tiles behaviour.
 *
 * @version $Rev$ $Date$
 */
public class TestAlternateTilesContainerFactory extends TestTilesContainerFactory {

    /**
     * The number of URLs to load..
     */
    private static final int URL_COUNT = 3;

    /** {@inheritDoc} */
    @Override
    protected List<ApplicationResource> getSources(ApplicationContext applicationContext) {
        List<ApplicationResource> urls = new ArrayList<ApplicationResource>(URL_COUNT);
        urls.add(applicationContext.getResource("classpath:/org/apache/tiles/test/alt/defs/tiles-alt-defs.xml"));
        urls.add(applicationContext.getResource("classpath:/org/apache/tiles/test/alt/defs/tiles-alt-freemarker-defs.xml"));
        urls.add(applicationContext.getResource("classpath:/org/apache/tiles/test/alt/defs/tiles-alt-velocity-defs.xml"));
        return urls;
    }

    /** {@inheritDoc} */
    @Override
    protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(
            ApplicationContext applicationContext,
            LocaleResolver resolver) {
        return new CachingLocaleUrlDefinitionDAO(applicationContext);
    }
}
