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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.junit.Test;

/**
 * Tests {@link BasicPatternDefinitionResolver}.
 *
 * @version $Rev$ $Date$
 */
public class BasicPatternDefinitionResolverTest {

    /**
     * Test method for
     * {@link BasicPatternDefinitionResolver#resolveDefinition(String, Object)}.
     */
    @Test
    public void testResolveDefinition() {
        DefinitionPatternMatcherFactory factory = createMock(DefinitionPatternMatcherFactory.class);
        PatternRecognizer recognizer = createMock(PatternRecognizer.class);
        DefinitionPatternMatcher firstMatcher = createMock(DefinitionPatternMatcher.class);
        DefinitionPatternMatcher thirdMatcher = createMock(DefinitionPatternMatcher.class);

        expect(recognizer.isPatternRecognized("first")).andReturn(true);
        expect(recognizer.isPatternRecognized("second")).andReturn(false);
        expect(recognizer.isPatternRecognized("third")).andReturn(true)
                .times(2);

        Definition firstDefinition = new Definition("first", (Attribute) null,
                null);
        Definition secondDefinition = new Definition("second",
                (Attribute) null, null);
        Definition thirdDefinition = new Definition("third", (Attribute) null,
                null);

        Definition firstTransformedDefinition = new Definition(
                "firstTransformed", (Attribute) null, null);
        Definition thirdTransformedDefinition = new Definition(
                "thirdTransformed", (Attribute) null, null);

        expect(factory.createDefinitionPatternMatcher("first", firstDefinition))
                .andReturn(firstMatcher);
        expect(factory.createDefinitionPatternMatcher("third", thirdDefinition))
                .andReturn(thirdMatcher).times(2);
        expect(firstMatcher.createDefinition("firstTransformed")).andReturn(
                firstTransformedDefinition);
        expect(firstMatcher.createDefinition("secondTransformed")).andReturn(
                null);
        expect(firstMatcher.createDefinition("thirdTransformed")).andReturn(
                null);
        expect(thirdMatcher.createDefinition("thirdTransformed")).andReturn(
                thirdTransformedDefinition).times(2);
        expect(thirdMatcher.createDefinition("firstTransformed")).andReturn(
                null);
        expect(thirdMatcher.createDefinition("secondTransformed")).andReturn(
                null).times(2);

        replay(factory, recognizer, firstMatcher, thirdMatcher);
        PatternDefinitionResolver<Integer> resolver = new BasicPatternDefinitionResolver<Integer>(
                factory, recognizer);
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
        verify(factory, recognizer, firstMatcher, thirdMatcher);
    }
}
