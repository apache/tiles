/*
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
 *
 */
package org.apache.tiles.context.enhanced;

import org.apache.tiles.TilesApplicationContext;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.*;
import java.net.URL;
import java.io.IOException;

/**
 * ApplicationContext decorator used to provide
 * enhancements to the standard context.
 *
 * Specifically, it provides:
 * <ul>
 *   <li>Ability to load resources from the classpath</li>
 *   <li>Ability to retrieve multiple resources of the same name</li>
 * </ul>
 *
 * Future features will include:
 * <ul>
 *   <li>Ability to utilize wildcards in resource pathcs</li>
 * </ul>
 *
 * @since Tiles 2.0
 * @version $Rev$
 * 
 */
public class EnhancedTilesApplicationContext implements TilesApplicationContext {

    private static final Log LOG =
        LogFactory.getLog(EnhancedTilesApplicationContext.class);

    private TilesApplicationContext rootContext;

    public EnhancedTilesApplicationContext(TilesApplicationContext rootContext) {
        this.rootContext = rootContext;
    }


    public TilesApplicationContext getRootContext() {
        return rootContext;
    }

    public void setRootContext(TilesApplicationContext rootContext) {
        this.rootContext = rootContext;
    }

    public Map<String, Object> getApplicationScope() {
        return rootContext.getApplicationScope();
    }

    public Map<String, String> getInitParams() {
        return rootContext.getInitParams();
    }

    public URL getResource(String path) throws IOException {
        URL rootUrl = rootContext.getResource(path);
        if(rootUrl == null) {
            Set<URL> resources = getResources(path);
            resources.remove(null);
            if(resources.size() > 0) {
                rootUrl = resources.toArray(new URL[resources.size()])[0];
            }
        }
        return rootUrl;
    }

    public Set<URL> getResources(String path) throws IOException {
        Set<URL> resources = new HashSet<URL>();
        resources.addAll(rootContext.getResources(path));
        resources.addAll(getClasspathResources(path));
        return resources;
    }

    public Set<URL> getClasspathResources(String path) throws IOException {
        Set<URL> resources = new HashSet<URL>();
        resources.addAll(searchResources(getClass().getClassLoader(), path));

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        if(contextLoader != null) {
            resources.addAll(searchResources(contextLoader, path));
        }

        if(resources.size() == 0 && path.startsWith("/")) {
            return getClasspathResources(path.substring(1));
        }

        return resources;
    }

    protected Set<URL> searchResources(ClassLoader loader, String path) {
        Set<URL> resources = new HashSet<URL>();
        try {
            Enumeration<URL> e = loader.getResources(path);
            while(e.hasMoreElements()) {
                resources.add(e.nextElement());
            }
        } catch (IOException e) {
            LOG.warn("Unable to retrieved resources from classloader: "+loader, e);
        }
        return resources;
    }
}
