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

package org.apache.tiles.definition;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.dao.DefinitionDAO;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.request.Request;
import org.junit.Test;

/**
 * Tests {@link LocaleDefinitionsFactory}.
 *
 * @version $Rev$ $Date$
 */
public class LocaleDefinitionsFactoryTest {

    /**
     * Test method for {@link LocaleDefinitionsFactory#getDefinition(String, Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetDefinition() {
        DefinitionDAO<Locale> dao = createMock(DefinitionDAO.class);
        LocaleResolver localeResolver = createMock(LocaleResolver.class);
        Request request = createMock(Request.class);
        Attribute templateAttribute = Attribute.createTemplateAttribute("/mytemplate.jsp");
        Definition definition = new Definition("myDefinition", null, null);
        definition.setExtends("anotherDefinition");
        Map<String, Attribute> attributes = new HashMap<String, Attribute>();
        attributes.put("first", new Attribute("myValue"));
        Definition anotherDefinition = new Definition("anotherDefinition", templateAttribute, attributes);
        Locale locale = Locale.ITALY;

        expect(localeResolver.resolveLocale(request)).andReturn(locale);
        expect(dao.getDefinition("myDefinition", locale)).andReturn(definition);
        expect(dao.getDefinition("anotherDefinition", locale)).andReturn(anotherDefinition);

        LocaleDefinitionsFactory factory = new LocaleDefinitionsFactory();

        replay(dao, localeResolver, request);
        factory.setDefinitionDAO(dao);
        factory.setLocaleResolver(localeResolver);
        Definition realDefinition = new Definition(definition);
        realDefinition.inherit(anotherDefinition);
        assertEquals(realDefinition, factory.getDefinition("myDefinition", request));
        verify(dao, localeResolver, request);
    }

    /**
     * Test method for {@link LocaleDefinitionsFactory#getDefinition(String, Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = NoSuchDefinitionException.class)
    public void testGetDefinitionNoParent() {
        DefinitionDAO<Locale> dao = createMock(DefinitionDAO.class);
        LocaleResolver localeResolver = createMock(LocaleResolver.class);
        Request request = createMock(Request.class);
        Definition definition = new Definition("myDefinition", null, null);
        definition.setExtends("anotherDefinition");
        Map<String, Attribute> attributes = new HashMap<String, Attribute>();
        attributes.put("first", new Attribute("myValue"));
        Locale locale = Locale.ITALY;

        expect(localeResolver.resolveLocale(request)).andReturn(locale);
        expect(dao.getDefinition("myDefinition", locale)).andReturn(definition);
        expect(dao.getDefinition("anotherDefinition", locale)).andReturn(null);

        LocaleDefinitionsFactory factory = new LocaleDefinitionsFactory();

        replay(dao, localeResolver, request);
        try {
            factory.setDefinitionDAO(dao);
            factory.setLocaleResolver(localeResolver);
            factory.getDefinition("myDefinition", request);
        } finally {
            verify(dao, localeResolver, request);
        }
    }
}
