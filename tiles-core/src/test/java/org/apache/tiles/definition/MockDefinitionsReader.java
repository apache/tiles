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

import java.util.Collections;
import java.util.Map;

import org.apache.tiles.Definition;

/**
 * Mock Defintions Reader implementation.  Stubs out all functionality.
 *
 * @version $Rev$ $Date$
 */
public class MockDefinitionsReader implements DefinitionsReader {

    /**
     * Hokey way to verify that this was created.
     */
    private static int instanceCount = 0;

    /**
     * Hokey way to verify that this class was created.
     *
     * @return The number of created instances.
     */
    public static int getInstanceCount() {
        return instanceCount;
    }

    /** Creates a new instance of MockDefinitionsReader. */
    public MockDefinitionsReader() {
        instanceCount++;
    }

    /**
     * Reads <code>{@link Definition}</code> objects from a source.
     *
     * Implementations should publish what type of source object is expected.
     *
     * @param source The source from which definitions will be read.
     * @return a Map of <code>Definition</code> objects read from
     *  the source.
     * @throws org.apache.tiles.definition.DefinitionsFactoryException if the source is invalid or
     *  an error occurs when reading definitions.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Definition> read(Object source)
            throws DefinitionsFactoryException {
        return Collections.EMPTY_MAP;
    }

    /**
     * Initializes the <code>DefinitionsReader</code> object.
     *
     * This method must be called before the {@link #read} method is called.
     *
     * @param params A map of properties used to set up the reader.
     * @throws org.apache.tiles.definition.DefinitionsFactoryException if required properties are not
     *  passed in or the initialization fails.
     */
    public void init(Map<String, String> params) throws DefinitionsFactoryException {
    }

}
