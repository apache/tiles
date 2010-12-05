/**
 *
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
     * Test method for {@link org.apache.tiles.autotag.generate.TemplateGeneratorBuilder#addClassesTemplateSuiteGenerator(org.apache.tiles.autotag.generate.TemplateSuiteGenerator)}.
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
     * Test method for {@link org.apache.tiles.autotag.generate.TemplateGeneratorBuilder#addClassesTemplateSuiteGenerator(org.apache.tiles.autotag.generate.TemplateSuiteGenerator)}.
     */
    @Test(expected=NullPointerException.class)
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
     * Test method for {@link org.apache.tiles.autotag.generate.TemplateGeneratorBuilder#addClassesTemplateClassGenerator(org.apache.tiles.autotag.generate.TemplateClassGenerator)}.
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
     * Test method for {@link org.apache.tiles.autotag.generate.TemplateGeneratorBuilder#addClassesTemplateClassGenerator(org.apache.tiles.autotag.generate.TemplateClassGenerator)}.
     */
    @Test(expected=NullPointerException.class)
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
     * Test method for {@link org.apache.tiles.autotag.generate.TemplateGeneratorBuilder#addResourcesTemplateSuiteGenerator(org.apache.tiles.autotag.generate.TemplateSuiteGenerator)}.
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
     * Test method for {@link org.apache.tiles.autotag.generate.TemplateGeneratorBuilder#addResourcesTemplateSuiteGenerator(org.apache.tiles.autotag.generate.TemplateSuiteGenerator)}.
     */
    @Test(expected=NullPointerException.class)
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
     * Test method for {@link org.apache.tiles.autotag.generate.TemplateGeneratorBuilder#addResourcesTemplateClassGenerator(org.apache.tiles.autotag.generate.TemplateClassGenerator)}.
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
     * Test method for {@link org.apache.tiles.autotag.generate.TemplateGeneratorBuilder#addResourcesTemplateClassGenerator(org.apache.tiles.autotag.generate.TemplateClassGenerator)}.
     */
    @Test(expected=NullPointerException.class)
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
