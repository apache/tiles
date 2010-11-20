/**
 *
 */
package org.apache.tiles.autotag.generate;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.tiles.autotag.core.AutotagRuntimeException;
import org.apache.tiles.autotag.core.ClassParseException;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AbstractTemplateSuiteGenerator}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractTemplateSuiteGeneratorTest {

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
    private AbstractTemplateSuiteGenerator generator;

    /**
     * Sets up the test.
     *
     * @throws IOException If something goes wrong.
     */
    @Before
    public void setUp() throws IOException {
        velocityEngine = createMock(VelocityEngine.class);
        generator = createMockBuilder(AbstractTemplateSuiteGenerator.class).withConstructor(velocityEngine).createMock();
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
     * Test method for {@link org.apache.tiles.autotag.generate.AbstractTemplateSuiteGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, org.apache.tiles.autotag.model.TemplateClass)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test
    public void testGenerate() throws ResourceNotFoundException, ParseErrorException, Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        Template template = createMock(Template.class);
        String packageName = "org.apache.tiles.autotag.test";

        expect(generator.getDirectoryName(directory, packageName, suite)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andReturn(template);
        template.merge(isA(VelocityContext.class), isA(FileWriter.class));

        replay(velocityEngine, generator, suite, template);
        generator.generate(directory, packageName, suite);
        verify(velocityEngine, generator, suite, template);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.generate.AbstractTemplateSuiteGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, org.apache.tiles.autotag.model.TemplateClass)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test(expected=AutotagRuntimeException.class)
    public void testGenerateException1() throws ResourceNotFoundException, ParseErrorException, Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        Template template = createMock(Template.class);
        String packageName = "org.apache.tiles.autotag.test";

        expect(generator.getDirectoryName(directory, packageName, suite)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andThrow(new ResourceNotFoundException("hello"));

        replay(velocityEngine, generator, suite, template);
        generator.generate(directory, packageName, suite);
        verify(velocityEngine, generator, suite, template);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.generate.AbstractTemplateSuiteGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, org.apache.tiles.autotag.model.TemplateClass)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test(expected=AutotagRuntimeException.class)
    public void testGenerateException2() throws ResourceNotFoundException, ParseErrorException, Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        Template template = createMock(Template.class);
        String packageName = "org.apache.tiles.autotag.test";

        expect(generator.getDirectoryName(directory, packageName, suite)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andThrow(new ParseErrorException("hello"));

        replay(velocityEngine, generator, suite, template);
        generator.generate(directory, packageName, suite);
        verify(velocityEngine, generator, suite, template);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.generate.AbstractTemplateSuiteGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, org.apache.tiles.autotag.model.TemplateClass)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test(expected=AutotagRuntimeException.class)
    public void testGenerateException3() throws ResourceNotFoundException, ParseErrorException, Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        Template template = createMock(Template.class);
        String packageName = "org.apache.tiles.autotag.test";

        expect(generator.getDirectoryName(directory, packageName, suite)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andThrow(new Exception());

        replay(velocityEngine, generator, suite, template);
        generator.generate(directory, packageName, suite);
        verify(velocityEngine, generator, suite, template);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.generate.AbstractTemplateSuiteGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, org.apache.tiles.autotag.model.TemplateClass)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test(expected=AutotagRuntimeException.class)
    public void testGenerateException4() throws ResourceNotFoundException, ParseErrorException, Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        Template template = createMock(Template.class);
        String packageName = "org.apache.tiles.autotag.test";

        expect(generator.getDirectoryName(directory, packageName, suite)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andReturn(template);
        template.merge(isA(VelocityContext.class), isA(FileWriter.class));
        expectLastCall().andThrow(new IOException());

        replay(velocityEngine, generator, suite, template);
        generator.generate(directory, packageName, suite);
        verify(velocityEngine, generator, suite, template);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.generate.AbstractTemplateSuiteGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, org.apache.tiles.autotag.model.TemplateClass)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test(expected=ClassParseException.class)
    public void testGenerateException5() throws ResourceNotFoundException, ParseErrorException, Exception {
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        Template template = createMock(Template.class);
        String packageName = "org.apache.tiles.autotag.test";

        expect(generator.getDirectoryName(directory, packageName, suite)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andReturn(template);
        template.merge(isA(VelocityContext.class), isA(FileWriter.class));
        expectLastCall().andThrow(new ClassParseException());

        replay(velocityEngine, generator, suite, template);
        generator.generate(directory, packageName, suite);
        verify(velocityEngine, generator, suite, template);
    }

}
