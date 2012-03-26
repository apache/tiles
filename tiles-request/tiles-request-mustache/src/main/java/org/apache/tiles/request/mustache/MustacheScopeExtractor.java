/*
 * Copyright 2012 Apache Software Foundation.
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

package org.apache.tiles.request.mustache;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

import com.sampullara.mustache.Scope;
import org.apache.tiles.request.attribute.AttributeExtractor;


public final class MustacheScopeExtractor  implements AttributeExtractor {
    private final Scope scope;

    public MustacheScopeExtractor(Scope scope){
        this.scope = scope;
    }

    @Override
    public void removeValue(String key) {
        scope.remove(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return (Enumeration<String>) Collections.enumeration((Set<?>)scope.keySet());
    }

    @Override
    public Object getValue(String key) {
        return scope.get(key);
    }

    @Override
    public void setValue(String key, Object value) {
        scope.put(value, value);
    }
}