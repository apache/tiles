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
package org.apache.tiles.autotag.generate;

import static org.easymock.EasyMock.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.tiles.autotag.core.AutotagRuntimeException;
import org.apache.tiles.autotag.core.ClassParseException;
import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AbstractTemplateClassGenerator}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractTemplateClassGeneratorTest {

    /**
     * The velocity engine.
     */
    private VelocityEngine velocityEngine;

    /**
     * The temporary directory.
     */
    private File directory;

    /**
     * The generator to test.
     */
    private AbstractTemplateClassGenerator generator;

    /**
     * Sets up the test.
     *
     * @throws IOException If something goes wrong.
     */
    @Before
    public void setUp() throws IOException {
        velocityEngine = createMock(VelocityEngine.class);
        generator = createMockBuilder(AbstractTemplateClassGenerator.class)
                .withConstructor(velocityEngine).createMock();
        directory = File.createTempFile("autotag", null);
    }

    /**
     * Tears down the test.
     *
     * @throws IOException If something goes wrong.
     */
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(directory);
    }

    /**
     * Test method for {@link AbstractTemplateClassGenerator#generate(java.io.File,
     * String, TemplateSuite, TemplateClass, Map)}.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testGenerate() throws Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass clazz = createMock(TemplateClass.class);
        Template template = createMock(Template.class);
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = createMock(Map.class);
        String packageName = "org.apache.tiles.autotag.test";
        String runtimeClass = "org.apache.tiles.autotag.test.DoStuffRuntime";

        expect(generator.getDirectoryName(directory, packageName, suite, clazz, parameters, runtimeClass)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite, clazz, parameters, runtimeClass)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite, clazz, parameters, runtimeClass)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andReturn(template);
        template.merge(isA(VelocityContext.class), isA(FileWriter.class));

        replay(velocityEngine, generator, suite, clazz, template, parameters);
        generator.generate(directory, packageName, suite, clazz, parameters, runtimeClass);
        verify(velocityEngine, generator, suite, clazz, template, parameters);
    }

    /**
     * Test method for {@link AbstractTemplateClassGenerator#generate(File, String, TemplateSuite, TemplateClass, Map)}.
     * @throws Exception If something goes wrong.
     */
    @Test(expected = AutotagRuntimeException.class)
    public void testGenerateException1() throws Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass clazz = createMock(TemplateClass.class);
        Template template = createMock(Template.class);
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = createMock(Map.class);
        String packageName = "org.apache.tiles.autotag.test";
        String runtimeClass = "org.apache.tiles.autotag.test.DoStuffRuntime";

        expect(generator.getDirectoryName(directory, packageName, suite, clazz, parameters, runtimeClass)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite, clazz, parameters, runtimeClass)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite, clazz, parameters, runtimeClass)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andThrow(new ResourceNotFoundException("hello"));

        replay(velocityEngine, generator, suite, clazz, template, parameters);
        generator.generate(directory, packageName, suite, clazz, parameters, runtimeClass);
        verify(velocityEngine, generator, suite, clazz, template, parameters);
    }

    /**
     * Test method for {@link AbstractTemplateClassGenerator#generate(File, String, TemplateSuite, TemplateClass, Map)}.
     * @throws Exception If something goes wrong.
     */
    @Test(expected = AutotagRuntimeException.class)
    public void testGenerateException2() throws Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass clazz = createMock(TemplateClass.class);
        Template template = createMock(Template.class);
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = createMock(Map.class);
        String packageName = "org.apache.tiles.autotag.test";
        String runtimeClass = "org.apache.tiles.autotag.test.DoStuffRuntime";

        expect(generator.getDirectoryName(directory, packageName, suite, clazz, parameters, runtimeClass)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite, clazz, parameters, runtimeClass)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite, clazz, parameters, runtimeClass)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andThrow(new ParseErrorException("hello"));

        replay(velocityEngine, generator, suite, clazz, template, parameters);
        generator.generate(directory, packageName, suite, clazz, parameters, runtimeClass);
        verify(velocityEngine, generator, suite, clazz, template, parameters);
    }

    /**
     * Test method for {@link AbstractTemplateClassGenerator#generate(File, String, TemplateSuite, TemplateClass, Map)}.
     * @throws Exception If something goes wrong.
     */
    @Test(expected = AutotagRuntimeException.class)
    public void testGenerateException3() throws Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass clazz = createMock(TemplateClass.class);
        Template template = createMock(Template.class);
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = createMock(Map.class);
        String packageName = "org.apache.tiles.autotag.test";
        String runtimeClass = "org.apache.tiles.autotag.test.DoStuffRuntime";

        expect(generator.getDirectoryName(directory, packageName, suite, clazz, parameters, runtimeClass)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite, clazz, parameters, runtimeClass)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite, clazz, parameters, runtimeClass)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andThrow(new Exception());

        replay(velocityEngine, generator, suite, clazz, template, parameters);
        generator.generate(directory, packageName, suite, clazz, parameters, runtimeClass);
        verify(velocityEngine, generator, suite, clazz, template, parameters);
    }

    /**
     * Test method for {@link AbstractTemplateClassGenerator#generate(File, String, TemplateSuite, TemplateClass, Map)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test(expected = AutotagRuntimeException.class)
    public void testGenerateException4() throws Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass clazz = createMock(TemplateClass.class);
        Template template = createMock(Template.class);
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = createMock(Map.class);
        String packageName = "org.apache.tiles.autotag.test";
        String runtimeClass = "org.apache.tiles.autotag.test.DoStuffRuntime";

        expect(generator.getDirectoryName(directory, packageName, suite, clazz, parameters, runtimeClass)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite, clazz, parameters, runtimeClass)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite, clazz, parameters, runtimeClass)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andReturn(template);
        template.merge(isA(VelocityContext.class), isA(FileWriter.class));
        expectLastCall().andThrow(new IOException());

        replay(velocityEngine, generator, suite, clazz, template, parameters);
        generator.generate(directory, packageName, suite, clazz, parameters, runtimeClass);
        verify(velocityEngine, generator, suite, clazz, template, parameters);
    }

    /**
     * Test method for {@link AbstractTemplateClassGenerator#generate(File, String, TemplateSuite, TemplateClass, Map)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test(expected = ClassParseException.class)
    public void testGenerateException5() throws Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass clazz = createMock(TemplateClass.class);
        Template template = createMock(Template.class);
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = createMock(Map.class);
        String packageName = "org.apache.tiles.autotag.test";
        String runtimeClass = "org.apache.tiles.autotag.test.DoStuffRuntime";

        expect(generator.getDirectoryName(directory, packageName, suite, clazz, parameters, runtimeClass)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite, clazz, parameters, runtimeClass)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite, clazz, parameters, runtimeClass)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andReturn(template);
        template.merge(isA(VelocityContext.class), isA(FileWriter.class));
        expectLastCall().andThrow(new ClassParseException());

        replay(velocityEngine, generator, suite, clazz, template, parameters);
        generator.generate(directory, packageName, suite, clazz, parameters, runtimeClass);
        verify(velocityEngine, generator, suite, clazz, template, parameters);
    }

}
