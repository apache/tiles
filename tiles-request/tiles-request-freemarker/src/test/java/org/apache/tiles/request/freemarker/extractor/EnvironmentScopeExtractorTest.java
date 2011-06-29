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
package org.apache.tiles.request.freemarker.extractor;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.tiles.request.collection.IteratorEnumeration;
import org.apache.tiles.request.freemarker.FreemarkerRequestException;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * Tests {@link EnvironmentScopeExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class EnvironmentScopeExtractorTest {

    /**
     * Test method for {@link EnvironmentScopeExtractor#removeValue(java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testRemoveValue() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        TemplateModel valueModel = createMock(TemplateModel.class);
        Configuration configuration = createMock(Configuration.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(model.get("key")).andReturn(null);
        expect(template.getConfiguration()).andReturn(configuration);
        expect(configuration.getSharedVariable("key")).andReturn(null);

        replay(template, model, valueModel, configuration);
        Environment env = new Environment(template, model, writer);
        env.setVariable("key", valueModel);
        EnvironmentScopeExtractor extractor = new EnvironmentScopeExtractor(env);
        extractor.removeValue("key");
        assertNull(env.getVariable("key"));
        verify(template, model, valueModel, configuration);
    }

    /**
     * Test method for {@link EnvironmentScopeExtractor#getKeys()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetKeys() {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        TemplateModel valueModel = createMock(TemplateModel.class);
        Configuration configuration = createMock(Configuration.class);
        Set<String> names = createMock(Set.class);
        Iterator<String> namesIt = createMock(Iterator.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(template.getConfiguration()).andReturn(configuration);
        expect(configuration.getSharedVariableNames()).andReturn(names);
        expect(names.iterator()).andReturn(namesIt);

        replay(template, model, valueModel, configuration, names, namesIt);
        Environment env = new Environment(template, model, writer);
        EnvironmentScopeExtractor extractor = new EnvironmentScopeExtractor(env);
        assertEquals(namesIt, ((IteratorEnumeration<String>) extractor.getKeys()).getIterator());
        verify(template, model, valueModel, configuration, names, namesIt);
    }

    /**
     * Test method for {@link EnvironmentScopeExtractor#getKeys()}.
     * @throws TemplateModelException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = FreemarkerRequestException.class)
    public void testGetKeysException() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModelEx model = createMock(TemplateHashModelEx.class);
        TemplateModel valueModel = createMock(TemplateModel.class);
        Configuration configuration = createMock(Configuration.class);
        Set<String> names = createMock(Set.class);
        Iterator<String> namesIt = createMock(Iterator.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(model.keys()).andThrow(new TemplateModelException());
        expect(template.getConfiguration()).andReturn(configuration);
        expect(configuration.getSharedVariableNames()).andReturn(names);

        replay(template, model, valueModel, configuration, names, namesIt);
        try {
            Environment env = new Environment(template, model, writer);
            EnvironmentScopeExtractor extractor = new EnvironmentScopeExtractor(env);
            extractor.getKeys();
        } finally {
            verify(template, model, valueModel, configuration, names, namesIt);
        }
    }

    /**
     * Test method for {@link EnvironmentScopeExtractor#getValue(java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetValue() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        TemplateScalarModel valueModel = createMock(TemplateScalarModel.class);
        Configuration configuration = createMock(Configuration.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(valueModel.getAsString()).andReturn("value");

        replay(template, model, valueModel, configuration, objectWrapper);
        Environment env = new Environment(template, model, writer);
        env.setVariable("key", valueModel);
        EnvironmentScopeExtractor extractor = new EnvironmentScopeExtractor(env);
        assertEquals("value", extractor.getValue("key"));
        verify(template, model, valueModel, configuration, objectWrapper);
    }

    /**
     * Test method for {@link EnvironmentScopeExtractor#getValue(java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetValueNull() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        TemplateScalarModel valueModel = createMock(TemplateScalarModel.class);
        Configuration configuration = createMock(Configuration.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(model.get("key")).andReturn(null);
        expect(template.getConfiguration()).andReturn(configuration);
        expect(configuration.getSharedVariable("key")).andReturn(null);

        replay(template, model, valueModel, configuration, objectWrapper);
        Environment env = new Environment(template, model, writer);
        EnvironmentScopeExtractor extractor = new EnvironmentScopeExtractor(env);
        assertNull(extractor.getValue("key"));
        verify(template, model, valueModel, configuration, objectWrapper);
    }

    /**
     * Test method for {@link EnvironmentScopeExtractor#getValue(java.lang.String)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test(expected = FreemarkerRequestException.class)
    public void testGetValueException() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        TemplateScalarModel valueModel = createMock(TemplateScalarModel.class);
        Configuration configuration = createMock(Configuration.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(model.get("key")).andThrow(new TemplateModelException());

        replay(template, model, valueModel, configuration, objectWrapper);
        try {
            Environment env = new Environment(template, model, writer);
            EnvironmentScopeExtractor extractor = new EnvironmentScopeExtractor(env);
            extractor.getValue("key");
        } finally {
            verify(template, model, valueModel, configuration, objectWrapper);
        }
    }

    /**
     * Test method for {@link EnvironmentScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testSetValue() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        TemplateModel valueModel = createMock(TemplateModel.class);
        Configuration configuration = createMock(Configuration.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(template.getObjectWrapper()).andReturn(objectWrapper);
        expect(objectWrapper.wrap("value")).andReturn(valueModel);

        replay(template, model, valueModel, configuration, objectWrapper);
        Environment env = new Environment(template, model, writer);
        EnvironmentScopeExtractor extractor = new EnvironmentScopeExtractor(env);
        extractor.setValue("key", "value");
        assertEquals(valueModel, env.getVariable("key"));
        verify(template, model, valueModel, configuration, objectWrapper);
    }

    /**
     * Test method for {@link EnvironmentScopeExtractor#setValue(java.lang.String, java.lang.Object)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test(expected = FreemarkerRequestException.class)
    public void testSetValueException() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        TemplateModel valueModel = createMock(TemplateModel.class);
        Configuration configuration = createMock(Configuration.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(template.getObjectWrapper()).andReturn(objectWrapper);
        expect(objectWrapper.wrap("value")).andThrow(new TemplateModelException());

        replay(template, model, valueModel, configuration, objectWrapper);
        try {
            Environment env = new Environment(template, model, writer);
            EnvironmentScopeExtractor extractor = new EnvironmentScopeExtractor(env);
            extractor.setValue("key", "value");
            assertEquals(valueModel, env.getVariable("key"));
        } finally {
            verify(template, model, valueModel, configuration, objectWrapper);
        }
    }
}
