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
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * @author antonio
 *
 * @version $Rev$ $Date$
 */
public class TemplateGeneratorBuilderTest {

    /**
     * Test method for {@link TemplateGeneratorBuilder#addClassesTemplateSuiteGenerator(TemplateSuiteGenerator)}.
     */
    @Test
    public void testAddClassesTemplateSuiteGenerator() {
        File dir = createMock(File.class);
        TemplateSuiteGenerator generator = createMock(TemplateSuiteGenerator.class);

        replay(dir, generator);
        TemplateGenerator templateGenerator = TemplateGeneratorBuilder
                .createNewInstance().setClassesOutputDirectory(dir)
                .addClassesTemplateSuiteGenerator(generator).build();
        assertTrue(templateGenerator.isGeneratingClasses());
        assertFalse(templateGenerator.isGeneratingResources());
        verify(dir, generator);
    }

    /**
     * Test method for {@link TemplateGeneratorBuilder#addClassesTemplateSuiteGenerator(TemplateSuiteGenerator)}.
     */
    @Test(expected = NullPointerException.class)
    public void testAddClassesTemplateSuiteGeneratorException() {
        TemplateSuiteGenerator generator = createMock(TemplateSuiteGenerator.class);

        replay(generator);
        try {
            TemplateGeneratorBuilder.createNewInstance()
                    .addClassesTemplateSuiteGenerator(generator);
        } finally {
            verify(generator);
        }
    }

    /**
     * Test method for {@link TemplateGeneratorBuilder#addClassesTemplateClassGenerator(TemplateClassGenerator)}.
     */
    @Test
    public void testAddClassesTemplateClassGenerator() {
        File dir = createMock(File.class);
        TemplateClassGenerator generator = createMock(TemplateClassGenerator.class);

        replay(dir, generator);
        TemplateGenerator templateGenerator = TemplateGeneratorBuilder
                .createNewInstance().setClassesOutputDirectory(dir)
                .addClassesTemplateClassGenerator(generator).build();
        assertTrue(templateGenerator.isGeneratingClasses());
        assertFalse(templateGenerator.isGeneratingResources());
        verify(dir, generator);
    }

    /**
     * Test method for {@link TemplateGeneratorBuilder#addClassesTemplateClassGenerator(TemplateClassGenerator)}.
     */
    @Test(expected = NullPointerException.class)
    public void testAddClassesTemplateClassGeneratorException() {
        TemplateClassGenerator generator = createMock(TemplateClassGenerator.class);

        replay(generator);
        try {
            TemplateGeneratorBuilder.createNewInstance()
                    .addClassesTemplateClassGenerator(generator);
        } finally {
            verify(generator);
        }
    }

    /**
     * Test method for {@link TemplateGeneratorBuilder#addResourcesTemplateSuiteGenerator(TemplateSuiteGenerator)}.
     */
    @Test
    public void testAddResourcesTemplateSuiteGenerator() {
        File dir = createMock(File.class);
        TemplateSuiteGenerator generator = createMock(TemplateSuiteGenerator.class);

        replay(dir, generator);
        TemplateGenerator templateGenerator = TemplateGeneratorBuilder
                .createNewInstance().setResourcesOutputDirectory(dir)
                .addResourcesTemplateSuiteGenerator(generator).build();
        assertFalse(templateGenerator.isGeneratingClasses());
        assertTrue(templateGenerator.isGeneratingResources());
        verify(dir, generator);
    }

    /**
     * Test method for {@link TemplateGeneratorBuilder#addResourcesTemplateSuiteGenerator(TemplateSuiteGenerator)}.
     */
    @Test(expected = NullPointerException.class)
    public void testAddResourcesTemplateSuiteGeneratorException() {
        TemplateSuiteGenerator generator = createMock(TemplateSuiteGenerator.class);

        replay(generator);
        try {
            TemplateGeneratorBuilder.createNewInstance()
                    .addResourcesTemplateSuiteGenerator(generator);
        } finally {
            verify(generator);
        }
    }

    /**
     * Test method for {@link TemplateGeneratorBuilder#addResourcesTemplateClassGenerator(TemplateClassGenerator)}.
     */
    @Test
    public void testAddResourcesTemplateClassGenerator() {
        File dir = createMock(File.class);
        TemplateClassGenerator generator = createMock(TemplateClassGenerator.class);

        replay(dir, generator);
        TemplateGenerator templateGenerator = TemplateGeneratorBuilder
                .createNewInstance().setResourcesOutputDirectory(dir)
                .addResourcesTemplateClassGenerator(generator).build();
        assertFalse(templateGenerator.isGeneratingClasses());
        assertTrue(templateGenerator.isGeneratingResources());
        verify(dir, generator);
    }

    /**
     * Test method for {@link TemplateGeneratorBuilder#addResourcesTemplateClassGenerator(TemplateClassGenerator)}.
     */
    @Test(expected = NullPointerException.class)
    public void testAddResourcesTemplateClassGeneratorException() {
        TemplateClassGenerator generator = createMock(TemplateClassGenerator.class);

        replay(generator);
        try {
            TemplateGeneratorBuilder.createNewInstance()
                    .addResourcesTemplateClassGenerator(generator);
        } finally {
            verify(generator);
        }
    }

}
