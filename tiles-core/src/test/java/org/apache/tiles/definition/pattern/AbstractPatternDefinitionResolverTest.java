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

package org.apache.tiles.definition.pattern;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.junit.Test;

/**
 * Tests {@link AbstractPatternDefinitionResolver}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractPatternDefinitionResolverTest {

    private DefinitionPatternMatcher firstMatcher;
    private DefinitionPatternMatcher thirdMatcher;

    private final PatternDefinitionResolver<Integer> resolver = new AbstractPatternDefinitionResolver<Integer>() {
        @Override
        protected Map<String, Definition> addDefinitionsAsPatternMatchers(
                List<DefinitionPatternMatcher> matchers,
                Map<String, Definition> defsMap) {

            if (defsMap.containsKey("first")) {
                matchers.add(firstMatcher);
            }
            if (defsMap.containsKey("third")) {
                matchers.add(thirdMatcher);
            }
            Map<String, Definition> retValue = new HashMap<String, Definition>(defsMap);
            retValue.remove("first");
            retValue.remove("third");
            return retValue;
        }
    };

    /**
     * Test method for
     * {@link BasicPatternDefinitionResolver#resolveDefinition(String, Object)}.
     */
    @Test
    public void testResolveDefinition() {
        testResolveDefinitionImpl();
    }

    /**
     * Test method for
     * {@link BasicPatternDefinitionResolver#clearPatternPaths(Object)}.
     */
    @Test
    public void testClearPatternPaths() {
        testResolveDefinitionImpl();
        resolver.clearPatternPaths(1);
        resolver.clearPatternPaths(2);
        testResolveDefinitionImpl();
    }

    private void testResolveDefinitionImpl() {

        firstMatcher = createMock(DefinitionPatternMatcher.class);
        thirdMatcher = createMock(DefinitionPatternMatcher.class);

        Definition firstDefinition = new Definition("first", (Attribute) null, null);
        Definition secondDefinition = new Definition("second", (Attribute) null, null);
        Definition thirdDefinition = new Definition("third", (Attribute) null, null);

        Definition firstTransformedDefinition = new Definition("firstTransformed", (Attribute) null, null);
        Definition thirdTransformedDefinition = new Definition("thirdTransformed", (Attribute) null, null);

        expect(firstMatcher.createDefinition("firstTransformed")).andReturn(firstTransformedDefinition);
        expect(firstMatcher.createDefinition("secondTransformed")).andReturn(null);
        expect(firstMatcher.createDefinition("thirdTransformed")).andReturn(null);
        expect(thirdMatcher.createDefinition("thirdTransformed")).andReturn(thirdTransformedDefinition).times(2);
        expect(thirdMatcher.createDefinition("firstTransformed")).andReturn(null);
        expect(thirdMatcher.createDefinition("secondTransformed")).andReturn(null).times(2);

        replay(firstMatcher, thirdMatcher);

        Map<String, Definition> localeDefsMap = new LinkedHashMap<String, Definition>();
        localeDefsMap.put("first", firstDefinition);
        localeDefsMap.put("second", secondDefinition);
        localeDefsMap.put("third", thirdDefinition);
        resolver.storeDefinitionPatterns(localeDefsMap, 1);
        localeDefsMap = new LinkedHashMap<String, Definition>();
        localeDefsMap.put("third", thirdDefinition);
        resolver.storeDefinitionPatterns(localeDefsMap, 2);
        assertEquals(firstTransformedDefinition, resolver.resolveDefinition(
                "firstTransformed", 1));
        assertNull(resolver.resolveDefinition("secondTransformed", 1));
        assertEquals(thirdTransformedDefinition, resolver.resolveDefinition(
                "thirdTransformed", 1));
        assertNull(resolver.resolveDefinition("firstTransformed", 2));
        assertNull(resolver.resolveDefinition("secondTransformed", 2));
        assertEquals(thirdTransformedDefinition, resolver.resolveDefinition(
                "thirdTransformed", 2));
        verify(firstMatcher, thirdMatcher);
    }
}
