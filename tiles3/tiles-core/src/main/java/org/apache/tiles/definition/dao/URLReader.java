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

import java.net.URL;
import java.util.List;

import org.apache.tiles.definition.DefinitionsReader;

/**
 * It represents an object that reads URLs and is able to read them throw the
 * use of a {@link DefinitionsReader}.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public interface URLReader {

    /**
     * Sets the source URLs to use.
     *
     * @param sourceURLs The source URLs.
     * @since 2.1.0
     */
    void setSourceURLs(List<URL> sourceURLs);

    /**
     * Sets the definitions reader that will read the URLs.
     *
     * @param reader The definitions reader.
     * @since 2.1.0
     */
    void setReader(DefinitionsReader reader);

    /**
     * Adds a single URL to use.
     *
     * @param sourceURL The source URL to add.
     * @since 2.1.0
     */
    void addSourceURL(URL sourceURL);
}
