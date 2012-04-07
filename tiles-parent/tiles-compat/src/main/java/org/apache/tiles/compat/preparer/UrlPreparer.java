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

package org.apache.tiles.compat.preparer;

import java.io.IOException;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.DispatchRequest;

/**
 * Uses a URL that acts as a preparer. When
 * {@link org.apache.tiles.preparer.factory.factory.ViewPreparer#execute(Request, AttributeContext)}
 * is called, the URL is got, but its response is discarded.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class UrlPreparer implements ViewPreparer {

    /**
     * The URL to be used as a preparer.
     */
    private String url;

    /**
     * Constructor.
     *
     * @param url The URL to be used as a preparer.
     */
    public UrlPreparer(String url) {
        this.url = url;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(Request tilesContext,
            AttributeContext attributeContext) {

        if (tilesContext instanceof DispatchRequest) {
            try {
                ((DispatchRequest) tilesContext).include(url);
            } catch (IOException e) {
                throw new PreparerException("The inclusion of the URL " + url
                        + " threw an I/O exception", e);
            }
        } else {
            throw new PreparerException("This preparer is restricted to web environments");
        }
    }
}
