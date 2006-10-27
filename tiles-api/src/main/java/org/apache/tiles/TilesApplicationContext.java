/*
 * $Id$
 *
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tiles;

import java.util.Map;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Defines a set of methods which tiles use to communicate to
 * the tiles container and runtime environment.  There is only
 * one application context per container.
 * 
 * @since 2.0
 * @version $Rev$
 *
 */
public interface TilesApplicationContext {

    /**
     * Returns a mutable Map that maps application scope attribute names to
     * their values.
     */
    public Map getApplicationScope();

    /**
     * Return an immutable Map that maps context application initialization
     * parameters to their values.
     */
    public Map getInitParams();

    /**
     * Return a URL for the application resource mapped to the specified path.
     */
    public URL getResource(String path) throws MalformedURLException;

    /**
     * Return a URL for the application resource mapped to the specified path.
     */
    public URL[] getResources(String path) throws MalformedURLException;
}
