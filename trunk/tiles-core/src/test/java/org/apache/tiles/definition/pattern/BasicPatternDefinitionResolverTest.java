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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
     * {@link BasicPatternDefinitionResolver#addDefinitionsAsPatternMatchers(java.util.List, Map)}.
     */
    @Test
    public void testAddDefinitionsAsPatternMatchers() {
        DefinitionPatternMatcherFactory factory = createMock(DefinitionPatternMatcherFactory.class);
        PatternRecognizer recognizer = createMock(PatternRecognizer.class);
        DefinitionPatternMatcher firstMatcher = createMock(DefinitionPatternMatcher.class);
        DefinitionPatternMatcher thirdMatcher = createMock(DefinitionPatternMatcher.class);

        expect(recognizer.isPatternRecognized("first")).andReturn(true);
        expect(recognizer.isPatternRecognized("second")).andReturn(false);
        expect(recognizer.isPatternRecognized("third")).andReturn(true);

        Definition firstDefinition = new Definition("first", (Attribute) null,
                null);
        Definition secondDefinition = new Definition("second",
                (Attribute) null, null);
        Definition thirdDefinition = new Definition("third", (Attribute) null,
                null);

        expect(factory.createDefinitionPatternMatcher("first", firstDefinition))
                .andReturn(firstMatcher);
        expect(factory.createDefinitionPatternMatcher("third", thirdDefinition))
                .andReturn(thirdMatcher);

        replay(factory, recognizer, firstMatcher, thirdMatcher);
        BasicPatternDefinitionResolver<Integer> resolver = new BasicPatternDefinitionResolver<Integer>(
                factory, recognizer);
        Map<String, Definition> localeDefsMap = new LinkedHashMap<String, Definition>();
        localeDefsMap.put("first", firstDefinition);
        localeDefsMap.put("second", secondDefinition);
        localeDefsMap.put("third", thirdDefinition);
        List<DefinitionPatternMatcher> matchers = new ArrayList<DefinitionPatternMatcher>();
        resolver.addDefinitionsAsPatternMatchers(matchers, localeDefsMap);
        assertEquals(2, matchers.size());
        assertEquals(firstMatcher, matchers.get(0));
        assertEquals(thirdMatcher, matchers.get(1));
        verify(factory, recognizer, firstMatcher, thirdMatcher);
    }
}
