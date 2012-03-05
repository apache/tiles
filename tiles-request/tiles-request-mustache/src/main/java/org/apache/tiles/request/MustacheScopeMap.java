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
package org.apache.tiles.request;

import java.util.Set;

import com.sampullara.mustache.Scope;
import org.apache.tiles.request.collection.ScopeMap;
import org.apache.tiles.request.extractor.MustacheScopeExtractor;


final class MustacheScopeMap extends ScopeMap {

    /**
     * The request object to use.
     */
    private Scope scope = null;

    /**
     * Constructor.
     *
     * @param request The request object to use.
     */
    public MustacheScopeMap(Scope request) {
        super(new MustacheScopeExtractor(request));
        this.scope = request;
    }

    @Override
    public Object remove(Object key) {
        return scope.remove(key);
    }

    @Override
    public Object put(String key, Object value) {
        return scope.put(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
        return scope.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return size() < 1;
    }

    @Override
    public Set<String> keySet() {
        return (Set<String>)(Set<?>)scope.keySet();
    }

    @Override
    public int size() {
        return scope.keySet().size();
    }
}
