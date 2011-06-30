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
 * Tests {@link PrefixedPatternDefinitionResolver}.
 *
 * @version $Rev$ $Date$
 */
public class PrefixedPatternDefinitionResolverTest {

    /**
     * Test method for {@link PrefixedPatternDefinitionResolver#addDefinitionsAsPatternMatchers(List, Map)}.
     */
    @Test
    public void testAddDefinitionsAsPatternMatchers() {
        DefinitionPatternMatcherFactory factory1 = createMock(DefinitionPatternMatcherFactory.class);
        DefinitionPatternMatcherFactory factory2 = createMock(DefinitionPatternMatcherFactory.class);
        DefinitionPatternMatcher matcher1 = createMock(DefinitionPatternMatcher.class);
        DefinitionPatternMatcher matcher2 = createMock(DefinitionPatternMatcher.class);
        Definition definition1 = new Definition("DF1:definition1", (Attribute) null, null);
        Definition definition2 = new Definition("DF2:definition2", (Attribute) null, null);
        Definition definition3 = new Definition("noLanguageHere", (Attribute) null, null);

        expect(factory1.createDefinitionPatternMatcher("definition1", definition1)).andReturn(matcher1);
        expect(factory2.createDefinitionPatternMatcher("definition2", definition2)).andReturn(matcher2);

        replay(factory1, factory2, matcher1, matcher2);

        PrefixedPatternDefinitionResolver<Integer> resolver = new PrefixedPatternDefinitionResolver<Integer>();
        resolver.registerDefinitionPatternMatcherFactory("DF1", factory1);
        resolver.registerDefinitionPatternMatcherFactory("DF2", factory2);
        List<DefinitionPatternMatcher> matchers = new ArrayList<DefinitionPatternMatcher>();
        Map<String, Definition> definitions = new LinkedHashMap<String, Definition>();
        definitions.put("DF1:definition1", definition1);
        definitions.put("DF2:definition2", definition2);
        definitions.put("noLanguageHere", definition3);

        resolver.addDefinitionsAsPatternMatchers(matchers, definitions);

        assertEquals(2, matchers.size());
        assertEquals(matcher1, matchers.get(0));
        assertEquals(matcher2, matchers.get(1));

        verify(factory1, factory2, matcher1, matcher2);
    }
}
