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

/**
 * Indicates support for reloading Tiles configuration when it changes.
 *
 * @version $Rev$ $Date$
 */
public interface ReloadableDefinitionsFactory {

    /**
     * Indicates whether the DefinitionsFactory is out of date and needs to be
     * reloaded.
     *
     * @return <code>true</code> if the definitions need to be refreshed.
     */
    boolean refreshRequired();

    /**
     * Refreshes the stored definitions, reloading them.
     *
     * @throws DefinitionsFactoryException If something goes wrong during
     * refresh.
     */
    void refresh() throws DefinitionsFactoryException;
}
