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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.reflect.ClassUtil;
import org.apache.tiles.request.servlet.wildcard.WildcardServletApplicationContext;
import org.apache.tiles.startup.TilesInitializer;

/**
 * Loads Tiles modules, initializes them and destroy them at the end.<br>
 * It loads all META-INF/MANIFEST.MF files, checks for the "Tiles-Initializer"
 * property that must contain a valid class name of a {@link TilesInitializer}.
 * After that, initializes all found initializers, one by one. When the
 * {@link #destroy()} method is called, all the initializers are then destroyed.
 *
 * @version $Rev$ $Date$
 * @since 2.2.1
 */
public class ModularTilesInitializer implements TilesInitializer {

    /**
     * The initializers to use.
     */
    private List<TilesInitializer> initializers;

    /** {@inheritDoc} */
    public void initialize(ApplicationContext preliminaryContext) {
        ApplicationContext applicationContext = new WildcardServletApplicationContext(
                (ServletContext) preliminaryContext.getContext());
        loadInitializers(applicationContext);

        for (TilesInitializer initializer : initializers) {
            initializer.initialize(preliminaryContext);
        }
    }

    /** {@inheritDoc} */
    public void destroy() {
        for (TilesInitializer initializer : initializers) {
            initializer.destroy();
        }
    }

    /**
     * Load all the initializers from manifest files.
     *
     * @param applicationContext The application context.
     */
    private void loadInitializers(ApplicationContext applicationContext) {
        initializers = new ArrayList<TilesInitializer>();
        try {
            Collection<ApplicationResource> resources = applicationContext
                    .getResources("classpath*:META-INF/MANIFEST.MF");
            ApplicationResource mainResource = applicationContext.getResource("/META-INF/MANIFEST.MF");
            if (mainResource != null) {
                resources.add(mainResource);
            }
            for (ApplicationResource resource : resources) {
                InputStream stream = resource.getInputStream();
                try {
                    Manifest manifest = new Manifest(stream);
                    Attributes attributes = manifest.getMainAttributes();
                    if (attributes != null) {
                        String initializerName = attributes.getValue("Tiles-Initializer");
                        if (initializerName != null) {
                            TilesInitializer initializer = (TilesInitializer) ClassUtil
                                    .instantiate(initializerName);
                            initializers.add(initializer);
                        }
                    }
                } finally {
                    stream.close();
                }
            }
        } catch (IOException e) {
            throw new DefinitionsFactoryException("Error getting manifest files", e);
        }
    }
}
