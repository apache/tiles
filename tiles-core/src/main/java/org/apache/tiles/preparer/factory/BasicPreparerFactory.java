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
package org.apache.tiles.preparer.factory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.reflect.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the {@link PreparerFactory}.
 * This factory provides no contextual configuration.  It
 * simply instantiates the named preparerInstance and returns it.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public class BasicPreparerFactory implements PreparerFactory {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(BasicPreparerFactory.class);

    /**
     * Maps a preparer name to the instantiated preparer.
     */
    protected Map<String, ViewPreparer> preparers;
    protected Set<String> knownPreparers;

    /**
     * Constructor.
     */
    public BasicPreparerFactory() {
        this.preparers = new ConcurrentHashMap<String, ViewPreparer>();
        this.knownPreparers = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    }


    /**
     * Create a new instance of the named preparerInstance.  This factory
     * expects all names to be qualified class names.
     *
     * @param name    the named preparerInstance
     * @param context current context
     * @return ViewPreparer instance
     */
    public ViewPreparer getPreparer(String name, Request context) {

        if (!knownPreparers.contains(name)) {
            knownPreparers.add(name);
            ViewPreparer preparer = createPreparer(name);
            if (preparer != null) {
               preparers.put(name, preparer);
            }
        }

        return preparers.get(name);
    }

    /**
     * Creates a view preparer for the given name.
     *
     * @param name The name of the preparer.
     * @return The created preparer.
     */
    protected ViewPreparer createPreparer(String name) {

        log.debug("Creating ViewPreparer '{}' . . .", name);

        Object instance = ClassUtil.instantiate(name, true);
        log.debug("ViewPreparer created successfully");
        return (ViewPreparer) instance;

    }
}
